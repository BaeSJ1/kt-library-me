package ktlibrary.infra;

import ktlibrary.infra.BookShelfApplicationService;

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

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    private BookShelfApplicationService bookShelfApplicationService;

    //public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookRegisteredEvent'"    // AI 서비스에서 전송한 이벤트 수신
    )
    public void wheneverRegisterationRequested_RegistBook(
        @Payload RegisterationRequested registerationRequested
    ) {
        System.out.println(
            "\n\n##### listener RegistBook : " 
            + registerationRequested 
            + " 도서등록요청됨 이벤트 수신 완료\n\n"
        );

        // 도서 등록 시작 (DB 저장)
        System.out.println("\n\n 도서 ID: " + registerationRequested.getId() + 
                            "\n 도서 제목: " + registerationRequested.getManuscriptTitle() +
                            "\n도서 등록 시작\n\n");
        bookShelfApplicationService.processRegisterBook(registerationRequested);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,  // Kafka 입력
        condition = "headers['type']=='ValidSubscription'" //특정 이벤트 타입만
    )
    // validsubscription 이벤트 타입 발생-> 메서드 실행
    public void wheneverValidSubscription_ReadBook(
        @Payload ValidSubscription validSubscription
    ) {
        ValidSubscription event = validSubscription;
        System.out.println(
            "\n\n##### listener ReadBook : " + validSubscription + "\n\n"
        );

        bookShelfApplicationService.processSubscriptionReading(validSubscription);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PointDeducted'"
    )
    public void wheneverPointDeducted_ReadBook(
        @Payload PointDeducted pointDeducted
    ) {
        PointDeducted event = pointDeducted;
        System.out.println(
            "\n\n##### listener ReadBook : " + pointDeducted + "\n\n"
        );

        bookShelfApplicationService.processPointReading(pointDeducted);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='BookRead'"
    )
    public void wheneverBookRead_IsRead(@Payload BookRead bookRead) {
        BookRead event = bookRead;
        System.out.println("\n\n##### listener IsRead : " + bookRead + "\n\n");

        bookShelfApplicationService.processBookReading(bookRead);
    }

    public void processBookReading(BookRead event) {
        // 기본적으로 아무 로직이 필요 없다면 로그만 출력해도 됨
        System.out.println("Processing BookRead event: " + event);

    }
}
//>>> Clean Arch / Inbound Adaptor
