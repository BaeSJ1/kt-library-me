package ktlibrary.infra.messaging.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import ktlibrary.config.kafka.KafkaProcessor;
import ktlibrary.infra.messaging.event.BookRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookEventPublisher {

    private final KafkaProcessor kafkaProcessor;

    public void publish(BookRegisteredEvent event) {
        try {
            // ✅ 1. Object -> JSON String 변환
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(event);

            // ✅ 2. Kafka 메시지 전송
            MessageChannel outputChannel = kafkaProcessor.outboundTopic();
            outputChannel.send(
                MessageBuilder
                    .withPayload(json)  // 👈 JSON 문자열로 보냄
                    .setHeader("type", "BookRegisteredEvent") // 👈 Kafka 헤더 조건 분기용
                    .setHeader("contentType", "application/json")
                    .build()
            );

            System.out.println("✅ Kafka 이벤트 발행 완료: " + json);

        } catch (Exception e) {
            System.err.println("❌ Kafka 발행 실패: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}