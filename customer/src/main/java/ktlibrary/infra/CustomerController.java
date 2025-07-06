package ktlibrary.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktlibrary.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/customers")
@Transactional
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping(
        value = "/customers/registeruser",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Customer registerUser(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RegisterUserCommand registerUserCommand
    ) throws Exception {
        System.out.println("##### /customer/registerUser  called #####");
        Customer customer = new Customer();
        customer.registerUser(registerUserCommand);
        customerRepository.save(customer);
        return customer;
    }

    @RequestMapping(
        value = "/customers/requestbook",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Customer requestBook(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RequestBookCommand requestBookCommand
    ) throws Exception {
        System.out.println("##### /customer/requestBook  called #####");
        Customer customer = new Customer();
        customer.requestBook(requestBookCommand);
        customerRepository.save(customer);
        return customer;
    }
    
    @RequestMapping(
        value = "/customers/login",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public LoginResponse login(
        @RequestBody LoginCommand loginCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /customers/login called #####");
        
        try {
            LoginResponse loginResponse = Customer.login(loginCommand, customerRepository);
            response.setStatus(HttpServletResponse.SC_OK);
            return loginResponse;
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new Exception(e.getMessage());
        } catch (IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            throw new Exception(e.getMessage());
        }
    }
    
    @RequestMapping(
        value = "/customers",
        method = RequestMethod.GET,
        produces = "application/json;charset=UTF-8"
    )
    public Iterable<Customer> getAllCustomers(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /customers getAllCustomers called #####");
        return customerRepository.findAll();
    }
}
//>>> Clean Arch / Inbound Adaptor