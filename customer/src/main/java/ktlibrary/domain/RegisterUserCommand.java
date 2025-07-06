package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class RegisterUserCommand {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Boolean isKtUser;
}
