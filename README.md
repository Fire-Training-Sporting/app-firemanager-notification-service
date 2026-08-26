# app-firemanager-notification-service

Microsserviço responsável por enviar notificações por e-mail (criação, confirmação, cancelamento e finalização de aula) para alunos e professores do Fire Manager. Extraído do monolito `fira-manager-api`, que antes fazia esse trabalho internamente através do padrão Observer + `EmailService`.

Este projeto segue **Clean Architecture**. Este README existe pra explicar, de forma direta, o que isso significa na prática — mesmo que você nunca tenha trabalhado com esse estilo de organização antes.

---

## 1. A ideia por trás da arquitetura

Todo sistema mistura duas coisas, se você não prestar atenção:

1. **Regra de negócio** — o motivo do sistema existir. No nosso caso: "quando um agendamento é cancelado, o aluno precisa ser avisado com tal mensagem".
2. **Detalhe técnico** — como isso roda hoje na prática. No nosso caso: Spring, SMTP, Thymeleaf, HTTP.

Detalhes técnicos mudam com muito mais frequência do que regra de negócio. Se as duas coisas ficam misturadas na mesma classe, toda troca de tecnologia (SMTP → outro provedor, e-mail → WhatsApp, REST → fila) ameaça quebrar a regra de negócio junto — mesmo que ela, em si, não tenha mudado nada.

Clean Architecture é, na prática, uma forma de **separar essas duas coisas em camadas**, garantindo que a camada de detalhe técnico dependa da camada de regra de negócio — nunca o contrário.

### O teste rápido para saber onde algo deveria morar

Para qualquer trecho de lógica, pergunte: **"isso muda se eu trocar a tecnologia por outra?"**

- **Não muda** → é regra de negócio → pertence ao `domain`
- **Muda** → é detalhe técnico → pertence à `infrastructure`
- **É a sequência/orquestração dos dois** → pertence à `application`

---

## 2. As três camadas do projeto

```
domain          → regras de negócio puras. Zero import de Spring, zero import de qualquer framework.
application     → orquestra o caso de uso. Só conhece interfaces do mundo externo, nunca implementações.
infrastructure  → onde Spring, HTTP, SMTP e Thymeleaf realmente aparecem.
```

A regra de dependência é sempre a mesma:

```
infrastructure  →  depende de  →  application  →  depende de  →  domain
```

`domain` nunca sabe que `application` existe. `application` nunca sabe que `infrastructure` existe. A seta sempre aponta para dentro.

---

## 3. Estrutura de pastas

```
src/main/java/.../app_firemanager_notification_service/

├── domain/
│   ├── enums/
│   │   └── StatusAgendamento.java          → PENDENTE, CONFIRMADO, CANCELADO, FINALIZADO
│   ├── models/
│   │   ├── ConteudoNotificacao.java        → resultado: assunto + mensagem prontos
│   │   ├── DadosNotificacaoAluno.java      → dados de contexto para montar a mensagem do aluno
│   │   └── DadosNotificacaoProfessor.java  → dados de contexto para montar a mensagem do professor
│   └── service/
│       └── ConteudoNotificacaoBuilder.java → decide o texto/assunto por status + perfil
│
├── application/
│   ├── interfaces/
│   │   ├── entrada/                        → "o que a aplicação oferece" (chamado de fora)
│   │   │   ├── ExecutarNotificacaoAluno.java
│   │   │   └── ExecutarNotificacaoProfessor.java
│   │   └── saida/                          → "o que a aplicação precisa do mundo externo"
│   │       ├── EmailSenderInterface.java
│   │       ├── RenderizarEmailAluno.java
│   │       └── RenderizarEmailProfessor.java
│   ├── models/
│   │   ├── Email.java                      → destinatário + assunto + corpo, pronto para envio
│   │   └── payload/
│   │       ├── PayloadAluno.java           → tudo que chega em uma notificação de aluno
│   │       └── PayloadProfessor.java       → tudo que chega em uma notificação de professor
│   └── usecases/
│       ├── EnviarNotificacaoAlunoUseCase.java
│       └── EnviarNotificacaoProfessorUseCase.java
│
└── infrastructure/
    ├── configuration/
    │   └── UseCaseConfig.java              → monta os UseCases como beans do Spring
    ├── springmail/
    │   └── EmailSenderService.java         → implementação real via JavaMailSender (SMTP)
    ├── thymeleaf/
    │   └── ThymeleafEmailRenderer.java     → implementação real via Thymeleaf (texto → HTML)
    └── firemanager/
        ├── controller/
        │   └── NotificacaoController.java  → recebe o POST do monolito
        ├── dto/
        │   ├── request/
        │   │   ├── NotificacaoAlunoRequest.java
        │   │   └── NotificacaoProfessorRequest.java
        │   └── response/
        │       └── NotificacaoResponse.java
        └── exception/
            └── NotificacaoExceptionHandler.java → trata payload inválido (400)

src/main/resources/
├── templates/
│   ├── email-aluno.html                    → template Thymeleaf do e-mail do aluno
│   └── email-professor.html                → template Thymeleaf do e-mail do professor
└── application.properties                   → configuração de SMTP e porta do servidor
```

---

## 4. Por que cada dado tem seu próprio objeto (e por que tantos `record`)

Aluno e professor recebem mensagens diferentes, com dados diferentes (o e-mail do professor mostra telefone e condomínio do aluno; o e-mail do aluno não). Por isso, em vez de um objeto genérico "guarda-chuva" com campos que às vezes ficam vazios, o projeto tem objetos específicos por perfil em cada camada:

| Camada | Objeto do aluno | Objeto do professor |
|---|---|---|
| domain (dados para decidir o texto) | `DadosNotificacaoAluno` | `DadosNotificacaoProfessor` |
| application (o que chega para o caso de uso) | `PayloadAluno` | `PayloadProfessor` |
| infrastructure (o que chega via HTTP) | `NotificacaoAlunoRequest` | `NotificacaoProfessorRequest` |

Todos esses objetos são `record` — um tipo do Java feito especificamente para "carregar dado, sem identidade, sem necessidade de mudar depois de criado". Um `record` gera sozinho o construtor, os métodos de leitura, `equals`/`hashCode`/`toString`, e não permite alterar valores depois de criado. Isso evita uma classe inteira de bugs onde algo é alterado sem querer no meio do fluxo.

### Por que os DTOs de HTTP são objetos separados dos objetos internos

`NotificacaoAlunoRequest` (o que chega no `POST`) e `PayloadAluno` (o que o caso de uso realmente usa) parecem redundantes, mas cumprem papéis diferentes: um é o **contrato público** que o monolito depende (não deveria mudar de repente porque algo interno mudou); o outro é o **modelo interno** da aplicação (pode evoluir livremente sem afetar quem consome a API). O `Controller` é quem faz a tradução entre os dois.

---

## 5. O fluxo completo de uma requisição

```
1. O monolito faz POST /api/notificacao/email/aluno (ou /email/professor)
   com o status do agendamento, dados do agendamento e o e-mail do destinatário.

2. NotificacaoController recebe o JSON, valida com @Valid, e traduz
   o Request (infrastructure) para um Payload (application).

3. Controller chama ExecutarNotificacaoAluno.executar(payload)
   — repare: ele chama a INTERFACE, nunca a classe concreta que implementa.

4. EnviarNotificacaoAlunoUseCase (a implementação real) orquestra:

   a) Traduz o Payload para DadosNotificacaoAluno (domínio não conhece o Payload).
   b) Pergunta ao domínio: ConteudoNotificacaoBuilder.buildParaAluno(status, dados)
      → domínio devolve um ConteudoNotificacao (assunto + mensagem em texto puro).
   c) Pede para renderizar em HTML: RenderizarEmailAluno.renderizar(conteudo, dados)
      → por trás, isso executa o Thymeleaf sobre o template email-aluno.html.
   d) Monta um Email (destinatário + assunto + HTML pronto).
   e) Chama EmailSenderInterface.enviar(email)
      → por trás, isso conecta no SMTP de verdade e manda o e-mail.

5. Controller devolve 202 Accepted para o monolito.
```

O ponto central para entender esse fluxo: **em nenhum momento o `UseCase` (application) menciona `JavaMailSender` ou `SpringTemplateEngine` diretamente.** Ele só conhece interfaces (`EmailSenderInterface`, `RenderizarEmailAluno`). Quem decide qual implementação real entra em cada uma é o Spring, através da `UseCaseConfig`.

---

## 6. Inversão de Dependência — o porquê disso tudo

Esse é o princípio que sustenta a arquitetura inteira, e vale a pena entender com um exemplo concreto do próprio projeto.

**Sem inversão** (não é como o projeto foi feito, é só para ilustrar o problema):

```java
public class EnviarNotificacaoAlunoUseCase {
    private final SpringMailSenderReal emailSender = new SpringMailSenderReal();
}
```

Aqui, a regra de orquestração (`application`) conheceria uma tecnologia específica (`infrastructure`). Trocar de provedor de e-mail, ou testar essa classe sem SMTP de verdade, exigiria editar essa classe.

**Com inversão** (como o projeto realmente é):

```java
public class EnviarNotificacaoAlunoUseCase {
    private final EmailSenderInterface emailSender; // interface, não classe concreta

    public EnviarNotificacaoAlunoUseCase(EmailSenderInterface emailSender) {
        this.emailSender = emailSender;
    }
}
```

A classe declara **o que precisa** (algo que saiba enviar e-mail), não **como conseguir isso**. Quem entrega a implementação de verdade (`EmailSenderService`, usando Spring Mail) é decidido de fora — na prática, pelo próprio Spring, injetando automaticamente o bean certo no construtor. Essa mecânica de "alguém de fora entrega a peça pronta" é chamada de **injeção de dependência**.

Ganhos práticos disso, aplicados neste projeto:

- **Testabilidade**: os `UseCase`s são testados com mocks de `EmailSenderInterface`/`RenderizarEmailAluno`, sem subir Spring, sem SMTP real, sem Thymeleaf real (veja `EnviarNotificacaoAlunoUseCaseTest`).
- **Troca de tecnologia sem quebrar o resto**: se um dia o e-mail for substituído por outro provedor, ou até por outro canal (WhatsApp), basta criar uma nova classe em `infrastructure` implementando as mesmas interfaces — `domain` e `application` não mudam uma linha.

---

## 7. Por que `domain` e `application` não têm nenhuma anotação do Spring

Todas as classes de `domain` e `application` são Java puro — nenhum `@Service`, `@Component`, `@Autowired`. Isso é proposital: significa que essas camadas continuariam funcionando mesmo se o Spring fosse removido do projeto inteiro.

Quem "liga os fios" entre essas classes puras e o mundo Spring é a `UseCaseConfig`, em `infrastructure.config`. Ela usa `@Bean` para criar manualmente os `UseCase`s, entregando a eles as dependências que o próprio Spring já sabe fornecer (`EmailSenderService`, `ThymeleafEmailRenderer`, que sim são gerenciados pelo Spring, com `@Service`).

---

## 8. Configuração e execução local

Configuração de SMTP em `src/main/resources/application.properties`:

```properties
spring.mail.host=smtp.office365.com
spring.mail.port=587
spring.mail.username=SEU_EMAIL_AQUI
spring.mail.password=SUA_SENHA_DE_APP_AQUI
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

server.port=8081
```

Rodar localmente:

```bash
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8081`.

### Endpoints

**`POST /api/notificacao/email/aluno`**

```json
{
  "agendamentoId": 42,
  "nomeProfessor": "João Silva",
  "data": "2026-08-30",
  "hora": "14:00",
  "status": "pendente",
  "emailDestinatario": "aluno@example.com"
}
```

**`POST /api/notificacao/email/professor`**

```json
{
  "agendamentoId": 42,
  "nomeAluno": "Maria Souza",
  "telefoneAluno": "11999990000",
  "nomeCondominio": "Condomínio Sol Nascente",
  "observacao": "Levar tatame",
  "data": "2026-08-30",
  "hora": "14:00",
  "status": "confirmado",
  "emailDestinatario": "professor@example.com"
}
```

Ambos respondem `202 Accepted` com um corpo `{"mensagem": "..."}` assim que a notificação é aceita — o envio de fato acontece em segundo plano (`@Async`), sem travar quem chamou.

Payload inválido (campo obrigatório faltando, ou `status` que não existe no enum) responde `400 Bad Request` com uma mensagem explicando o problema.

---

## 9. Testes

- `ConteudoNotificacaoBuilderTest` — testa a regra de negócio pura (todos os 4 status × 2 perfis), sem mocks.
- `EnviarNotificacaoAlunoUseCaseTest` / `EnviarNotificacaoProfessorUseCaseTest` — testam a orquestração com mocks das três dependências injetadas, confirmando que os dados são traduzidos e repassados corretamente entre as camadas.
- `EmailSenderServiceTest` — testa a montagem do e-mail via `MimeMessageHelper`, incluindo o caminho de erro (destinatário inválido).
- `ThymeleafEmailRendererTest` — testa que as variáveis certas chegam ao motor de template para cada perfil.

Nenhum desses testes precisa de SMTP real, servidor web ou banco de dados — é o benefício direto de manter `domain` e `application` livres de framework.

---

## 10. O que ficou de fora deste microsserviço (de propósito)

- **Decidir quem precisa ser notificado** (que aluno, que professor, de um agendamento específico) continua no monolito — quem monta o `Agendamento` já sabe essa relação.
- **Detectar que um agendamento foi criado, cancelado, atualizado, ou está a 24h de acontecer** também continua no monolito (inclusive o `@Scheduled` que verifica lembretes).

Este serviço faz apenas duas coisas: decidir o texto certo para cada situação, e entregar esse texto por e-mail. Esse escopo pequeno e específico é intencional — é o que torna o serviço fácil de entender, testar e evoluir isoladamente.
