package demo.doubledispatch;

import demo.doubledispatch.player.Square;
import demo.doubledispatch.player.Triangle;
import demo.doubledispatch.role.PrintableSquare;
import demo.doubledispatch.role.PrintableSquareGerman;
import demo.doubledispatch.role.PrintableTriangle;
import demo.doubledispatch.role.PrintableTriangleGerman;
import net.role4j.Registry;

/**
 * We do not support double method dispatch but we can use binding to work around.
 * Created by nguonly on 11/24/16.
 */
public class Main {
    public static void main(String... args) throws Throwable{
        Registry reg = Registry.getRegistry();
        Printer printer = reg.newCompartment(Printer.class);
        Printer germanPrinter = reg.newCompartment(Printer.class);

        Triangle triangle = reg.newCore(Triangle.class);
        Square square = reg.newCore(Square.class);

        printer.activate();
        triangle.bind(PrintableTriangle.class);
        square.bind(PrintableSquare.class);
        triangle.print();
        square.print();
        printer.deactivate();

        germanPrinter.activate();
        triangle.bind(PrintableTriangleGerman.class);
        square.bind(PrintableSquareGerman.class);
        triangle.print();
        square.print();
        germanPrinter.deactivate();
    }
}
