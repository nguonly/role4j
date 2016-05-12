package rolefeature;

import net.role4j.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

/**
 * Created by nguonly on 5/9/16.
 */
public class CompartmentAsRelationshipTest extends BaseTest{

    @Test
    public void testScopingOfCompartmentAsRelationship() throws Throwable{
        Person ly = _reg.newCore(Person.class, "Ly");
        Person markus = _reg.newCore(Person.class, "Markus");
        Person martin = _reg.newCore(Person.class, "Martin");
        Person thomas = _reg.newCore(Person.class, "Thomas");
        Person nicolas = _reg.newCore(Person.class, "Nicolas");

        Course cop = _reg.newCompartment(Course.class, "COP");
        Course rop = _reg.newCompartment(Course.class, "ROP");

        rop.activate();
        ly.bind(Student.class);
        martin.bind(Student.class);
        thomas.bind(Professor.class);

        thomas.interfaceOf(Professor.class).setMark(ly.getRoleInstance(Student.class), 77);
        thomas.interfaceOf(Professor.class).setMark(martin.getRoleInstance(Student.class), 88);

        Assert.assertEquals(77, ly.interfaceOf(Student.class).getMark(), DELTA);
        Assert.assertEquals(88, martin.interfaceOf(Student.class).getMark(), DELTA);

//        DumpHelper.dumpRelations(rop);

        List<Person> ps = rop.getStudents();
        Assert.assertTrue(ps.contains(ly));
        Assert.assertTrue(ps.contains(martin));
        Assert.assertFalse(ps.contains(markus)); //Markus not in the list

        cop.activate();
        markus.bind(Student.class);
        nicolas.bind(Professor.class);

        nicolas.interfaceOf(Professor.class).setMark(markus.getRoleInstance(Student.class), 99);

        Assert.assertEquals(99, markus.interfaceOf(Student.class).getMark(), DELTA);

        Person p = (Person)cop.getProfessor();
        Assert.assertEquals(nicolas, p);
    }

    public static class Person implements IPlayer{
        private String _name;

        public Person(){

        }

        public Person(String name){
            _name = name;
        }
    }

    public static class Student implements IRole{

        public double getMark(){
            Course course = getCompartment(Course.class);
            return course.getMark(this);
        }
    }

    public static class Professor implements IRole{
        public void setMark(Student student, double mark){

            Course course = getCompartment(Course.class);
            course.setMark(student, mark);
        }
    }

    public static class Course implements ICompartment, IRelationship{
        private String _name;

        private HashMap<Student, Double> marks = new HashMap<>();

        public Course(String name){
            _name = name;
        }

        public void setMark(Student student, Double mark){
            marks.put(student, mark);
        }

        public Double getMark(Student student){
            return marks.get(student);
        }

        public List<Person> getStudents(){
            List<Person> ps = getCores(Person.class, Student.class);
            return ps;
        }

        public IPlayer getProfessor(){
            List<IPlayer> ps = getCores(Professor.class);

            return ps.get(0);
        }
    }
}
