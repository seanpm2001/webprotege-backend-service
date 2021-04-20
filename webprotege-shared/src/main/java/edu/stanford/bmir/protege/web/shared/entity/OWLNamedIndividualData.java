package edu.stanford.bmir.protege.web.shared.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.value.AutoValue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.ShortForm;
import org.semanticweb.owlapi.model.OWLEntityVisitorEx;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 28/11/2012
 */
@AutoValue

@JsonTypeName("OWLNamedIndividualData")
public abstract class OWLNamedIndividualData extends OWLEntityData {


    public static OWLNamedIndividualData get(@Nonnull OWLNamedIndividual individual,
                                            @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms) {
        return get(individual, shortForms, false);
    }


    public static OWLNamedIndividualData get(@Nonnull OWLNamedIndividual individual,
                                            @Nonnull ImmutableMap<DictionaryLanguage, String> shortForms,
                                            boolean deprecated) {
        return get(individual, toShortFormList(shortForms), deprecated);
    }

    @JsonCreator
    public static OWLNamedIndividualData get(@JsonProperty("entity") OWLNamedIndividual individual,
                                            @JsonProperty("shortForms") ImmutableList<ShortForm> shortForms,
                                            @JsonProperty("deprecated") boolean deprecated) {
        return new AutoValue_OWLNamedIndividualData(shortForms, deprecated, individual);
    }

    @Nonnull
    @Override
    public abstract OWLNamedIndividual getObject();

    @Override
    public OWLNamedIndividual getEntity() {
        return getObject();
    }

    @Override
    public PrimitiveType getType() {
        return PrimitiveType.NAMED_INDIVIDUAL;
    }

    @Override
    public <R, E extends Throwable> R accept(OWLPrimitiveDataVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public <R> R accept(OWLEntityVisitorEx<R> visitor, R defaultValue) {
        return visitor.visit(getEntity());
    }

    @Override
    public <R> R accept(OWLEntityDataVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

}
