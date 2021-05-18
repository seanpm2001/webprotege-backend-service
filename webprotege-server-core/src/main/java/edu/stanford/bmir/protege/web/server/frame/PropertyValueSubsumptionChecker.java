package edu.stanford.bmir.protege.web.server.frame;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/02/2014
 */
public interface PropertyValueSubsumptionChecker {

    boolean isSubsumedBy(PlainPropertyValue propertyValueA, PlainPropertyValue propertyValueB);
}
