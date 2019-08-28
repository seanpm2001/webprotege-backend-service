package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.index.AxiomsIndex;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-02
 *
 * This isn't used for anything other than to force the eager instantiation of certain objects
 * in the project component object graph.
 */
@ProjectSingleton
public class EagerProjectSingletons {

    private final ProjectId projectId;

    private final RevisionManager revisionManager;

    private final AxiomsIndex axiomsIndex;

    private final ProjectActionHandlerRegistry projectActionHandlerRegistry;

    @Inject
    public EagerProjectSingletons(ProjectId projectId,
                                  RevisionManager revisionManager,
                                  AxiomsIndex axiomsIndex,
                                  ProjectActionHandlerRegistry projectActionHandlerRegistry) {
        this.projectId = projectId;
        this.revisionManager = revisionManager;
        this.axiomsIndex = axiomsIndex;
        this.projectActionHandlerRegistry = projectActionHandlerRegistry;
    }
}
