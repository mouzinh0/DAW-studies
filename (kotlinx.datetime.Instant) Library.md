### JDBC doesn't natively understand kotlinx.datetime.Instant, we need to ensure that:

- When inserting/updating data: Convert kotlinx.datetime.Instant to a type that JDBC can handle (e.g., Long, java.sql.Timestamp).
- When reading data: Convert from JDBC types to kotlinx.datetime.Instant.
