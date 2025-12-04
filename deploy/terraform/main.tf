terraform {
  backend "s3" {
    bucket = "notification-service-tfstate-268021560448"
    key    = "terraform.tfstate"
    region = "us-east-1"
  }
}

provider "aws" {
  region = "us-east-1"
}

data "aws_caller_identity" "current" {}