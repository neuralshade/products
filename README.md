# Products API

API simples para operações envolvendo produtos.

## Requisitos
- Java 17
- Maven 3.9+

## Executar
```bash
./mvnw spring-boot:run
```

## Endpoints
- `POST /products`
- `GET /products` (suporta paginação via `page`, `size`, `sort`)
- `GET /products/{id}`
- `PUT /products/{id}`
- `DELETE /products/{id}`
