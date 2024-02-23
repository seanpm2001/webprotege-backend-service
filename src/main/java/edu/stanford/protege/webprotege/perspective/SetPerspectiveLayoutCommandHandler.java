package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-21
 */
@WebProtegeHandler
public class SetPerspectiveLayoutCommandHandler implements CommandHandler<SetPerspectiveLayoutAction, SetPerspectiveLayoutResult> {

    private final Logger logger = LoggerFactory.getLogger(SetPerspectiveLayoutCommandHandler.class);

    private final ActionExecutor executor;

    public SetPerspectiveLayoutCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return SetPerspectiveLayoutAction.CHANNEL;
    }

    @Override
    public Class<SetPerspectiveLayoutAction> getRequestClass() {
        return SetPerspectiveLayoutAction.class;
    }

    @Override
    public Mono<SetPerspectiveLayoutResult> handleRequest(SetPerspectiveLayoutAction request,
                                                          ExecutionContext executionContext) {
        logger.info("ALEX execut din command handler " + executor.getClass());
        return executor.executeRequest(request, executionContext);
    }
}