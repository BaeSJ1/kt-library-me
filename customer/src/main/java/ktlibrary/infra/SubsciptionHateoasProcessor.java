package ktlibrary.infra;

import ktlibrary.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class SubsciptionHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Subsciption>> {

    @Override
    public EntityModel<Subsciption> process(EntityModel<Subsciption> model) {
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/cancelsubscription"
                )
                .withRel("cancelsubscription")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/subscribe")
                .withRel("subscribe")
        );

        return model;
    }
}
