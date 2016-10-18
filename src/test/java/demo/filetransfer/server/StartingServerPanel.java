package demo.filetransfer.server;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/14/16.
 */
public class StartingServerPanel extends JPanel {

    private JRadioButton withoutTranquility;
    private JRadioButton withTranquility;

    public StartingServerPanel(){

        withoutTranquility = new JRadioButton("Without Tranquility");
        withTranquility = new JRadioButton("With Tranquility");
        ButtonGroup btnTranquility = new ButtonGroup();
        btnTranquility.add(withoutTranquility);
        btnTranquility.add(withTranquility);

        withoutTranquility.setSelected(true);

        JButton btnStartServer = new JButton("Start Server");
        JButton btnStopServer = new JButton("Stop Server");

        btnStartServer.addActionListener(e -> {
            AppState.isTranquil= withTranquility.isSelected();

            AppState.startServerService();
        });

        btnStopServer.addActionListener(e -> AppState.stopServerService());

        add(withoutTranquility);
        add(withTranquility);
        add(btnStartServer);
        add(btnStopServer);

        setBorder(BorderFactory.createTitledBorder("Server"));
        setPreferredSize(new Dimension(20*10, 10*10));

    }
}
