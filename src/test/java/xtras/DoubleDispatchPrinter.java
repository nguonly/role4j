package xtras;

/**
 * Created by nguonly on 5/10/16.
 */
public class DoubleDispatchPrinter {

    public static void main(String... args){
        Figure [] figures = new Figure [] {
                new Circle(), new Rectangle() };
        Printer [] printers = new Printer [] {
                new PostscriptPrinter(), new InkjetPrinter() };

        printAllEverywhere(figures, printers);
    }

    /** Prints all figures on each of the printers. */
    static void printAllEverywhere( Figure[] figures, Printer[] printers ) {
        for ( int i = 0; i < figures.length; i++ ) {
            Figure figure = figures[ i ];
            for ( int j = 0; j < printers.length; j++ ) {
                Printer printer = printers[ j ];

                figure.printOn( printer );
                // must work for any printer or figure !
            }
        }
    }


    interface Figure {
        void printOn( Printer printer );
    }
    interface Printer {
        void printCircle( Circle circle );
        void printRectangle( Rectangle rectangle );
    }

    static class Circle implements Figure {
        public void printOn( Printer printer ) {
            printer.printCircle( this ); // <-- the "trick" !
        }
    }
    static class Rectangle implements Figure {
        public void printOn( Printer printer ) {
            printer.printRectangle( this );
        }
    }

    static class InkjetPrinter implements Printer {
        public void printCircle( Circle circle ) {
            // ... rasterizing logic for inkjet printing of circles here ...
            System.out.println( "Inkjet printer prints a cirlce." );
        }
        public void printRectangle( Rectangle rectangle ) {
            // ... rasterizing logic for inkjet printing of rectangles here ...
            System.out.println( "Inkjet printer prints a rectangle." );
        }
    }
    static class PostscriptPrinter implements Printer {
        public void printCircle( Circle circle ) {
            // ... postscript preprocessing logic for circles here ...
            System.out.println( "PostScript printer prints a cirlce." );
        }
        public void printRectangle( Rectangle rectangle ) {
            // ... postscript preprocessing logic for rectangles here ...
            System.out.println( "PostScript printer prints a rectangle." );
        }
    }
}
