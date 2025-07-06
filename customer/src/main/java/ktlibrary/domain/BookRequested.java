package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
@Getter
@Setter
public class BookRequested extends AbstractEvent {

    private Long id;
    private Long subsciptionId;
    private Long bookId;
    private Long bookshelfId;
    private String title;
    private Long price;
    private boolean isValid;

    public BookRequested(Customer aggregate) {
        super(aggregate);
        //this.id = aggregate.getId();
        //this.subsciptionId = aggregate.subsciptionId;
    }

    public BookRequested() {
        super();
    }
    public Long getSubscriptionId() {
    return subsciptionId != null ? subsciptionId : null;
}   
    public Long getCustomerId(){
        return id;
    }


    // public void setCustomerId(Long customerId) {
    //     this.id = customerId;
    // }

    public Long getBookId() {
        return bookId;
    }

    // public void setBookId(Long bookId) {
    //     this.bookId = bookId;
    // }

}
//>>> DDD / Domain Event
