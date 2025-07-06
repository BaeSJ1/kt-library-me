package ktlibrary.domain;

import lombok.Data;
import lombok.ToString;
import ktlibrary.infra.AbstractEvent;

@Data
@ToString
public class BookReadEvent extends AbstractEvent {

    private Long bookId;
    private Long customerId;
    private Long newViewCount;
    private Boolean bestsellerPromoted;

    public BookReadEvent(Long bookId, Long customerId, Long newViewCount, Boolean bestsellerPromoted) {
        super();
        this.bookId = bookId;
        this.customerId = customerId;
        this.newViewCount = newViewCount;
        this.bestsellerPromoted = bestsellerPromoted;
    }
}
