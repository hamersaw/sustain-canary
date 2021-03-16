package io.blackpine.sustain.concurrentSpark;

import io.blackpine.sustain.concurrentSpark.ConcurrentSparkServiceImpl;

import io.grpc.ServerBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    protected static final Logger log =
        LoggerFactory.getLogger(Server.class);
    protected static final int PORT = 15605;

    public static void main(String[] args) {
        // initialize SparkService
        SparkService sparkService =
            new SparkService("spark://nightcrawler:7077", 2);

        sparkService.addJar("server/build/libs/mongo-spark-connector_2.12-3.0.0.jar");
        sparkService.addJar("server/build/libs/bson-4.0.5.jar");
        sparkService.addJar("server/build/libs/mongo-java-driver-3.12.8.jar");

        // initialize server
        io.grpc.Server server = ServerBuilder.forPort(PORT)
            .addService(new ConcurrentSparkServiceImpl(sparkService))
            .build();

        // start server
        try {
            server.start();
        } catch (Exception e) {
            log.error("failed to start server", e);
            System.exit(1);
        }

        // block until manually stopped
        try {
            server.awaitTermination();
        } catch (Exception e) {
            log.error("server failure", e);
            System.exit(1);
        }
    }
}
