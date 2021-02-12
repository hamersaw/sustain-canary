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
        log.info("received query [msg='" + request.getMsg() + "']");

        try {
            for (int i=0; i<request.getCount(); i++) {
                // initailize response
                GrpcJsonRelay.EchoResponse response =
                    GrpcJsonRelay.EchoResponse.newBuilder()
                        .setMsg(request.getMsg())
                        .build();

                // send response
                responseObserver.onNext(response);
            }

            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("failed to evaluate count", e);
            responseObserver.onError(e);
        }
    }
}
