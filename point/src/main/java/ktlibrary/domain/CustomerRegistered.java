package ktlibrary.domain;

import java.util.*;
import ktlibrary.domain.*;
import ktlibrary.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistered extends AbstractEvent {

    private Long id;
    private Boolean isKtUser;
}
