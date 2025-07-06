package ktlibrary.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktlibrary.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ktlibrary.infra.BookShelfApplicationService;  // ✅ ApplicationService 위치 확인
import ktlibrary.domain.ReadBookCommand;              // ⬅️ 같이 필요

import ktlibrary.domain.BookReadModel;

import ktlibrary.domain.AccessType;                        // ⬅️ Enum 정의된 위치
import org.springframework.http.ResponseEntity;            // ✅ Spring의 HTTP 응답 객체
import org.springframework.web.bind.annotation.*;   
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

//<<< Clean Arch / Inbound Adaptor

@RestController
@RequestMapping(value="/bookShelves")
@RequiredArgsConstructor
@Transactional
public class BookShelfController {

    private final BookShelfApplicationService bookShelfApplicationService;
    private final BookReadRepository bookReadRepository;
    // private final BookShelfRepository bookShelfRepository;

    @GetMapping
    public List<BookReadModel> getBookshelfByCustomer(@RequestParam Long customerId) {
        return bookReadRepository.findByCustomerId(customerId);
    }

    @Autowired
    private BookShelfRepository bookShelfRepository;

    @GetMapping("/all")
    public Iterable<BookShelf> getAllBooks() {
        return bookShelfRepository.findAll();
    }

    @PostMapping("/registerTest")
    public ResponseEntity<Void> registerTestBook() {
        RegisterationRequested event = new RegisterationRequested();

        // 테스트용 도서 등록 정보 설정 -> 하드코딩
        event.setId(100L);                          // 도서 ID
        event.setManuscriptTitle("테스트 도서");    // 도서 제목
        event.setManuscriptContent("테스트 내용");  // 도서 내용
        event.setCategory("문학");                  // 카테고리
        event.setAuthorId(123L);                    // 작가 ID
        event.setAuthorName("홍길동");              // 작가 이름
        event.setIntroduction("테스트용");          // 작가 소개
        event.setSummary("요약");                   // 도서 요약
        event.setCoverUrl("cover.jpg");             // 도서 표지 URL
        event.setBookUrl("file.pdf");               // 도서 파일 URL
        event.setPrice(1000L);                      // 도서 가격

        bookShelfApplicationService.processRegisterBook(event);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bookId}/read")
    public ResponseEntity<Map<String, Object>> readBook(@PathVariable Long bookId) {
        // 디버깅을 위해 도서 상태 확인
        BookShelf bookBefore = bookShelfRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        System.out.println("Before - ViewCount: " + bookBefore.getViewCount() + 
                        ", IsBestSeller: " + bookBefore.getIsBestSeller());
        
        ReadBookCommand command = ReadBookCommand.builder()
            .bookId(bookId)
            .customerId(1L)
            .accessType(AccessType.SUBSCRIPTION)
            .build();

        bookShelfApplicationService.executeBookReading(command);
        
        // 업데이트 후 상태 확인
        BookShelf bookAfter = bookShelfRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        System.out.println("After - ViewCount: " + bookAfter.getViewCount() + 
                        ", IsBestSeller: " + bookAfter.getIsBestSeller());
        
        // 응답 데이터 반환
        Map<String, Object> response = new HashMap<>();
        response.put("viewCount", bookAfter.getViewCount());
        response.put("isBestSeller", bookAfter.getIsBestSeller());
        
        return ResponseEntity.ok(response);
    }

    //도서상태 확인
    @GetMapping("/{bookId}")
    public ResponseEntity<BookShelf> getBook(@PathVariable Long bookId) {
        BookShelf book = bookShelfRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        return ResponseEntity.ok(book);
    }

}
//>>> Clean Arch / Inbound Adaptor
