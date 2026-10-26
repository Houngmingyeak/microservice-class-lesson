//package kh.edu.istad.platform.customer.restapi.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//public class InstanceController {
//
//    @Value("${server.port}")
//    private String port;
//
//    @Value("${spring.application.name:unknown}")
//    private String name;
//
//    @GetMapping("/api/v1/instance-info")
//    public Map<String, String> info() {
//        return Map.of("service", name, "port", port);
//    }
//}