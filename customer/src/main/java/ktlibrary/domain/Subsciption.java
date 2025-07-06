package ktlibrary.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import ktlibrary.CustomerApplication;
import ktlibrary.domain.InvalidSubscription;
import ktlibrary.domain.ValidSubscription;
import lombok.Data;

@Entity
@Table(name = "Subsciption_table")
@Data
//<<< DDD / Aggregate Root
public class Subsciption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean isValid;

    private String startDate;

    private String endDate;

    private Date createdAt;

    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public static SubsciptionRepository repository() {
        SubsciptionRepository subsciptionRepository = CustomerApplication.applicationContext.getBean(
            SubsciptionRepository.class
        );
        return subsciptionRepository;
    }

    public static CustomerRepository customerRepository() {
        CustomerRepository customerRepository = CustomerApplication.applicationContext.getBean(
            CustomerRepository.class
        );
        return customerRepository;
    }


    public void cancelSubscription(CancelSubscriptionCommand command) {
    this.isValid = false;
    this.updatedAt = new Date();

    InvalidSubscription event = new InvalidSubscription(this);
    event.publishAfterCommit();
}


    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Methodwa
    public void subscribe(SubscribeCommand subscribeCommand) {
    this.customer =   customerRepository().findById( subscribeCommand.getCustomerId()).get();
    this.isValid = true;
    this.startDate = LocalDate.now().toString();
    this.endDate = LocalDate.now().plusMonths(1).toString();
    this.createdAt = new Date();
    this.updatedAt = new Date();

    // 이벤트 발행
    ValidSubscription event = new ValidSubscription(this);
    event.publishAfterCommit();
}

public static void isSubscribed(BookRequested bookRequested) {
    repository().findById(bookRequested.getSubscriptionId()).ifPresent(subsciption -> {
        if (subsciption.getIsValid()) {
            ValidSubscription event = new ValidSubscription(subsciption);
            event.publishAfterCommit();
        } else {
            InvalidSubscription event = new InvalidSubscription(subsciption);
            event.publishAfterCommit();
        }
    });
}
    public Long getCustomerId() {
        return this.customer != null ? this.customer.getId() : null;
    }

    //>>> Clean Arch / Port Method

    //<<< Clean Arch / Port Method
    
    // public static void isSubscribed(BookRequested bookRequested) {
    //     //implement business logic here:

    //     /** Example 1:  new item 
    //     Subsciption subsciption = new Subsciption();
    //     repository().save(subsciption);

    //     ValidSubscription validSubscription = new ValidSubscription(subsciption);
    //     validSubscription.publishAfterCommit();
    //     InvalidSubscription invalidSubscription = new InvalidSubscription(subsciption);
    //     invalidSubscription.publishAfterCommit();
    //     */

    //     /** Example 2:  finding and process
        
    //     // if bookRequested.subsciptionId exists, use it
        
    //     // ObjectMapper mapper = new ObjectMapper();
    //     // Map<Long, Object> customerMap = mapper.convertValue(bookRequested.getSubsciptionId(), Map.class);

    //     repository().findById(bookRequested.get???()).ifPresent(subsciption->{
            
    //         subsciption // do something
    //         repository().save(subsciption);

    //         ValidSubscription validSubscription = new ValidSubscription(subsciption);
    //         validSubscription.publishAfterCommit();
    //         InvalidSubscription invalidSubscription = new InvalidSubscription(subsciption);
    //         invalidSubscription.publishAfterCommit();

    //      });
    //     */

    // }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
