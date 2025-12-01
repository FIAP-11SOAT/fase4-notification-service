#!/usr/bin/env bash
set -e

# Configurações
ENDPOINT_URL="http://localhost:4566"
REGION="us-east-1"
QUEUE_NAME="notification-queue"
MESSAGE_FILE="src/test/resources/message.json"

echo "=== 1) Verificando AWS CLI ==="
if ! command -v aws >/dev/null 2>&1; then
  echo "Erro: aws CLI não encontrado. Instale o AWS CLI antes de continuar."
  exit 1
fi

echo "=== 2) Criando fila (se não existir) ==="
aws sqs create-queue \
  --endpoint-url "$ENDPOINT_URL" \
  --region "$REGION" \
  --queue-name "$QUEUE_NAME" >/dev/null

echo "=== 3) Obtendo URL da fila ==="
QUEUE_URL=$(aws sqs get-queue-url \
  --endpoint-url "$ENDPOINT_URL" \
  --region "$REGION" \
  --queue-name "$QUEUE_NAME" \
  --output text \
  --query 'QueueUrl')

echo "QUEUE_URL = $QUEUE_URL"

if [ ! -f "$MESSAGE_FILE" ]; then
  echo "Erro: arquivo de mensagem não encontrado em: $MESSAGE_FILE"
  exit 1
fi

echo "=== 4) Enviando mensagem da fila ==="
aws sqs send-message \
  --endpoint-url "$ENDPOINT_URL" \
  --region "$REGION" \
  --queue-url "$QUEUE_URL" \
  --message-body "file://$MESSAGE_FILE"

echo "Mensagem enviada com sucesso para $QUEUE_URL usando $MESSAGE_FILE"
