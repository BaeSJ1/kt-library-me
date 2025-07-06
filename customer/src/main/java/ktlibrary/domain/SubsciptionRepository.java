package ktlibrary.domain;

import ktlibrary.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(
    collectionResourceRel = "subsciptions",
    path = "subsciptions"
)
public interface SubsciptionRepository
    extends PagingAndSortingRepository<Subsciption, Long> {
         Subsciption findByCustomer_IdAndIsValid(Long customerId, Boolean isValid);
         Subsciption findByCustomer_Id(Long customerId);
    }
