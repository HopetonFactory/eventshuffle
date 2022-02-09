# Eventshuffle docs

## Setup

```
# clone repository 
git clone .... 
```

### Run database in Docker 

``` 
# Build PostgreSQL container from the Dockerfile
docker build -t eg_postgresql

# Run the container
docker run --rm -P -d --name pg_test eg_postgresql

# Access container
docker exec -it pg_test /bin/sh
```

### Run eventshuffle-backend 

``` 
# Build project
mvn clean install

# Run Spring Boot Application locally
mvn clean compile spring-boot:start
```

### Build and Run Docker container

``` 
TODO
```

## Architecture

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

``` 
TODO
```

## Data model

``` 
TODO
```