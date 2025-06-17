# RAG Spring AI

This project provides a minimal Retrieval-Augmented Generation (RAG) REST API built with Spring Boot.

* **/upload** – Upload a PDF or text document, automatically split into chunks, embed each chunk with DeepSeek Embeddings, and persist them in PostgreSQL/pgvector.
* **/query** – Ask a question; the service retrieves the most relevant chunks with a similarity search and calls DeepSeek LLM to generate the answer.

---

## Running locally

### 1. Prerequisites

* Java 17+
* Maven 3.9+
* Docker (for a quick Postgres instance)

### 2. Start PostgreSQL with pgvector

```bash
# Creates a local DB listening on 5432 with the pgvector extension pre-installed
# Data is kept in the `pgdata` folder next to the repo.
docker run --name rag-pg -e POSTGRES_DB=rag -e POSTGRES_USER=rag -e POSTGRES_PASSWORD=rag \
  -p 5432:5432 -v $(pwd)/pgdata:/var/lib/postgresql/data \
  ankane/pgvector:latest
```

Alternatively, install the `pgvector` extension on an existing database:

```sql
CREATE EXTENSION IF NOT EXISTS vector;
```

Then create the table used by this service:

```sql
CREATE TABLE IF NOT EXISTS document_chunk (
    id UUID PRIMARY KEY,
    file_name TEXT NOT NULL,
    chunk TEXT NOT NULL,
    embedding VECTOR(1536) NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_document_chunk_embedding ON document_chunk USING ivfflat (embedding vector_cosine_ops);
```

### 3. Configure environment variables

| Variable | Description |
|----------|-------------|
| `DEEPSEEK_API_KEY` | Your DeepSeek (OpenAI-compatible) API key |
| `DEEPSEEK_BASE_URL` | Base URL of the API (e.g. `https://api.deepseek.com/v1`) |
| `SPRING_DATASOURCE_URL` | JDBC URL, e.g. `jdbc:postgresql://localhost:5432/rag` |
| `SPRING_DATASOURCE_USERNAME` | DB username |
| `SPRING_DATASOURCE_PASSWORD` | DB password |

Example:

```bash
export DEEPSEEK_API_KEY=sk-xxx
export DEEPSEEK_BASE_URL=https://api.deepseek.com/v1
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/rag
export SPRING_DATASOURCE_USERNAME=rag
export SPRING_DATASOURCE_PASSWORD=rag
```

### 4. Build & run

```bash
./mvnw spring-boot:run
```

### 5. Explore the API

Open `http://localhost:8080/swagger-ui.html` to use the Swagger UI.

---

## Project structure (Clean Architecture-ish)

```
├── config/                # Configuration classes
│   ├── AppConfig.java
│   ├── DatabaseConfig.java
│   └── OpenApiConfig.java
├── controller/            # REST controllers
│   └── RagController.java
├── dto/                   # Data transfer objects
│   ├── QueryRequest.java
│   ├── QueryResponse.java
│   └── UploadResponse.java
├── exception/             # Exception handling
│   └── GlobalExceptionHandler.java
├── model/                 # Database entities
│   └── DocumentChunk.java
├── repository/            # Database repositories
│   └── DocumentChunkRepository.java
├── service/               # Business logic
│   ├── EmbeddingService.java
│   ├── FileProcessingService.java
│   ├── LlmService.java
│   └── RagService.java
└── RagApplication.java    # Main application class
```

---

## Notes

* The code purposefully keeps implementation simple while showcasing best practices (layers, WebClient, environment variables, proper logging, OpenAPI documentation).
* Replace hard-coded model names (`deepseek-llm`, `deepseek-embedding`) with the actual names once available.
* Improve chunking/embedding strategy (token-based) for production use.
