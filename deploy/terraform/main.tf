terraform {
  backend "s3" {
    bucket = "tc-fiap-fase4-notification-service-tfstate"
    key    = "terraform.tfstate"
    region = "us-east-1"
  }
}

provider "aws" {
  region = "us-east-1"
}

data "aws_caller_identity" "current" {}