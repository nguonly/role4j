package net.role4j.runtime.ui;

import net.role4j.runtime.RTCommon;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

/**
 * Created by nguonly on 10/19/16.
 */
public class HashCallablesPanel extends JPanel {
    public HashCallablesPanel(){
        initUI();
    }

    private void initUI(){
        JTree treeHashCallables = new JTree(RTCommon.modelHashCallable);
        JScrollPane spTreeHashCallables = new JScrollPane(treeHashCallables);

        setLayout(new BorderLayout());
        add(spTreeHashCallables, BorderLayout.CENTER);

        setBorder(BorderFactory.createTitledBorder("Hash Callable Tree"));
        setPreferredSize(new Dimension(50*10, 30*10));
    }
}
