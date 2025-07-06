package ktlibrary.application;

import javax.transaction.Transactional;
import ktlibrary.domain.*;
import ktlibrary.infra.*;
import ktlibrary.infra.ReadBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerApplicationService {

    @Autowired
    private SubsciptionRepository subsciptionRepository;

    @Autowired
    private ReadBookRepository readBookRepository;

    public void handleBookRequested(BookRequested bookRequested) {
        System.out.println("\n\n##### [CustomerApplicationService] handleBookRequested : " + bookRequested + "\n\n");

        Long customerId = bookRequested.getCustomerId();
        Long bookId = bookRequested.getBookId();

        // 유효한 구독인지 확인
        Subsciption sub = subsciptionRepository.findByCustomer_Id(customerId);

        if (sub.getIsValid() ) {
            // ReadModel에서 책 정보 가져오기
            ReadBook bookInfo = readBookRepository.findByBookId(bookId);

            if (bookInfo != null) {
                // 유효한 구독 이벤트 발행
                ValidSubscription event = new ValidSubscription(sub);
                event.setBookId(bookInfo.getBookId());
                event.setBookshelfId(bookInfo.getBookShelfId());
                event.setTitle(bookInfo.getTitle());
                event.publishAfterCommit();
                System.out.println("[CustomerApplicationService] 유효한 구독 + 책 정보 : " + event);
            } else {
                System.out.println("[CustomerApplicationService] (ReadBook 조회 실패)");
            }
        } else {
            // 유효하지 않은 구독 이벤트 발행
            InvalidSubscription event = new InvalidSubscription(sub);
            event.setCustomerId(customerId);
            event.publishAfterCommit();
            System.out.println("[CustomerApplicationService] 유효하지 않은 구독 : " + event);
        }
    }
}
