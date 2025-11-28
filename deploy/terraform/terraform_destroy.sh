#!/bin/bash
set -e

BUCKET="notification-service-tfstate-268021560448"
REGION="us-east-1"   # ajuste se necessário

echo "============================================"
echo "        Destruindo infraestrutura"
echo "============================================"

echo ""
echo "☠️  Executando terraform destroy..."
terraform destroy -auto-approve

echo "✔ Terraform destruído com sucesso!"


echo ""
echo "🧨  Apagando todas as versões do bucket: $BUCKET"

VERSIONS=$(aws s3api list-object-versions \
  --bucket "$BUCKET" \
  --query 'Versions[].{Key:Key,VersionId:VersionId}' \
  --output json)

DELETE_MARKERS=$(aws s3api list-object-versions \
  --bucket "$BUCKET" \
  --query 'DeleteMarkers[].{Key:Key,VersionId:VersionId}' \
  --output json)

if [[ "$VERSIONS" != "[]" ]]; then
  echo "🗑  Removendo versões..."
  aws s3api delete-objects \
    --bucket "$BUCKET" \
    --delete "{\"Objects\": $VERSIONS}"
fi

if [[ "$DELETE_MARKERS" != "[]" ]]; then
  echo "🗑  Removendo delete markers..."
  aws s3api delete-objects \
    --bucket "$BUCKET" \
    --delete "{\"Objects\": $DELETE_MARKERS}"
fi

echo "✔ Versões e delete markers removidos!"


echo ""
echo "💣  Apagando bucket..."
aws s3api delete-bucket --bucket "$BUCKET" --region "$REGION"

echo "✔ Bucket removido com sucesso!"

echo ""
echo "============================================"
echo "   Remoção completa da infraestrutura!"
echo "============================================"
