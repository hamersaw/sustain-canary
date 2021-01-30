# sustain-canry
## overview
High-level repo to test codebases for project sustain

## dynspark
A gRPC framework for executing Apache Spark jobs over MongoDB
### build
    gradle :server:build
    gradle :client:build
### run
    java -cp "server/build/libs/*" io.blackpine.sustain.dynspark.Server
    java -cp "client/build/libs/*" io.blackpine.sustain.dynspark.Client foo bar

## resources
- [mongodb spark connector](https://docs.mongodb.com/spark-connector/master/java-api)
- [standalone spark cluster](https://spark.apache.org/docs/latest/spark-standalone.html)

## todo
- everything
