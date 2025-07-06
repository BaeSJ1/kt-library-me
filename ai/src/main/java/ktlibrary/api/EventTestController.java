package ktlibrary.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ktlibrary.infra.messaging.event.BookRegisteredEvent;
import ktlibrary.infra.messaging.producer.BookEventPublisher;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test/event")
@RequiredArgsConstructor
public class EventTestController {
    private final BookEventPublisher publisher;

    @PostMapping
    public String publishTestEvent(@RequestBody BookRegisteredEvent event) {
        publisher.publish(event);

        return "Event published!";
    }
}
