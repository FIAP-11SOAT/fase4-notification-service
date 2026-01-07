# DynamoDB

resource "aws_dynamodb_table" "notifications" {
  name         = "${var.project_name}-notifications"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "id"

  attribute {
    name = "id"
    type = "S"
  }

  point_in_time_recovery {
    enabled = true
  }

  server_side_encryption {
    enabled = true
  }

  tags = merge(var.tags, {
    Name = "${var.project_name}-notifications"
  })
}

output "dynamodb_table_name" {
  value = aws_dynamodb_table.notifications.name
}

output "dynamodb_table_arn" {
  value = aws_dynamodb_table.notifications.arn
}

resource "aws_iam_policy" "dynamodb_access" {
  name = "dynamodb-access"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "dynamodb:GetItem",
          "dynamodb:PutItem",
          "dynamodb:UpdateItem",
          "dynamodb:Query",
          "dynamodb:Scan"
        ],
        Resource = aws_dynamodb_table.notifications.arn
      }
    ]
  })
}