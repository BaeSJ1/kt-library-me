package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class CustomerRegistered extends AbstractEvent {

    private Long id;
    private Boolean isKtUser;

    public CustomerRegistered(Customer aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.isKtUser = aggregate.getIsKtUser();
    }

    public CustomerRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
