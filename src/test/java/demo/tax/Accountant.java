package demo.tax;

import net.role4j.IPlayer;
import net.role4j.IRole;

import java.util.List;

/**
 * Created by nguonly on 11/4/16.
 */
public class Accountant extends Employee implements IRole {
    public void paySalary(){
        List<IPlayer> developers = getCores(Developer.class);
        List<IPlayer> accountants = getCores(Accountant.class);

        payByPosition(developers);
        payByPosition(accountants);
    }

    private void payByPosition(List<IPlayer> cores){
        for(IPlayer player : cores){
            Person person = Person.class.cast(player);
            int salary = person.interfaceOf(Employee.class).getPaid();
            person.setSaving(salary);
        }
    }
}
