package ktlibrary.domain;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long bookId) {
        super("도서 ID [" + bookId + "] 를 찾을 수 없습니다.");
    }
}