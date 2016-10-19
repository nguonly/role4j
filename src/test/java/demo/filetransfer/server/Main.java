package demo.filetransfer.server;

import demo.filetransfer.server.ui.AdaptationPanel;
import demo.filetransfer.server.ui.MessagePanel;
import demo.filetransfer.server.ui.StartingServerPanel;
import net.role4j.Compartment;
import net.role4j.Registry;
import net.role4j.runtime.ui.MainRTMonitorUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/14/16.
 */
public class Main extends JFrame {

    private StartingServerPanel serverPanel;
    private MessagePanel messagePanel;
    private AdaptationPanel adaptationPanel;

    private JTextArea txtMsg = new JTextArea();
//    private boolean isTranquil;

    private AppState appState = new AppState();

    public Main() throws Throwable{

        Registry reg = Registry.getRegistry();
        AppState.channel = reg.newCore(Channel.class);

        AppState.compartment = reg.newCompartment(Compartment.class);
//        comp.activate();
        AppState.txtMsg = new JTextArea();

        serverPanel = new StartingServerPanel();
        messagePanel = new MessagePanel();
        adaptationPanel = new AdaptationPanel();

        add(serverPanel, BorderLayout.NORTH);
        add(messagePanel, BorderLayout.WEST);
        add(adaptationPanel, BorderLayout.EAST);

        setTitle("File Transfer : Consistency");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
    }

    public static void main(String... args) throws Throwable{
        SwingUtilities.invokeLater(new MainRTMonitorUI());
//        Thread rtMonitor = new Thread(new MainRTMonitorUI());
//        rtMonitor.start();
        JFrame main = new Main();
        main.setVisible(true);
    }
}
