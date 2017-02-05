package demo.rollback.client;

import demo.filetransfer.server.ui.SmartScroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by nguonly on 10/14/16.
 */
public class ClientMain extends JFrame{
    private JTextArea txtMsg;

    private NetworkService service;

    public ClientMain(){

        txtMsg = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtMsg);
        new SmartScroller(scrollPane);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(30*10, 30*10));
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(e -> connect());

        JButton btnDisconnect = new JButton("Discontect");
        btnDisconnect.addActionListener(e-> disconnect());

        add(panel, BorderLayout.NORTH);
        add(btnConnect, BorderLayout.WEST);
        add(btnDisconnect, BorderLayout.EAST);


        setTitle("Client of File Transfer Apps");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
    }

    public static void main(String... args){
        ClientMain client = new ClientMain();
        client.setVisible(true);


    }

    public void connect(){
        service = new NetworkService(txtMsg);
        service.start();
    }

    public void disconnect() {
        service.disconnect();
    }

}
