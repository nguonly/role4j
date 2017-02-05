package demo.rollback.server.ui;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.role4j.Registry;

import javax.swing.*;

/**
 * Created by nguonly on 1/14/17.
 */
public class ClassReloadingPanel extends JPanel {
    public ClassReloadingPanel(){
        JRadioButton rdoEnable = new JRadioButton("Enable");
        JRadioButton rdoDisable = new JRadioButton("Disable");

        ButtonGroup btnClassReloading = new ButtonGroup();
        btnClassReloading.add(rdoDisable);
        btnClassReloading.add(rdoEnable);

        rdoDisable.setSelected(true);

        rdoDisable.addActionListener(e -> Registry.getRegistry().enableEvolution(false));
        rdoEnable.addActionListener(e -> {
            Registry reg = Registry.getRegistry();
//            if(!reg.isByteBuddyAgentInstalled()) ByteBuddyAgent.install();
            reg.enableEvolution(true);
        });

        add(rdoDisable);
        add(rdoEnable);

        setBorder(BorderFactory.createTitledBorder("Class Reloading"));
    }
}
