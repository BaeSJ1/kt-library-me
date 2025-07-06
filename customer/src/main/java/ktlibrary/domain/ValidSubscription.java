package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ValidSubscription extends AbstractEvent {

    private Long id;
    private Long customerId; //
    private Long bookId;
    private Long bookshelfId;
    private String title;

    public ValidSubscription(Subsciption aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.customerId = aggregate.getCustomerId();
    }

    public ValidSubscription() {
        super();
    }
}
//>>> DDD / Domain Event
