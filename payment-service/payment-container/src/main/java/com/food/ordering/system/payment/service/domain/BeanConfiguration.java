package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.payment.service.domain.service.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.service.impl.PaymentDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public PaymentDomainService paymentDomainService() {
        return new PaymentDomainServiceImpl();
    }
}
