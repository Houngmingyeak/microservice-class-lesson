package kh.edu.istad.platform.order.grpc;

import kh.edu.istad.contract.customerapi.v1.CustomerServiceGrpc;
import kh.edu.istad.contract.customerapi.v1.GetCustomerRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomerGrpcClient {

    private final CustomerServiceGrpc.CustomerServiceBlockingStub stub;

    public CustomerGrpcClient(CustomerServiceGrpc.CustomerServiceBlockingStub stub) {
        this.stub = stub;
    }

    public kh.edu.istad.contract.customer.v1.Customer getCustomer(String id) {
        return stub.getCustomer(GetCustomerRequest.newBuilder().setId(id).build())
                .getCustomer();
    }
}