package demo.rollback.client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by nguonly on 10/14/16.
 */
public class NetworkService extends Thread {
    private JTextArea txtMsg;

    NetworkService(JTextArea txtMsg){
        this.txtMsg = txtMsg;
    }

    private Socket client;

    private boolean isConnected;

    public void run(){
        try {
            client = new Socket("localhost", 8888);

            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            isConnected = true;
            String data;
            while((data=input.readLine())!=null){
                if(!isConnected){
                    sendDisconnect();
                    break;
                }

                txtMsg.append(data + "\n");
                if(data.contains("DISCONNECT")) break;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    void disconnect(){
        isConnected=false;
    }

    private void sendDisconnect() {
        PrintWriter out;
        try {
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error during disconnect!");
        }
        txtMsg.append("Client sends :::: DISCONNECT\n");
        out.println("DISCONNECT");
    }
}
