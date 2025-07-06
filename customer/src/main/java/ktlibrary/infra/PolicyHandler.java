package ktlibrary.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import ktlibrary.config.kafka.KafkaProcessor;
import ktlibrary.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ktlibrary.application.*;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SubsciptionRepository subsciptionRepository;


    @Autowired
    ReadBookRepository readBookRepository;

    @Autowired
    private CustomerApplicationService customerApplicationService;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}


    @StreamListener(
    value = KafkaProcessor.INPUT,
    condition = "headers['type']=='BookRequested'"
)
public void wheneverBookRequested_IsSubscribed(@Payload BookRequested bookRequested) {
    System.out.println("\n\n##### listener IsSubscribed (delegating to service): " + bookRequested + "\n\n");
    customerApplicationService.handleBookRequested(bookRequested);
}
//     @StreamListener(
//     value = KafkaProcessor.INPUT,
//     condition = "headers['type']=='BookRequested'"
// )
// public void wheneverBookRequested_IsSubscribed(@Payload BookRequested bookRequested) {
//     System.out.println("\n\n##### listener IsSubscribed : " + bookRequested + "\n\n");

//     Long customerId = bookRequested.getCustomerId();
//     Long bookId = bookRequested.getBookId();

//     // 유효한 구독인지 확인
//     Subsciption sub = subsciptionRepository.findByCustomer_IdAndIsValid(customerId, true);
    
//     if (sub != null) {
//         // ReadModel에서 책 정보 가져오기
//         ReadBook bookInfo = readBookRepository.findByBookId(bookId);

//         if (bookInfo != null) {
//             // 유효한 구독 이벤트 발행
//             ValidSubscription event = new ValidSubscription(sub);
//             event.setBookId(bookInfo.getBookId());
//             event.setBookshelfId(bookInfo.getBookShelfId());
//             event.setTitle(bookInfo.getTitle());
//             event.publishAfterCommit();
//             System.out.println(" 유효한 구독 + 책 정보 : " + event);
//         } else {
//             System.out.println(" (ReadBook 조회 실패)");
//         }
//     } else {
//         // 유효하지 않은 구독 이벤트 발행
//         InvalidSubscription event = new InvalidSubscription();
//         event.setCustomerId(customerId);
//         event.publishAfterCommit();
//     }
// }

}
//>>> Clean Arch / Inbound Adaptor
