package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PublishingRequested extends AbstractEvent {

    private Long id;
    private String manuscriptTitle;
    private String manuscriptContent;
    private Long authorId;
    private String authorName;
    private String authorIntroduction;

    public PublishingRequested(Manuscript aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.manuscriptTitle = aggregate.getManuscriptTitle();
        this.manuscriptContent = aggregate.getManuscriptContent();
        this.authorId = aggregate.getAuthorId();
        this.authorName = aggregate.getAuthorName();
        this.authorIntroduction = aggregate.getAuthorIntroduction();
    }

    public PublishingRequested() {
        super();
    }
}
//>>> DDD / Domain Event
