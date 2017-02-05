package demo.tax;

import net.role4j.Registry;

/**
 * Created by nguonly on 11/4/16.
 */
public class Main {
    public static void main(String... args) throws Throwable{
        Registry reg = Registry.getRegistry();
        Company abc = reg.newCompartment(Company.class);
        Person ely = reg.newCore(Person.class, "Ely");
        Person bob = reg.newCore(Person.class, "Bob");
        Person alice = reg.newCore(Person.class, "Alice");

        abc.activate();
        ely.bind(Developer.class);
        ely.interfaceOf(Developer.class).setSalary(1000);

        bob.bind(Developer.class);
        bob.interfaceOf(Developer.class).setSalary(1500);

        alice.bind(Accountant.class);
        alice.interfaceOf(Accountant.class).setSalary(1200);

        alice.interfaceOf(Accountant.class).paySalary();

        System.out.println(ely.getName() + " saves " + ely.getSaving());
        System.out.println(bob.getName() + " saves " + bob.getSaving());
        System.out.println(alice.getName() + " saves " + alice.getSaving());
    }
}
