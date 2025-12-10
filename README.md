# AlgaSensors Monitor

Temperature monitoring and alerting service. Collects temperature data from sensors, evaluates alert conditions, and provides monitoring endpoints.

## Running

### Development

1. Load environment variables:
   ```bash
   set -a; source .env.dev; set +a
   ```
   (Works in bash/zsh)

2. Start the development server:
   ```bash
   ./gradlew bootRun
   ```

The application will be available at `http://localhost:8081`.

### Docker Compose

Start via docker-compose from the root directory:

```bash
docker-compose up algasensors-monitor -d
```

## Environment

Key environment variables (see `.env.dev` for all options):

- `SPRING_DATASOURCE_URL` - H2 database connection
- `SPRING_DATASOURCE_USERNAME` - Database user
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `SPRING_RABBITMQ_HOST` - RabbitMQ host
- `SPRING_RABBITMQ_PORT` - RabbitMQ port
- `SPRING_RABBITMQ_USERNAME` - RabbitMQ credentials
- `SPRING_RABBITMQ_PASSWORD` - RabbitMQ credentials

## Related Services

- [Manager Service](../manager/README.md) - Device management
- [Processor Service](../processor/README.md) - Data processing
- [Client](../client/README.md) - Web dashboard
