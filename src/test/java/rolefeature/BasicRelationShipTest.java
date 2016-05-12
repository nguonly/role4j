package rolefeature;

import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nguonly on 5/2/16.
 */
public class BasicRelationShipTest extends BaseTest {

    @Test
    public void testTakingCourseRelationship() throws Throwable{
        Person ly = _reg.newCore(Person.class); //student
        Person markus = _reg.newCore(Person.class); //student
        Person martin = _reg.newCore(Person.class); //student
        Person thomas = _reg.newCore(Person.class); //professor

        ly.setName("Ly");
        markus.setName("Markus");
        martin.setName("Martin");
        thomas.setName("Thomas");

        University tud = _reg.newCompartment(University.class);
        tud.activate();
        //construct binding
        ly.bind(Student.class);
        markus.bind(Student.class);
        martin.bind(Student.class);

        ly.interfaceOf(Student.class).setStudentId(111);
        markus.interfaceOf(Student.class).setStudentId(222);
        martin.interfaceOf(Student.class).setStudentId(333);

        Course rop = new Course("ROP"); //role programming
        ly.interfaceOf(Student.class).takeCourse(rop);
        martin.interfaceOf(Student.class).takeCourse(rop);

        thomas.bind(Professor.class);
        thomas.interfaceOf(Professor.class).addLecture(rop); //thomas teaches ROP

        List<Person> pList = thomas.interfaceOf(Professor.class).getStudents(rop); //only ly and martin
        Assert.assertTrue(pList.contains(ly));
        Assert.assertTrue(pList.contains(martin));

        Student lyStudent = ly.getRoleInstance(Student.class);
        Assert.assertEquals(111, lyStudent.getStudentId());

        thomas.interfaceOf(Professor.class).setMark(lyStudent, rop, 77);
        thomas.interfaceOf(Professor.class).setMark(martin.getRoleInstance(Student.class), rop, 88);

        Assert.assertEquals(77, lyStudent.getMark(rop), DELTA);
        Assert.assertEquals(88, martin.interfaceOf(Student.class).getMark(rop), DELTA);
    }

    public static class Person implements IPlayer{
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class Student implements IRole {
        private int _stuId;

        public void setStudentId(int studentId){
            _stuId = studentId;
        }

        public int getStudentId(){
            return _stuId;
        }

        public void takeCourse(Course course){
            TakingCourse.add(this, course);
        }

        public double getMark(Course course){
            return TakingCourse.getRelations().stream()
                    .filter(p->p._learner.equals(this) && p._course.equals(course))
                    .mapToDouble(p->p._mark).findFirst().getAsDouble();
        }
    }

    //Relationship
    public static class TakingCourse{
        public Student _learner;
        public Course _course;
        public double _mark; //score

        public TakingCourse(){}

        public TakingCourse(Student learner, Course course){
            _learner = learner;
            _course = course;
        }

        static ArrayDeque<TakingCourse> list = new ArrayDeque<>();

        static void add(Student learner, Course course){
            list.add(new TakingCourse(learner, course));
        }

        public static ArrayDeque<TakingCourse> getRelations(){
            return list;
        }
    }

    //ordinary object
    public static class Course{
        private String _name;

        public Course(){}

        public Course(String name){
            _name = name;
        }

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }


    }

    public static class Professor implements IRole{
        List<Course> taughtCourses = new ArrayList<>();

        public void addLecture(Course course){
            taughtCourses.add(course);
        }

        public List<Person> getStudents(Course course){

            List<TakingCourse> relations = TakingCourse.getRelations().stream()
                    .filter(p -> p._course.equals(course))
                    .collect(Collectors.toList());

            List<Person> pList = new ArrayList<>();

            relations.forEach(c->{
                Person p = (Person)getCore(c._learner);
//                System.out.println(p.getName());
                pList.add(p);
            });

            return pList;
        }

        public void setMark(Student student, Course course, double mark){
            Optional<TakingCourse> relation = TakingCourse.getRelations().stream()
                    .filter(p -> p._learner.equals(student) && p._course.equals(course))
                    .findFirst();

            if(relation.isPresent()){
                TakingCourse tc = relation.get();
                tc._mark = mark;
            }
        }
    }

    public static class University implements ICompartment{

    }
}
