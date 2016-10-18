package demo.filetransfer.server.ui;

import demo.filetransfer.server.AppState;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/14/16.
 */
public class MessagePanel extends JPanel {

    public MessagePanel(){
        JScrollPane listScrollPane = new JScrollPane(AppState.getTextMessageUI());

        new SmartScroller(listScrollPane, SmartScroller.VERTICAL, SmartScroller.END);

        setLayout(new BorderLayout());
        add(listScrollPane);

        setBorder(BorderFactory.createTitledBorder("Message"));
        //setBackground(Color.BLUE);
        setPreferredSize(new Dimension(40*10, 30*10));
    }
}
