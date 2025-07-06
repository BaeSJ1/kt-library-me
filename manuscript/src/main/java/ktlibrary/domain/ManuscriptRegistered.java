package ktlibrary.domain;

import ktlibrary.infra.AbstractEvent;

public class ManuscriptRegistered extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String manuscriptTitle;
    private String manuscriptContent;
    private String authorName;              
    private String authorIntroduction;      

    public ManuscriptRegistered(Manuscript manuscript) {
        super(manuscript);
        this.id = manuscript.getId();
        this.authorId = manuscript.getAuthorId();
        this.manuscriptTitle = manuscript.getManuscriptTitle();
        this.manuscriptContent = manuscript.getManuscriptContent();
        this.authorName = manuscript.getAuthorName();                  
        this.authorIntroduction = manuscript.getAuthorIntroduction();  


    }

    // Getter 추가 (Kafka 직렬화를 위해 필요함)
    public Long getId() {
        return id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getManuscriptTitle() {
        return manuscriptTitle;
    }

    public String getManuscriptContent() {
        return manuscriptContent;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorIntroduction() {
        return authorIntroduction;
    }
}
