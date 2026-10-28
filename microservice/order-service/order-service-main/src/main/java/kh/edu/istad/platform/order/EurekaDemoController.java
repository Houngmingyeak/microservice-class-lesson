//package kh.edu.istad.platform.order;
//
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestClient;
//
//import java.util.Map;
//@RestController
//@RequestMapping("/api/demo")
//public class EurekaDemoController {
//
//    private final RestClient restClient;
//
//    public EurekaDemoController(@LoadBalanced RestClient.Builder builder) {
//        this.restClient = builder.build();
//    }
//
//    @GetMapping("/customers")
//    public String callCustomerService() {
//        // "customer-service" ត្រូវត្រូវនឹង spring.application.name របស់ Customer service
//        return restClient.get()
//                .uri("http://customer-service/api/customers")
//                .retrieve()
//                .body(String.class);
//    }
//}