package demo.tax;

import net.role4j.IRole;

/**
 * Created by nguonly on 11/4/16.
 */
public class Employee{
    private int _salary;

    public void setSalary(int amount){
        _salary = amount;
    }

    public int getPaid(){
        return _salary;
    }
}
