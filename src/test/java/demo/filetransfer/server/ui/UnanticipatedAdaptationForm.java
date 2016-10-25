package demo.filetransfer.server.ui;

import demo.filetransfer.server.AppState;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/21/16.
 */
public class UnanticipatedAdaptationForm extends JFrame {

    public UnanticipatedAdaptationForm(){
        //JTextArea txtXML = new JTextArea();
        AppState.txtXML = new JTextArea();
        JScrollPane spTxtXML = new JScrollPane(AppState.txtXML);
        JButton btnPerform = new JButton("Perform");

        AppState.txtXML.setPreferredSize(new Dimension(50*10, 30*10));

        btnPerform.addActionListener(e -> AppState.performUAdpatation());

        add(spTxtXML, BorderLayout.NORTH);
        add(btnPerform, BorderLayout.SOUTH);

        setTitle("Unanticipated Adaptation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();
    }
}
