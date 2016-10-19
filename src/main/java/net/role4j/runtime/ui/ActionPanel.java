package net.role4j.runtime.ui;

import net.role4j.runtime.RTCommon;

import javax.swing.*;

/**
 * Created by nguonly on 10/19/16.
 */
public class ActionPanel extends JPanel{
    public ActionPanel(){
        initUI();
    }

    private void initUI(){
        JButton btnRefresh = new JButton("Refresh");

        btnRefresh.addActionListener(e -> RTCommon.performRefresh());

        add(btnRefresh);

    }
}
