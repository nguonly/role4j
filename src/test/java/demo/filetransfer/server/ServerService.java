package demo.filetransfer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nguonly on 10/14/16.
 */
public class ServerService extends Thread{
    ServerSocket server;
    ExecutorService pool = Executors.newFixedThreadPool(5);
    List<ServiceHandler> listClient = new ArrayList<>();

    public void run(){
        try {
            int PORT = 8888;
            server = new ServerSocket(PORT);

            while (true) {
                System.out.println("Waiting for client...");
                AppState.txtMsg.append("Waiting for client...\n");
                Socket client = server.accept();
                AppState.txtMsg.append("Get connection from " + client.getInetAddress().toString() + " port:" + client.getPort() + "\n");

                ServiceHandler handler = new ServiceHandler(client);
                pool.submit(handler);
                listClient.add(handler);
            }
        }catch(SocketException se){
            System.out.println("Server is shutdown");
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void stopServer(){
        try {
            //alert all connected clients to disconnect
            listClient.forEach(ServiceHandler::disconnect);

            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
