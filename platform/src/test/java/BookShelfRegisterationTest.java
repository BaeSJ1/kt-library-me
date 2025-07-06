import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import ktlibrary.domain.AccessType;

import static org.junit.jupiter.api.Assertions.*;

import ktlibrary.domain.BookShelf;
import ktlibrary.domain.BookShelfRepository;
import ktlibrary.domain.RegisterationRequested;

@SpringBootTest(classes = ktlibrary.PlatformApplication.class)
@Transactional
public class BookShelfRegisterationTest {

    @Autowired
    private BookShelfRepository repository;

    @BeforeEach
    void clearDB() {
        repository.deleteAll();
    }

    @Test
    void testBookRegisterationViaEvent() {
        // given: 가짜 Kafka 이벤트 객체 생성
        RegisterationRequested event = new RegisterationRequested();
        event.setId(1L);
        event.setManuscriptTitle("등록 테스트 도서");
        event.setManuscriptContent("등록 테스트 도서 내용");
        event.setCategory("기술");
        event.setAuthorId(123L);
        event.setAuthorName("홍길동");
        event.setIntroduction("이 도서는 테스트용입니다.");
        event.setSummary("요약 내용");
        event.setCoverUrl("cover.png");
        event.setBookUrl("file.pdf");
        event.setPrice(1000L);

        // when: regist() 호출 후 저장
        BookShelf shelf = new BookShelf();
        shelf.regist(event);
        repository.save(shelf);

        // then: 저장 확인
        BookShelf saved = repository.findByBookId(1L)
            .orElseThrow(() -> new RuntimeException("등록 실패"));

        // assertEquals("등록 테스트 도서", saved.getManuscriptTitle());
        assertEquals("홍길동", saved.getAuthorName());
        assertEquals(0L, saved.getViewCount());  // 초기값 확인
    }

    @Test
    void testBestsellerPromotion() {
        // given
        RegisterationRequested event = new RegisterationRequested();
        event.setId(2L);
        event.setManuscriptTitle("베스트셀러 테스트 도서");
        event.setManuscriptContent("베스트셀러 도서 내용");
        event.setCategory("문학");
        event.setAuthorId(456L);
        event.setAuthorName("작가B");
        event.setIntroduction("소개");
        event.setSummary("요약");
        event.setCoverUrl("cover2.png");
        event.setBookUrl("file2.pdf");
        event.setPrice(1000L);

        BookShelf shelf = new BookShelf();
        shelf.regist(event);
        repository.save(shelf);

        // when: 5번 열람
        for (int i = 0; i < 5; i++) {
            shelf.readBy(1L, AccessType.SUBSCRIPTION);
        }
        repository.save(shelf);

        // then
        BookShelf updated = repository.findByBookId(2L)
            .orElseThrow(() -> new RuntimeException("도서 없음"));

        assertEquals(5L, updated.getViewCount());
        assertTrue(updated.getIsBestSeller(), "5회 이상 열람되면 베스트셀러로 승격되어야 함");
    }
}
