package ktlibrary.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestPointCommand {

    private Long id;
    private Long customerId;
    private Long point;
}
