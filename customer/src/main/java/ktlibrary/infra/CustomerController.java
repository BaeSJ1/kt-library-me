package ktlibrary.infra;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktlibrary.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/customers")
@Transactional
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private ReadBookRepository readBookRepository;

    @Autowired
    private SubsciptionRepository subsciptionRepository;

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
    public ResponseEntity<?> requestBook(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RequestBookCommand requestBookCommand
    ) throws Exception {
        System.out.println("##### /customer/requestBook  called #####");
        // 1. 고객 정보 가져오기
        Customer customer = customerRepository.findById(requestBookCommand.getCustomerId()).orElseThrow(() -> new Exception("고객 ID가 존재하지 않습니다: " + requestBookCommand.getCustomerId()));
        
        // 2. 도서 가격 가져오기
        ReadBook bookInfo = readBookRepository.findByBookId(requestBookCommand.getBookId()).orElse(null);
        Long price = (bookInfo != null) ? bookInfo.getPrice() : null;

        // 3. 구독권 유효성 확인
        Subsciption sub = subsciptionRepository.findByCustomer_Id(requestBookCommand.getCustomerId());

        // 4. 커맨드 수행
        customer.requestBook(requestBookCommand);
        customerRepository.save(customer);

        // 5. 응답 분기
        if (sub == null || !Boolean.TRUE.equals(sub.getIsValid())) {
            return ResponseEntity.ok(Map.of(
                "status", "SUBSCRIPTION_INVALID",
                "price", price,
                "message", "구독권이 없으므로 포인트 차감이 필요합니다."
            ));
        }

        return ResponseEntity.ok(Map.of(
            "status", "SUCCESS",
            "message", "도서 열람 요청이 완료되었습니다."
        ));
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