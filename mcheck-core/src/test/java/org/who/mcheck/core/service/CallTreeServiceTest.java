package org.who.mcheck.core.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.motechproject.decisiontree.core.model.AudioPrompt;
import org.motechproject.decisiontree.core.model.Prompt;
import org.motechproject.decisiontree.core.model.Tree;
import org.motechproject.decisiontree.core.repository.AllTrees;

import java.util.List;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CallTreeServiceTest {
    @Mock
    private AllTrees allTrees;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldCreateCallTree() throws Exception {
        CallTreeService service = new CallTreeService(allTrees, "tree name", "audio file url");

        service.createMCheckIVRTrees();

        verify(allTrees).addOrReplace(assertTree("tree name", "audio file url"));
    }

    private Tree assertTree(final String name, final String audioFileURL) {
        return argThat(new ArgumentMatcher<Tree>() {
            @Override
            public boolean matches(Object o) {
                Tree tree = (Tree) o;
                List<Prompt> prompts = tree.getRootTransition().getDestinationNode(null, null).getPrompts();
                return tree.getName().equals(name)
                        && prompts.size() == 1
                        && audioFileURL.equals(((AudioPrompt) prompts.get(0)).getAudioFileUrl());
            }
        });
    }

}
