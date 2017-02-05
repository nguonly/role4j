package demo.tax;

import net.role4j.IRole;

/**
 * Created by nguonly on 11/4/16.
 */
public class Developer extends Employee implements IRole {

    public void code(){
        System.out.println("Develop Tax Computation");
    }
}
