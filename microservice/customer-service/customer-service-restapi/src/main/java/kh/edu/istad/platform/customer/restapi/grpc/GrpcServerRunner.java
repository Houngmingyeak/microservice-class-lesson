package kh.edu.istad.platform.customer.restapi.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionServiceV1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class GrpcServerRunner implements SmartLifecycle {

    private final CustomerGrpcService customerGrpcService;
    private final int port;
    private Server server;

    public GrpcServerRunner(CustomerGrpcService customerGrpcService,
                            @Value("${grpc.server.port:9090}") int port) {
        this.customerGrpcService = customerGrpcService;
        this.port = port;
    }

    @Override
    public void start() {
        try {
            server = ServerBuilder.forPort(port)
                    .addService(customerGrpcService)
                    .addService(ProtoReflectionServiceV1.newInstance())
                    .build()
                    .start();
            System.out.println("gRPC server started on port " + port);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot start gRPC server", e);
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    @Override
    public boolean isRunning() {
        return server != null && !server.isShutdown();
    }
}