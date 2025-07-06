package ktlibrary;

import ktlibrary.config.kafka.KafkaProcessor;
import ktlibrary.domain.Point;
import ktlibrary.infra.AbstractEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootApplication
@EnableBinding(KafkaProcessor.class)
@EnableFeignClients
public class PointApplication {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(PointApplication.class, args);

        // ✅ 핵심 해결 코드: static 필드에 필요한 빈 수동 등록
        // Point.setEventPublisher(applicationContext.getBean(ApplicationEventPublisher.class));
        AbstractEvent.setKafkaProcessor(applicationContext.getBean(KafkaProcessor.class));
    }
}
