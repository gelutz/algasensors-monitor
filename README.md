# AlgaSensors Monitor

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-6DB33F?logo=springboot&logoColor=white)

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

### Seeding Temperature Logs

To populate the database with historical temperature data for testing/development:

```bash
./gradlew bootRun --args='--seed'
```

This generates temperature logs for every minute of the last 30 days for all registered sensors.

To specify a custom number of days:

```bash
./gradlew bootRun --args='--seed=60'
```

**Note:** Ensure sensors exist in the database before running the seeder. The seeder reads sensors from the `SensorMonitoring` table.

### Docker Compose

Start via docker-compose from the root directory:

```bash
docker-compose up monitor -d
```

## Environment

Key environment variables (see `.env.dev` for all options):

- `SPRING_APPLICATION_NAME=monitor` - Service name
- `SERVER_PORT=8081` - HTTP port
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
- [Generator Service](../generator/README.md) - Mock data generator
- [Client](../client/README.md) - Web dashboard
