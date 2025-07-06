package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.infra.AbstractEvent;
import lombok.Data;

@Data
public class AuthorRegistered extends AbstractEvent {

    private Long id;
    private String email;
    private String name;
    private String introduction;
    private Boolean isApproved;
}
