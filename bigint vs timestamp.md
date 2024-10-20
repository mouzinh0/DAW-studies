### Difference Between bigint and timestamp for Time Storage:

* bigint: Storing time as a bigint typically means you're storing a Unix epoch timestamp, which is the number of seconds or milliseconds since January 1, 1970. This is a simple integer format, useful if you're manually converting between time units in your application logic.

* timestamp: The timestamp type (or timestamptz for timezone-aware) is PostgreSQL's dedicated type for storing date-time values. It's more readable and provides automatic formatting and timezone handling if necessary.

* 
