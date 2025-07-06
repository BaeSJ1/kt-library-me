package ktlibrary.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktlibrary.ManuscriptApplication;
import lombok.Data;
import ktlibrary.infra.ReadAuthorRepository;



@Entity
@Table(name = "Manuscript_table")
@Data
//<<< DDD / Aggregate Root
public class Manuscript {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String manuscriptTitle;
    
    @Column(nullable = false)
    private String manuscriptContent;

    @Column(nullable = false)
    private Long authorId;

    private String authorName;

    private String authorIntroduction;

    @Column(updatable = false)
    private Date createdAt;

    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    @PostPersist
    protected void afterCreate() {
        // 새 원고 등록 이벤트 발행
        ManuscriptRegistered manuscriptRegistered = new ManuscriptRegistered(this);
        manuscriptRegistered.publish();  // 즉시 발행 (commit 기다리지 않음)
    }
    
    public static ManuscriptRepository repository() {
        ManuscriptRepository manuscriptRepository = ManuscriptApplication.applicationContext.getBean(
            ManuscriptRepository.class
        );
        return manuscriptRepository;
    }

    private static ReadAuthorRepository readAuthorRepository() {
        return ManuscriptApplication.applicationContext.getBean(ReadAuthorRepository.class);
    }
    

    //<<< Clean Arch / Port Method
    public void registerManuscript(RegisterManuscriptCommand command) {

        // 승인 여부 확인
        ReadAuthor author = readAuthorRepository().findByAuthorId(command.getAuthorId()).stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("작가 정보가 없습니다."));

        if (!author.getIsApproved()) {
            throw new IllegalStateException("승인되지 않은 작가는 원고를 등록할 수 없습니다.");
        }

        this.authorId = command.getAuthorId();
        this.manuscriptTitle = command.getManuscriptTitle();
        this.manuscriptContent = command.getManuscriptContent();
        this.authorName = author.getAuthorName();
        this.authorIntroduction = author.getIntroduction();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void editManuscript(EditManuscriptCommand command) {
        // 승인여부 확인
        ReadAuthor author = readAuthorRepository().findByAuthorId(command.getAuthorId()).stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("작가 정보가 없습니다."));

        if (!author.getIsApproved()) {
            throw new IllegalStateException("승인되지 않은 작가는 원고를 수정할 수 없습니다.");
        }

        this.authorId = command.getAuthorId();
        this.manuscriptTitle = command.getManuscriptTitle();
        this.manuscriptContent = command.getManuscriptContent();
        this.authorName = author.getAuthorName();
        this.authorIntroduction = author.getIntroduction();

        ManuscriptEdited manuscriptEdited = new ManuscriptEdited(this);
        manuscriptEdited.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    // 출간 요청
    public void requestPublishing(RequestPublishingCommand command) {
        if (this.manuscriptTitle == null || this.manuscriptTitle.trim().isEmpty() ||
            this.manuscriptContent == null || this.manuscriptContent.trim().isEmpty()) {
            throw new IllegalStateException("원고가 등록되지 않았습니다. 출간 요청 불가합니다.");
        }

        ReadAuthor author = readAuthorRepository().findByAuthorId(this.authorId)
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("작가 정보가 없습니다."));

        this.authorName = author.getAuthorName();
        this.authorIntroduction = author.getIntroduction();

        PublishingRequested event = new PublishingRequested(this);
        event.publishAfterCommit();
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
