#!/bin/bash
set -euo pipefail

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BACKEND_SCRIPT="$BASE_DIR/backend/backend_bucket_infra.sh"
TERRAFORM_DIR="$BASE_DIR"

echo "============================================"
echo "       APLICANDO INFRAESTRUTURA"
echo "============================================"

echo ""
echo "📦 1) Criando/verificando bucket do Terraform Backend..."
chmod +x "$BACKEND_SCRIPT"
"$BACKEND_SCRIPT"
echo "✔ Bucket backend ok!"

echo ""
echo "🔧 2) Rodando Terraform init..."
cd "$TERRAFORM_DIR"
terraform init

echo ""
echo "🚀 3) Aplicando Terraform..."
terraform apply -auto-approve

echo ""
echo "✔ Terraform aplicado com sucesso!"
