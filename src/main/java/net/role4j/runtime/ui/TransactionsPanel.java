package net.role4j.runtime.ui;

import net.role4j.runtime.RTCommon;

import javax.swing.*;
import java.awt.*;

/**
 * Transaction List Panel contains the list of actively available transactions with entering timestamp.
 * Created by nguonly on 10/19/16.
 */
public class TransactionsPanel extends JPanel {
    public TransactionsPanel(){
        initUI();
    }


    private void initUI(){
        JTable tableTrans = new JTable(RTCommon.modelTransactions);
        JScrollPane spTableTrans = new JScrollPane(tableTrans, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        setLayout(new BorderLayout());
        add(spTableTrans, BorderLayout.CENTER);

        setBorder(BorderFactory.createTitledBorder("Active Transactions"));
        setPreferredSize(new Dimension(30*10, 30*10));
    }
}
