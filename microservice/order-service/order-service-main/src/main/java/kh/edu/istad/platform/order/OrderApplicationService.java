package kh.edu.istad.platform.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableConfigurationProperties
@EnableR2dbcRepositories(basePackages = "kh.edu.istad.platform.order.persistence.repository")
@ComponentScan(basePackages = {
        "kh.edu.istad.platform.order",
        "kh.edu.istad.platform.order.client"
})
public class OrderApplicationService {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplicationService.class, args);
    }

}