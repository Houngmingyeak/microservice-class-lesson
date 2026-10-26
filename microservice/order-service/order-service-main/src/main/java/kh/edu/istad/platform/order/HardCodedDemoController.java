//package kh.edu.istad.platform.order;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestClient;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/demo")
//public class HardCodedDemoController {
//
//    private final RestClient customer = RestClient.create("http://localhost:9091");
//    private final RestClient business = RestClient.create("http://localhost:9092");
//    private final RestClient payment  = RestClient.create("http://localhost:9094");
//
//    @GetMapping("/hardcoded")
//    public Map<String, Object> call() {
//        return Map.of(
//                "customer", customer.get().uri("/api/v1/instance-info").retrieve().body(Map.class),
//                "business", business.get().uri("/api/v1/instance-info").retrieve().body(Map.class),
//                "payment",  payment.get().uri("/api/v1/instance-info").retrieve().body(Map.class));
//    }
//}