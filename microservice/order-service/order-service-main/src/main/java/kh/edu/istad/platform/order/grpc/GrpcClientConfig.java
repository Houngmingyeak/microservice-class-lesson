package kh.edu.istad.platform.order.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import kh.edu.istad.contract.customerapi.v1.CustomerServiceGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean(destroyMethod = "shutdown")
    public ManagedChannel customerChannel(
            @Value("${grpc.client.customer.address:localhost:9090}") String address) {
        return ManagedChannelBuilder.forTarget(address).usePlaintext().build();
    }

    @Bean
    public CustomerServiceGrpc.CustomerServiceBlockingStub customerBlockingStub(
            ManagedChannel customerChannel) {
        return CustomerServiceGrpc.newBlockingStub(customerChannel);
    }
}