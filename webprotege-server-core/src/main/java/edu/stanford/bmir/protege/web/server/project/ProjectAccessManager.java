package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.user.UserId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 3 Mar 2017
 */
public interface ProjectAccessManager {
    void logProjectAccess(ProjectId projectId, UserId userId, long timestamp);
}
