package edu.stanford.protege.webprotege.dispatch.impl;

import edu.stanford.protege.webprotege.common.*;
import edu.stanford.protege.webprotege.dispatch.*;
import edu.stanford.protege.webprotege.ipc.CommandExecutionException;
import edu.stanford.protege.webprotege.permissions.PermissionDeniedException;
import edu.stanford.protege.webprotege.project.HasProjectId;
import edu.stanford.protege.webprotege.project.ProjectManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 */
public class DispatchServiceExecutorImpl implements DispatchServiceExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DispatchServiceExecutorImpl.class.getName());

    @Nonnull
    private final ApplicationActionHandlerRegistry handlerRegistry;

    @Nonnull
    private final ProjectManager projectManager;

    @Inject
    public DispatchServiceExecutorImpl(@Nonnull ApplicationActionHandlerRegistry handlerRegistry,
                                       @Nonnull ProjectManager projectManager) {
        this.handlerRegistry = checkNotNull(handlerRegistry);
        this.projectManager = checkNotNull(projectManager);
    }

    /**
     * Sets the name of a thread so that it contains details of the action (and target project) being executed
     *
     * @param thread    The thread.
     * @param action    The action.
     * @param projectId The optional project id.
     */
    private static void setTemporaryThreadName(@Nonnull Thread thread,
                                               @Nonnull Request<?> action,
                                               @Nullable ProjectId projectId) {
        String tempThreadName;
        final ProjectId targetProjectId;
        if (projectId != null) {
            targetProjectId = projectId;
        }
        else if (action instanceof HasProjectId) {
            targetProjectId = ((HasProjectId) action).projectId();
        }
        else {
            targetProjectId = null;
        }
        if (targetProjectId == null) {
            tempThreadName = String.format("[DispatchService] %s",
                                           action.getClass().getSimpleName());
        }
        else {
            tempThreadName = String.format("[DispatchService] %s %s",
                                           action.getClass().getSimpleName(),
                                           targetProjectId);
        }
        thread.setName(tempThreadName);
    }

    @Override
    public <A extends Request<R>, R extends Response> DispatchServiceResultContainer execute(A action, RequestContext requestContext, ExecutionContext executionContext) throws ActionExecutionException, PermissionDeniedException {
        return execAction(action, requestContext, executionContext);
    }

    @Nullable
    private ProjectId extractProjectId(Request<?> request) {
        if(request instanceof ProjectAction) {
            var projectId = ((ProjectAction) request).projectId();
            if(projectId == null) {
                throw new CommandExecutionException(HttpStatus.BAD_REQUEST);
            }
            return projectId;
        }
        else if(request instanceof ProjectRequest) {
            var projectId = ((ProjectRequest<?>) request).projectId();
            if(projectId == null) {
                throw new CommandExecutionException(HttpStatus.BAD_REQUEST);
            }
            return projectId;
        }
        else {
            return null;
        }

    }

    private <A extends Request<R>, R extends Response> DispatchServiceResultContainer execAction(A action, RequestContext requestContext, ExecutionContext executionContext) {
        try {
            final ActionHandler<A, R> actionHandler;
            final Thread thread = Thread.currentThread();
            String threadName = thread.getName();
            logger.info("ALEX din execAction {} si threaname {}", action.getClass(), threadName);
            var projectId = extractProjectId(action);
            if (projectId != null) {
                setTemporaryThreadName(thread, action, projectId);
                ProjectActionHandlerRegistry actionHanderRegistry = projectManager.getActionHandlerRegistry(projectId);
                actionHandler = actionHanderRegistry.getActionHandler(action);
            } else {
                setTemporaryThreadName(thread, action, null);
                actionHandler = handlerRegistry.getActionHandler(action);
            }
            logger.info("ALEX din execAction cu projectId {}", projectId);

            RequestValidator validator = actionHandler.getRequestValidator(action, requestContext);
            RequestValidationResult validationResult = validator.validateAction();
            logger.info("ALEX din execImpl validationResult "+ validationResult.getValidationResult());
            if (!validationResult.isValid()) {
                logger.info("ALEX NU E VALIDDDDDD");
                throw getPermissionDeniedException(requestContext.getUserId(),
                        validationResult);
            }
            try {
                logger.info("ALEX din execImpl incerc sa fac execute cu actionHandler " + actionHandler.getActionClass() + " " + actionHandler.getClass());
                System.out.println("ALEX din execImpl incerc sa fac execute cu actionHandler " + actionHandler.getActionClass() + " " + actionHandler.getClass());

                R result = actionHandler.execute(action, executionContext);
                logger.info("ALEX din execImpl am reusit  sa fac execute " + result);
                System.out.println("ALEX din execImpl am reusit  sa fac execute " + result);

                return DispatchServiceResultContainer.create(result);
            } catch (PermissionDeniedException e) {
                logger.info("ALEX NU E VALIDDDDDD din catch exception");

                throw e;
            } catch (Exception e) {
                logger.error("An error occurred whilst executing an action ({})", action, e);
                throw new ActionExecutionException(e);
            } finally {
                thread.setName(threadName);
            }
        }catch (Exception e) {
            logger.error("eroare " ,e);
            throw new RuntimeException(e);
        }


    }

    private PermissionDeniedException getPermissionDeniedException(@Nonnull UserId userId,
                                                                   @Nonnull RequestValidationResult validationResult) {
        if (validationResult.getInvalidException().isPresent()) {
            Exception validationException = validationResult.getInvalidException().get();
            if (validationException instanceof PermissionDeniedException) {
                return ((PermissionDeniedException) validationException);
            }
        }
        throw new PermissionDeniedException(validationResult.getInvalidMessage());
    }
}
