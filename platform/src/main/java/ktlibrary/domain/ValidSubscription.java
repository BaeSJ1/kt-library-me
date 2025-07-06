package ktlibrary.domain;

import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class ValidSubscription extends AbstractEvent {

    private Long id;
    private Long customerId;
    private Long bookId;
    private Long bookshelfId;
    private String title;
}
