package xtras;

import net.role4j.DumpHelper;
import net.role4j.ICompartment;
import net.role4j.IPlayer;
import net.role4j.IRole;
import net.role4j.view.NodeInfoUI;
import net.role4j.view.Tree;
import net.role4j.view.NodeInfo;
import net.role4j.view.RelationToTree;
import org.junit.Test;
import rolefeature.BaseTest;
import rolefeature.CorePlaysRoleTest;
import rolefeature.InterfaceInvocationTest;

import java.util.List;

/**
 * Created by nguonly on 5/11/16.
 */
public class TreeNodeTest extends BaseTest {

//    public static void main(String... args) {
//        Tree<String> parentNode = new Tree<>("Parent");
//        Tree<String> childNode1 = new Tree<>("Child 1");
////        Tree<String> childNode2 = new Tree<>("Child 2");
//
////        childNode2.setParent(parentNode);
//
//        parentNode.addChild(new Tree<>("aa"));
//
////        Tree<String> grandchildNode = new Tree<>("Grandchild of parentNode. Child of childNode1", childNode1);
//        List<Tree<String>> childrenNodes = parentNode.getChildren();
//
//        childrenNodes.forEach(System.out::println);
//    }

    @Test
    public void testTreeNode(){
        Tree<String> parentNode = new Tree<>("root");
        Tree<String> childNode1 = new Tree<>("Child 1");
//        Tree<String> childNode2 = new Tree<>("Child 2");

//        childNode2.setParent(parentNode);

        parentNode.addChild(new Tree<>("aa"));
        parentNode.addChild(new Tree<>("bb"));
        parentNode.addChild(childNode1);
        parentNode.addChild("cc");

        childNode1.addChild("dd");

//        Tree<String> grandchildNode = new Tree<>("Grandchild of parentNode. Child of childNode1", childNode1);
        List<Tree<String>> childrenNodes = parentNode.getChildren();

        childrenNodes.forEach(k -> {
            System.out.println(k.getData() + " parent : " + k.getParent().getData());
        });

        childNode1.getChildren().forEach(k -> {
            System.out.println(k.getData() + " parent : " + k.getParent().getData());
        });

        parentNode.printNode("");
    }

    @Test
    public void testConvertTableToTreeNode() throws Throwable{
        Any any1 = _reg.newCore(Any.class);
        Any any2 = _reg.newCore(Any.class);

        MyCompartment comp1 = _reg.newCompartment(MyCompartment.class);
        comp1.activate();

        IRole a = any1.bind(A.class);

        any2.bind(F.class).bind(G.class);

        any1.bind(C.class);

        a.bind(B.class);

        DumpHelper.dumpRelations();

        Tree<NodeInfo> root = RelationToTree.convertRelationToTree();

        root.printNode("");
    }

    @Test
    public void testTwoCompartments() throws Throwable{
        MyCompartment comp1 = _reg.newCompartment(MyCompartment.class);
        MyCompartment comp2 = _reg.newCompartment(MyCompartment.class);
        Any any1 = _reg.newCore(Any.class);
        Any any2 = _reg.newCore(Any.class);
        Any any3 = _reg.newCore(Any.class);

        comp1.activate();

        IRole a = any1.bind(A.class);

        any2.bind(F.class).bind(G.class);

        any1.bind(C.class);

        a.bind(B.class);

        comp1.deactivate();

        comp2.activate();
        any2.bind(A.class);
        any2.bind(E.class);

        any3.bind(F.class);
        any3.bind(H.class).bind(D.class);

        DumpHelper.dumpRelations();

        Tree<NodeInfo> root = RelationToTree.convertRelationToTree();

        root.printNode("");
    }

    @Test
    public void testConvertTableToTreeNodeWithCompartment() throws Throwable{
        Any any1 = _reg.newCore(Any.class);
        Any any2 = _reg.newCore(Any.class);

        MyCompartment comp1 = _reg.newCompartment(MyCompartment.class);
        comp1.activate();

        IRole a = any1.bind(A.class);

        any2.bind(F.class).bind(G.class).bind(H.class);

        any1.bind(C.class);

        a.bind(B.class);

        DumpHelper.dumpRelations();

        Tree<NodeInfoUI> root = RelationToTree.convertRelationToTreeWithCompartment();

        root.printNode("");
    }

    public static class Any implements IPlayer {
        private String _name;

        public Any(){}

        public Any(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class A implements IRole  {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class B implements IRole  {
        private String _name;

        public void setName(String name){
            _name = name;
        }

        public String getName(){
            return _name;
        }
    }

    public static class C implements IRole{

    }

    public static class D implements IRole{

    }

    public static class E implements IRole{

    }

    public static class F implements IRole{

    }

    public static class G implements IRole{

    }

    public static class H implements IRole{

    }

    public static class MyCompartment implements ICompartment {

    }
}
