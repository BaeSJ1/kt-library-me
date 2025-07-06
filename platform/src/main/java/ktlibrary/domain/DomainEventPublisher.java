package ktlibrary.domain;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ktlibrary.infra.AbstractEvent;  

@Component
public class DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public DomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(Object event) {
        publisher.publishEvent(event);

        // AbstractEvent를 상속받은 이벤트라면 카프카로도 발행
        if (event instanceof AbstractEvent) {
            ((AbstractEvent) event).publish();
        }
    }
}