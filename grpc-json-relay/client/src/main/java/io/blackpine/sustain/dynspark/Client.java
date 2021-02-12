package io.blackpine.sustain.grpcJsonRelay;

import io.blackpine.sustain.grpcJsonRelay.protos.GrpcJsonRelay;
import io.blackpine.sustain.grpcJsonRelay.protos.JsonRelayServiceGrpc;

import java.util.Iterator;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
    protected static final int PORT = 15605;

    public static void main(String[] args) {
        // validate arguments
        if (args.length != 2) {
            System.out.println("Usage: ./APP <msg> <count>");
            System.exit(1);
        }

        // open grpc channel
        ManagedChannel channel = ManagedChannelBuilder
            .forAddress("127.0.0.1", PORT)
            .usePlaintext()
            .build();

        // initialize blocking stub
        JsonRelayServiceGrpc.JsonRelayServiceBlockingStub blockingStub =
            JsonRelayServiceGrpc.newBlockingStub(channel);

        // initialize request
        GrpcJsonRelay.JsonRequest request =
            GrpcJsonRelay.JsonRequest.newBuilder()
                .setMethod("sustain.echo")
                .setJson("{\"msg\" : \"" + args[0]  + "\","
                    + "\"count\" : " + args[1] + "}")
                .build();

        // send request
        Iterator<GrpcJsonRelay.JsonResponse> iterator =
            blockingStub.request(request);
        
        while (iterator.hasNext()) {
            GrpcJsonRelay.JsonResponse response = iterator.next();

            // print response
            System.out.println("message: '" + response.getJson() + "'");
        }

        channel.shutdownNow();
    }
}
