package ktlibrary.domain;

import lombok.Data;
import java.util.Date;

@Data
public class LoginResponse {
    private Long id;
    private String email;
    private String name;
    private Boolean isKtUser;
    private Date createdAt;
    private Date updatedAt;
    private String token; // 임시 토큰 (실제로는 JWT 등을 사용)
    private String message;

    public LoginResponse(Customer customer, String token, String message) {
        this.id = customer.getId();
        this.email = customer.getEmail();
        this.name = customer.getName();
        this.isKtUser = customer.getIsKtUser();
        this.createdAt = customer.getCreatedAt();
        this.updatedAt = customer.getUpdatedAt();
        this.token = token;
        this.message = message;
    }
}
