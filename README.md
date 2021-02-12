# sustain-canry
## overview
High-level repo to test codebases for project sustain

## dynspark
A grpc framework for executing Apache Spark jobs over MongoDB
### build
    gradle :server:build
    gradle :client:build
### run
    java -cp "server/build/libs/*" io.blackpine.sustain.dynspark.Server
    java -cp "client/build/libs/*" io.blackpine.sustain.dynspark.Client foo bar

## grpc-json-relay
An example using a JsonRelayServer to allow json formatted grpc requests / responses
### build
    gradle :server:build
    gradle :client:build
### run
    java -cp "server/build/libs/*" io.blackpine.sustain.grpcJsonRelay.Server
    java -cp "client/build/libs/*" io.blackpine.sustain.grpcJsonRelay.Client foo 4

## resources
- [mongodb spark connector](https://docs.mongodb.com/spark-connector/master/java-api)
- [standalone spark cluster](https://spark.apache.org/docs/latest/spark-standalone.html)

## todo
- everything
