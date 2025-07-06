package ktlibrary.domain;

import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class InvalidSubscription extends AbstractEvent {

    private Long id;
    private Long customerId;
    private Long bookId;
    private Long bookshelfId;
    private Long price;
    private String title;
}
