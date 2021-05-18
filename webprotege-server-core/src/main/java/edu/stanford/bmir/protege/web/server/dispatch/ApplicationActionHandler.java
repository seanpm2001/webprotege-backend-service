package edu.stanford.bmir.protege.web.server.dispatch;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2017
 */
public interface ApplicationActionHandler<A extends Action<R>, R extends Result> extends ActionHandler<A, R> {

}
