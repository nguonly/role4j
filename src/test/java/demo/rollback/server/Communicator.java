package demo.rollback.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by nguonly on 10/14/16.
 */
public class Communicator {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public void setSocket(Socket socket) {
        this.socket = socket;

        try {
            input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            output = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void send(String data) {
        String fMsg = AppState.channel.prepareChannelForSending(data);
        output.println(fMsg);
    }

    public String receive() {
        try {
            String data = input.readLine();
            return AppState.channel.prepareChannelForReceiving(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
