package edu.stanford.bmir.protege.web.server.events;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.entity.EntityNodeRenderer;
import edu.stanford.bmir.protege.web.server.hierarchy.*;
import edu.stanford.bmir.protege.web.server.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

import static edu.stanford.bmir.protege.web.server.hierarchy.HierarchyId.OBJECT_PROPERTY_HIERARCHY;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLObjectPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLObjectProperty> {

    @Nonnull
    private final ObjectPropertyHierarchyProvider hierarchyProvider;

    @Nonnull
    private final EntityNodeRenderer renderer;

    @Inject
    public OWLObjectPropertyHierarchyChangeComputer(@Nonnull ProjectId projectId,
                                                    @Nonnull ObjectPropertyHierarchyProvider hierarchyProvider,
                                                    @Nonnull EntityNodeRenderer renderer) {
        super(projectId, EntityType.OBJECT_PROPERTY, hierarchyProvider, OBJECT_PROPERTY_HIERARCHY, renderer);
        this.hierarchyProvider = hierarchyProvider;
        this.renderer = renderer;
    }

    @Override
    protected Collection<HighLevelProjectEventProxy> createRemovedEvents(OWLObjectProperty child, OWLObjectProperty parent) {
        RemoveEdge removeEdge = new RemoveEdge(new GraphEdge(
                new GraphNode(renderer.render(parent)),
                new GraphNode(renderer.render(child))
        ));
        return Collections.singletonList(SimpleHighLevelProjectEventProxy.wrap(new EntityHierarchyChangedEvent(getProjectId(),
                                                                                                               OBJECT_PROPERTY_HIERARCHY,
                                                                                                               GraphModelChangedEvent
                                                                                                                       .create(ImmutableList
                                                                                                                                       .of(
                                                                                                                                               removeEdge)))));
    }

    @Override
    protected Collection<HighLevelProjectEventProxy> createAddedEvents(OWLObjectProperty child, OWLObjectProperty parent) {
        AddEdge addEdge = new AddEdge(new GraphEdge(
                new GraphNode(renderer.render(parent), hierarchyProvider.isLeaf(parent)),
                new GraphNode(renderer.render(child), hierarchyProvider.isLeaf(child))
        ));
        return Collections.singletonList(SimpleHighLevelProjectEventProxy.wrap(new EntityHierarchyChangedEvent(getProjectId(),
                                                                         OBJECT_PROPERTY_HIERARCHY,
                                                                         GraphModelChangedEvent.create(ImmutableList.of(
                                                                                 addEdge)))));
    }
}
