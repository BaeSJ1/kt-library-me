package ktlibrary.domain;

import lombok.AllArgsConstructor;
import lombok.*;

@Data
@AllArgsConstructor
public class BookReadResult {
    private Long newViewCount;
    private boolean bestSellerPromoted;
}