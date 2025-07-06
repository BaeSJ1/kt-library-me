package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class InvalidSubscription extends AbstractEvent {

    private Long id;
    private Boolean isValid;
    private Long customerId;

    public InvalidSubscription(Subsciption aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.isValid = aggregate.getIsValid();
        this.customerId = aggregate.getCustomerId(); 
    }

    public InvalidSubscription() {
        super();
    }
}
//>>> DDD / Domain Event
