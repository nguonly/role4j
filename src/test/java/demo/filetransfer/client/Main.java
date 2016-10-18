package demo.filetransfer.client;

import demo.filetransfer.server.SmartScroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by nguonly on 10/14/16.
 */
public class Main extends JFrame{
    JTextArea txtMsg;

    public Main(){

        txtMsg = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(txtMsg);
        new SmartScroller(scrollPane);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(30*10, 30*10));
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane);

        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });

        add(panel, BorderLayout.NORTH);
        add(btnConnect, BorderLayout.SOUTH);


        setTitle("Client of File Transfer Apps");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
    }

    public static void main(String... args){
        Main client = new Main();
        client.setVisible(true);


    }

    public void connect(){
        NetworkService service = new NetworkService(txtMsg);
        service.start();
    }
}
