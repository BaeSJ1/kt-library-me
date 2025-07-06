package ktlibrary.infra;

import java.util.List;
import ktlibrary.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(
    collectionResourceRel = "readAuthors",
    path = "readAuthors"
)
public interface ReadAuthorRepository
    extends PagingAndSortingRepository<ReadAuthor, Long> {
    List<ReadAuthor> findByAuthorId(Long authorId);
}
