package rolefeature;

import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by nguonly on 5/2/16.
 */
public class CompartmentPlaysRoleTest extends BaseTest{

    @Test
    public void testBasicCompartmentPlaysRole() throws Throwable{
        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        Company abc = _reg.newCompartment(Company.class);

        comp.activate();
        abc.bind(TaxPayer.class);

        abc.setRevenue(2000);

        abc.interfaceOf(TaxPayer.class).pay();

        double balance = abc.getRevenue();
//        System.out.println(balance);
        assertEquals(1600, balance , DELTA);
    }

    @Test
    public void testCompartmentAndCorePlayTheSameRole() throws Throwable{
        MyCompartment comp = _reg.newCompartment(MyCompartment.class);
        Company abc = _reg.newCompartment(Company.class);
        Person ely = _reg.newCore(Person.class);

        comp.activate();
        abc.bind(TaxPayer.class);
        ely.bind(FreeLance.class).bind(TaxPayer.class);

        abc.setRevenue(2000);
        ely.interfaceOf(FreeLance.class).setRevenue(1000);

        abc.interfaceOf(TaxPayer.class).pay(); //company pays 20% of revenue
        ely.interfaceOf(TaxPayer.class).pay(); //freelance pays 10% of revenue

        assertEquals(1600, abc.getRevenue(), DELTA);
        assertEquals(900, ely.interfaceOf(FreeLance.class).getRevenue(), DELTA);
    }

    public static class Person implements IPlayer{
        private String _name;

        public String getName(){
            return _name;
        }

        public void setName(String name){
            _name = name;
        }
    }

    public static interface ITax {
        double taxToBePaid();
        void setRevenue(double amount);
        double getRevenue();
    }

    public static class FreeLance implements IRole, ITax {
        private double _revenue;

        public void setRevenue(double revenue){
            _revenue = revenue;
        }

        public double getRevenue(){
            return _revenue;
        }

        public double taxToBePaid(){
            return 0.1;
        }
    }

    public static class Company implements ICompartment, ITax {
        private double _revenue;

        public void setRevenue(double revenue){
            _revenue = revenue;
        }

        public double getRevenue(){
            return _revenue;
        }

        public double taxToBePaid(){
            return 0.2;
        }
    }

    public static class TaxPayer implements IRole{
        public void pay(){
            double tax = interfaceOf(ITax.class).taxToBePaid();
            double revenue = interfaceOf(ITax.class).getRevenue();
            double balance = revenue - revenue*tax;
//            interfaceOf(ITax.class).setRevenue(balance);
            getPlayer(ITax.class).setRevenue(balance);
        }
    }

    public static class MyCompartment implements ICompartment{

    }
}
