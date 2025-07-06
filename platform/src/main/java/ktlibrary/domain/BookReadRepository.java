package ktlibrary.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReadRepository extends JpaRepository<BookReadModel, Long> {
    List<BookReadModel> findByCustomerId(Long customerId);
}