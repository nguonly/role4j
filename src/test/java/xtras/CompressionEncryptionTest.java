package xtras;

import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import net.role4j.Registry;

/**
 * Created by nguonly on 5/10/16.
 */
public class CompressionEncryptionTest {

    public static void main(String... args) throws Throwable{
        Registry reg = Registry.getRegistry();

        Sender sender = reg.newCore(Sender.class);

        Compartment c1 = reg.newCompartment(Compartment.class);
        Compartment c2 = reg.newCompartment(Compartment.class);
        Compartment c3 = reg.newCompartment(Compartment.class);
        Compartment c4 = reg.newCompartment(Compartment.class);

        c1.activate();
        sender.bind(Encryption.class);
        c1.deactivate();

        c2.activate();
        sender.bind(Compression.class);
        c2.deactivate();

        c3.activate();
        sender.bind(Encryption.class).bind(Compression.class);
        c3.deactivate();

        c4.activate();
        sender.bind(Compression.class).bind(Encryption.class);
        c4.deactivate();

        //Program area

        System.out.println("Original Behavior");
        System.out.println(sender.send());

        c1.activate();
        System.out.println("C1 is activate");
        System.out.println(sender.send());
        c1.deactivate();

        c2.activate();
        System.out.println("C2 is activate");
        System.out.println(sender.send());
        c2.deactivate();

        c3.activate();
        System.out.println("C3 is activate");
        System.out.println(sender.send());
        c3.deactivate();

        c4.activate();
        System.out.println("C4 is activate");
        System.out.println(sender.send());
        c4.deactivate();

        System.out.println("No compartment is activated");
        System.out.println(sender.send());
    }

    public static class Sender implements IPlayer, ISending{
        public String send(){
            return "data";
        }
    }
    public static class Encryption implements IRole, ISending {
        public String send(){
            String proceed = getPlayer(ISending.class).send();
            return "<E>" + proceed + "<E>";
        }
    }

    public static class Compression implements IRole, ISending{
        public String send(){
            String proceed = getPlayer(ISending.class).send();
            return "<C>" + proceed + "<C>";
        }
    }

    interface ISending{
        String send();
    }

    public static class Compartment implements ICompartment{

    }
}
