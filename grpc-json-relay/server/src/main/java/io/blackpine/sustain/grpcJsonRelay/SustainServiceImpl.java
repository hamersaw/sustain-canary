package io.blackpine.sustain.grpcJsonRelay;

import io.blackpine.sustain.grpcJsonRelay.protos.SustainServiceGrpc;
import io.blackpine.sustain.grpcJsonRelay.protos.GrpcJsonRelay;

import io.grpc.stub.StreamObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SustainServiceImpl 
        extends SustainServiceGrpc.SustainServiceImplBase {
    protected static final Logger log =
        LoggerFactory.getLogger(SustainServiceGrpc.class);

    @Override
    public void echo(GrpcJsonRelay.EchoRequest request,
            StreamObserver<GrpcJsonRelay.EchoResponse> responseObserver) {
        log.info("received query [type='" + request.getType() + "']");

        try {
            for (int i=0; i<request.getCount(); i++) {
                // initailize response
                GrpcJsonRelay.EchoResponse.Builder responseBuilder =
                    GrpcJsonRelay.EchoResponse.newBuilder()
                        .setType(request.getType());

                switch (request.getType()) {
                    case LOWER:
                        String lowerMsg = request.getLowerEchoRequest()
                            .getMsg().toLowerCase();

                        GrpcJsonRelay.LowerEchoResponse lowerResponse =
                            GrpcJsonRelay.LowerEchoResponse.newBuilder()
                                .setMsg(lowerMsg)
                                .build();

                        responseBuilder
                            .setLowerEchoResponse(lowerResponse);

                        break;
                    case UPPER:
                        String upperMsg = request.getUpperEchoRequest()
                            .getMsg().toUpperCase();

                        GrpcJsonRelay.UpperEchoResponse upperResponse =
                            GrpcJsonRelay.UpperEchoResponse.newBuilder()
                                .setMsg(upperMsg)
                                .build();

                        responseBuilder
                            .setUpperEchoResponse(upperResponse);

                        break;
                }

                // send response
                responseObserver.onNext(responseBuilder.build());
            }

            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("failed to evaluate count", e);
            responseObserver.onError(e);
        }
    }
}
