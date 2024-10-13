The backend component is composed by two sub-components:

* A Relational Data Base Management System (RDBMS), holding a relational database with the systems persisted information.
* An application running in the Java Virtual Machine, exposing the HTTP API for consumption by the frontend and performing interactions with the RDBMS.


The backend application is a JVM-based application, written using the Kotlin Programming Language. It is composed by a set of classes and functions, organized into four main groups:

* Repository group - contains functions and types to interact with the RDBMS and its database.

* Services group - contains functions and types providing domain operations, such as creating an user, creating a game, or making a turn play on a game.

* Domain group - contains functions and types representing domain concepts.

* HTTP group - contains functions and types responsible for exposing and implementing the HTTP API.


### The domain group

The domain group is composed by types and functions that represent domain concepts, such as a user, a stored token, or a game. One of its distinct properties is that the domain group does not depend on types and functions of any of the other groups.

The types and functions of the domain group define a domain language used by all other groups.

### The repository group

The repository group defines types and functions to interact with the persisted data namely the data managed by the RDBMS. It is composed by:

* Interfaces that define the signature of the interactions with the persisted data.

* Concrete classes that implement these interfaces, for one or more ways of ways of persisting, changing, and querying data.

> The repository group classes and functions may use items from the domain group, however they should not use anything from the other groups (services and HTTP).
