package net.role4j.runtime.ui;

import net.role4j.runtime.RTCommon;
import net.role4j.runtime.XMLConstructor;
import net.role4j.view.NodeInfoUI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;

/**
 * Created by nguonly on 10/19/16.
 */
public class RelationTreePanel extends JPanel{
    JTree treeRelation = new JTree(RTCommon.modelRelationTree);
    private DefaultMutableTreeNode selectedNode;

    public RelationTreePanel(){
        initUI();
    }

    private void initUI(){

        JScrollPane spTreeRelation = new JScrollPane(treeRelation);

        treeRelation.addMouseListener(mouseListener);
        treeRelation.setComponentPopupMenu(getPopupMenu());
        treeRelation.setCellRenderer(new RelationTreeCellRenderer());
        treeRelation.setRootVisible(false);
        
        setLayout(new BorderLayout());
        add(spTreeRelation, BorderLayout.CENTER);

        setBorder(BorderFactory.createTitledBorder("Relation Tree"));
        setPreferredSize(new Dimension(50*10, 30*10));
    }

    MouseListener mouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
                TreePath pathForLocation = treeRelation.getPathForLocation(e.getX(), e.getY());
                if(pathForLocation != null){
                    selectedNode = (DefaultMutableTreeNode)pathForLocation.getLastPathComponent();
                }else{
                    selectedNode = null;
                }
            super.mouseClicked(e);
        }
    };

    private JPopupMenu getPopupMenu(){
        JPopupMenu menu = new JPopupMenu();
        JMenuItem mnuBind = new JMenuItem("Bind");
        JMenuItem mnuRebind = new JMenuItem("Rebind");
        JMenuItem mnuUnbind = new JMenuItem("Unbind");

        mnuBind.addActionListener(getBindActionListener());
        mnuRebind.addActionListener(getRebindActionListener());
        mnuUnbind.addActionListener(getUnbindActionListener());

        menu.add(mnuBind);
        menu.add(mnuRebind);
        menu.add(mnuUnbind);

        return menu;
    }

    private ActionListener getBindActionListener(){
        return e -> {
            if(selectedNode!=null){
                NodeInfoUI node = (NodeInfoUI)selectedNode.getUserObject();
                String xml = "";
                if(selectedNode.getLevel()==2) { //Core object
                    xml = XMLConstructor.getXMLCoreBindOperation(node);

                }else if(selectedNode.getLevel()>2){ //Role
                    xml = XMLConstructor.getXMLRoleBindingOperation(node);
                }
                SwingUtilities.invokeLater(new UnanticipatedAdaptationUI(xml));
            }
        };
    }

    private ActionListener getRebindActionListener(){
        return e-> {
            if(selectedNode != null){
                NodeInfoUI node = (NodeInfoUI)selectedNode.getUserObject();
                String xml = "";
                if(selectedNode.getLevel()==2) { //Core object
                    xml = XMLConstructor.getXMLCoreRebindOperation(node);

                }else if(selectedNode.getLevel()>2){ //Role
                    xml = XMLConstructor.getXMLRoleRebindOperation(node);
                }
                SwingUtilities.invokeLater(new UnanticipatedAdaptationUI(xml));
            }
        };
    }

    private ActionListener getUnbindActionListener(){
        return e -> {
            if(selectedNode != null){
                NodeInfoUI node = (NodeInfoUI)selectedNode.getUserObject();
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode)selectedNode.getParent();
                NodeInfoUI parentNode = (NodeInfoUI)parent.getUserObject();
                System.out.println(parent);
                String xml = "";
                if(parent.getLevel()==2) { //Core object
                    xml = XMLConstructor.getXMLCoreUnbindOperation(node, parentNode);

                }else if(parent.getLevel()>2){ //Role
                    xml = XMLConstructor.getXMLRoleUnbindOperation(node, parentNode);
                }
                SwingUtilities.invokeLater(new UnanticipatedAdaptationUI(xml));
            }
        };
    }

    class RelationTreeCellRenderer extends DefaultTreeCellRenderer{
        private JLabel label;

        public RelationTreeCellRenderer(){
            label = new JLabel();
        }


        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            Component ret = super.getTreeCellRendererComponent(tree, value,
                    selected, expanded, leaf, row, hasFocus);

            ClassLoader cl = RelationTreePanel.class.getClassLoader();
            Icon compartmentIcon = new ImageIcon(cl.getResource("images/c16.png"));
            Icon objectIcon = new ImageIcon(cl.getResource("images/o16.png"));
            Icon roleIcon = new ImageIcon(cl.getResource("images/r16.png"));

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            JLabel label = (JLabel) ret ;

            if(node.getLevel()==1)
                label.setIcon(compartmentIcon);
            else if(node.getLevel()==2)
                label.setIcon(objectIcon);
            else if(node.getLevel()>2)
                label.setIcon(roleIcon);
            else
                label.setIcon(null);

            return label;
        }
    }
}
