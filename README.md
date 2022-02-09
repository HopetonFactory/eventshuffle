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