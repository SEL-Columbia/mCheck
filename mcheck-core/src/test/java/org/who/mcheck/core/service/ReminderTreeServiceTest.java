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
        ReminderTreeService service = new ReminderTreeService(allTrees, "mCheckTree-Day{0}", "http://server.com/PostPartum/Day{0}.mp3");

        service.createMCheckIVRTrees();

        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day1", "http://server.com/PostPartum/Day1.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day2", "http://server.com/PostPartum/Day2.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day3", "http://server.com/PostPartum/Day3.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day4", "http://server.com/PostPartum/Day4.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day5", "http://server.com/PostPartum/Day5.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day6", "http://server.com/PostPartum/Day6.mp3"));
        verify(allTrees).addOrReplace(assertTree("mCheckTree-Day7", "http://server.com/PostPartum/Day7.mp3"));
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
