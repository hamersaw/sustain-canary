package io.blackpine.sustain.grpcJsonRelay;

import io.blackpine.sustain.grpcJsonRelay.protos.JsonRelayServiceGrpc;
import io.blackpine.sustain.grpcJsonRelay.protos.GrpcJsonRelay;
import io.blackpine.sustain.grpcJsonRelay.protos.SustainServiceGrpc;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import java.util.Iterator;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonRelayServiceImpl 
        extends JsonRelayServiceGrpc.JsonRelayServiceImplBase {
    protected static final Logger log =
        LoggerFactory.getLogger(JsonRelayServiceGrpc.class);

    @Override
    public void echo(GrpcJsonRelay.JsonEchoRequest request,
            StreamObserver<GrpcJsonRelay.JsonEchoResponse> responseObserver) {
        log.info("received request [json='" + request.getJson() + "']");

        ManagedChannel channel = null;

        try {
            // open grpc channel
            channel = ManagedChannelBuilder
                .forAddress("127.0.0.1",
                    io.blackpine.sustain.grpcJsonRelay.Server.PORT)
                .usePlaintext()
                .build();

            // convert json to protobuf and service request
            JsonFormat.Parser parser = JsonFormat.parser();
            JsonFormat.Printer printer = JsonFormat.printer()
                .includingDefaultValueFields()
                .omittingInsignificantWhitespace();

            // create echo request
            GrpcJsonRelay.EchoRequest.Builder requestBuilder =
                GrpcJsonRelay.EchoRequest.newBuilder();
            parser.merge(request.getJson(), requestBuilder);

            // issue echo request
            SustainServiceGrpc.SustainServiceBlockingStub blockingStub =
                SustainServiceGrpc.newBlockingStub(channel);

            Iterator<GrpcJsonRelay.EchoResponse> iterator =
                blockingStub.echo(requestBuilder.build());

            // iterate over results
            while (iterator.hasNext()) {
                GrpcJsonRelay.EchoResponse response =
                    iterator.next();

                // build JsonRequest
                String json = printer.print(response);
                GrpcJsonRelay.JsonEchoResponse jsonResponse =
                    GrpcJsonRelay.JsonEchoResponse.newBuilder()
                        .setJson(json)
                        .build();

                responseObserver.onNext(jsonResponse);
            }

            // send response
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("failed to evaluate", e);
            responseObserver.onError(e);
        } finally {
            if (channel != null) {
                channel.shutdownNow();
            }
        }
    }
}
