package ktlibrary.infra;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import ktlibrary.domain.*;
import ktlibrary.domain.Repository.*;
import ktlibrary.domain.Command.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//<<< Clean Arch / Inbound Adaptor

@RestController
// @RequestMapping(value="/books")
@Transactional
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(
        value = "/books/requestcover",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Book requestCover(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody GenerateCoverCommand generateCoverCommand
    ) throws Exception {
        System.out.println("##### /book/requestCover  called #####");
        Book book = new Book();
        book.requestCover(generateCoverCommand);
        bookRepository.save(book);
        return book;
    }

    @RequestMapping(
        value = "/books/transformebook",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Book transformEbook(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody GenerateEbookCommand transformEbookCommand
    ) throws Exception {
        System.out.println("##### /book/transformEbook  called #####");
        Book book = new Book();
        book.transformEbook(transformEbookCommand);
        bookRepository.save(book);
        return book;
    }

    @RequestMapping(
        value = "/books/setcategory",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Book setCategory(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody SetCategoryCommand setCategoryCommand
    ) throws Exception {
        System.out.println("##### /book/setCategory  called #####");
        Book book = new Book();
        book.settingCategory(setCategoryCommand);
        bookRepository.save(book);
        return book;
    }

    @RequestMapping(
        value = "/books/requestregistration",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Book requestRegistration(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RegistBookCommand requestRegistrationCommand
    ) throws Exception {
        System.out.println("##### /book/requestRegistration  called #####");
        Book book = new Book();
        book.requestRegistration(requestRegistrationCommand);
        bookRepository.save(book);
        return book;
    }

    @RequestMapping(
        value = "/books/{id}/calculateprice",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Book calculatePrice(
        @PathVariable(value = "id") Long id,
        @RequestBody CalculatePriceCommand calculatePriceCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /book/calculatePrice  called #####");
        Optional<Book> optionalBook = bookRepository.findById(id);

        optionalBook.orElseThrow(() -> new Exception("No Entity Found"));
        Book book = optionalBook.get();
        book.calculatePrice(calculatePriceCommand);

        bookRepository.save(book);
        return book;
    }

    @RequestMapping(
        value = "/books/{id}/generatesummary",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Book generateSummary(
        @PathVariable(value = "id") Long id,
        @RequestBody GenerateSummaryCommand generateSummaryCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /book/generateSummary  called #####");
        Optional<Book> optionalBook = bookRepository.findById(id);

        optionalBook.orElseThrow(() -> new Exception("No Entity Found"));
        Book book = optionalBook.get();
        book.generateSummary(generateSummaryCommand);

        bookRepository.save(book);
        return book;
    }
}
//>>> Clean Arch / Inbound Adaptor
