package kh.edu.istad.platform.order.grpc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/grpc-demo")
public class GrpcDemoController {

    private final CustomerGrpcClient client;

    public GrpcDemoController(CustomerGrpcClient client) {
        this.client = client;
    }

    @GetMapping("/customers/{id}")
    public String get(@PathVariable String id) {
        return client.getCustomer(id).toString();
    }
}