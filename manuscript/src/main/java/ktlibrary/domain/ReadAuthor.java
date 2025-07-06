package ktlibrary.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "ReadAuthor_table")
@Data
public class ReadAuthor {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long authorId;
    private Boolean isApproved;
    private String authorName;
    private String introduction;
}
