package ktlibrary.domain;

import lombok.Data;
import java.util.Date;

@Data
public class LoginResponse {
    private Long id;
    private String email;
    private String name;
    private String introduction;
    private Boolean isApproved;
    private Boolean isAdmin;
    private Date createdAt;
    private Date updatedAt;
    private String token; // 임시 토큰 (실제로는 JWT 등을 사용)
    private String message;

    public LoginResponse(Author author, String token, String message) {
        this.id = author.getId();
        this.email = author.getEmail();
        this.name = author.getName();
        this.introduction = author.getIntroduction();
        this.isApproved = author.getIsApproved();
        this.isAdmin = author.getIsAdmin();
        this.createdAt = author.getCreatedAt();
        this.updatedAt = author.getUpdatedAt();
        this.token = token;
        this.message = message;
    }
}