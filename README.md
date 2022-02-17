```
 /$$$$$$$$                             /$$     /$$$$$$  /$$                  /$$$$$$  /$$$$$$  /$$          
| $$_____/                            | $$    /$$__  $$| $$                 /$$__  $$/$$__  $$| $$          
| $$    /$$    /$$/$$$$$$  /$$$$$$$  /$$$$$$ | $$  \__/| $$$$$$$  /$$   /$$| $$  \__/ $$  \__/| $$  /$$$$$$ 
| $$$$$|  $$  /$$/$$__  $$| $$__  $$|_  $$_/ |  $$$$$$ | $$__  $$| $$  | $$| $$$$   | $$$$    | $$ /$$__  $$
| $$__/ \  $$/$$/ $$$$$$$$| $$  \ $$  | $$    \____  $$| $$  \ $$| $$  | $$| $$_/   | $$_/    | $$| $$$$$$$$
| $$     \  $$$/| $$_____/| $$  | $$  | $$ /$$/$$  \ $$| $$  | $$| $$  | $$| $$     | $$      | $$| $$_____/
| $$$$$$$$\  $/ |  $$$$$$$| $$  | $$  |  $$$$/  $$$$$$/| $$  | $$|  $$$$$$/| $$     | $$      | $$|  $$$$$$$
|________/ \_/   \_______/|__/  |__/   \___/  \______/ |__/  |__/ \______/ |__/     |__/      |__/ \_______/ 
```

## Project setup

```
# clone repository 
> git clone .... 
```

### Build and Run with docker-compose

``` 
# Build project
> mvn clean install

# Build containers
> docker-compose build

# Run postgresql and the backend service
> docker-compose up -d 

Service should now be accessible from localhost:8080
```

### Run individually in local environment

#### Run database in Docker 

Or use whatever database instance you like...

``` 
# Build (in module db inside eventshuffle-backend)
> docker build -t eventshuffle-postgres .

# Run the container
> docker run --rm -P -d --name eventshuffle-db eventshuffle-postgres

# Access container
> docker exec -it eventshuffle-db /bin/sh

# PostgreSQL can be opened with following command inside the container
> psql 
```

#### Run eventshuffle-backend 

``` 
# Build project
> mvn clean install

# Run Spring Boot Application locally
> mvn clean compile spring-boot:start
```

## Architecture and technical notes

### Project structure
```
-- evenshuffle (project root)
 |
 |-- eventshuffle-backend (backend implementation)
 |   |-- db (database container)
 |   |-- src (source)
 |
 |-- openapi-specification (backend interface)

```

### Application 

Application is composed of generated API from the module `openapi-specification` and jOOQ-database ORM, which generates a class model from the SQL-files located in `eventshuffle-backend/resources/db/migrations/`. 

After compiling the necessary modules with command `mvn clean install`, only thing that needs to be hand-written are the database queries along with an implementation of `ApiApiDelegate`, which is injected into the generated REST interface class, and contains the business logic for the interface. 

### Database

The generated jOOQ-files were stored in the version control, so another programmer may instantly notice, if any database definitions are changed along project modifications. 

*Currently only PostgreSQL is supported.* The underlying database should be able to handle unique indices and optimistic locking to work properly. Moreover, for PostgreSQL and jOOQ to work together, there is a configuration related to removing quotes from the jOOQ-generated SQL-queries as PostgreSQL does not support those. 

## TODO's :) 

* Implement an actual Domain Model for the application
* Implement actual GitHub Actions workflows 
* Add code quality tools to the project
* Deploy into "Production" 