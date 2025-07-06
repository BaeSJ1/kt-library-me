package ktlibrary.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ktlibrary.domain.PublishingRequested;
import ktlibrary.domain.Command.GenerateSummaryCommand;
import ktlibrary.infra.application.service.BookAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import ktlibrary.domain.Book;

@RestController
@RequestMapping("/test/book")
@RequiredArgsConstructor
public class BookPostController {
    
    private final BookAiService bookAiService;

    @PostMapping()
    public ResponseEntity<?> testPipeline(@RequestBody GenerateSummaryCommand command) {
        // 1. 요청 본문에서 GenerateSummaryCommand 객체를 받습니다.
        // 2. BookAiService의 processBookAiPipeline 메서드를 호출하여 처리합니다.
        // 3. 결과를 ResponseEntity로 반환합니다.
        
        Book result = bookAiService.processBookAiPipeline(command);
        return ResponseEntity.ok(result);
    }
    
}