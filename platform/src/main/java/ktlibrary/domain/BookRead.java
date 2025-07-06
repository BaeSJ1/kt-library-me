package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookRead extends AbstractEvent {

    private Long id;
    private Long customerId;
    private Long bookId;
    private String title;
    private Boolean isBestSeller;
    private Long viewCount;
    private Long price;
    private String accessType;

    public BookRead(BookShelf aggregate) {
        super(aggregate);
    }

    public BookRead() {
        super();
    }
}
//>>> DDD / Domain Event
