### Initial Setup

Pull the PostgreSQL Docker Image:

```
 docker pull postgres
```

Run a PostgreSQL Container:

```
docker run --name my-postgres -e POSTGRES_PASSWORD=mysecretpassword -d -p 5432:5432 postgres
```

* --name my-postgres: Names your PostgreSQL container.
* -e POSTGRES_PASSWORD=mysecretpassword: Sets the password for the postgres user.
* -d: Runs the container in detached mode.
* -p 5432:5432: Maps the PostgreSQL default port (5432) from the container to your machine.


After the container is running, this will open the psql shell where you can run SQL commands:

```
docker exec -it my-postgres psql -U postgres
```

Stop the PostgreSQL container:

```
docker stop my-postgres
```

Restart the container:
```
docker start my-postgres
```

Some more:
```
docker run -it im-project-db-tests /bin/bash

docker exec -it db-tests /bin/bash

ls /app/bin/wait-for-postgres.sh

docker exec -it db-tests printenv POSTGRES_PASSWORD

```
