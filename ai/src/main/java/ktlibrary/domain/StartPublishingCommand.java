package ktlibrary.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StartPublishingCommand {
    private Long bookId;
    private String manuscriptTitle;
    private String manuscriptContent;
    private Long authorId;
    private String authorName;
    private String authorIntroduction;
}
