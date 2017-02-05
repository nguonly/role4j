package demo.rollback.server;

/**
 * Created by nguonly on 10/17/16.
 */
public interface IChannel {
    String prepareChannelForSending(String data);
    String prepareChannelForReceiving(String data);
}
