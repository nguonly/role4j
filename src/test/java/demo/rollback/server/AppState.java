package demo.rollback.server;

import demo.rollback.server.evolution.Compression;
import demo.rollback.server.evolution.Encryption;
import net.role4j.Compartment;
import net.role4j.DumpHelper;
import net.role4j.evolution.UAdaptationXMLParser;
import net.role4j.rollback.ControlUnit;

import javax.swing.*;

/**
 * Created by nguonly on 10/14/16.
 */
public class AppState {
    static ServerService serverService;

    static Compartment compartment;
    static Channel channel;

    static JTextArea txtMsg; //ServerMain Server Message in MessagePanel

    public static JTextArea txtXML; //XML in Unanticipated Adaptation

    static boolean isTranquil;

    private static AppState appState;

    public AppState(){}

    public static synchronized AppState getInstance(){
        if(appState == null){
            appState = new AppState();
        }
        return appState;
    }

    public static void setTranquilState(boolean state){
        isTranquil = state;
    }

    public static boolean isIsTranquil(){
        return isTranquil;
    }

    public static JTextArea getTextMessageUI(){
        return txtMsg;
    }

    public static void startServerService(){
        String tranquilMsg;
        if(isTranquil) tranquilMsg = "::: With Tranquility :::";
        else tranquilMsg = "::: WithOUT Tranquility :::";

        txtMsg.setText(tranquilMsg + "\n");
        serverService = new ServerService();
        serverService.start(); //Thread
    }

    public static void stopServerService(){
        if(serverService!=null && serverService.server!=null && !serverService.server.isClosed()){
            serverService.stopServer();
            txtMsg.append("Server STOP!\n");
        }
    }

    public static void performEncryptionAdaptation(){
//        System.out.println("Perform Encryption Adaptation");
        txtMsg.append("Adapt (Encryption)\n");

        compartment.activate();
        try {
//            channel.unbindAll();
            ControlUnit.checkpoint();
            channel.bind(Encryption.class);
            DumpHelper.dumpRelations();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void performCompressionAdaptation(){
//        System.out.println("Perform Encryption Adaptation");
        txtMsg.append("Adapt (Compression)\n");

        compartment.activate();
        try {
//            channel.unbindAll();
            ControlUnit.checkpoint();
            channel.bind(Compression.class);
            DumpHelper.dumpRelations();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void performEncryptionCompressionAdaptation(){
        txtMsg.append("Adapt (Encryption -> Compression)\n");
        compartment.activate();
        try {
            ControlUnit.checkpoint();
            channel.bind(Encryption.class).bind(Compression.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void performCompressionEncryptionAdaptation(){
        txtMsg.append("Adapt (Compression -> Encryption)\n");
        compartment.activate();
        try {
            ControlUnit.checkpoint();
            channel.bind(Compression.class).bind(Encryption.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void resetAdaptation(){
        txtMsg.append("Reset Adaptation\n");
        compartment.activate();
        try {
            ControlUnit.checkpoint();
            channel.unbindAll();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void performUAdpatation(){
        UAdaptationXMLParser.parse(txtXML.getText());
    }

    public static void appendMessage(String message){
        txtMsg.append(message + "\n");
    }

}
