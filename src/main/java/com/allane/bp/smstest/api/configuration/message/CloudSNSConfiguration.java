package com.allane.bp.smstest.api.configuration.message;

import io.awspring.cloud.sns.core.SnsTemplate;
import io.awspring.cloud.sns.core.TopicsListingTopicArnResolver;
import io.awspring.cloud.sns.sms.SnsSmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Slf4j
@Configuration
@ConditionalOnCloudPlatform(CloudPlatform.KUBERNETES)
public class CloudSNSConfiguration {

    @Bean
    public SnsSmsTemplate snsSmsTemplate() {
        log.info("SNS SnsSmsTemplate - Detected cloud environment, using AWS credentials provider chain.");
        SnsClient snsClient = SnsClient.builder()
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .region(Region.EU_CENTRAL_1)
                .build();

        return new SnsSmsTemplate(snsClient);
    }

    @Bean
    public SnsTemplate snsTemplate() {
        log.info("SNS SnsTemplate - Detected cloud environment, using AWS credentials provider chain.");

        SnsClient snsClient = SnsClient.builder()
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .region(Region.EU_CENTRAL_1)
                .build();

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setSerializedPayloadClass(String.class);

        return new SnsTemplate(snsClient, new TopicsListingTopicArnResolver(snsClient), converter);
    }

}
