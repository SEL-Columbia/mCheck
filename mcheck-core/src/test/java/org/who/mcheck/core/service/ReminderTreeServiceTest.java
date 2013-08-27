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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class ReminderTreeServiceTest {
    @Mock
    private AllTrees allTrees;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldCreateCallTree() throws Exception {
        ReminderTreeService service = new ReminderTreeService(allTrees, "mCheckTree-Day {0}", "http://server.com/PostPartum/Day {0}.mp3");

        service.createMCheckIVRTrees();

        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day 1", "http://server.com/PostPartum/Day 1.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day 2", "http://server.com/PostPartum/Day 2.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day 3", "http://server.com/PostPartum/Day 3.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day 4", "http://server.com/PostPartum/Day 4.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day 5", "http://server.com/PostPartum/Day 5.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day 6", "http://server.com/PostPartum/Day 6.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day 7", "http://server.com/PostPartum/Day 7.mp3"));
        verifyNoMoreInteractions(allTrees);
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
