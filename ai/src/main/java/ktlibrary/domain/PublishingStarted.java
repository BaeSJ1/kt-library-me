package ktlibrary.domain;

import ktlibrary.infra.AbstractEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PublishingStarted extends AbstractEvent {

    private Long id;
    private String manuscriptTitle;
    private String manuscriptContent;
    private Long authorId;
    private String authorName;
    private String authorIntroduction;

    public PublishingStarted(Book book) {
        super(book);
        this.id = book.getId();
        this.manuscriptTitle = book.getManuscriptTitle();
        this.manuscriptContent = book.getManuscriptContent();
        this.authorId = book.getAuthorId();
        this.authorName = book.getAuthorName();
        this.authorIntroduction = book.getIntroduction();
    }
}
