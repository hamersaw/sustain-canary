package io.blackpine.sustain.concurrentSpark;

import io.blackpine.sustain.concurrentSpark.protos.ConcurrentSparkServiceGrpc;
import io.blackpine.sustain.concurrentSpark.protos.Concurrentspark;

import io.grpc.stub.StreamObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public class ConcurrentSparkServiceImpl 
        extends ConcurrentSparkServiceGrpc.ConcurrentSparkServiceImplBase {
    protected static final Logger log =
        LoggerFactory.getLogger(ConcurrentSparkServiceGrpc.class);
    protected SparkService sparkService;

    public ConcurrentSparkServiceImpl(SparkService sparkService) {
        this.sparkService = sparkService;
    }

    @Override
    public void exception(Concurrentspark.ExceptionRequest request,
            StreamObserver<Concurrentspark.ExceptionResponse> responseObserver) {
        try {
            // initialize ExceptionSparkTask
            ExceptionSparkTask exceptionSparkTask =
                new ExceptionSparkTask(request, responseObserver);

            // execute ExceptionCountTask on SparkService
            Future<Boolean> future =
                this.sparkService.submit(exceptionSparkTask, "exception-TODO");
            future.get();

            // send response
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("failed to evaluate count", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void cancel(Concurrentspark.CountRequest request,
            StreamObserver<Concurrentspark.CountResponse> responseObserver) {
        try {
            // initialize CountSparkTask
            CountSparkTask countSparkTask =
                new CountSparkTask(request, responseObserver);

            // execute SparkCountTask on SparkService
            Future<Boolean> future =
                this.sparkService.submit(countSparkTask, "cancel-TODO");
            Thread.sleep(2000);
            future.cancel(true);
            future.get();

            // send response
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("failed to evaluate count", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void count(Concurrentspark.CountRequest request,
            StreamObserver<Concurrentspark.CountResponse> responseObserver) {
        log.info("received query [database='" + request.getDatabase()
            + "', collection='" + request.getCollection() + "']");

        try {
            // initialize CountSparkTask
            CountSparkTask countSparkTask =
                new CountSparkTask(request, responseObserver);

            // execute SparkCountTask on SparkService
            Future<Boolean> future =
                this.sparkService.submit(countSparkTask, "count-TODO");
            future.get();

            // send response
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("failed to evaluate count", e);
            responseObserver.onError(e);
        }
    }
}
