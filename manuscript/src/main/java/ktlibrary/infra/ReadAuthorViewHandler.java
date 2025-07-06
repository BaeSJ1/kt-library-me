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
public class ReadAuthorViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private ReadAuthorRepository readAuthorRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorRegistered_then_CREATE_1(
        @Payload AuthorRegistered authorRegistered
    ) {
        try {
            if (!authorRegistered.validate()) return;

            // view 객체 생성
            ReadAuthor readAuthor = new ReadAuthor();
            // view 객체에 이벤트의 Value 를 set 함
            readAuthor.setId(authorRegistered.getId());  // Author ID를 ReadAuthor ID로 사용
            readAuthor.setAuthorId(authorRegistered.getId());
            readAuthor.setIsApproved(authorRegistered.getIsApproved());
            readAuthor.setAuthorName(authorRegistered.getName());
            readAuthor.setIntroduction(authorRegistered.getIntroduction());
            // view 레파지 토리에 save
            readAuthorRepository.save(readAuthor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAuthorApproved_then_UPDATE_1(
        @Payload AuthorApproved authorApproved
    ) {
        try {
            if (!authorApproved.validate()) return;
            // view 객체 조회

            List<ReadAuthor> readAuthorList = readAuthorRepository.findByAuthorId(
                authorApproved.getId()
            );
            for (ReadAuthor readAuthor : readAuthorList) {
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                readAuthor.setIsApproved(authorApproved.getIsApproved());
                // view 레파지 토리에 save
                readAuthorRepository.save(readAuthor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
