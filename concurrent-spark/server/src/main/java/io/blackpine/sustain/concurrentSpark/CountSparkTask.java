package io.blackpine.sustain.concurrentSpark;

import io.grpc.stub.StreamObserver;

import org.apache.spark.api.java.JavaSparkContext;

public class CountSparkTask implements SparkTask<Boolean> {
    private String database;
    private String collection;

    public CountSparkTask(String database, String collection) {
        this.database = database;
        this.collection = collection;
    }

    @Override
    public Boolean execute(JavaSparkContext sparkContext) throws Exception {
        /*// initailize read configuratoin
        ReadConfig readConfig = ReadConfig.create(sparkContext);

        // load RDD
        JavaMongoRDD<Document> rdd =
            MongoSpark.load(sparkContext, readConfig);

        // perform count evaluation
        long count = rdd.count();

        // close spark context
        sparkContext.close();

        // initailize response
        Concurrentspark.CountResponse response =
            Concurrentspark.CountResponse.newBuilder()
                .setCount(count)
                .build();

        // send response
        responseObserver.onNext(response);*/

        return true;
    }
}
