package ktlibrary.domain.Command;

import lombok.Data;

@Data
public class GenerateCategoryCommand extends BaseManuscriptCommand {
    private String summary;     // 요약 내용
    private String category;    // 카테고리
}
