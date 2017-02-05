package demo.rollback.server.evolution;

import demo.rollback.server.IChannel;
import net.role4j.IRole;

/**
 * Created by nguonly on 10/17/16.
 */
public class Compression implements IChannel,IRole {
    public String prepareChannelForReceiving(String data){
//        int idx = data.indexOf("@:");
//        return data.substring(idx+2);
        return data;
    }

    public String prepareChannelForSending(String data){
        String fMsg = getPlayer(IChannel.class).prepareChannelForSending(data);
        int i=1/0;
        return "<C>" + fMsg + "<C>";
    }
}
