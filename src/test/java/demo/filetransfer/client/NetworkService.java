package demo.filetransfer.client;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by nguonly on 10/14/16.
 */
public class NetworkService extends Thread {
    private JTextArea txtMsg;

    public NetworkService(JTextArea txtMsg){
        this.txtMsg = txtMsg;
    }

    public void run(){
        try {
            Socket client = new Socket("localhost", 8888);

            //PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String data;
            while((data=input.readLine())!=null){
                //data = input.readLine();
                txtMsg.append(data + "\n");
                if(data.contains("DISCONNECT")) break;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
