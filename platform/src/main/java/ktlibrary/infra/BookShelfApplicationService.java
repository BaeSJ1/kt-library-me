package ktlibrary.infra;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import ktlibrary.domain.BookShelfRepository;
import ktlibrary.domain.RegisterationRequested;
import ktlibrary.domain.ValidSubscription;
import ktlibrary.domain.ReadBookCommand;
import ktlibrary.domain.PointDeducted;
import ktlibrary.domain.BookReadResult;
import ktlibrary.domain.DomainEventPublisher;

import ktlibrary.domain.BookShelf;
import ktlibrary.domain.BookNotFoundException;
import ktlibrary.domain.BookReadEvent;
import ktlibrary.domain.BecameBestseller;
import ktlibrary.domain.AccessType;
import ktlibrary.domain.BookRead;
import ktlibrary.domain.BookRegistered; 

import ktlibrary.domain.BookReadModel;
import ktlibrary.domain.BookReadRepository;

import ktlibrary.event.CreateBookEvent;


@Service
@Transactional
public class BookShelfApplicationService {

    @Autowired
    private BookReadRepository bookReadRepository;

    @Autowired
    private BookShelfRepository bookShelfRepository;

    @Autowired
    private DomainEventPublisher eventPublisher;

    /**
     * AI 서비스에서 처리된 결과를 수신하여
     * 도서 등록을 수행하는 메서드
     * @param event
     */
    public void processRegisterBook(RegisterationRequested event) {
        BookShelf bookShelf = new BookShelf();

        bookShelf.regist(event);  // 도메인 메서드 호출로 서재에 도서 등록 정보 설정

        // 도메인 상태 변경
        bookShelfRepository.save(bookShelf);    // 저장
        System.out.println("\n\n**도서 등록 완료**\n" + 
                            "서재 ID: " + bookShelf.getId() + "\n" +
                            "도서 ID: " + bookShelf.getBookId() + "\n" +
                            "도서 제목: " + bookShelf.getTitle() + "\n\n");

        // 도서 등록 완료됨 이벤트 발행
        CreateBookEvent createBookEvent = new CreateBookEvent(bookShelf);
        createBookEvent.publish();
    }

    // 3. Application Service에서 비즈니스 플로우 조율
    public void processSubscriptionReading(ValidSubscription event) {
        // Command 생성
        ReadBookCommand command = ReadBookCommand.builder()
            .bookId(event.getBookId())
            .accessType(AccessType.SUBSCRIPTION)
            .build();

        // 공통 처리 로직 호출
        executeBookReading(command);
    }

    public void processBookReading(BookRead event) {
        ReadBookCommand command = ReadBookCommand.builder()
            .bookId(event.getBookId())
            .customerId(event.getCustomerId())
            .accessType(AccessType.valueOf(event.getAccessType()))
            .build();
        executeBookReading(command);
    }

    public void executeBookReading(ReadBookCommand command) {
        // 1. Repository를 통해 Aggregate 조회
        BookShelf bookShelf = bookShelfRepository.findById(command.getBookId())
            .orElseThrow(() -> new BookNotFoundException(command.getBookId()));

        // 2. Aggregate의 인스턴스 메서드 호출
        BookReadResult result = bookShelf.readBy(command.getCustomerId(), command.getAccessType());

        // 3. 상태 저장
        bookShelfRepository.save(bookShelf);

        // 4.1 리드모델 저
        BookReadModel readModel = BookReadModel.builder()
            .customerId(command.getCustomerId())
            .bookId(command.getBookId())
            .title(bookShelf.getTitle())
            .isBestSeller(bookShelf.getIsBestSeller())
            .viewCount(bookShelf.getViewCount())
            .price(bookShelf.getPrice())
            .accessType(command.getAccessType())
            .build();

        bookReadRepository.save(readModel);

        // 4. 도서 열람 이벤트 발행
        eventPublisher.publish(new BookReadEvent(
            bookShelf.getId(),
            command.getCustomerId(),
            result.getNewViewCount(),
            result.isBestSellerPromoted()
        ));

        // 5. 베스트셀러 이벤트 발행 (조건부)
        if (result.isBestSellerPromoted()) {
            eventPublisher.publish(new BecameBestseller(bookShelf));
        }
    }

    public void processPointReading(PointDeducted event) {
        ReadBookCommand command = ReadBookCommand.builder()
            .bookId(event.getBookId())
            .customerId(event.getCustomerId())
            .accessType(AccessType.POINT)
            .build();
        executeBookReading(command);
    }
}
