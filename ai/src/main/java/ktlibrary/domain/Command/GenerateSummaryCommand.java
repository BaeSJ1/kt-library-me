package ktlibrary.domain.Command;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class GenerateSummaryCommand extends BaseManuscriptCommand {
    private String summary;             // 요약 내용
}
