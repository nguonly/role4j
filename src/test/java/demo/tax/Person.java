package demo.tax;

import net.role4j.IPlayer;

/**
 * Created by nguonly on 11/4/16.
 */
public class Person implements IPlayer {
    private String _name;
    private double _saving = 0;

    public Person() {}

    public Person(String name){
        _name = name;
    }

    public String getName(){return _name;}

    public void setSaving(double amount){
        _saving += amount;
    }

    public double getSaving(){
        return _saving;
    }
}
