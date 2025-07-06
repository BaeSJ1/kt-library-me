package ktlibrary.infra.application.service;

import ktlibrary.domain.Book;
import ktlibrary.domain.Repository.BookRepository;
import ktlibrary.domain.PublishingRequested;
import ktlibrary.domain.StartPublishingCommand;
import ktlibrary.domain.BookNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ktlibrary.domain.PublishingStarted;
import ktlibrary.domain.Command.GenerateSummaryCommand;
import ktlibrary.infra.application.service.BookAiService;

@Service
@Transactional
public class StartPublishingService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookAiService bookAiService;

    public void handlePublishingRequest(PublishingRequested event) {
        StartPublishingCommand command = StartPublishingCommand.builder()
            .bookId(event.getId())
            .manuscriptTitle(event.getManuscriptTitle())
            .manuscriptContent(event.getManuscriptContent())
            .authorId(event.getAuthorId())
            .authorName(event.getAuthorName())
            .authorIntroduction(event.getAuthorIntroduction())
            .build();

        executePublishing(command);
    }

    private void executePublishing(StartPublishingCommand command) {
        // 새로운 Book 객체 생성
        Book book = new Book();
        
        // Command의 정보를 Book 객체에 저장
        book.publishingStarted(command);

        // 도메인 상태 변경: 저장
        bookRepository.save(book);

        // 출간처리 시작 이벤트 발행 (즉시 발행)
        PublishingStarted publishingStarted = new PublishingStarted(book);
        publishingStarted.publish();

        // AI 출간 파이프라인 실행
        GenerateSummaryCommand summaryCommand = new GenerateSummaryCommand();
        summaryCommand.setId(command.getBookId());
        summaryCommand.setManuscriptTitle(command.getManuscriptTitle());
        summaryCommand.setManuscriptContent(command.getManuscriptContent());
        summaryCommand.setAuthorId(command.getAuthorId());
        summaryCommand.setAuthorName(command.getAuthorName());
        summaryCommand.setIntroduction(command.getAuthorIntroduction());

        bookAiService.processBookAiPipeline(summaryCommand);
        System.out.println("AI 서비스 파이프라인 실행 완료");
        System.out.println("id: " + summaryCommand.getId() + "\n" +
                           "manuscriptTitle: " + summaryCommand.getManuscriptTitle() + "\n" +
                           "도서 등록 요청됨"
        );
    }
}
