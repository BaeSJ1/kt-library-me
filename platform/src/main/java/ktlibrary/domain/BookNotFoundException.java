package ktlibrary.domain;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long bookId) {
        super("도서를 찾을 수 없습니다. bookId: " + bookId);
    }
}