package io.blackpine.sustain.grpcJsonRelay;

import io.blackpine.sustain.grpcJsonRelay.JsonRelayServiceImpl;
import io.blackpine.sustain.grpcJsonRelay.SustainServiceImpl;

import io.grpc.ServerBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    protected static final Logger log =
        LoggerFactory.getLogger(Server.class);
    protected static final int PORT = 15605;

    public static void main(String[] args) {
        // initailize server
        io.grpc.Server server = ServerBuilder.forPort(PORT)
            .addService(new JsonRelayServiceImpl())
            .addService(new SustainServiceImpl())
            .build();

        // start server
        try {
            server.start();
        } catch (Exception e) {
            log.error("failed to start server", e);
            System.exit(1);
        }

        // block until manually stopped
        try {
            server.awaitTermination();
        } catch (Exception e) {
            log.error("server failure", e);
            System.exit(1);
        }
    }
}
