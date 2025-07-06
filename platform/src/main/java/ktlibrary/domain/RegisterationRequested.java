package ktlibrary.domain;

import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class RegisterationRequested extends AbstractEvent {
    private Long authorId;              // 작가 id
    private String authorName;          // 작가 이름
    private String introduction;        // 작가 소개

    private Long id;                    // 도서 id
    private String manuscriptTitle;     // 원고 제목
    private String manuscriptContent;   // 원고 내용

    private String summary;             // 도서 요약
    private String coverUrl;            // 도서 커버 URL
    private String bookUrl;             // 도서 파일 URL
    private String category;            // 도서 카테고리
    private Long price;                 // 도서 가격

    private Date createdAt;
    private Date updatedAt;
}
