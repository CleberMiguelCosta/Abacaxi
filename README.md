# Sistema de Gestão de Resíduos do Abacaxi

**ResíduosAbacaxi** — backend em **Java 17 + Spring Boot 3.2** e frontend em **React + Vite + shadcn/ui** para coleta, classificação, reaproveitamento e venda de resíduos do abacaxi.

## Como executar (completo)

### 1. Backend
Pré-requisitos: **JDK 17** e **Maven 3.8+**.

```bash
mvn spring-boot:run
```

- API: http://localhost:8080  
- Console H2: http://localhost:8080/h2-console  
  - JDBC URL: `jdbc:h2:mem:abacaxi_db`  
  - User: `sa` / senha vazia  

### 2. Frontend
Pré-requisitos: **Node.js 18+**.

```bash
cd frontend
npm install
npm run dev
```

- Interface: http://localhost:5173  
- Login demo: `vendas@abacaxi.com` / `vendas123` (ou `admin@abacaxi.com` / `admin123`)

O Vite faz proxy de `/api` para o backend na porta 8080.

## Arquitetura (camadas)

```
com.abacaxi.residuos
├── model / model.enums   → Entidades JPA
├── dto                   → Objetos de transferência
├── repository            → Acesso a dados (Spring Data JPA)
├── service               → Regras de negócio
├── controller            → Endpoints REST
├── config                → Segurança, seed, CORS
└── exception             → Tratamento global de erros
```

## Como executar

Pré-requisitos: **JDK 17** e **Maven 3.8+**.

```bash
mvn spring-boot:run
```

- API: http://localhost:8080  
- Console H2: http://localhost:8080/h2-console  
  - JDBC URL: `jdbc:h2:mem:abacaxi_db`  
  - User: `sa` / senha vazia  

## Autenticação (RNF03 / RNF04)

Envie o token no header:

```
Authorization: Bearer <token>
```

ou

```
X-API-KEY: abacaxi-vendas-2024
```

### Usuários seed

| E-mail | Senha | Perfil | Token |
|--------|-------|--------|-------|
| admin@abacaxi.com | admin123 | ADMINISTRADOR | token-admin-001 |
| maria@polo.com | polo123 | RESPONSAVEL_POLO | token-polo-001 |
| operador@abacaxi.com | op123 | OPERADOR_COLETA | token-operador-001 |
| vendas@abacaxi.com | vendas123 | RESPONSAVEL_VENDAS | token-vendas-001 |

Login:

```http
POST /api/auth/login
{ "email": "vendas@abacaxi.com", "senha": "vendas123" }
```

## Endpoints principais

| Método | Endpoint | RF |
|--------|----------|----|
| POST/GET/PUT/PATCH/DELETE | `/api/coletas` | RF01–RF05 |
| GET | `/api/coletas/calendario?inicio=&fim=` | RF07 |
| GET | `/api/calendario/stream` (SSE) | RNF05 |
| GET/POST/PUT/DELETE | `/api/polos` | RF06 |
| GET/POST/PUT/DELETE | `/api/tipos-residuo` | RF08 |
| POST/GET | `/api/registros-residuo` | Classificar/pesar |
| GET | `/api/registros-residuo/rendimento` | RF09 |
| GET | `/api/registros-residuo/alertas-qualidade` | RF15 |
| GET/POST/PUT/DELETE | `/api/destinos` | RF10 |
| GET/POST | `/api/reaproveitamento` | RF11 |
| GET/POST/PUT/DELETE | `/api/metas` | RF12 |
| GET | `/api/financeiro/relatorio` | RF13 |
| GET | `/api/estoques` e `/api/estoques/alertas` | RF14 |
| CRUD | `/api/vendas` (autenticado) | Vendas |
| CRUD | `/api/usuarios` | RNF04 |

## Exemplos rápidos

### Cadastrar coleta (RF01)

```http
POST /api/coletas
Authorization: Bearer token-polo-001
Content-Type: application/json

{
  "poloId": 1,
  "dataColeta": "2026-11-26",
  "horario": "09:00:00",
  "tipoResiduoId": 1,
  "quantidadePrevistaKg": 100,
  "localColeta": "Galpão A"
}
```

### Registrar resíduo com validação de peso (RF15)

```http
POST /api/registros-residuo
Authorization: Bearer token-operador-001

{
  "coletaId": 1,
  "tipoResiduoId": 1,
  "poloId": 1,
  "pesoKg": 150,
  "quantidadeAbacaxiProcessadaKg": 500
}
```

### Venda (RNF03 — exige token de vendas/admin)

```http
POST /api/vendas
Authorization: Bearer token-vendas-001

{
  "tipoResiduoId": 1,
  "poloId": 1,
  "quantidadeKg": 10,
  "valorUnitario": 2.50,
  "dataVenda": "2026-11-25",
  "comprador": "Cooperativa Verde"
}
```

## Mapeamento de requisitos

| ID | Atendimento |
|----|-------------|
| RF01–RF05 | `ColetaController` + `ColetaService` |
| RF06 | `PoloController` |
| RF07 / RNF05 | Calendário + SSE `/api/calendario/stream` |
| RF08 | `TipoResiduoController` + registro de resíduos |
| RF09 | `/api/registros-residuo/rendimento` |
| RF10 | `DestinoResiduoController` |
| RF11 | `InformacaoReaproveitamentoController` |
| RF12 | `MetaArrecadacaoController` (com % atingimento) |
| RF13 / RNF02 | `/api/financeiro/relatorio` |
| RF14 | `/api/estoques/alertas` (`corAlerta`: VERDE/AMARELO/VERMELHO) |
| RF15 | Validação de margem de peso no registro |
| RNF03 | Token/API Key em `/api/vendas` |
| RNF04 | Perfis RBAC + gestão de usuários |
