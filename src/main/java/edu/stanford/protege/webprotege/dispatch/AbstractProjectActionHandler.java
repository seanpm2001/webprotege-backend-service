package edu.stanford.protege.webprotege.dispatch;


import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.authorization.ActionId;
import edu.stanford.protege.webprotege.common.ProjectId;
import edu.stanford.protege.webprotege.common.ProjectRequest;
import edu.stanford.protege.webprotege.common.Request;
import edu.stanford.protege.webprotege.common.Response;
import edu.stanford.protege.webprotege.dispatch.validators.CompositeRequestValidator;
import edu.stanford.protege.webprotege.dispatch.validators.NullValidator;
import edu.stanford.protege.webprotege.dispatch.validators.ProjectPermissionValidator;
import edu.stanford.protege.webprotege.project.HasProjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 * <p>
 * A skeleton handler for handling actions that pertain to projects (i.e. {@link Action}s that implement
 * {@link HasProjectId}. Further more, the validation includes a check to see if the project
 * actually exists and fails if this isn't the case.
 * </p>
 */
public abstract class AbstractProjectActionHandler<A extends Request<R>, R extends Response> implements ProjectActionHandler<A, R> {

    private final Logger logger = LoggerFactory.getLogger(AbstractProjectActionHandler.class);
    @Nonnull
    private final AccessManager accessManager;

    public AbstractProjectActionHandler(@Nonnull AccessManager accessManager) {
        this.accessManager = checkNotNull(accessManager);
    }

    @Nonnull
    @Override
    public final RequestValidator getRequestValidator(@Nonnull A action, @Nonnull RequestContext requestContext) {
        List<RequestValidator> validators = new ArrayList<>();

        BuiltInAction builtInAction = getRequiredExecutableBuiltInAction(action);
        ProjectId projectId;
        if(action instanceof ProjectAction) {
            projectId = ((ProjectAction<?>) action).projectId();
        }
        else if(action instanceof ProjectRequest) {
            projectId = ((ProjectRequest<?>) action).projectId();
        }
        else {
            throw new RuntimeException("Not a project action or request");
        }
        logger.info("ALEX din abstract action handler built in action {}", builtInAction);
        if(builtInAction != null) {
            ProjectPermissionValidator validator = new ProjectPermissionValidator(accessManager,
                                                                                  projectId,
                                                                                  requestContext.getUserId(),
                                                                                  builtInAction.getActionId());
            validators.add(validator);
        }



        ActionId reqActionId = getRequiredExecutableAction();
        if (reqActionId != null) {
            ProjectPermissionValidator validator = new ProjectPermissionValidator(accessManager,
                                                                                  projectId,
                                                                                  requestContext.getUserId(),
                                                                                  reqActionId);
            validators.add(validator);
        }

        Iterable<BuiltInAction> requiredExecutableBuiltInActions = getRequiredExecutableBuiltInActions(action);
        for(BuiltInAction actionId : requiredExecutableBuiltInActions) {
            ProjectPermissionValidator validator = new ProjectPermissionValidator(accessManager,
                                                                                  projectId,
                                                                                  requestContext.getUserId(),
                                                                                  actionId.getActionId());
            validators.add(validator);
        }

        final RequestValidator additionalRequestValidator = getAdditionalRequestValidator(action, requestContext);
        if (additionalRequestValidator != NullValidator.get()) {
            validators.add(additionalRequestValidator);
        }
        var response = CompositeRequestValidator.get(validators);
        logger.info("ALEX din abstract validator response {} cu size {}", response, validators.size() );
        return response;
    }

    @Nullable
    protected BuiltInAction getRequiredExecutableBuiltInAction(A action) {
        return null;
    }



    @Nullable
    protected ActionId getRequiredExecutableAction() {
        return null;
    }

    @Nonnull
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions(A action) {
        return Collections.emptyList();
    }

    /**
     * Gets an additional validator that is specific to the implementing handler.  This is returned as part of a
     * {@link CompositeRequestValidator} by the the implementation of
     * the {@link #getRequestValidator(Action,
     * edu.stanford.protege.webprotege.dispatch.RequestContext)} method.
     *
     * @param action         The action that the validation will be completed against.
     * @param requestContext The {@link RequestContext} that describes the context for the request.
     * @return A {@link RequestValidator} for this handler.  Not {@code null}.
     */
    @Nonnull
    protected RequestValidator getAdditionalRequestValidator(A action, RequestContext requestContext) {
        return NullValidator.get();
    }
}
