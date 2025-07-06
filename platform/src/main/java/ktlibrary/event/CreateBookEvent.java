package ktlibrary.event;

import ktlibrary.domain.BookShelf;
import ktlibrary.infra.AbstractEvent;

import lombok.*;

@Data
@ToString
public class CreateBookEvent extends AbstractEvent {
    private Long id;                // 서재 ID
    private Boolean isBestSeller;   // 베스트셀러 여부
    private Long viewCount;         // 조회수
    private Long authorId;          // 저자 ID
    private String authorName;      // 저자 이름
    private String introduction;    // 저자 소개
    private Long bookId;        // 도서 ID
    private String title;       // 도서 제목
    private String summary;     // 도서 요약
    private String coverUrl;    // 도서 표지 URL
    private String category;    // 도서 카테고리
    private String fileUrl;     // 도서 파일 URL
    private Long price;         // 도서 가격
    
    public CreateBookEvent(BookShelf aggregate) {
        super(aggregate);
    }

    public CreateBookEvent() {
        super();
    }
}