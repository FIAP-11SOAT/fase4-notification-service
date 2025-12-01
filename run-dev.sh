#!/usr/bin/env bash
set -e

echo "=== Encerrando containers antigos ==="
docker-compose down -v --remove-orphans

echo "=== Subindo ambiente com docker-compose ==="
docker-compose up -d

echo "Aguardando alguns segundos para serviços inicializarem..."
sleep 15

echo "=== Iniciando aplicação Spring Boot (profile dev) ==="
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev &
APP_PID=$!

echo "PID da aplicação: $APP_PID"
echo "Aguardando a aplicação iniciar..."
sleep 20

echo "=== Enviando mensagem para a fila SQS local ==="
./send-sqs-localstack.sh

echo "Ambiente de desenvolvimento está em execução."
echo "Para parar a aplicação, use: kill $APP_PID ou Ctrl+C se estiver em foreground."