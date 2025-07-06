package ktlibrary.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktlibrary.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/subsciptions")
@Transactional
public class SubsciptionController {

    @Autowired
    SubsciptionRepository subsciptionRepository;


    @Autowired
    CustomerRepository customerRepository;
    @RequestMapping(
        value = "/subsciptions/{id}/cancelsubscription",
        method = RequestMethod.DELETE,
        produces = "application/json;charset=UTF-8"
    )
    public Subsciption cancelSubscription(
        @PathVariable(value = "id") Long id,
        @RequestBody CancelSubscriptionCommand cancelSubscriptionCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println(
            "##### /subsciption/cancelSubscription  called #####"
        );
        Optional<Subsciption> optionalSubsciption = subsciptionRepository.findById(
            id
        );

        optionalSubsciption.orElseThrow(() -> new Exception("No Entity Found"));
        Subsciption subsciption = optionalSubsciption.get();
        subsciption.cancelSubscription(cancelSubscriptionCommand);

        subsciptionRepository.delete(subsciption);
        return subsciption;
    }

    @RequestMapping(
        value = "/subsciptions/subscribe",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Subsciption subscribe(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody SubscribeCommand subscribeCommand
    ) throws Exception {
        System.out.println("##### /subsciption/subscribe  called #####");
        //Long  customerId = customerRepository.findById(subscribeCommand.getCustomerId()).get().getId();
        Subsciption mySubsciption = subsciptionRepository.findByCustomer_Id(subscribeCommand.getCustomerId());
        if (mySubsciption != null){
          
            //Subsciption subsciption = optionalSubsciption.get();
            mySubsciption.subscribe(subscribeCommand);
            subsciptionRepository.save(mySubsciption);
            return mySubsciption;
        }
      
        Subsciption subsciption = new Subsciption();
        subsciption.subscribe(subscribeCommand);
        subsciptionRepository.save(subsciption);
        return subsciption;
    }
}
//>>> Clean Arch / Inbound Adaptor
