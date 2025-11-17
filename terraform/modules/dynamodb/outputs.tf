output "table_name" {
  description = "Name of the DynamoDB notifications table"
  value       = aws_dynamodb_table.notifications.name
}

output "table_arn" {
  description = "ARN of the DynamoDB notifications table"
  value       = aws_dynamodb_table.notifications.arn
}

output "table_id" {
  description = "ID of the DynamoDB notifications table"
  value       = aws_dynamodb_table.notifications.id
}