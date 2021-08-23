package edu.stanford.protege.webprotege.issues;

import edu.stanford.protege.webprotege.api.ActionExecutor;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-08-20
 */
@WebProtegeHandler
public class EditCommentCommandHandler implements CommandHandler<EditCommentAction, EditCommentResult> {

    private final ActionExecutor executor;

    public EditCommentCommandHandler(ActionExecutor executor) {
        this.executor = executor;
    }

    @NotNull
    @Override
    public String getChannelName() {
        return EditCommentAction.CHANNEL;
    }

    @Override
    public Class<EditCommentAction> getRequestClass() {
        return EditCommentAction.class;
    }

    @Override
    public Mono<EditCommentResult> handleRequest(EditCommentAction request, ExecutionContext executionContext) {
        return executor.executeRequest(request, executionContext);
    }
}