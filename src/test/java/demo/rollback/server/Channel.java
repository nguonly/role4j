package demo.rollback.server;

import net.role4j.IPlayer;

/**
 * Created by nguonly on 10/17/16.
 */
public class Channel implements IChannel, IPlayer{
    public String prepareChannelForSending(String data){
        return data;
    }

    public String prepareChannelForReceiving(String data){ return data;}
}
