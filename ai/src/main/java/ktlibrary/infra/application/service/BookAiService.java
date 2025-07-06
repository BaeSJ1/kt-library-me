package ktlibrary.infra.application.service;

import ktlibrary.domain.Book;
import ktlibrary.domain.Command.GenerateCoverCommand;
import ktlibrary.domain.Command.GenerateEbookCommand;
import ktlibrary.domain.Command.GenerateSummaryCommand;
import ktlibrary.domain.Command.GenerateCategoryCommand;
import ktlibrary.domain.Command.RegistBookCommand;
import ktlibrary.domain.Repository.BookRepository;
import ktlibrary.infra.messaging.event.BookRegisteredEvent;
import ktlibrary.infra.messaging.producer.BookEventPublisher;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookAiService {

    private final SummaryService summaryService;
    private final CoverService coverService;
    private final CategoryService categoryService;
    private final EbookService ebookService;
    private final FileStorageService fileStorageService;

    private final BookRepository bookRepository;

    // 1. 원고 요약 생성
    public String generateSummary(GenerateSummaryCommand command) {
        return summaryService.summarize(
                command.getManuscriptTitle(),
                command.getManuscriptContent(),
                command.getAuthorName(),
                command.getIntroduction()
        );
    }

    // 2. 커버 이미지 생성
    public String generateCoverImage(GenerateCoverCommand command) {
        String title = command.getManuscriptTitle();
        String summary = command.getSummary();

        return coverService.generateCoverImage(title, summary);
    }

    // 3. 카테고리 생성
    public String generateCategory(GenerateCategoryCommand command) {
        String summary = command.getSummary();

        return categoryService.generateCategory(summary);
    }

    // 4. 전자책 생성
    public byte[] generateEbook(GenerateEbookCommand command) {
        return ebookService.generateEbook(
            command.getManuscriptTitle(),
            command.getAuthorName(),
            command.getManuscriptContent()
        );
    }

    // 5. 최종 도서 등록
    public Book registerBook(RegistBookCommand command) {
        Book book = new Book();

        book.setId(command.getId());
        book.setManuscriptTitle(command.getManuscriptTitle());
        book.setManuscriptContent(command.getManuscriptContent());
        book.setAuthorId(command.getAuthorId());
        book.setAuthorName(command.getAuthorName());
        book.setIntroduction(command.getIntroduction());

        book.setSummary(command.getSummary());
        book.setCoverUrl(command.getCoverUrl());
        book.setCategory(command.getCategory());
        book.setBookUrl(command.getBookUrl());
        book.setPrice(command.getPrice());

        return bookRepository.save(book);
    }

    // 전체 파이프라인 실행
    public Book processBookAiPipeline(GenerateSummaryCommand command) {
        // 1. 요약 생성
        String summary = generateSummary(command);
        command.setSummary(summary);
        System.out.println(summary);

        // 2. 커버 이미지 생성
        GenerateCoverCommand coverCommand = new GenerateCoverCommand();
        coverCommand.setId(command.getId());
        coverCommand.setManuscriptTitle(command.getManuscriptTitle());
        coverCommand.setManuscriptContent(command.getManuscriptContent());
        coverCommand.setAuthorId(command.getAuthorId());
        coverCommand.setAuthorName(command.getAuthorName());
        coverCommand.setIntroduction(command.getIntroduction());
        coverCommand.setSummary(summary);

        String coverUrl = generateCoverImage(coverCommand);
        coverCommand.setCoverUrl(coverUrl);
        System.out.println(coverUrl);

        // 3. 카테고리 생성
        GenerateCategoryCommand categoryCommand = new GenerateCategoryCommand();
        categoryCommand.setId(command.getId());
        categoryCommand.setManuscriptTitle(command.getManuscriptTitle());
        categoryCommand.setManuscriptContent(command.getManuscriptContent());
        categoryCommand.setAuthorId(command.getAuthorId());
        categoryCommand.setAuthorName(command.getAuthorName());
        categoryCommand.setIntroduction(command.getIntroduction());
        categoryCommand.setSummary(summary);

        String category = generateCategory(categoryCommand);
        categoryCommand.setCategory(category);
        System.out.println(category);

        // 4. 전자책 생성
        GenerateEbookCommand ebookCommand = new GenerateEbookCommand();
        ebookCommand.setId(command.getId());
        ebookCommand.setManuscriptTitle(command.getManuscriptTitle());
        ebookCommand.setManuscriptContent(command.getManuscriptContent());
        ebookCommand.setAuthorId(command.getAuthorId());
        ebookCommand.setAuthorName(command.getAuthorName());
        ebookCommand.setIntroduction(command.getIntroduction());
        
        byte[] ebookFile = generateEbook(ebookCommand);

        String bookUrl;
        try {
            bookUrl = fileStorageService.savePdf(ebookCommand.getId(), ebookFile);
        } catch (IOException e) {
            throw new RuntimeException("PDF 저장 중 오류가 발생했습니다.", e);
        }

        ebookCommand.setBookUrl(bookUrl);
        System.out.println("bookUrl: " + ebookCommand.getBookUrl());

        // 5. 도서 등록
        RegistBookCommand registCommand = new RegistBookCommand();
        registCommand.setId(command.getId());
        registCommand.setManuscriptTitle(command.getManuscriptTitle());
        registCommand.setManuscriptContent(command.getManuscriptContent());
        registCommand.setAuthorId(command.getAuthorId());
        registCommand.setAuthorName(command.getAuthorName());
        registCommand.setIntroduction(command.getIntroduction());

        registCommand.setSummary(command.getSummary());             // 요약
        registCommand.setCoverUrl(coverCommand.getCoverUrl());      // 커버 url
        registCommand.setCategory(categoryCommand.getCategory());   // 카테고리
        registCommand.setBookUrl(ebookCommand.getBookUrl());        // 전자책 url
        registCommand.setPrice(Long.valueOf(1000));               // 책 가격

        // 도서 등록 후 이벤트 발행 추가
        Book book = registerBook(registCommand);

        // kafka 이벤트 발행
        BookRegisteredEvent event = new BookRegisteredEvent(book);
        event.publish();

        return book;
    }
}
