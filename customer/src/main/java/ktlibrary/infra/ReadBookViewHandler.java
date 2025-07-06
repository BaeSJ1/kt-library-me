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
    public void whenBookRequested_then_CREATE(
        @Payload BookRegistered bookRegistered  // 도서 열람 요청될 때가 아니라 도서가 등록될 때 조회 데이터를 만들어야함.
    ) {
        try {
            if (!bookRegistered.validate()){
                System.out.println("이벤트 validate 실패: " + bookRegistered);
                return;
            } 

            // 중복 방지
            Optional<ReadBook> existing = readBookRepository.findByBookId(bookRegistered.getBookId());
            if (existing.isPresent()) {
                System.out.println("이미 등록된 도서입니다. 저장 생략 → bookId: " + bookRegistered.getBookId());
                return;
            }

            System.out.println("BookRegistered 이벤트 수신: " + bookRegistered);

            // view 객체 생성
            ReadBook readBook = new ReadBook();
            // view 객체에 이벤트의 Value 를 set 함
            readBook.setBookId(bookRegistered.getBookId());
            readBook.setBookShelfId(bookRegistered.getId());
            readBook.setTitle(bookRegistered.getTitle());
            readBook.setPrice(bookRegistered.getPrice());
            // view 레파지 토리에 save
            readBookRepository.save(readBook);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
