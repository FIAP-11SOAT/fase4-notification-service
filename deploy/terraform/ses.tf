# Amazon Simple Email Service

# Verificação de Email Individual
resource "aws_ses_email_identity" "from" {
  email = "mandacosta94@gmail.com"
}

# IAM User para SMTP
resource "aws_iam_user" "ses_smtp_user" {
  name = "ses-smtp-user"
}

# Access Key SMTP
resource "aws_iam_access_key" "ses_smtp" {
  user = aws_iam_user.ses_smtp_user.name
}

# Política IAM mínima para envio
resource "aws_iam_policy" "ses_send" {
  name = "SES-SendEmail-Policy"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Action = [
        "ses:SendEmail",
        "ses:SendRawEmail"
      ]
      Resource = "*"
    }]
  })
}

resource "aws_iam_user_policy_attachment" "ses_policy" {
  user       = aws_iam_user.ses_smtp_user.name
  policy_arn = aws_iam_policy.ses_send.arn
}

resource "aws_secretsmanager_secret" "smtp_credentials_ses" {
  name                    = "smtp-credentials-ses"
  recovery_window_in_days = 0
}

resource "aws_secretsmanager_secret_version" "smtp_credentials_version" {
  secret_id = aws_secretsmanager_secret.smtp_credentials_ses.id

  secret_string = jsonencode({
    username = aws_iam_access_key.ses_smtp.id
    password = aws_iam_access_key.ses_smtp.ses_smtp_password_v4
  })
}

output "smtp_username" {
  value = aws_iam_access_key.ses_smtp.id
}

output "smtp_password" {
  description = "Senha SMTP SES"
  value       = aws_iam_access_key.ses_smtp.ses_smtp_password_v4
  sensitive   = true
}