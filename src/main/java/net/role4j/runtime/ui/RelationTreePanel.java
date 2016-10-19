package net.role4j.runtime.ui;

import net.role4j.runtime.RTCommon;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/19/16.
 */
public class RelationTreePanel extends JPanel {
    public RelationTreePanel(){
        initUI();
    }

    private void initUI(){
        JTree treeRelation = new JTree(RTCommon.modelRelationTree);
        JScrollPane spTreeRelation = new JScrollPane(treeRelation);

        setLayout(new BorderLayout());
        add(spTreeRelation, BorderLayout.CENTER);

        setBorder(BorderFactory.createTitledBorder("Relation Tree"));
        setPreferredSize(new Dimension(50*10, 30*10));
    }
}
