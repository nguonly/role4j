package net.role4j.runtime.ui;

import net.role4j.runtime.RTCommon;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/19/16.
 */
public class TransHashCallablePanel extends JPanel {
    public TransHashCallablePanel(){
        initUI();
    }

    private void initUI(){
        JTree treeHashCallables = new JTree(RTCommon.modelTransHashCallable);
        JScrollPane spTreeHashCallables = new JScrollPane(treeHashCallables);

        setLayout(new BorderLayout());
        add(spTreeHashCallables, BorderLayout.CENTER);

        setBorder(BorderFactory.createTitledBorder("Trans Hash Callable Tree"));
        setPreferredSize(new Dimension(30*10, 30*10));
    }
}
