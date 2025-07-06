package ktlibrary.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.*;

//<<< EDA / CQRS
@Entity
@Table(name = "ReadBook_table")
@Data
@Getter
public class ReadBook {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Long bookId;
    private Long bookShelfId;
    private String title;
    private Long price;
}
