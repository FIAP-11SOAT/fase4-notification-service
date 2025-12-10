variable "shared_secret_name" {
  type        = string
  description = "Nome do secret com info de infra compartilhada"
  default     = "fase4-infra-microservices-secrets"
}

variable "service_name" {
  type        = string
  description = "Nome lógico do serviço"
  default     = "notification-service"
}

variable "ecr_image_tag" {
  type        = string
  description = "Tag da imagem do ECR"
  default     = "latest"
}

data "aws_secretsmanager_secret" "shared" {
  name = var.shared_secret_name
}

data "aws_secretsmanager_secret_version" "shared" {
  secret_id = data.aws_secretsmanager_secret.shared.id
}

locals {
  shared = jsondecode(data.aws_secretsmanager_secret_version.shared.secret_string)
}

data "aws_vpc" "main" {
  id = local.shared["VPC_ID"]
}

data "aws_subnets" "public" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.main.id]
  }
}

data "aws_ecs_cluster" "main" {
  cluster_name = local.shared["ECS_CLUSTER_ID"]
}

data "aws_iam_policy_document" "notification_consumer" {
  statement {
    actions = [
      "sqs:ReceiveMessage",
      "sqs:DeleteMessage",
      "sqs:ChangeMessageVisibility",
      "sqs:GetQueueAttributes",
      "sqs:GetQueueUrl"
    ]

    resources = [
      aws_sqs_queue.notification_queue.arn
    ]
  }
}

resource "aws_iam_policy" "notification_consumer" {
  name        = "notification-service-consume-notification-queue"
  description = "Allow notification-service to consume messages from notification-queue"
  policy      = data.aws_iam_policy_document.notification_consumer.json
}

resource "aws_iam_role" "ecs_execution_role" {
  name = "ecs-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect = "Allow",
      Principal = { Service = "ecs-tasks.amazonaws.com" },
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy" "ecs_execution_role_secrets_policy" {
  name = "ecs-execution-secrets-ses-policy"
  role = aws_iam_role.ecs_execution_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue",
          "secretsmanager:DescribeSecret"
        ]
        Resource = "${aws_secretsmanager_secret.smtp_credentials_ses.arn}*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_execution_role_policy" {
  role       = aws_iam_role.ecs_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role" "ecs_task_role" {
  name = "notification-service-task-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect = "Allow",
      Principal = { Service = "ecs-tasks.amazonaws.com" },
      Action = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy_attachment" "task_sqs" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = aws_iam_policy.notification_consumer.arn
}

resource "aws_iam_role_policy_attachment" "task_dynamodb" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = aws_iam_policy.dynamodb_access.arn
}

resource "aws_iam_role_policy_attachment" "task_secretsmanager" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = "arn:aws:iam::aws:policy/SecretsManagerReadWrite"
}

resource "aws_ecs_task_definition" "notification_task" {
  family                   = "notification-service-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "512"
  memory                   = "1024"

  execution_role_arn = aws_iam_role.ecs_execution_role.arn
  task_role_arn      = aws_iam_role.ecs_task_role.arn

  container_definitions = jsonencode([
    {
      name      = "notification-container",
      image     = "${aws_ecr_repository.main.repository_url}:${var.ecr_image_tag}",
      secrets = [
        {
          name      = "SPRING_MAIL_USERNAME"
          valueFrom = "${aws_secretsmanager_secret.smtp_credentials_ses.arn}:username::"
        },
        {
          name      = "SPRING_MAIL_PASSWORD"
          valueFrom = "${aws_secretsmanager_secret.smtp_credentials_ses.arn}:password::"
        }
      ]
      essential = true,
      portMappings = [
        {
          containerPort = 8080
          protocol      = "tcp"
        }
      ],
      environment = [
        { name = "QUEUE_URL", value = aws_sqs_queue.notification_queue.url },
        { name = "SPRING_PROFILES_ACTIVE", value = "prod" }
      ]
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          "awslogs-group"         = "/ecs/notification-service",
          "awslogs-region"        = "us-east-1",
          "awslogs-stream-prefix" = "ecs"
        }
      }
    }
  ])
}

resource "aws_security_group" "ecs_sg" {
  name        = "notification-service-ecs-sg"
  description = "SG for notification-service Fargate Tasks"
  vpc_id      = data.aws_vpc.main.id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_ecs_service" "notification_service" {
  name            = "notification-service"
  cluster         = data.aws_ecs_cluster.main.arn
  task_definition = aws_ecs_task_definition.notification_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets = data.aws_subnets.public.ids
    security_groups = [aws_security_group.ecs_sg.id]
    assign_public_ip = true
  }
}

resource "aws_cloudwatch_log_group" "ecs_logs" {
  name              = "/ecs/notification-service"
  retention_in_days = 7
}
