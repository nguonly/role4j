package demo.filetransfer.server;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/14/16.
 */
public class MessagePanel extends JPanel {

    public MessagePanel(){
        JScrollPane listScrollPane = new JScrollPane(AppState.txtMsg);

        new SmartScroller(listScrollPane, SmartScroller.VERTICAL, SmartScroller.END);

        setLayout(new BorderLayout());
        add(listScrollPane);

        setBorder(BorderFactory.createTitledBorder("Message"));
        //setBackground(Color.BLUE);
        setPreferredSize(new Dimension(30*10, 30*10));
    }
}
