package ktlibrary.domain;

import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class PointDeducted extends AbstractEvent {

    private Long id;
    private Long point;
    private Long customerId;
    private Long price;
    private Long bookId;
}
