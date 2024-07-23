package com.example.Desafio_anota_ai.services.aws;

import com.amazonaws.services.sns.AmazonSNS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AwsSnsService {

    private final AmazonSNS snsClient;
    private final String catalogTopicArn;

    public AwsSnsService(AmazonSNS snsClient, @Qualifier("catalogEventsTopicArn") String catalogTopicArn) {
        this.snsClient = snsClient;
        this.catalogTopicArn = catalogTopicArn;
    }

    public void publish(MessageDTO message) {
        snsClient.publish(catalogTopicArn, message.toString());
    }
}
