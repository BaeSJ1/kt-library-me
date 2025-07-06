package ktlibrary.domain;

import ktlibrary.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional; 

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "bookShelves",
    path = "bookShelves"
)
public interface BookShelfRepository
    extends PagingAndSortingRepository<BookShelf, Long> {

        Optional<BookShelf> findByBookId(Long bookId);
    }
