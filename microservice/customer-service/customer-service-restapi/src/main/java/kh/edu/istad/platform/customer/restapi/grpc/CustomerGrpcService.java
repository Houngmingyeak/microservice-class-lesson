package kh.edu.istad.platform.customer.restapi.grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import kh.edu.istad.contract.customer.v1.Customer;
import kh.edu.istad.contract.customerapi.v1.CustomerServiceGrpc;
import kh.edu.istad.contract.customerapi.v1.GetCustomerRequest;
import kh.edu.istad.contract.customerapi.v1.GetCustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerGrpcService extends CustomerServiceGrpc.CustomerServiceImplBase {

    @Override
    public void getCustomer(GetCustomerRequest request,
                            StreamObserver<GetCustomerResponse> responseObserver) {
        String id = request.getId();

        if (id == null || id.isBlank()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("id is required").asRuntimeException());
            return;
        }

        Customer customer = Customer.newBuilder()
                .setId(id)
                .setFirstName("Mingyeak")
                .setLastName("Houng")
                .setEmail("mingyeak@example.com")
                .build();

        responseObserver.onNext(GetCustomerResponse.newBuilder()
                .setCustomer(customer)
                .build());
        responseObserver.onCompleted();
    }
}