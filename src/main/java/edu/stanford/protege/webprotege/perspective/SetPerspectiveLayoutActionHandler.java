package edu.stanford.protege.webprotege.perspective;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.access.BuiltInAction;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 28/02/16
 */
public class SetPerspectiveLayoutActionHandler extends AbstractProjectActionHandler<SetPerspectiveLayoutAction, SetPerspectiveLayoutResult> {

    private final static Logger logger = LoggerFactory.getLogger(SetPerspectiveLayoutActionHandler.class);

    private final PerspectivesManager perspectivesManager;

    @Inject
    public SetPerspectiveLayoutActionHandler(@Nonnull AccessManager accessManager,
                                             PerspectivesManager perspectivesManager) {
        super(accessManager);
        this.perspectivesManager = checkNotNull(perspectivesManager);
    }

    @Nonnull
    @Override
    public Class<SetPerspectiveLayoutAction> getActionClass() {
        return SetPerspectiveLayoutAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction(SetPerspectiveLayoutAction action) {
        return BuiltInAction.VIEW_PROJECT;
    }

    @Nonnull
    @Override
    public SetPerspectiveLayoutResult execute(@Nonnull SetPerspectiveLayoutAction action, @Nonnull ExecutionContext executionContext) {
        var projectId = action.projectId();
        var userId = action.userId();
        var layout = action.layout();
        logger.info("ALEX set perspective projectId {} userId {} layout {}", projectId, userId, layout);
        if(!userId.isGuest()) {
            perspectivesManager.savePerspectiveLayout(projectId, userId, layout);
        }
        return new SetPerspectiveLayoutResult();
    }
}
