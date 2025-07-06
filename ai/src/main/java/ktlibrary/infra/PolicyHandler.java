package ktlibrary.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.transaction.Transactional;
import ktlibrary.config.kafka.KafkaProcessor;
import ktlibrary.domain.PublishingRequested;
import ktlibrary.infra.application.service.StartPublishingService;
import ktlibrary.infra.messaging.event.BookRegisteredEvent;
import ktlibrary.domain.Repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    private StartPublishingService startPublishingService;

    @StreamListener(
    value = KafkaProcessor.INPUT,
    condition = "headers['type']=='PublishingRequested'"
    )
    public void wheneverPublishingRequested_PublishingStarted(
        @Payload PublishingRequested publishingRequested
    ) {
        System.out.println(
            "\n\n##### listener PublishingStarted : " +
            publishingRequested + " 출간요청됨 수신 완료" +
            "\n\n"
        );

        startPublishingService.handlePublishingRequest(publishingRequested);
    }
}
