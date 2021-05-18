package edu.stanford.bmir.protege.web.server.perspective;

import edu.stanford.bmir.protege.web.server.lang.LanguageMap;
import edu.stanford.bmir.protege.web.server.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-09-01
 */
public class PerspectiveDescriptor_Serialization_TestCase {

    @Test
    public void shouldSerialize() throws IOException {
        var descriptor = PerspectiveDescriptor.get(
                PerspectiveId.generate(), LanguageMap.of("en", "Hello"), true
        );
        JsonSerializationTestUtil.testSerialization(descriptor, PerspectiveDescriptor.class);
    }
}
