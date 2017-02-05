package rolefeature;

import net.role4j.Compartment;
import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import org.junit.Assert;
import org.junit.Test;

/**
 * This test is inspired by SCROLL example where Robot acquires new behavoirs based on roles.
 * In that example, roles are located in different compartments and the dispatcher works w.r.t to the merge operation.
 * In our test, we will use only single compartment in which roles resided and construct play-relation.
 * Created by nguonly on 11/4/16.
 */
public class RobotTest extends BaseTest{
    public static class Robot implements IPlayer{
        private String _name;

        public Robot(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class Service implements IRole{
        public String move(){
            String name = getPlayer(Robot.class).getName();
            String target = interfaceOf(Navigation.class).getTarget();
            int sensorValue = interfaceOf(Sensor.class).readSensor();
            String actor = interfaceOf(Actor.class).getActor();
            double speed = getCompartment(BasicMovement.class).getSpeed();

            return String.format("I am %s and moving at %.2f to the %s with my %s w.r.t. sensor value of %d.",
                    name, speed, target, actor, sensorValue);
        }
    }

    public static class Navigation implements IRole{
        public String getTarget(){ return "kitchen";}
    }

    public static class Sensor implements IRole{
        public int readSensor(){return 100;}
    }

    public static class Actor implements IRole{
        public String getActor(){return "wheels";}
    }

    public static class BasicMovement implements ICompartment{
        public double getSpeed(){return 0.5;}
    }

    @Test
    public void driveRobotTest() throws Throwable{
        BasicMovement comp = _reg.newCompartment(BasicMovement.class);
        Robot myRobot = _reg.newCore(Robot.class, "Peter");

        comp.activate();
        IRole service = myRobot.bind(Service.class);
        service.bind(Navigation.class);
        service.bind(Sensor.class);
        service.bind(Actor.class);

        String expected = "I am Peter and moving at 0.50 to the kitchen with my wheels w.r.t. sensor value of 100.";
        String real = myRobot.interfaceOf(Service.class).move();

        Assert.assertEquals(expected, real);

    }
}
