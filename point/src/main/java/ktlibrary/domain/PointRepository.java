package ktlibrary.domain;

import java.util.Optional;
import ktlibrary.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "points", path = "points")
public interface PointRepository
    extends PagingAndSortingRepository<Point, Long> {
        // 고객 ID로 포인트 조회
        Optional<Point> findByCustomerId(Long customerId);
    }
