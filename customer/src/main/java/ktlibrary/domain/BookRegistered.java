package ktlibrary.domain;

import lombok.Data;

@Data
public class BookRegistered {

    private Long id;             // BookShelf ID
    private Long bookId;         // 도서 ID
    private String title;
    private Long price;
    private Boolean isBestSeller;
    private Long viewCount;

    public boolean validate() {
        return bookId != null && title != null;
    }
}