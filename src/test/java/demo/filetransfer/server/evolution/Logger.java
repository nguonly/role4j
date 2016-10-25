package demo.filetransfer.server.evolution;

import demo.filetransfer.server.IChannel;
import net.role4j.IRole;

/**
 * Created by nguonly on 10/21/16.
 */
public class Logger implements IChannel, IRole{
    public String prepareChannelForReceiving(String data){
        int idx = data.indexOf("@:");
        return data.substring(idx+2);
    }

    public String prepareChannelForSending(String data){
        String fMsg = getPlayer(IChannel.class).prepareChannelForSending(data);
        return "<L>" + fMsg + "<L>";
    }
}
