package org.who.mcheck.core.service;

import org.motechproject.decisiontree.core.model.AudioPrompt;
import org.motechproject.decisiontree.core.model.Node;
import org.motechproject.decisiontree.core.model.Transition;
import org.motechproject.decisiontree.core.model.Tree;
import org.motechproject.decisiontree.core.repository.AllTrees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CallTreeService {

    private AllTrees allTrees;
    private final String treeName;
    private final String audioFileUrl;

    @Autowired
    public CallTreeService(@Qualifier("treeDao") AllTrees allTrees,
                           @Value("#{mCheck['ivr.tree.name']}") String treeName,
                           @Value("#{mCheck['ivr.audio.file.url']}") String audioFileUrl) {
        this.allTrees = allTrees;
        this.treeName = treeName;
        this.audioFileUrl = audioFileUrl;
    }

    public void createMCheckIVRTrees() {
        Node rootNode = new Node().addPrompts(new AudioPrompt().setAudioFileUrl(audioFileUrl));
        Tree tree = new Tree()
                .setName(treeName)
                .setRootTransition(new Transition().setDestinationNode(rootNode));

        allTrees.addOrReplace(tree);
    }
}
