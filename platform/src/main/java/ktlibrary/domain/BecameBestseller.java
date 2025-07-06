package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BecameBestseller extends AbstractEvent {

    private Long id;
    private Long bookId;
    private String title;
    private Boolean isBestSeller;
    private Long viewCount;
    private Long price;

    public BecameBestseller(BookShelf aggregate) {
        super(aggregate);
    }

    public BecameBestseller() {
        super();
    }
}
//>>> DDD / Domain Event
