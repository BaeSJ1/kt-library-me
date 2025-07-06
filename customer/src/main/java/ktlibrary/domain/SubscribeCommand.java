package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;

@Data
public class SubscribeCommand {

    private Long id;
    private Long customerId;
}
