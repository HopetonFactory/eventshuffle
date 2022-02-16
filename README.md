# Eventshuffle docs

## Setup

```
# clone repository 
git clone .... 
```

### Run database in Docker 

``` 
# Build PostgreSQL container from the Dockerfile
docker build -t eventshuffle-postgres .

# Run the container
docker run --rm -P -d --name eventshuffle-db eventshuffle-postgres

# Access container
docker exec -it eventshuffle-db /bin/sh
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