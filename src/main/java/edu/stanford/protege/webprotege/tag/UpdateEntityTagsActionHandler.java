package edu.stanford.protege.webprotege.tag;

import edu.stanford.protege.webprotege.access.AccessManager;
import edu.stanford.protege.webprotege.dispatch.AbstractProjectActionHandler;
import edu.stanford.protege.webprotege.dispatch.ExecutionContext;
import edu.stanford.protege.webprotege.common.ProjectEvent;
import edu.stanford.protege.webprotege.events.EventManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21 Mar 2018
 */
public class UpdateEntityTagsActionHandler extends AbstractProjectActionHandler<UpdateEntityTagsAction, UpdateEntityTagsResult> {

    @Nonnull
    private final EventManager<ProjectEvent> eventEventManager;

    @Nonnull
    private final TagsManager tagsManager;

    @Inject
    public UpdateEntityTagsActionHandler(@Nonnull AccessManager accessManager,
                                         @Nonnull EventManager<ProjectEvent> eventEventManager, @Nonnull TagsManager tagsManager) {
        super(accessManager);
        this.eventEventManager = checkNotNull(eventEventManager);
        this.tagsManager = checkNotNull(tagsManager);
    }

    @Nonnull
    @Override
    public Class<UpdateEntityTagsAction> getActionClass() {
        return UpdateEntityTagsAction.class;
    }

    @Nonnull
    @Override
    public UpdateEntityTagsResult execute(@Nonnull UpdateEntityTagsAction action,
                                          @Nonnull ExecutionContext executionContext) {
        tagsManager.updateTags(action.entity(),
                               action.fromTagIds(),
                               action.toTagIds());
        return new UpdateEntityTagsResult();
    }
}
