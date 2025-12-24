# Notification Service

Serviço responsável por receber eventos de pagamento, enviar notificações por e-mail e persistir os dados em MongoDB utilizando infraestrutura AWS simulada com LocalStack.

## Integrando com demais microsserviços (produtores)

Adicionar no Terraform:

```hcl
// Referência ao terraform da aplicação notification-service
data "terraform_remote_state" "notification_infra" {
  backend = "s3"
  config = {
    bucket = "notification-service-tfstate-268021560448"
    key    = "terraform.tfstate"
    region = "us-east-1"
  }
}

// Anexa a policy de producer ao role do serviço
resource "aws_iam_role_policy_attachment" "attach_sqs_send_to_ms1" {
  role       = aws_iam_role.ms1_ec2_role.name
  policy_arn = data.terraform_remote_state.notification_infra.outputs.notification_queue_producer_policy_arn
}
```

Payload que deve ser enviado à fila:
```hcl
{   
    "meta": {
        "event_id": "dd6d52c8-ec76-485c-9db4-eb96e62cd2d5",
        "event_target": "notification-service-queue",
        "event_name": "payment-created-event"
    },
    "payload": {
        "customer_name": "Fulano de Tal",
        "customer_email": "fulano@gmail.com",
        "order_id": 5698,
        "items": [
            { "productId": 1, "name": "Hambúrguer Clássico", "quantity": 2 },
            { "productId": 3, "name": "Batata Frita", "quantity": 1 }
        ],
        "amount": 59.65,
        "qr_code": "00020101021243650016COM.MERCADOLIBRE020130636bc071ef6-eac8-4fd0-b283-928d15a764845204000053039865802BR5923VendedorTCFiapUmMeiaUm6009SAOPAULO62070503***6304E983"   
    }
}
```

Possibilidades de eventos
- Apenas no evento `payment-completed-event` (pagamento confirmado) não é necessário enviar o QRCode.

```hcl
[
  "payment-created-event",
  "payment-completed-event",
  "payment-failed-event",
  "production-completed-event"
]
```






# Pré-requisitos

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

Rode o ambiente de desenvolvimento:

``
./run-dev.sh
``

Abra um novo terminal

Crie a fila sqs e envie uma notificação:

``
./send-sqs-localstack.sh
``

Certifique-se de que o script `send-sqs-localstack.sh` já exista e esteja configurado para enviar a mensagem usando o JSON em `src/test/resources` (ou outro caminho que você definiu).

---

### Verificando o e-mail enviado (Ethereal)

Após o envio da mensagem para a fila e o processamento pela aplicação, um e-mail será enviado através do serviço Ethereal.

Acesse:

- URL: https://ethereal.email/messages
- Login: `orlo5@ethereal.email`
- Senha: `XDa5mzbMRzJWzNC5Tm`

Você deverá ver a mensagem de notificação correspondente ao evento processado.

---

### Verificando o documento salvo no MongoDB

Os dados também são persistidos no MongoDB, com interface via Mongo Express.

Acesse:

- URL: http://localhost:8081/
- Usuário: `admin`
- Senha: `pass`

Nessa interface, é possível visualizar o banco `notificationdb` e os documentos gerados a partir dos eventos processados.

