# Notification Service

Serviço responsável por receber eventos de pagamento, enviar notificações por e-mail e persistir os dados em MongoDB utilizando infraestrutura AWS simulada com LocalStack.

## Pré-requisitos

- Docker e Docker Compose instalados e em funcionamento.
- Java 21+ e Maven (ou `./mvnw`) instalados.
- AWS CLI instalado e configurado para uso local (qualquer access key/secret).

---

## Rodando os testes da aplicação

Para executar a suíte de testes:

``
mvn clean test
``

## Executando o ambiente de desenvolvimento

Para facilitar, use o script `run-dev.sh` descrito abaixo. Ele realiza os seguintes passos:

1. Derruba containers existentes e volumes antigos.
2. Sobe o ambiente com `docker-compose` (LocalStack, Mongo, etc.).
3. Sobe a aplicação Spring Boot com o profile `dev`.
4. Envia uma mensagem de exemplo para a fila SQS local (`notification-queue`), usando o script `send-sqs-localstack.sh`.

Para rodar o ambiente de desenvolvimento:

``
./run-dev.sh
``

Certifique-se de que o script `send-sqs-localstack.sh` já exista e esteja configurado para enviar a mensagem usando o JSON em `src/test/resources` (ou outro caminho que você definiu).

---

## Verificando o e-mail enviado (Ethereal)

Após o envio da mensagem para a fila e o processamento pela aplicação, um e-mail será enviado através do serviço Ethereal.

Acesse:

- URL: https://ethereal.email/messages
- Login: `orlo5@ethereal.email`
- Senha: `XDa5mzbMRzJWzNC5Tm`

Você deverá ver a mensagem de notificação correspondente ao evento processado.

---

## Verificando o documento salvo no MongoDB

Os dados também são persistidos no MongoDB, com interface via Mongo Express.

Acesse:

- URL: http://localhost:8081/
- Usuário: `admin`
- Senha: `pass`

Nessa interface, é possível visualizar o banco `notificationdb` e os documentos gerados a partir dos eventos processados.