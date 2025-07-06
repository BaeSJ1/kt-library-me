package ktlibrary.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReadBookCommand {
    private Long bookId;
    private Long customerId;
    private AccessType accessType;
}