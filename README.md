# Serviço de Notificação

Microsserviço de notificação do **[Agendador de Tarefas](https://github.com/willian-dev2/agendador-tarefas)**. Recebe os dados de uma tarefa e envia ao usuário um e-mail em HTML, montado com um template Thymeleaf.

A visão geral do sistema e de como os serviços se conectam está no README do [agendador-tarefas](https://github.com/willian-dev2/agendador-tarefas).

## Tecnologias

- **Java 17** e **Spring Boot 4**
- **Spring Mail** (SMTP)
- **Thymeleaf** para o template do e-mail
- **Lombok** e **Gradle**

## Como funciona

1. O serviço recebe uma tarefa (nome, descrição, data do evento e e-mail do usuário)
2. Preenche o template `templates/notificacao.html` com esses dados
3. Envia o e-mail em HTML pelo servidor SMTP configurado

## Endpoint

| Método | Rota | Descrição |
|---|---|---|
| POST | `/email` | Envia o e-mail de notificação de uma tarefa |

```http
POST /email
Content-Type: application/json

{
  "nomeTarefa": "Reunião de planejamento",
  "descricao": "Revisar as entregas da sprint",
  "dataEvento": "2026-03-10T14:00:00",
  "emailUsuario": "destinatario@exemplo.com"
}
```

## Como rodar

### Pré-requisitos

- Java 17+
- Uma conta de e-mail com acesso SMTP. No Gmail, use uma **senha de app** (Conta Google → Segurança → Senhas de app), não a senha da conta.

### Configuração

Preencha em `src/main/resources/application.yaml`:

```yaml
spring:
  mail:
    username: seu_email@gmail.com
    password: sua_senha_de_app

envio:
  email:
    remetente: seu_email@gmail.com
    nomeRemetente: Agendador de Tarefas
```

Não faça commit da senha: prefira variáveis de ambiente, por exemplo `password: ${MAIL_PASSWORD}`.

### Rodar

```bash
./gradlew bootRun
```

O serviço sobe em `http://localhost:8082`.

## Autor

**Willian Vinicius Sousa do Nascimento** · [GitHub](https://github.com/willian-dev2) · [LinkedIn](https://linkedin.com/in/willian-vinicius-3128b7363)
