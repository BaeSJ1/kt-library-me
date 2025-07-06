package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class EditAuthorCommand {

    private String email;
    private String introduction;
}
