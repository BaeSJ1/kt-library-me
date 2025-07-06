package ktlibrary.infra;

import ktlibrary.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class BookShelfHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<BookShelf>> {

    @Override
    public EntityModel<BookShelf> process(EntityModel<BookShelf> model) {
        return model;
    }
}
