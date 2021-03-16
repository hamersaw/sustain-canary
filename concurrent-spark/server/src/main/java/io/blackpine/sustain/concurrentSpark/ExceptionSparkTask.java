package io.blackpine.sustain.concurrentSpark;

import io.blackpine.sustain.concurrentSpark.protos.Concurrentspark;

import io.grpc.stub.StreamObserver;

import org.apache.spark.api.java.JavaSparkContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionSparkTask implements SparkTask<Boolean> {
    protected static final Logger log =
        LoggerFactory.getLogger(ExceptionSparkTask.class);

    protected Concurrentspark.ExceptionRequest request;
    protected StreamObserver<Concurrentspark.ExceptionResponse> responseObserver;

    public ExceptionSparkTask(Concurrentspark.ExceptionRequest request,
            StreamObserver<Concurrentspark.ExceptionResponse> responseObserver) {
        this.request = request;
        this.responseObserver = responseObserver;
    }

    @Override
    public Boolean execute(JavaSparkContext sparkContext) throws Exception {
        throw new Exception("test exception");
    }
}
