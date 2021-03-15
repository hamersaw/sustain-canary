package io.blackpine.sustain.concurrentSpark;

import io.blackpine.sustain.concurrentSpark.protos.ConcurrentSparkServiceGrpc;
import io.blackpine.sustain.concurrentSpark.protos.Concurrentspark;

import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.ReadConfig;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;

import io.grpc.stub.StreamObserver;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import org.bson.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConcurrentSparkServiceImpl 
        extends ConcurrentSparkServiceGrpc.ConcurrentSparkServiceImplBase {
    protected static final Logger log =
        LoggerFactory.getLogger(ConcurrentSparkServiceGrpc.class);

    @Override
    public void count(Concurrentspark.CountRequest request,
            StreamObserver<Concurrentspark.CountResponse> responseObserver) {
        log.info("received query [database='" + request.getDatabase()
            + "', collection='" + request.getCollection() + "']");

        try {
            // open spark session - TODO fix hardcoded values
            SparkSession sparkSession = SparkSession.builder()
                .master("spark://nightcrawler:7077")
                .config("spark.mongodb.input.uri",
                    "mongodb://admin:password@127.0.0.1:27017/admin")
                .config("spark.mongodb.input.database",
                    request.getDatabase())
                .config("spark.mongodb.input.collection",
                    request.getCollection())
                .appName("ConcurrentSpark")
                .getOrCreate();

            // initialize spark context
            JavaSparkContext sparkContext = 
                new JavaSparkContext(sparkSession.sparkContext());

            // add dependency jar files to spark context - TODO fix hardcoded values
            sparkContext.addJar("/home/hamersaw/development/sustain-canary/concurrentSpark/libs/mongo-spark-connector_2.12-3.0.0.jar");
            sparkContext.addJar("/home/hamersaw/development/sustain-canary/concurrentSpark/libs/bson-4.2.0.jar");
            sparkContext.addJar("/home/hamersaw/development/sustain-canary/concurrentSpark/libs/mongo-java-driver-3.12.7.jar");

            // initailize read configuratoin
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
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("failed to evaluate count", e);
            responseObserver.onError(e);
        }
    }
}
