package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class RegisterationRequested extends AbstractEvent {

    private Long id;
    private String summary;
    private String coverUrl;
    private String bookUrl;
    private Date createdAt;
    private Date updatedAt;
    private String manuscriptTitle;
    private String manuscriptContent;
    private Long authorId;
    private String authorName;
    private String introduction;
    private String category;
    private Long price;

    public RegisterationRequested(Book aggregate) {
        super(aggregate);
    }

    public RegisterationRequested() {
        super();
    }
}
//>>> DDD / Domain Event
