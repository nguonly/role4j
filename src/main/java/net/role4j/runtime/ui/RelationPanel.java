package net.role4j.runtime.ui;

import net.role4j.runtime.RTCommon;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/19/16.
 */
public class RelationPanel extends JPanel {
    public RelationPanel(){
        JTable tabRelationUI = new JTable();
        JScrollPane spTabRelationUI = new JScrollPane(tabRelationUI, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabRelationUI.setFillsViewportHeight(true);

        tabRelationUI.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabRelationUI.setModel(RTCommon.modelRelation);

        setLayout(new BorderLayout());
        add(spTabRelationUI, BorderLayout.CENTER);

        setBorder(BorderFactory.createTitledBorder("Relation Table"));
        setPreferredSize(new Dimension(50*10, 30*10));
    }
}
