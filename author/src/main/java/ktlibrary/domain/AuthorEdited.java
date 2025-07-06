package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AuthorEdited extends AbstractEvent {

    private Long id;
    private String email;
    private String introduction;

    public AuthorEdited(Author aggregate) {
        super(aggregate);
    }

    public AuthorEdited() {
        super();
    }
}
//>>> DDD / Domain Event
