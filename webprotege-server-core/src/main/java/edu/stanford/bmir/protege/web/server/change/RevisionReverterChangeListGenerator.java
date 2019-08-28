package edu.stanford.bmir.protege.web.server.change;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.revision.Revision;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19/03/15
 */
@AutoFactory
public class RevisionReverterChangeListGenerator implements ChangeListGenerator<Boolean> {

    @Nonnull
    private final RevisionNumber revisionNumber;

    @Nonnull
    private final OWLOntologyChangeDataReverter changeDataReverter;

    @Nonnull
    private final RevisionManager revisionManager;

    @Nonnull
    private final OntologyChangeRecordTranslator changeFactory;

    @Inject
    public RevisionReverterChangeListGenerator(@Nonnull RevisionNumber revisionNumber,
                                               @Provided @Nonnull OWLOntologyChangeDataReverter changeDataReverter,
                                               @Provided @Nonnull RevisionManager revisionManager,
                                               @Provided @Nonnull OntologyChangeRecordTranslator changeFactory) {
        this.revisionNumber = checkNotNull(revisionNumber);
        this.changeDataReverter = changeDataReverter;
        this.revisionManager = revisionManager;
        this.changeFactory = changeFactory;
    }

    @Override
    public Boolean getRenamedResult(Boolean result, RenameMap renameMap) {
        return result;
    }

    @Override
    public OntologyChangeList<Boolean> generateChanges(ChangeGenerationContext context) {
        Optional<Revision> revision = revisionManager.getRevision(revisionNumber);
        if(revision.isEmpty()) {
            return OntologyChangeList.<Boolean>builder().build(false);
        }
        var changes = new ArrayList<OntologyChange>();
        for(OWLOntologyChangeRecord record : revision.get()) {
            var revertingChangeData = changeDataReverter.getRevertingChange(record);
            var revertingRecord = new OWLOntologyChangeRecord(record.getOntologyID(), revertingChangeData);
            var ontologyChange = changeFactory.getOntologyChange(revertingRecord);
            changes.add(0, ontologyChange);
        }
        return OntologyChangeList.<Boolean>builder().addAll(changes).build(true);
    }

    @Nonnull
    @Override
    public String getMessage(ChangeApplicationResult<Boolean> result) {
        return "Reverted revision " + revisionNumber.getValue();
    }
}
