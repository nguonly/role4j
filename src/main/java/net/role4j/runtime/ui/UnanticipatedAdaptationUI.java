package net.role4j.runtime.ui;

import net.role4j.evolution.UAdaptationXMLParser;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/24/16.
 */
public class UnanticipatedAdaptationUI extends JFrame implements Runnable{
    public UnanticipatedAdaptationUI(String xml){
        JTextArea txtXML = new JTextArea(xml);
        JScrollPane spTxtXML = new JScrollPane(txtXML);
        JButton btnPerform = new JButton("Perform");

        txtXML.setPreferredSize(new Dimension(50*10, 30*10));

        btnPerform.addActionListener(e -> UAdaptationXMLParser.parse(txtXML.getText()));

        add(spTxtXML, BorderLayout.NORTH);
        add(btnPerform, BorderLayout.SOUTH);

        setTitle("Unanticipated Adaptation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();
        setVisible(true);
    }

    @Override
    public void run() {
//        UnanticipatedAdaptationUI ui = new UnanticipatedAdaptationUI();

    }
}
