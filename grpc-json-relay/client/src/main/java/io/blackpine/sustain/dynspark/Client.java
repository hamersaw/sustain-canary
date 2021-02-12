package io.blackpine.sustain.grpcJsonRelay;

import io.blackpine.sustain.grpcJsonRelay.protos.GrpcJsonRelay;
import io.blackpine.sustain.grpcJsonRelay.protos.JsonRelayServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
    protected static final int PORT = 15605;

    public static void main(String[] args) {
        // validate arguments
        if (args.length != 1) {
            System.out.println("Usage: ./APP <msg>");
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
                .setJson("{\"msg\" : \"" + args[0]  + "\"}")
                .build();

        // send request
        GrpcJsonRelay.JsonResponse response =
            blockingStub.request(request);
        
        // print response
        System.out.println("message: '" + response.getJson() + "'");
    }
}
