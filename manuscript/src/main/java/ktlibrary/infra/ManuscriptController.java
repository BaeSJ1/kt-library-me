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
// @RequestMapping(value="/manuscripts")
@Transactional
public class ManuscriptController {

    @Autowired
    ManuscriptRepository manuscriptRepository;

    @RequestMapping(
        value = "/manuscripts/registermanuscript",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Manuscript registerManuscript(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestBody RegisterManuscriptCommand registerManuscriptCommand
    ) throws Exception {
        System.out.println(
            "##### /manuscript/registerManuscript  called #####"
        );
        Manuscript manuscript = new Manuscript();
        manuscript.registerManuscript(registerManuscriptCommand);
        manuscriptRepository.save(manuscript);
        return manuscript;
    }

    @RequestMapping(
        value = "/manuscripts/{id}/editmanuscript",
        method = RequestMethod.PUT,
        produces = "application/json;charset=UTF-8"
    )
    public Manuscript editManuscript(
        @PathVariable(value = "id") Long id,
        @RequestBody EditManuscriptCommand editManuscriptCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /manuscript/editManuscript  called #####");
        Optional<Manuscript> optionalManuscript = manuscriptRepository.findById(
            id
        );

        optionalManuscript.orElseThrow(() -> new Exception("No Entity Found"));
        Manuscript manuscript = optionalManuscript.get();
        manuscript.editManuscript(editManuscriptCommand);

        manuscriptRepository.save(manuscript);
        return manuscript;
    }

    @RequestMapping(
        value = "/manuscripts/{id}/requestpublishing",
        method = RequestMethod.POST,
        produces = "application/json;charset=UTF-8"
    )
    public Manuscript requestPublishing(
        @PathVariable("id") Long id,
        @RequestBody RequestPublishingCommand requestPublishingCommand,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        System.out.println("##### /manuscript/requestPublishing called #####");
        
        Optional<Manuscript> optionalManuscript = manuscriptRepository.findById(id);
        optionalManuscript.orElseThrow(() -> new Exception("No Manuscript Found"));

        Manuscript manuscript = optionalManuscript.get();
        manuscript.requestPublishing(requestPublishingCommand);
        manuscriptRepository.save(manuscript);

        return manuscript;
    }
}
//>>> Clean Arch / Inbound Adaptor
