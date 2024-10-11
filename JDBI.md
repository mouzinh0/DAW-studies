**JDBI** is a **Java database interaction library** designed to provide a lightweight and flexible way 
to access relational databases in Java and Kotlin applications.
It serves as a middle ground between high-level Object-Relational Mapping (ORM) libraries 
like Hibernate and low-level JDBC (Java Database Connectivity) APIs.
JDBI allows developers to write raw SQL queries while simplifying common database operations, 
offering more control and flexibility than ORM frameworks, without the verbosity and complexity of JDBC.

### Key Concepts of JDBI:

* SQL-Centric Approach:

Unlike ORM tools that automatically map objects to database tables, JDBI focuses on SQL. You write SQL queries directly, giving you full control over how data is retrieved, inserted, or updated.

* Simplifies JDBC:

JDBI reduces the boilerplate code required when using plain JDBC. With JDBC, you typically need to manage connections, prepared statements, result sets, and exception handling manually. JDBI abstracts much of this, allowing you to focus on the logic of your database interactions.

* Flexible Mapping:

JDBI allows mapping between SQL query results and Java/Kotlin objects. You can use simple mappers (e.g., mapping a result row to a domain object) or custom mappers for more complex conversions, such as mapping a timestamp to a Kotlin Instant.

* Plugins:

JDBI is extendable through plugins. For instance, there are plugins for specific databases (like PostgreSQL) or programming languages (such as Kotlin), providing additional functionality.

* Transaction Support:

JDBI offers built-in support for managing database transactions. You can define a transaction block where all operations are either committed together if successful or rolled back in case of failure.

* Handle API:

A central part of JDBI is the Handle, which represents a connection to the database. It allows you to create queries, execute updates, and manage transactions.


### Why Use JDBI?

* Direct SQL Control:

Developers who prefer writing SQL for performance, flexibility, or readability often prefer JDBI. It avoids the complexity of an ORM's automatic query generation and lets you craft SQL statements directly.

* Less Boilerplate:

Compared to plain JDBC, JDBI reduces the amount of code required for common database operations, such as executing queries and mapping results to objects.

* Lightweight:

JDBI is not as heavy as an ORM like Hibernate or JPA. It's faster to set up and has fewer abstractions, which makes it suitable for applications where you need fine-grained control over database queries.

* Extensible and Flexible:

JDBI allows custom mappers, plugins, and extensions to handle more complex use cases. You can register custom result mappers, making it easier to integrate with your domain models.


### Example: Basic JDBI Usage

```
val jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/mydb", "user", "password")

jdbi.useHandle<Exception> { handle ->
    // Insert a user
    handle.createUpdate("INSERT INTO users (username, password) VALUES (:username, :password)")
        .bind("username", "johndoe")
        .bind("password", "securepassword")
        .execute()

    // Query the user
    val username = handle.createQuery("SELECT username FROM users WHERE username = :username")
        .bind("username", "johndoe")
        .mapTo<String>()
        .findOnly()

    println("User found: $username")
}
```

![image](https://github.com/user-attachments/assets/cc51eb5f-957d-47d3-b53e-415a7559398f)




