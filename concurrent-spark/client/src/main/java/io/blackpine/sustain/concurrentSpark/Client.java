package io.blackpine.sustain.concurrentSpark;

import io.blackpine.sustain.concurrentSpark.protos.Concurrentspark;
import io.blackpine.sustain.concurrentSpark.protos.ConcurrentSparkServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
    protected static final int PORT = 15605;

    public static void main(String[] args) {
        // validate arguments
        if (args.length != 2) {
            System.out.println("Usage: ./APP <database> <collection>");
            System.exit(1);
        }

        // open grpc channel
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress("127.0.0.1", PORT)
            .usePlaintext()
            .build();

        // initialize blocking stub
        ConcurrentSparkServiceGrpc.ConcurrentSparkServiceBlockingStub blockingStub =
            ConcurrentSparkServiceGrpc.newBlockingStub(channel);

        // initialize request
        /*Concurrentspark.CountRequest request =
            Concurrentspark.CountRequest.newBuilder()
                .setDatabase(args[0])
                .setCollection(args[1])
                .build();*/

        Concurrentspark.ExceptionRequest request =
            Concurrentspark.ExceptionRequest.newBuilder()
                .build();

        // send request
        //Concurrentspark.CountResponse response =
        //    blockingStub.count(request);
        //Concurrentspark.CountResponse response =
        //    blockingStub.cancel(request);

        Concurrentspark.ExceptionResponse response =
            blockingStub.exception(request);
        
        // print response
        //System.out.println("COUNT: " + response.getCount());
    }
}
