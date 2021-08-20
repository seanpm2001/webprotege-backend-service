package edu.stanford.protege.webprotege.dispatch.handlers;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import edu.stanford.protege.webprotege.project.RemoveProjectFromTrashAction;
import edu.stanford.protege.webprotege.project.RemoveProjectFromTrashResult;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class RemoveProjectFromTrashHandler implements CommandHandler<RemoveProjectFromTrashAction, RemoveProjectFromTrashResult> {

    private final ActionExecutor executor;

    public RemoveProjectFromTrashHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return RemoveProjectFromTrashAction.CHANNEL;
    }

    @Override
    public Class<RemoveProjectFromTrashAction> getRequestClass() {
        return RemoveProjectFromTrashAction.class;
    }

    @Override
    public Mono<RemoveProjectFromTrashResult> handleRequest(RemoveProjectFromTrashAction request,
                                                            ExecutionContext executionContext) {
        return Mono.just(executor.execute(request, executionContext));
    }
}