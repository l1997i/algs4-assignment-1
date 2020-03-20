import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code PointSET} class represents a set of points in the unit square (all
 * points have x- and y-coordinates between 0 and 1) using a {@code SET} to
 * support efficient range search (find all of the points contained in a query
 * rectangle) and nearest-neighbor search (find a closest point to a query
 * point).
 * <p />
 * This class is a brute-force implementation of programming assignment 5:
 * Kd-Trees
 *
 * @author Li Li
 * @since Mar. 19, 2020
 *
 */
public class PointSET {

    private SET<Point2D> points;

    /**
     * construct an empty set of points
     */
    public PointSET() {
        points = new SET<>();
    }

    /**
     * is the set empty?
     * 
     * @return true if is empty, otherwise false
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * number of points in the set
     * 
     * @return number of points in the set
     */
    public int size() {
        return points.size();
    }

    /**
     * add the point to the set (if it is not already in the set)
     * 
     * @param p the point which to be inserted
     */
    public void insert(Point2D p) {
        if (p == null) { // corner cases
            throw new IllegalArgumentException();
        }
        if (!contains(p)) {
            points.add(p);
        }

    }

    /**
     * does the set contain point p?
     * 
     * @param p point p is in the set or not?
     * @return true if contains, otherwise false
     */
    public boolean contains(Point2D p) {
        if (p == null) { // corner cases
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     * 
     * @param rect the rectangle which is to be detected
     * @return an Iterable {@code Point2D} variable
     */
    public Iterable<Point2D> range(RectHV rect) {

        Bag<Point2D> inRange = new Bag<>();

        if (rect == null) { // corner cases
            throw new IllegalArgumentException();
        }
        double xmin = rect.xmin();
        double xmax = rect.xmax();
        double ymin = rect.ymin();
        double ymax = rect.ymax();

        for (Point2D point : points) {
            double px = point.x();
            double py = point.y();

            if (((px - xmin) * (px - xmax) < 0.0) && ((py - ymin) * (py - ymax) < 0.0)) {
                inRange.add(point);
            }
        }

        return inRange;

    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     * 
     * @param p the point which is in the set
     * @return null if the set is empty, otherwise a {@code Point2D} p
     */
    public Point2D nearest(Point2D p) {

        if (p == null) { // corner cases
            throw new IllegalArgumentException();
        }

        if (isEmpty()) { // null if the set is empty
            return null;
        }

        Point2D nearestP = points.min();
        double dmin = p.distanceTo(nearestP);
        for (Point2D point : points) {
            double d = p.distanceTo(point);
            if (d < dmin) {
                nearestP = point;
                dmin = d;
            }
        }

        return nearestP;

    }

    // unit testing of the methods
    public static void main(String[] args) {

        PointSET testSET = new PointSET();
        Point2D p1 = new Point2D(1,3);
        Point2D p2 = new Point2D(-3,3);
        Point2D p3 = new Point2D(2,-5);
        Point2D p4 = new Point2D(2.3,2);
        Point2D p5 = new Point2D(-8,4);
        Point2D p6 = new Point2D(-3.3,5);
        Point2D p7 = new Point2D(3,6);
        Point2D p8 = new Point2D(1.2,-4);
        Point2D p9 = new Point2D(5.2,-2);
        Point2D p10 = new Point2D(-4,3.6);
        Point2D p0 = new Point2D(-1,3.6);
        Point2D liar = new Point2D(-4,3.6);
        RectHV rect = new RectHV(-4, -4, 4, 4);
        testSET.insert(p1);
        testSET.insert(p2);
        testSET.insert(p3);
        testSET.insert(p4);
        testSET.insert(p5);
        testSET.insert(p6);
        testSET.insert(p7);
        testSET.insert(p8);
        testSET.insert(p9);
        testSET.insert(p10);
        StdOut.println("p1 nearest: "+testSET.nearest(p1));
        StdOut.println("Is p0 contains: "+testSET.contains(p0));
        StdOut.println("Is liar contains: "+testSET.contains(liar));
        for (Point2D point : testSET.range(rect)){
            StdOut.print(point+"\t");
        }
        

    }
}