# train-service

Helidon SE application that uses the dbclient API with MySQL database.

## Build and run


This example requires a database, see [Database Setup](#database-setup).


With JDK21
```bash
mvn package
java -jar target/train-service.jar
```

## Exercise the application

Basic:
```
curl -X GET http://localhost:8080/simple-greet
Hello World!
```


JSON:
```
curl -X GET http://localhost:8080/greet
{"message":"Hello World!"}

curl -X GET http://localhost:8080/greet/Joe
{"message":"Hello Joe!"}

curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:8080/greet/greeting

curl -X GET http://localhost:8080/greet/Jose
{"message":"Hola Jose!"}
```

```shell
curl -X POST -H "Content-Type: application/json" \
 -d '{"ssn" : "123-45-6789", "firstName" : "Frank", "lastName" : "Helidon", "dateOfBirth" : "02/14/2019"}' \
  http://localhost:8080/creditscore
```

You'll notice a short delay as the application computes the credit score.
Now repeat the same request. You'll see the score is returned instantly as it is retrieved from the cache.


Tracing:
```
curl -X GET http://localhost:8080/traced
```


Database:
```
# List all Pokémon
curl http://localhost:8080/db/pokemon

# List all Pokémon types
curl http://localhost:8080/db/type

# Get a single Pokémon by id
curl http://localhost:8080/db/pokemon/2

# Get a single Pokémon by name
curl http://localhost:8080/db/pokemon/name/Squirtle

# Add a new Pokémon Rattata
curl -i -X POST -d '{"id":7,"name":"Rattata","idType":1}' http://localhost:8080/db/pokemon

# Rename Pokémon with id 7 to Raticate
curl -i -X PUT -d '{"id":7,"name":"Raticate","idType":2}' http://localhost:8080/db/pokemon

# Delete Pokémon with id 7
curl -i -X DELETE http://localhost:8080/db/pokemon/7
```



## Try metrics

```
# Prometheus Format
curl -s -X GET http://localhost:8080/observe/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:8080/observe/metrics
{"base":...
. . .
```


## Try health

This example shows the basics of using Helidon SE Health. It uses the
set of built-in health checks that Helidon provides plus defines a
custom health check.

Note the port number reported by the application.

Probe the health endpoints:

```bash
curl -X GET http://localhost:8080/observe/health
curl -X GET http://localhost:8080/observe/health/ready
```


## Tracing

### Set up Jaeger

First, you need to run the Jaeger tracer. Helidon will communicate with this tracer at runtime.

Run Jaeger within a docker container:
```
docker run -d --name jaeger\
   -e COLLECTOR_ZIPKIN_HOST_PORT=:9411\
   -e COLLECTOR_OTLP_ENABLED=true\
   -p 6831:6831/udp\
   -p 6832:6832/udp\
   -p 5778:5778\
   -p 16686:16686\
   -p 4317:4317\   
   -p 4318:4318\
   -p 14250:14250\
   -p 14268:14268\
   -p 14269:14269\
   -p 9411:9411\
   jaegertracing/all-in-one:1.43
```

### View Tracing Using Jaeger UI

Jaeger provides a web-based UI at http://localhost:16686, where you can see a visual representation of
the same data and the relationship between spans within a trace.


## Building a Native Image

The generation of native binaries requires an installation of GraalVM 22.1.0+.

You can build a native binary using Maven as follows:

```
mvn -Pnative-image install -DskipTests
```

The generation of the executable binary may take a few minutes to complete depending on
your hardware and operating system. When completed, the executable file will be available
under the `target` directory and be named after the artifact ID you have chosen during the
project generation phase.



### Database Setup

In the `pom.xml` and `application.yaml` we provide configuration needed for mysql database.
Start your database before running this example.

Example docker commands to start databases in temporary containers:

MySQL:
```
docker run --rm --name mysql -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=root \
    -e MYSQL_DATABASE=pokemon \
    -e MYSQL_USER=user \
    -e MYSQL_PASSWORD=changeit \
    mysql:5.7
```



## Building the Docker Image

```
docker build -t train-service .
```

## Running the Docker Image

```
docker run --rm -p 8080:8080 train-service:latest
```

Exercise the application as described above.
                                

## Run the application in Kubernetes

If you don’t have access to a Kubernetes cluster, you can [install one](https://helidon.io/docs/latest/#/about/kubernetes) on your desktop.

### Verify connectivity to cluster

```
kubectl cluster-info                        # Verify which cluster
kubectl get pods                            # Verify connectivity to cluster
```

### Deploy the application to Kubernetes

```
kubectl create -f app.yaml                              # Deploy application
kubectl get pods                                        # Wait for quickstart pod to be RUNNING
kubectl get service  train-service                     # Get service info
kubectl port-forward service/train-service 8081:8080   # Forward service port to 8081
```

You can now exercise the application as you did before but use the port number 8081.

After you’re done, cleanup.

```
kubectl delete -f app.yaml
```


## Building a Custom Runtime Image

Build the custom runtime image using the jlink image profile:

```
mvn package -Pjlink-image
```

This uses the helidon-maven-plugin to perform the custom image generation.
After the build completes it will report some statistics about the build including the reduction in image size.

The target/train-service-jri directory is a self contained custom image of your application. It contains your application,
its runtime dependencies and the JDK modules it depends on. You can start your application using the provide start script:

```
./target/train-service-jri/bin/start
```

Class Data Sharing (CDS) Archive
Also included in the custom image is a Class Data Sharing (CDS) archive that improves your application’s startup
performance and in-memory footprint. You can learn more about Class Data Sharing in the JDK documentation.

The CDS archive increases your image size to get these performance optimizations. It can be of significant size (tens of MB).
The size of the CDS archive is reported at the end of the build output.

If you’d rather have a smaller image size (with a slightly increased startup time) you can skip the creation of the CDS
archive by executing your build like this:

```
mvn package -Pjlink-image -Djlink.image.addClassDataSharingArchive=false
```

For more information on available configuration options see the helidon-maven-plugin documentation.
                                
