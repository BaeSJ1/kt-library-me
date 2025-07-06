package ktlibrary.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktlibrary.PlatformApplication;
import ktlibrary.domain.BecameBestseller;
import ktlibrary.domain.BookRead;
import ktlibrary.domain.BookRegistered;
import lombok.Data;

@Entity
@Table(name = "BookShelf_table")
@Data
//<<< DDD / Aggregate Root
public class BookShelf {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;                // 서재 ID

    private Boolean isBestSeller;   // 베스트셀러 여부

    private Long viewCount;         // 조회수

    private Long authorId;          // 저자 ID

    private String authorName;      // 저자 이름

    private String introduction;    // 저자 소개

    private Long bookId;        // 도서 ID
    
    private String title;       // 도서 제목

    @Column(length = 1000)
    private String summary;     // 도서 요약
    
    @Column(length = 1000)
    private String coverUrl;    // 도서 표지 URL

    private String category;    // 도서 카테고리

    @Column(length = 1000)
    private String fileUrl;     // 도서 파일 URL

    private Long price;         // 도서 가격

    public static BookShelfRepository repository() {
        BookShelfRepository bookShelfRepository = PlatformApplication.applicationContext.getBean(
            BookShelfRepository.class
        );
        return bookShelfRepository;
    }

    public void regist(RegisterationRequested event) {
        this.bookId = event.getId();
        this.title = event.getManuscriptTitle();
        this.category = event.getCategory();
        this.summary = event.getSummary();
        this.authorId = event.getAuthorId();
        this.authorName = event.getAuthorName();
        this.introduction = event.getIntroduction();
        this.coverUrl = event.getCoverUrl();
        this.fileUrl = event.getBookUrl();
        this.price = event.getPrice();
        this.viewCount = 0L;
        this.isBestSeller = false;
    }

    public BookRegistered createBookRegisteredEvent() {
        return new BookRegistered(this);  // 생성자에서 BookShelf를 넘기면 내부에서 필요한 필드 꺼내게 설계
    }

    public BookReadResult readBy(Long customerId, AccessType accessType) {

        this.viewCount += 1;

        // 베스트셀러 승격 
        boolean promoted = false;
        if (this.viewCount >= 5 && !Boolean.TRUE.equals(this.isBestSeller)) {
            this.isBestSeller = true;
            promoted = true;
        }

        // 결과 객체 반환
        return new BookReadResult(this.viewCount, promoted);
    }


}
