package ktlibrary.infra;

import java.util.List;
import ktlibrary.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "readBooks", path = "readBooks")
public interface ReadBookRepository
    extends PagingAndSortingRepository<ReadBook, Long> {

          Optional<ReadBook> findByBookId(Long bookId);
    }
