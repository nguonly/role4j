package demo.filetransfer.server.evolution;

import demo.filetransfer.server.IChannel;
import net.role4j.IRole;

/**
 * Created by nguonly on 10/17/16.
 */
public class Compression implements IChannel,IRole {
    public String prepareChannelForReceiving(String data){
        int idx = data.indexOf("@:");
        return data.substring(idx+2);
    }

    public String prepareChannelForSending(String data){
        String fMsg = getPlayer(IChannel.class).prepareChannelForSending(data);
        return "<C>" + fMsg + "<C>";
    }
}
