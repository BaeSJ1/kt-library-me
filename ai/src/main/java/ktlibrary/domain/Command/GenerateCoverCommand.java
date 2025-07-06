package ktlibrary.domain.Command;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class GenerateCoverCommand extends BaseManuscriptCommand {
    private String summary;     // 요약 내용
    private String coverUrl;    // 커버 이미지 url
}
