package com.allane.bp.smstest.api.configuration.message;

import java.net.URI;

import io.awspring.cloud.sns.core.SnsTemplate;
import io.awspring.cloud.sns.sms.SnsSmsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Slf4j
@Configuration
@Conditional(LocalSNSConfiguration.NotOnCloudKubernetesCondition.class)
@Profile("!test")
public class LocalSNSConfiguration {

    @Bean
    public SnsSmsTemplate localSnsSmsTemplate() {
        log.info("SNS SnsSmsTemplate - Detected local environment, using dummy AWS credentials");

        SnsClient snsClient = SnsClient.builder()
                .endpointOverride(URI.create("http://localstack:4566"))
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                .build();

        return new SnsSmsTemplate(snsClient);
    }

    @Bean
    public SnsTemplate localSnsTemplate() {
        log.info("SNS SnsTemplate - Detected local environment, using dummy AWS credentials");

        SnsClient snsClient = SnsClient.builder()
                .endpointOverride(URI.create("http://localstack:4566"))
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                .build();

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setSerializedPayloadClass(String.class);

        return new SnsTemplate(snsClient, converter);
    }

    public static class NotOnCloudKubernetesCondition extends NoneNestedConditions {

        NotOnCloudKubernetesCondition() {
            super(ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnCloudPlatform(CloudPlatform.KUBERNETES)
        static class CloudKubernetesCondition {

        }

    }
}
