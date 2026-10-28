package kh.edu.istad.platform.order;

import kh.edu.istad.platform.order.config.ServicePropConfig;
import kh.edu.istad.platform.order.domain.port.out.client.CustomerServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@RefreshScope
@RestController
@RequiredArgsConstructor
public class WebFluxController {

    private final CustomerServiceClient customerServiceClient;

    @GetMapping("customers")
    public Flux<CustomerServiceClient.CustomerResponse> getCustomers(){
        return customerServiceClient.getCustomers();
    }

    @GetMapping("/flux")
    public Flux<String> getFluxData() {
        return Flux.just(" [A] Initializing...",
                        " [B] Loading modules...",
                        " [C] Connecting nodes...",
                        " [D] Streaming data...",
                        " [E] Optimizing buffer...",
                        " [F] Complete!")
                .map(item -> ">> " + item)
                .log()
                .delayElements(Duration.ofSeconds(1));
    }

//    @Value("${service.message}")
//    private String serviceMessage;
//    @Value("${service.environment}")
//    private String serviceEnvironment;
//    @Value("${service.version}")
//    private String serviceVersion;
    @Value("${global.version}")
    private String globalVersion;

    private final ServicePropConfig servicePropConfig;

    @GetMapping("/service-info")
    public Mono<Map<String, Object >> getConsulConfig() {
        return Mono.just(
                Map.of("message", servicePropConfig.getMessage(),
                        "env", servicePropConfig.getEnvironment(),
                        "version", servicePropConfig.getVersion(),
                        "global-version", globalVersion,
                        "base-url", servicePropConfig.getBaseUrl())
        );
    }

}