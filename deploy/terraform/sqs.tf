resource "aws_sqs_queue" "notification_queue" {
  name = "notification-queue"

  visibility_timeout_seconds = 30
  message_retention_seconds  = 345600 # 4 dias
  max_message_size           = 262144 # 256 KB
  delay_seconds              = 0
  receive_wait_time_seconds  = 5

  tags = {
    Service     = "notification-service"
    Environment = "prod"
  }
}

output "notification_queue_url" {
  value = aws_sqs_queue.notification_queue.url
}

output "notification_queue_arn" {
  value = aws_sqs_queue.notification_queue.arn
}

resource "aws_iam_policy" "producers_send_to_notification_queue" {
  name        = "producers-send-to-notification-queue"
  description = "Allow producer services to send messages to notification-queue"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "AllowSendToNotificationQueue"
        Effect = "Allow"
        Action = [
          "sqs:SendMessage",
          "sqs:GetQueueUrl"
        ]
        Resource = aws_sqs_queue.notification_queue.arn
      }
    ]
  })
}

output "notification_queue_producer_policy_arn" {
  value       = aws_iam_policy.producers_send_to_notification_queue.arn
  description = "ARN da policy que permite enviar mensagens para a fila"
}