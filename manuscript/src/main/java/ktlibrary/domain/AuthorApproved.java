package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.infra.AbstractEvent;
import lombok.Data;

@Data
public class AuthorApproved extends AbstractEvent {

    private Long id;
    private Boolean isApproved;
}
