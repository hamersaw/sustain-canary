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
    public void request(GrpcJsonRelay.JsonRequest request,
            StreamObserver<GrpcJsonRelay.JsonResponse> responseObserver) {
        log.info("received request [method='"
            + request.getMethod() + "']");

        try {
            // open grpc channel
            ManagedChannel channel = ManagedChannelBuilder
                .forAddress("127.0.0.1",
                    io.blackpine.sustain.grpcJsonRelay.Server.PORT)
                .usePlaintext()
                .build();

            // convert json to protobuf and service request
            JsonFormat.Parser parser = JsonFormat.parser();
            JsonFormat.Printer printer = JsonFormat.printer();

            Message message = null;
            switch (request.getMethod()) {
                case "sustain.echo":
                    GrpcJsonRelay.EchoRequest.Builder requestBuilder =
                        GrpcJsonRelay.EchoRequest.newBuilder();
                    parser.merge(request.getJson(), requestBuilder);

                    // issue echo request
                    SustainServiceGrpc.SustainServiceBlockingStub blockingStub =
                        SustainServiceGrpc.newBlockingStub(channel);

                    Iterator<GrpcJsonRelay.EchoResponse> iterator =
                        blockingStub.echo(requestBuilder.build());

                    while (iterator.hasNext()) {
                        GrpcJsonRelay.EchoResponse response =
                            iterator.next();

                        // build JsonRequest
                        String json = printer.print(response);
                        GrpcJsonRelay.JsonResponse jsonResponse =
                            GrpcJsonRelay.JsonResponse.newBuilder()
                                .setJson(json)
                                .build();

                        responseObserver.onNext(jsonResponse);
                    }

                    break;
                default:
                    throw new Exception("unsupported method '"
                        + request.getMethod() + "'");
            }

            // send response
            responseObserver.onCompleted();
            channel.shutdownNow();
        } catch (Exception e) {
            log.error("failed to evaluate", e);
            responseObserver.onError(e);
        }
    }
}
