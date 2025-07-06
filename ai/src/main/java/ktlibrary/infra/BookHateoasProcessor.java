package ktlibrary.infra;

import ktlibrary.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class BookHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Book>> {

    @Override
    public EntityModel<Book> process(EntityModel<Book> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/requestcover")
                .withRel("requestcover")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/transformebook")
                .withRel("transformebook")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/setcategory")
                .withRel("setcategory")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/requestregistration"
                )
                .withRel("requestregistration")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/calculateprice")
                .withRel("calculateprice")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() + "/generatesummary"
                )
                .withRel("generatesummary")
        );

        return model;
    }
}
