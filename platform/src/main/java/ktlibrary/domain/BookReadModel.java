package ktlibrary.domain;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReadModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long bookId;
    private String title;
    private Boolean isBestSeller;
    private Long viewCount;
    private Long price;

    @Enumerated(EnumType.STRING)
    private AccessType accessType;
}