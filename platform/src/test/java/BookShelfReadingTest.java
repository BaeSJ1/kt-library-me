import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import ktlibrary.domain.*;

@SpringBootTest(classes = ktlibrary.PlatformApplication.class)
@Transactional
public class BookShelfReadingTest {

    @Autowired
    private BookShelfRepository repository;

    @BeforeEach
    void clearDB() {
        repository.deleteAll();
    }

    @Test
    void testBookReadingIncreasesViewCount() {
        // given: 도서 등록
        RegisterationRequested event = new RegisterationRequested();
        event.setId(1L);
        event.setManuscriptTitle("열람 테스트 도서");
        event.setManuscriptContent("열람 테스트 도서 내용");
        event.setCategory("기술");
        event.setAuthorId(321L);
        event.setAuthorName("이몽룡");
        event.setIntroduction("테스트용 도서입니다.");
        event.setSummary("요약");
        event.setCoverUrl("cover.png");
        event.setBookUrl("file.pdf");
        event.setPrice(1000L);

        BookShelf shelf = new BookShelf();
        shelf.regist(event);
        repository.save(shelf);

        // when: 열람 수행
        BookShelf target = repository.findByBookId(1L).get();
        BookReadResult result = target.readBy(100L, AccessType.SUBSCRIPTION);  // 예: 구독으로 열람

        // then: 뷰카운트 증가 및 열람 결과 검증
        assertEquals(1L, target.getViewCount());
        assertEquals(1L, result.getNewViewCount());  // 열람 결과 반환 값도 확인
        // assertEquals(100L, result.getCustomerId());
        // assertEquals(AccessType.SUBSCRIPTION, result.getAccessType());
    }

    @Test
    void testBestSellerPromotion() {
        // given
        RegisterationRequested event = new RegisterationRequested();
        event.setId(2L);
        event.setManuscriptTitle("베스트셀러 테스트");
        event.setManuscriptContent("베스트셀러 도서 내용");
        event.setCategory("문학");
        event.setAuthorId(222L);
        event.setAuthorName("허균");
        event.setIntroduction("열람 횟수 테스트입니다.");
        event.setSummary("요약입니다.");
        event.setCoverUrl("cover.png");
        event.setBookUrl("file.pdf");
        event.setPrice(1000L);

        BookShelf shelf = new BookShelf();
        shelf.regist(event);
        repository.save(shelf);

        BookShelf target = repository.findByBookId(2L).get();

        // when: 5번 열람
        for (int i = 0; i < 5; i++) {
            target.readBy(100L, AccessType.SUBSCRIPTION);
        }

        // then
        assertTrue(target.getIsBestSeller());  // 베스트셀러 승격 여부
        assertEquals(5L, target.getViewCount());  // 열람 횟수
    }
}
