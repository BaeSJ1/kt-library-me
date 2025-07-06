package ktlibrary.domain.Command;

import java.util.*;
import lombok.Data;

@Data
public class RegistBookCommand {
    private Long id;
    private String manuscriptTitle;
    private String manuscriptContent;
    private Long authorId;
    private String authorName;
    private String introduction;

    private String summary;
    private String coverUrl;
    private String category;
    private String bookUrl;

    private Date createdAt;
    private Date updatedAt;
    private Long price;
}
