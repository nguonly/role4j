package net.role4j.runtime.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/19/16.
 */
public class MainRTMonitorUI extends JFrame implements Runnable{
    public MainRTMonitorUI(){
        initUI();
    }

    private void initUI(){
        RelationPanel relationPanel = new RelationPanel();
        ActionPanel actionPanel = new ActionPanel();
        HashCallablesPanel hashCallablesPanel = new HashCallablesPanel();
        TransHashCallablePanel transHashCallablePanel = new TransHashCallablePanel();
        RelationTreePanel relationTreePanel = new RelationTreePanel();
        TransactionsPanel transactionsPanel = new TransactionsPanel();

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(hashCallablesPanel, BorderLayout.WEST);
        southPanel.add(transHashCallablePanel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(relationTreePanel, BorderLayout.WEST);
        centerPanel.add(transactionsPanel, BorderLayout.EAST);

        add(actionPanel, BorderLayout.NORTH);
//        add(relationPanel, BorderLayout.CENTER);
//        add(relationTreePanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

//        add(hashCallablesPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        setTitle("Runtime Monitor and Management Tool");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
    }

    @Override
    public void run(){
        MainRTMonitorUI monitor = new MainRTMonitorUI();
        monitor.setVisible(true);
    }
}
