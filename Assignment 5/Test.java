import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

/**
 * Test
 */
public class Test {

    public static void main(String[] args) {
        subTest1();
        subTest2();
        subTest3();
        subTest4();
        subTest5();
    }

    private static void subTest1() {
        KdTree testSET = new KdTree();
        Point2D A = new Point2D(0.372, 0.497);
        Point2D B = new Point2D(0.564, 0.413);
        Point2D C = new Point2D(0.226, 0.577);
        Point2D D = new Point2D(0.144, 0.179);
        Point2D E = new Point2D(0.083, 0.51);
        Point2D F = new Point2D(0.32, 0.708);
        Point2D G = new Point2D(0.417, 0.362);
        Point2D H = new Point2D(0.862, 0.825);
        Point2D I = new Point2D(0.785, 0.725);
        Point2D J = new Point2D(0.499, 0.208);

        Point2D p = new Point2D(0.31, 0.37);

        RectHV rect = new RectHV(0.4, 0.18, 0.52, 0.4);
        testSET.insert(A);
        testSET.insert(B);
        testSET.insert(C);
        testSET.insert(D);
        testSET.insert(E);
        testSET.insert(F);
        testSET.insert(G);
        testSET.insert(H);
        testSET.insert(I);
        testSET.insert(J);

        StdOut.println("Is C contains: " + testSET.contains(C));
        StdOut.println("Is A contains: " + testSET.contains(A));
        StdOut.println("Is p contains: " + testSET.contains(p));
        StdOut.println("p's nearest point: " + testSET.nearest(p));
        StdOut.println("p's nearest point: " + testSET.nearest(p));
        StdOut.println("p's nearest point: " + testSET.nearest(p));

        // test method: range(rect)
        for (Point2D point : testSET.range(rect)) {
            StdOut.println(point);
        }

        StdOut.println("nearest to C" + testSET.nearest(C));

        rect.draw();
        testSET.draw();
    }

    private static void subTest2() {
        KdTree testSET = new KdTree();
        Point2D A = new Point2D(0.5, 0.25);
        Point2D B = new Point2D(0.75, 0.875);
        Point2D C = new Point2D(0.875, 0.375);
        Point2D D = new Point2D(0.0, 0.625);
        Point2D E = new Point2D(0.25, 0.125);
        Point2D P = new Point2D(0.375, 1.0);
        testSET.insert(A);
        testSET.insert(B);
        testSET.insert(C);
        testSET.insert(D);
        testSET.insert(E);

        StdOut.println("Nearest to P: " + testSET.nearest(P));

    }

    private static void subTest3() {
        KdTree testSET = new KdTree();
        Point2D A = new Point2D(0.5, 0.625);
        Point2D B = new Point2D(0.75, 0.125);
        Point2D C = new Point2D(0.75, 1.0);
        Point2D D = new Point2D(0.0, 0.25);
        Point2D E = new Point2D(0.5, 0.75);
        testSET.insert(A);
        StdOut.println("size: " + testSET.size());
        testSET.insert(B);
        StdOut.println("size: " + testSET.size());
        testSET.insert(C);
        StdOut.println("size: " + testSET.size());
        testSET.insert(D);
        StdOut.println("size: " + testSET.size());
        testSET.insert(E);
        StdOut.println("size: " + testSET.size());

    }

    private static void subTest4() {
        KdTree testSET = new KdTree();
        Point2D A = new Point2D(0.8125, 0.9375);
        Point2D B = new Point2D(0.3125, 0.375);
        Point2D C = new Point2D(0.25, 0.875);
        Point2D D = new Point2D(0.375, 0.5);
        Point2D E = new Point2D(0.4375, 1.0);
        Point2D F = new Point2D(1.0, 0.25);
        Point2D G = new Point2D(0.875, 0.1875);
        Point2D H = new Point2D(0.9375, 0.4375);
        Point2D I = new Point2D(0.1875, 0.8125);
        Point2D J = new Point2D(0.5625, 0.0625);

        RectHV rect = new RectHV(0.125, 0.625, 0.3125, 0.6875);
        testSET.insert(A);
        testSET.insert(B);
        testSET.insert(C);
        testSET.insert(D);
        testSET.insert(E);
        testSET.insert(F);
        testSET.insert(G);
        testSET.insert(H);
        testSET.insert(I);
        testSET.insert(J);

        // test method: range(rect)
        for (Point2D point : testSET.range(rect)) {
            StdOut.println(point);
        }

        testSET.draw();
        rect.draw();
    }

    private static void subTest5() {
        KdTree testSET = new KdTree();
        Point2D A = new Point2D(0.59375, 0.15625);
        Point2D B = new Point2D(0.90625, 0.84375);
        Point2D C = new Point2D(0.28125, 0.71875);
        Point2D D = new Point2D(0.875, 0.53125);
        Point2D E = new Point2D(0.09375, 0.25);
        Point2D F = new Point2D(0.71875, 0.6875);
        Point2D G = new Point2D(0.40625, 0.0);
        Point2D H = new Point2D(0.75, 0.8125);
        Point2D I = new Point2D(0.34375, 0.4375);
        Point2D J = new Point2D(0.3125, 0.34375);
        Point2D K = new Point2D(0.25, 0.59375);
        Point2D L = new Point2D(0.0, 0.5);
        Point2D M = new Point2D(0.84375, 0.0625);
        Point2D N = new Point2D(0.125, 0.46875);
        Point2D O = new Point2D(0.78125, 0.625);
        Point2D P = new Point2D(0.96875, 0.40625);
        Point2D Q = new Point2D(0.4375, 0.28125);
        Point2D R = new Point2D(0.0625, 0.5625);
        Point2D S = new Point2D(0.15625, 0.1875);
        Point2D T = new Point2D(0.03125, 0.75);
        Point2D p = new Point2D(0.1875, 0.78125);

        testSET.insert(A);
        testSET.insert(B);
        testSET.insert(C);
        testSET.insert(D);
        testSET.insert(E);
        testSET.insert(F);
        testSET.insert(G);
        testSET.insert(H);
        testSET.insert(I);
        testSET.insert(J);
        testSET.insert(K);
        testSET.insert(L);
        testSET.insert(M);
        testSET.insert(N);
        testSET.insert(O);
        testSET.insert(P);
        testSET.insert(Q);
        testSET.insert(R);
        testSET.insert(S);
        testSET.insert(T);

        testSET.nearest(p);

        testSET.draw();
    }
}