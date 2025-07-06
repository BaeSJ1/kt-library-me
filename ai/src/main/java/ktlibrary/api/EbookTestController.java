package ktlibrary.api;

import org.springframework.web.bind.annotation.RestController;

import ktlibrary.infra.application.service.EbookService;
import ktlibrary.infra.application.service.FileStorageService;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class EbookTestController {
    private final EbookService ebookService;
    private final FileStorageService fileStorageService;

    @GetMapping("/test-ebook")
    public String testEbook() {
        String manuscriptTitle = "소나기";
        String authorName = "황순원";
        String content;

        try {
            // 1. resources 폴더의 루트에 위치한 test_book.txt 파일을 읽습니다.
            // ClassPathResource를 사용하면 빌드된 JAR 내부에서도 리소스를 접근할 수 있습니다.
            ClassPathResource resource = new ClassPathResource("test_book2.txt");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                
                // 2. 파일의 모든 줄을 하나의 문자열로 결합합니다. (줄바꿈 기준)
                // 만약 책 내용이 매우 길다면 이 문자열은 SummaryService 내부에서 chunking 처리됩니다.
                content = reader.lines().collect(Collectors.joining("\n"));
            }

        } catch (Exception e) {
            // 파일을 읽거나 요약 중 문제가 발생한 경우, 에러 메시지를 반환합니다.
            e.printStackTrace();
            return "파일을 읽거나 요약하는 도중 오류가 발생했습니다: " + e.getMessage();
        }

        byte[] ebookFile = ebookService.generateEbook(manuscriptTitle, authorName, content);

        String bookUrl;
        try {
            bookUrl = fileStorageService.savePdf(Long.valueOf(1), ebookFile);
        } catch (IOException e) {
            throw new RuntimeException("PDF 저장 중 오류가 발생했습니다.", e);
        }

        return bookUrl;
    }


    
}
