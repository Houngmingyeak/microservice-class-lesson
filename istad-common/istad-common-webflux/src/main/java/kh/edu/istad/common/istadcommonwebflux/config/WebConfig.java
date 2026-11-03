package kh.edu.istad.common.istadcommonwebflux.config;

import kh.edu.istad.common.istadcommonwebflux.exception.DomainExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class WebConfig {

    @Bean
//    @ConditionalOnMissingBean
    @ConditionalOnBean
    public DomainExceptionHandler domainExceptionHandler() {
        return new DomainExceptionHandler();
    }

}
