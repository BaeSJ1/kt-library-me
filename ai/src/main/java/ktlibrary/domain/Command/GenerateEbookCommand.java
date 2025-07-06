package ktlibrary.domain.Command;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class GenerateEbookCommand extends BaseManuscriptCommand {
    private String bookUrl;
}
