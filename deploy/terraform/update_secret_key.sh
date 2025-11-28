#!/bin/bash
set -e

# ==== 1) BUSCAR DADOS DO TERRAFORM ====

echo "Lendo valores do Terraform..."

SMTP_SECRET_KEY=$(terraform output -raw smtp_secret_key)
SMTP_USERNAME=$(terraform output -raw smtp_username)
REGION="us-east-1"

echo "Secret Key lida."
echo "Username: $SMTP_USERNAME"


# ==== 2) RODAR O CÁLCULO EM PYTHON ====

echo "Calculando SMTP password..."

SMTP_PASSWORD=$(python3 <<EOF
from calculate_key import calculate_key
print(calculate_key("$SMTP_SECRET_KEY", "$REGION"))
EOF
)

echo "Password calculado:"
echo "$SMTP_PASSWORD"


# ==== 3) ATUALIZAR SECRET NO SECRETS MANAGER ====

echo "Atualizando secret no AWS Secrets Manager..."

aws secretsmanager put-secret-value \
  --secret-id smtp-credentials \
  --secret-string "{
    \"username\": \"$SMTP_USERNAME\",
    \"password\": \"$SMTP_PASSWORD\",
    \"smtp_secret_key\": \"$SMTP_SECRET_KEY\"
  }" \
  --region $REGION

echo "Secret atualizado com sucesso!"
