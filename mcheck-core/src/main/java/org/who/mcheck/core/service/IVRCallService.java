package org.who.mcheck.core.service;

import org.motechproject.decisiontree.core.model.AudioPrompt;
import org.motechproject.decisiontree.core.model.Node;
import org.motechproject.decisiontree.core.model.Tree;
import org.motechproject.decisiontree.core.repository.AllTrees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class IVRCallService {

    private AllTrees allTrees;

    @Autowired
    public IVRCallService(@Qualifier("treeDao") AllTrees allTrees) {
        this.allTrees = allTrees;
        createTree();
    }

    public void createTree() {
        Tree tree = new Tree()
                .setName("mCheckTree")
                .setRootNode(new Node().addPrompts(new AudioPrompt().setAudioFileUrl("http://li310-155.members.linode.com/sample.wav")));

        allTrees.add(tree);
    }
}
