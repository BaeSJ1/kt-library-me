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
// @RequestMapping(value="/authors")
@Transactional
public class AuthorController {

    @Autowired
    AuthorRepository authorRepository;

    @RequestMapping(
        value = "/authors/registerauthor",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Author registerAuthor(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RegisterAuthorCommand registerAuthorCommand
    ) throws Exception {
        System.out.println("##### /author/registerAuthor  called #####");
        Author author = new Author();
        author.registerAuthor(registerAuthorCommand);
        authorRepository.save(author);
        return author;
    }

    @RequestMapping(
        value = "/authors/{id}/approveauthor",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Author approveAuthor(
        @PathVariable(value = "id") Long id,
        @RequestBody ApproveAuthorCommand approveAuthorCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /author/approveAuthor  called #####");
        Optional<Author> optionalAuthor = authorRepository.findById(id);

        optionalAuthor.orElseThrow(() -> new Exception("No Entity Found"));
        Author author = optionalAuthor.get();
        author.approveAuthor(approveAuthorCommand);

        authorRepository.save(author);
        return author;
    }

    @RequestMapping(
        value = "/authors/{id}/disapproveauthor",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Author disapproveAuthor(
        @PathVariable(value = "id") Long id,
        @RequestBody DisapproveAuthorCommand disapproveAuthorCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /author/disapproveAuthor  called #####");
        Optional<Author> optionalAuthor = authorRepository.findById(id);

        optionalAuthor.orElseThrow(() -> new Exception("No Entity Found"));
        Author author = optionalAuthor.get();
        author.disapproveAuthor(disapproveAuthorCommand);

        authorRepository.save(author);
        return author;
    }

    @RequestMapping(
        value = "/authors/{id}/editauthor",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Author editAuthor(
        @PathVariable(value = "id") Long id,
        @RequestBody EditAuthorCommand editAuthorCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /author/editAuthor  called #####");
        Optional<Author> optionalAuthor = authorRepository.findById(id);

        optionalAuthor.orElseThrow(() -> new Exception("No Entity Found"));
        Author author = optionalAuthor.get();
        author.editAuthor(editAuthorCommand);

        authorRepository.save(author);
        return author;
    }
    
    @RequestMapping(
        value = "/authors/login",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public LoginResponse login(
        @RequestBody LoginCommand loginCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /authors/login called #####");
        
        try {
            LoginResponse loginResponse = Author.login(loginCommand, authorRepository);
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
        value = "/authors",
        method = RequestMethod.GET,
        produces = "application/json;charset=UTF-8"
    )
    public Iterable<Author> getAllAuthors(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /authors getAllAuthors called #####");
        return authorRepository.findAll();
    }
}
//>>> Clean Arch / Inbound Adaptor
