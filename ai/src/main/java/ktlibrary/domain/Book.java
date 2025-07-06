package ktlibrary.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktlibrary.AiApplication;
import ktlibrary.domain.Command.*;
import ktlibrary.domain.Repository.*;
import lombok.Data;

@Entity
@Table(name = "Book_table")
@Data
//<<< DDD / Aggregate Root
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 1000)
    private String summary;

    @Column(length = 1000)
    private String coverUrl;

    @Column(length = 1000)
    private String bookUrl;

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

    private String manuscriptTitle;

    @Lob
    @Column
    private String manuscriptContent;

    private Long authorId;

    private String authorName;

    private String introduction;

    private String category;

    private Long price;

    public static BookRepository repository() {
        BookRepository bookRepository = AiApplication.applicationContext.getBean(
            BookRepository.class
        );
        return bookRepository;
    }

    // Aggregate의 publishingStarted 인스턴스 메서드
    public void publishingStarted(StartPublishingCommand command) {
        System.out.println("수신된 원고 제목: " + command.getManuscriptTitle());
        this.manuscriptTitle = command.getManuscriptTitle();
        this.manuscriptContent = command.getManuscriptContent();
        this.authorId = command.getAuthorId();
        this.authorName = command.getAuthorName();
        this.introduction = command.getAuthorIntroduction();
        this.updatedAt = new Date();
    }

    //<<< Clean Arch / Port Method
    public void requestCover(GenerateCoverCommand generateCoverCommand) {
        //implement business logic here:

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void transformEbook(GenerateEbookCommand transformEbookCommand) {
        //implement business logic here:

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void settingCategory(SetCategoryCommand setCategoryCommand) {
        //implement business logic here:

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void requestRegistration(
        RegistBookCommand requestRegistrationCommand
    ) {
        //implement business logic here:

        RegisterationRequested registerationRequested = new RegisterationRequested(
            this
        );
        registerationRequested.publishAfterCommit();
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void calculatePrice(CalculatePriceCommand calculatePriceCommand) {
        //implement business logic here:

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public void generateSummary(GenerateSummaryCommand generateSummaryCommand) {
        //implement business logic here:

    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    // public static void publishingStarted(
    //     PublishingRequested publishingRequested
    // ) {
    //     //implement business logic here:

    //     /** Example 1:  new item 
    //     Book book = new Book();
    //     repository().save(book);

    //     */

    //     /** Example 2:  finding and process
        

    //     repository().findById(publishingRequested.get???()).ifPresent(book->{
            
    //         book // do something
    //         repository().save(book);


    //      });
    //     */

    // }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
