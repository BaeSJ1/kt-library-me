package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class BookRegistered extends AbstractEvent {

    private Long id;
    private Long bookId;
    private String title;
    private Long price;
    private Boolean isBestSeller;
    private Long viewCount;

    // 생성자 내부 필드 매핑
    public BookRegistered(BookShelf aggregate) {
        super(aggregate);
        this.id = aggregate.getId();  // 서재 ID
        this.bookId = aggregate.getBookId();
        this.title = aggregate.getTitle();
        this.price = aggregate.getPrice();
        this.isBestSeller = aggregate.getIsBestSeller();
        this.viewCount = aggregate.getViewCount();
    }

    public BookRegistered() {
        super();
    }
}
//>>> DDD / Domain Event
