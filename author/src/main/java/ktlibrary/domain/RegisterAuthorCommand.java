package ktlibrary.domain;

import lombok.Data;

@Data
public class RegisterAuthorCommand {
    private String email;
    private String name;
    private String introduction;
    private String password;
}