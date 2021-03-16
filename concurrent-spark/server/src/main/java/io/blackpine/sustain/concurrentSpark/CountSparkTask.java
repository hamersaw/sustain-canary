package io.blackpine.sustain.concurrentSpark;

import io.blackpine.sustain.concurrentSpark.protos.Concurrentspark;

import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.ReadConfig;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;

import io.grpc.stub.StreamObserver;

import org.apache.spark.api.java.JavaSparkContext;

import org.bson.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CountSparkTask implements SparkTask<Boolean> {
    protected static final Logger log =
        LoggerFactory.getLogger(CountSparkTask.class);

    protected Concurrentspark.CountRequest request;
    protected StreamObserver<Concurrentspark.CountResponse> responseObserver;

    public CountSparkTask(Concurrentspark.CountRequest request,
            StreamObserver<Concurrentspark.CountResponse> responseObserver) {
        this.request = request;
        this.responseObserver = responseObserver;
    }

    @Override
    public Boolean execute(JavaSparkContext sparkContext) throws Exception {
        // initialize ReadConfig
        Map<String, String> readOverrides = new HashMap();
        readOverrides.put("spark.mongodb.input.collection",
            request.getCollection());
        readOverrides.put("spark.mongodb.input.database",
            request.getDatabase());
        readOverrides.put("spark.mongodb.input.uri",
            "mongodb://127.0.0.1:27017");

        ReadConfig readConfig = 
            ReadConfig.create(sparkContext.getConf(), readOverrides);

        // load RDD
        JavaMongoRDD<Document> rdd =
            MongoSpark.load(sparkContext, readConfig);

        // perform count evaluation
        long count = rdd.count();

        // initailize response
        Concurrentspark.CountResponse response =
            Concurrentspark.CountResponse.newBuilder()
                .setCount(count)
                .build();

        // send response
        responseObserver.onNext(response);

        return true;
    }
}
