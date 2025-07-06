package ktlibrary.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import ktlibrary.config.kafka.KafkaProcessor;
import ktlibrary.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import ktlibrary.application.PointApplicationService;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    // @Autowired
    // PointRepository pointRepository;

    @Autowired
    private PointApplicationService pointService;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Map<String, Object> event = objectMapper.readValue(eventString, Map.class);
            String eventType = (String) event.get("eventType");

            if (eventType.equals("CustomerRegistered")) {
                CustomerRegistered customerRegistered = objectMapper.readValue(eventString, CustomerRegistered.class);
                wheneverCustomerRegistered_AddPoint(customerRegistered);
            } else if (eventType.equals("InvalidSubscription")) {
                InvalidSubscription invalidSubscription = objectMapper.readValue(eventString, InvalidSubscription.class);
                wheneverInvalidSubscription_DeductPoint(invalidSubscription);
            } else if (eventType.equals("PointSaved")) {
                PointSaved pointSaved = objectMapper.readValue(eventString, PointSaved.class);
                wheneverPointSaved_Notify(pointSaved);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void wheneverCustomerRegistered_AddPoint(
        @Payload CustomerRegistered customerRegistered
    ) {
        CustomerRegistered event = customerRegistered;
        System.out.println(
            "\n\n##### listener AddPoint : " + customerRegistered + "\n\n"
        );
        
        pointService.processCustomerRegistration(event);
    }

    public void wheneverInvalidSubscription_DeductPoint(
        @Payload InvalidSubscription invalidSubscription
    ) {
        InvalidSubscription event = invalidSubscription;
        System.out.println(
            "\n\n##### listener DeductPoint : " + invalidSubscription + "\n\n"
        );

        // Sample Logic //
        // Point.deductPoint(event);
         boolean success = pointService.processInvalidSubscription(event);
        if (!success) {
            System.out.println("포인트 부족: 적립 실패 (customerId = " + event.getCustomerId() + ")");
            }
        }

    public void wheneverPointSaved_Notify(@Payload PointSaved pointSaved) {
        PointSaved event = pointSaved;
        System.out.println(
            "\n\n##### listener Notify : " + pointSaved + "\n\n"
        );
    }
}
//>>> Clean Arch / Inbound Adaptor
