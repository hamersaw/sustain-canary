package io.blackpine.dynspark.client;

import io.blackpine.dynspark.protos.Dynspark;
import io.blackpine.dynspark.protos.DynSparkServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Main {
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
        DynSparkServiceGrpc.DynSparkServiceBlockingStub blockingStub =
            DynSparkServiceGrpc.newBlockingStub(channel);

        // initialize request
        Dynspark.CountRequest request =
            Dynspark.CountRequest.newBuilder()
                .setDatabase(args[0])
                .setCollection(args[1])
                .build();

        // send request
        Dynspark.CountResponse response =
            blockingStub.count(request);
        
        // print response
        System.out.println("COUNT: " + response.getCount());
    }
}
