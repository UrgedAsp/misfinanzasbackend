# misfinanzas — Backend

Gestor de finanzas personales. API REST con Spring Boot 4.1.1, Java 25,
PostgreSQL 18, Flyway y Docker.

## Requisitos

- **Docker Desktop** — suficiente para correr todo (backend + base de datos).
- **JDK 25** — solo si quieres correr el backend desde el IDE.

## Levantar todo con un comando

```bash
docker compose up --build -d
```

| Comando | Para qué |
|---|---|
| `docker compose up --build -d` | Levanta todo. Usa `--build` si cambiaste código. |
| `docker compose up -d` | Levanta reusando la imagen (sin recompilar). |
| `docker compose logs -f app` | Ver el log del backend (migraciones de Flyway, errores). |
| `docker compose ps` | Ver estado. La DB debe aparecer como `(healthy)`. |
| `docker compose down` | Detiene todo **conservando** los datos. |
| `docker compose down -v` | Detiene y **borra la base de datos**. Flyway la recrea, pero se pierden los datos. |

## Verificar que está funcionando

```bash
curl -i localhost:8080/api/categories      # 200 OK
docker compose ps                          # app: Up  |  db: Up (healthy)
```

También puedes probar los endpoints desde Swagger UI
(<http://localhost:8080/swagger-ui.html>).

## Configuración

La configuración se lee de variables de entorno con valores por defecto,
para que el mismo jar funcione tanto en Docker como en el IDE:

| Variable | Valor por defecto | Notas |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/finanzas` | Dentro de Docker el host es `db`, no `localhost`. |
| `DB_USER` | `postgres` | |
| `DB_PASS` | `postgres` | |

## Desarrollo desde el IDE (modo híbrido)

```bash
docker compose up -d db     # solo la base de datos
./mvnw spring-boot:run      # el backend desde el IDE
```

Ojo: el puerto 8080 lo ocupa la app. No dejes el contenedor `app` corriendo
al mismo tiempo o el arranque fallará por conflicto de puertos.

## Migraciones (Flyway)

- Viven en `src/main/resources/db/migration/`.
- Se aplican **solas** al arrancar la app.
- Una migración ya aplicada **no se edita**: crea `V2__nombre.sql`.
  (Si te equivocaste, `docker compose down -v` y vuelve a empezar.)
- **Postgres 18+**: el volumen se monta en `/var/lib/postgresql`,
  no en `/var/lib/postgresql/data`. Con la ruta antigua el contenedor
  arranca y se muere de inmediato.

## Endpoints

Categorías:

| Método | Ruta | Respuesta |
|---|---|---|
| `POST` | `/api/categories` | 201 |
| `GET` | `/api/categories` | 200 |
| `GET` | `/api/categories/{id}` | 200 / 404 |
| `PUT` | `/api/categories/{id}` | 200 / 404 |
| `DELETE` | `/api/categories/{id}` | 204 / 404 / 409 |

`DELETE` devuelve **409** si la categoría tiene movimientos asociados.

Transacciones (`GET` acepta `startDate`, `endDate`, `type`, `categoryId`
y parámetros de paginación `page` / `size`):

| Método | Ruta | Respuesta |
|---|---|---|
| `POST` | `/api/transactions` | 201 |
| `GET` | `/api/transactions` | 200 (paginado) |
| `GET` | `/api/transactions/{id}` | 200 / 404 |
| `PUT` | `/api/transactions/{id}` | 200 / 404 |
| `DELETE` | `/api/transactions/{id}` | 204 / 404 |

Presupuestos:

| Método | Ruta | Respuesta |
|---|---|---|
| `POST` | `/api/budgets` | 201 / 400 |
| `GET` | `/api/budgets` | 200 |
| `GET` | `/api/budgets/{id}` | 200 / 404 |
| `PUT` | `/api/budgets/{id}` | 200 / 404 |
| `DELETE` | `/api/budgets/{id}` | 204 / 404 |

`POST` y `PUT` devuelven **400** si la categoría no es de tipo `EXPENSE`.
Cada respuesta incluye `spentAmount`, lo ya gastado en ese periodo.

Resumen:

| Método | Ruta | Respuesta |
|---|---|---|
| `GET` | `/api/summary?month=&year=` | 200 / 400 |

Devuelve totales de ingreso, gasto y balance, más el desglose de gastos
por categoría.

### Documentación interactiva

Con la app corriendo: <http://localhost:8080/swagger-ui.html>

## Formato de errores

Todos los errores de negocio y de validación se devuelven como
`ProblemDetail` (RFC 7807):

```json
{
  "type": "about:blank",
  "title": "Category Not Found",
  "status": 404,
  "detail": "La categoria con id 99 no se encontró"
}
```

## Tests

```bash
./mvnw test
```