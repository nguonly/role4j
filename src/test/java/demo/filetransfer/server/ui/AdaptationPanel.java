package demo.filetransfer.server.ui;

import demo.filetransfer.server.AppState;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nguonly on 10/14/16.
 */
public class AdaptationPanel extends JPanel {


    public AdaptationPanel() throws Throwable{
        JButton btnEncryptionAdaptation = new JButton("Adapt(Encryption)");
        JButton btnCompressionAdaptation = new JButton("Adapt(Compression)");
        JButton btnEncCompAdaptation = new JButton("Adapt(Encryption -> Compression)");
        JButton btnCompEncAdaptation = new JButton("Adapt(Compression -> Encryption)");
        JButton btnResetAdaptation = new JButton("Reset Adaptation");

        Dimension btnSize = new Dimension(280, 30);
        btnResetAdaptation.setPreferredSize(btnSize);
        btnEncryptionAdaptation.setPreferredSize(btnSize);
        btnCompressionAdaptation.setPreferredSize(btnSize);
        btnEncCompAdaptation.setPreferredSize(btnSize);
        btnCompEncAdaptation.setPreferredSize(btnSize);

        btnEncryptionAdaptation.addActionListener(e -> AppState.performEncryptionAdaptation());
        btnCompressionAdaptation.addActionListener(e -> AppState.performCompressionAdaptation());
        btnEncCompAdaptation.addActionListener(e -> AppState.performEncryptionCompressionAdaptation());
        btnCompEncAdaptation.addActionListener(e -> AppState.performCompressionEncryptionAdaptation());
        btnResetAdaptation.addActionListener(e -> AppState.resetAdaptation());

        add(btnEncryptionAdaptation);
        add(btnCompressionAdaptation);
        add(btnEncCompAdaptation);
        add(btnCompEncAdaptation);
        add(btnResetAdaptation);

        setBorder(BorderFactory.createTitledBorder("Adaptation Panel"));
        //setBackground(Color.CYAN);
        setPreferredSize(new Dimension(30*10, 30*10));
    }
}
