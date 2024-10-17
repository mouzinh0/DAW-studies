The purpose of the mappers like InstantMapper, PasswordValidationInfoMapper, and TokenValidationInfoMapper is to convert between Java/Kotlin objects and database values when using Jdbi (Java Database Interface), which simplifies database operations.

Jdbi works by mapping database rows (from SQL queries) to Kotlin or Java objects. Sometimes, database fields don’t directly match the types you use in your Kotlin classes. Mappers help bridge that gap.

### How Mappers Work:

* **Jdbi Queries and Mappings:** When you execute a query in Jdbi, it returns results from the database. These results are often in basic types (like String, Long, Int), but your Kotlin objects might have more complex types (like Instant, PasswordValidationInfo, or TokenValidationInfo).

* **Custom Mappers:** To deal with these more complex types, you provide a custom ColumnMapper that tells Jdbi how to convert from the database value (e.g., a Long representing a timestamp or a String representing a hashed password) to the Kotlin type you’re working with (like Instant or PasswordValidationInfo).

### Examples:

* **InstantMapper:**
The database likely stores timestamps as Unix epoch seconds (as a Long type), but in your Kotlin code, you're using the Instant class from kotlinx.datetime. The InstantMapper converts the Long from the database to an Instant in Kotlin.

```
class InstantMapper : ColumnMapper<Instant> {
    @Throws(SQLException::class)
    override fun map(rs: ResultSet, columnNumber: Int, ctx: StatementContext): Instant =
        Instant.fromEpochSeconds(rs.getLong(columnNumber)) // Convert Long to Instant
}
```
**Why it's needed:** If your database stores timestamps as Long, but your code uses Instant, the mapper handles the conversion when retrieving data.

* **PasswordValidationInfoMapper:**
  The database stores password validation info (such as a hashed password) as a String, but in your Kotlin code, you wrap this in the PasswordValidationInfo class. This mapper converts the String from the database into a PasswordValidationInfo object.
```
class PasswordValidationInfoMapper : ColumnMapper<PasswordValidationInfo> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): PasswordValidationInfo =
        PasswordValidationInfo(r.getString(columnNumber)) // Wrap the String as PasswordValidationInfo
}
```
**Why it's needed:** Your database stores the password hash as a String, but your code expects a PasswordValidationInfo object.

### Why You Need These Mappers:

* **Jdbi Cannot Automatically Map Custom Types:**
   Jdbi has built-in support for mapping basic types like String, Int, Long, etc., but when it comes to custom types like Instant, PasswordValidationInfo, or TokenValidationInfo, Jdbi needs to know how to convert those database types into your Kotlin objects. These mappers provide that logic.

* **Simplified Querying:** Once you have these mappers in place, you can perform queries without manually converting the database result into the correct Kotlin object. Jdbi will automatically apply these mappers.


### How They Are Used:

In repository implementation (e.g., JdbiUsersRepository), when performing a query that involves fields like Instant, PasswordValidationInfo, or TokenValidationInfo, Jdbi will use the registered mappers to convert the database fields into the appropriate Kotlin types.

```
override fun getUserByUsername(username: String): User? =
    handle.createQuery("select * from dbo.Users where username = :username")
        .bind("username", username)
        .mapTo<User>()  // Uses mappers to map query result to User object
        .singleOrNull()

```


