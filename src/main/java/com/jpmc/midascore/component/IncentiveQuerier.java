package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class IncentiveQuerier {

    private final RestTemplate restTemplate;
    private final String incentiveUrl;

    public IncentiveQuerier(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${general.incentive-url:http://localhost:8080/incentive}") String incentiveUrl
    ) {
        this.restTemplate = restTemplateBuilder.build();
        this.incentiveUrl = incentiveUrl;
    }

    public Incentive queryIncentive(Transaction transaction) {
        try {
            Incentive incentive = restTemplate.postForObject(incentiveUrl, transaction, Incentive.class);
            return (incentive == null) ? new Incentive(0f) : incentive;
        } catch (RestClientException e) {
            return new Incentive(0f);
        }
    }
}
