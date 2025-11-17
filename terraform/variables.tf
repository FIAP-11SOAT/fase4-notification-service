variable "project_name" {
  description = "Name of the project"
  type        = string
  default     = "notification-service"
}

variable "environment" {
  description = "Environment name"
  type        = string
  default     = "production"
}

variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}