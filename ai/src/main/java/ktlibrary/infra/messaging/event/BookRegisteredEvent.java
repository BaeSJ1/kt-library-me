package ktlibrary.infra.messaging.event;

import ktlibrary.domain.Book;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class BookRegisteredEvent extends AbstractEvent {
    private Long id;
    private String manuscriptTitle;
    private String manuscriptContent;
    private Long authorId;
    private String authorName;
    private String introduction;

    private String summary;
    private String coverUrl;
    private String category;
    private String bookUrl;

    private Long price;

    public BookRegisteredEvent(Book aggregate) {
        super(aggregate);
    }

    public BookRegisteredEvent() {
        super();
    }
}