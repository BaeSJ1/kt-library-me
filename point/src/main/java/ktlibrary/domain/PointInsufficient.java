package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PointInsufficient extends AbstractEvent {

    private Long id;
    private Long point;
    private Long customerId;
    private Long price;

    public PointInsufficient(Point aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.point = aggregate.getPoint();
        this.customerId = aggregate.getCustomerId();
        this.price = aggregate.getPrice();
    }

    public PointInsufficient() {
        super();
    }
}
//>>> DDD / Domain Event
