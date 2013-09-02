package org.who.mcheck.core.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.motechproject.decisiontree.core.model.AudioPrompt;
import org.motechproject.decisiontree.core.model.Node;
import org.motechproject.decisiontree.core.model.Transition;
import org.motechproject.decisiontree.core.model.Tree;
import org.motechproject.decisiontree.core.repository.AllTrees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class ReminderTreeService {

    private AllTrees allTrees;
    private String treeName;
    private String audioFileUrl;
    private Log log = LogFactory.getLog(ReminderTreeService.class);


    @Autowired
    public ReminderTreeService(@Qualifier("treeDao") AllTrees allTrees,
                               @Value("#{mCheck['ivr.tree.name']}") String treeName,
                               @Value("#{mCheck['ivr.audio.file.url']}") String audioFileUrl) {
        this.allTrees = allTrees;
        this.treeName = treeName;
        this.audioFileUrl = audioFileUrl;
    }

    public void createMCheckIVRTrees() {
        for (int day = 1; day <= 7; day++) {
            Node rootNode = new Node().addPrompts(new AudioPrompt().setAudioFileUrl(MessageFormat.format(audioFileUrl, day)));
            Tree tree = new Tree()
                    .setName(MessageFormat.format(treeName, day))
                    .setRootTransition(new Transition().setDestinationNode(rootNode));
            log.info("Creating mCheck IVR trees. Tree name: " + MessageFormat.format(treeName, day) + ", Audio file url: " + audioFileUrl);
            allTrees.addOrReplace(tree);
        }
    }
}
