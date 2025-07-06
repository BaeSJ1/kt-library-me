package ktlibrary.infra;

import ktlibrary.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Customer>> {

    @Override
    public EntityModel<Customer> process(EntityModel<Customer> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/registeruser")
                .withRel("registeruser")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/requestbook")
                .withRel("requestbook")
        );

        return model;
    }
}
