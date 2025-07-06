package ktlibrary.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import ktlibrary.config.kafka.KafkaProcessor;
import ktlibrary.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ReadBookViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private ReadBookRepository readBookRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenBookRequested_then_CREATE_1(
        @Payload BookRequested bookRequested
    ) {
        try {
            if (!bookRequested.validate()) return;

            // view 객체 생성
            ReadBook readBook = new ReadBook();
            // view 객체에 이벤트의 Value 를 set 함
            readBook.setBookId(bookRequested.getBookId());
            readBook.setBookShelfId(bookRequested.getBookshelfId());
            readBook.setTitle(bookRequested.getTitle());
            readBook.setPrice(bookRequested.getPrice());
            // view 레파지 토리에 save
            readBookRepository.save(readBook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
