import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code KdTree} class represents a set of points in the unit square (all
 * points have x- and y-coordinates between 0 and 1) using a kd-tree to support
 * efficient range search (find all of the points contained in a query
 * rectangle) and nearest-neighbor search (find a closest point to a query
 * point). kd-trees have numerous applications, ranging from classifying
 * astronomical objects to computer animation to speeding up neural networks to
 * mining data to image retrieval.
 * 
 * <p />
 * This class is the kd-tree implementation of programming assignment 5. A
 * kd-tree is a generalization of a BST to two-dimensional keys. The idea is to
 * build a BST with points in the nodes, using the x- and y-coordinates of the
 * points as keys in strictly alternating sequence. Kd-Trees
 * 
 * @author Li Li
 * @since Mar. 19, 2020
 */
public class KdTree {

    private xyBST<Point2D, Integer> points;

    /**
     * construct an empty set of points
     */
    public KdTree() {
        points = new xyBST<>();

    }

    /**
     * <p>
     * This is a private class from KdTree which is another BST implementation of
     * {@code Point2D} set.
     * </p>
     * 
     * <p>
     * Its insert operation is a bit different from normal {@code BST} At the root
     * use the x-coordinates (if the point to be inserted has a smaller x-coordinate
     * than the point at the root, go left; otherwise go right); then at the next
     * level, we use the y-coordinate (if the point to be inserted has a smaller
     * y-coordinate than the point at the root, go left; otherwise go right); then
     * at the next level the x-coordinate, and so forth.
     * </p>
     * 
     * @param <Key>   {@code Point2D} to be inserted
     * @param <Value> associated data
     * @author Li Li
     * @since Mar. 20, 2020
     */

    private class xyBST<Key extends Comparable<Key>, Value> extends BST<Point2D, Integer> {
        private Node root; // root of BST

        private class Node {
            private Key key; // sorted by key
            private Value val; // associated data
            private Node left, right; // left and right subtrees
            private int size; // number of nodes in subtree

            public Node(Key key, Value val, int size) {
                this.key = key;
                this.val = val;
                this.size = size;
            }
        }

        /**
         * Returns all keys in the symbol table in the given range, as an
         * {@code Iterable}.
         *
         * @param lo minimum endpoint
         * @param hi maximum endpoint
         * @return all keys in the symbol table between {@code lo} (inclusive) and
         *         {@code hi} (inclusive)
         * @throws IllegalArgumentException if either {@code lo} or {@code hi} is
         *                                  {@code null}
         */

        public Iterable<Point2D> keys() {
            return keys(min(), max());
        }

        public Iterable<Point2D> keys(Point2D lo, Point2D hi) {

            if (lo == null)
                throw new IllegalArgumentException("first argument to keys() is null");
            if (hi == null)
                throw new IllegalArgumentException("second argument to keys() is null");

            Queue<Point2D> queue = new Queue<Point2D>();
            keys(root, queue, lo, hi, 0);
            return queue;
        }

        private void keys(Node x, Queue<Point2D> queue, Point2D lo, Point2D hi, int times) {
            if (x == null)
                return;

            double loCompare = 0;
            double hiCompare = 0;
            double xCompare = 0;
            if (times % 2 == 0) {
                loCompare = lo.x();
                hiCompare = hi.x();
                xCompare = ((Point2D)x.key).x();
            } else {
                loCompare = lo.y();
                hiCompare = hi.y();
                xCompare = ((Point2D)x.key).y();
            }

            int cmplo = comparexy(loCompare, xCompare);
            int cmphi = comparexy(hiCompare, xCompare);
            if (cmplo < 0)
                keys(x.left, queue, lo, hi, ++times);
            if (cmplo <= 0 && cmphi >= 0)
                queue.enqueue((Point2D)x.key);
            if (cmphi > 0)
                keys(x.right, queue, lo, hi, ++times);
        }

        private int size(Node x) {
            if (x == null) {
                return 0;
            } else {
                return x.size;
            }
        }

        private int comparexy(double a, double b) {
            if (a < b) {
                return -1;
            } else if (a > b) {
                return 1;
            } else {
                return 0;
            }
        }

        public void add(Key key, Value val) {
            if (key == null)
                throw new IllegalArgumentException("calls add() with a null key");
            if (val == null) {
                // delete(key);
                return;
            }
            root = add(root, key, val, 0);
        }

        private Node add(Node x, Key key, Value val, int times) {
            if (x == null)
                return new Node(key, val, 1);

            double keyCompare = 0;
            double xCompare = 0;
            if (times % 2 == 0) {
                keyCompare = ((Point2D) key).x();
                xCompare = ((Point2D) x.key).x();
            } else {
                keyCompare = ((Point2D) key).y();
                xCompare = ((Point2D) x.key).y();
            }

            int cmp = comparexy(keyCompare, xCompare);
            if (cmp < 0)
                x.left = add(x.left, key, val, ++times);
            else if (cmp > 0)
                x.right = add(x.right, key, val, ++times);
            else
                x.val = val;

            x.size = 1 + size(x.left) + size(x.right);
            return x;
        }
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
     * @throws IllegalArgumentException if {@code p} is {@code null}
     */
    public void insert(Point2D p) {

        if (p == null) { // corner cases
            throw new IllegalArgumentException();
        }
        if (!contains(p)) {
            points.add(p, points.size());
        }

    }

    /**
     * does the set contain point p?
     * 
     * @param p point p is in the set or not?
     * @throws IllegalArgumentException if {@code p} is {@code null}
     * @return true if contains, otherwise false
     */
    public boolean contains(Point2D p) {
        if (p == null) { // corner cases
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    /**
     * <p>
     * draw all points to standard draw
     * </p>
     * <p>
     * A 2d-tree divides the unit square in a simple way: <b>all the points to the
     * left of the root go in the left subtree; all those to the right go in the
     * right subtree</b>; and so forth, recursively. {@code draw()} method draws all
     * of the points to standard draw in black and the subdivisions in red (for
     * vertical splits) and blue (for horizontal splits). This method may not be
     * efficient—it is primarily for debugging.
     * </p>
     */
    public void draw() {
        // TODO: draw()

        for (Point2D point : points.keys()) {
            point.draw();
        }
    }

    /**
     * <p>
     * all points that are inside the rectangle (or on the boundary)
     * </p>
     * 
     * <p>
     * To find all points contained in a given query rectangle, start at the root
     * and recursively search for points in both subtrees using the following
     * pruning rule: if the query rectangle does not intersect the rectangle
     * corresponding to a node, there is no need to explore that node (or its
     * subtrees). A subtree is searched only if it might contain a point contained
     * in the query rectangle.
     * </p>
     * 
     * @param rect the rectangle which is to be detected
     * @throws IllegalArgumentException if {@code rect} is {@code null}
     * @return an Iterable {@code Point2D} variable
     */
    public Iterable<Point2D> range(RectHV rect) {
        // TODO: range(RectHV rect)

        if (rect == null) { // corner cases
            throw new IllegalArgumentException();
        }
        return new Bag<>();

    }

    /**
     * 
     * <p>
     * a nearest neighbor in the set to point p; null if the set is empty.
     * </p>
     * 
     * <p>
     * To find a closest point to a given query point, start at the root and
     * recursively search in both subtrees using the following pruning rule: if the
     * closest point discovered so far is closer than the distance between the query
     * point and the rectangle corresponding to a node, there is no need to explore
     * that node (or its subtrees). That is, search a node only only if it might
     * contain a point that is closer than the best one found so far. The
     * effectiveness of the pruning rule depends on quickly finding a nearby point.
     * To do this, organize the recursive method so that when there are two possible
     * subtrees to go down, you always choose the subtree that is on the same side
     * of the splitting line as the query point as the first subtree to explore—the
     * closest point found while exploring the first subtree may enable pruning of
     * the second subtree.
     * </p>
     * 
     * @param p the point which is in the set
     * @throws IllegalArgumentException if {@code p} is {@code null}
     * @return null if the set is empty, otherwise a {@code Point2D} p
     */
    public Point2D nearest(Point2D p) {
        // TODO: nearest(Point2D p)
        if (p == null) { // corner cases
            throw new IllegalArgumentException();
        }
        return new Point2D(0, 0);

    }

    // unit testing of the methods
    public static void main(String[] args) {
        KdTree testSET = new KdTree();
        Point2D p1 = new Point2D(.7, .2);
        Point2D p2 = new Point2D(.5, .4);
        Point2D p3 = new Point2D(.2, .3);
        Point2D p4 = new Point2D(.4, .7);
        Point2D p5 = new Point2D(.9, .6);
        // Point2D p6 = new Point2D(-3.3, 5);
        // Point2D p7 = new Point2D(3, 6);
        // Point2D p8 = new Point2D(1.2, -4);
        // Point2D p9 = new Point2D(5.2, -2);
        // Point2D p10 = new Point2D(-4, 3.6);
        // Point2D p0 = new Point2D(-1, 3.6);
        // Point2D liar = new Point2D(-4, 3.6);
        RectHV rect = new RectHV(-4, -4, 4, 4);
        testSET.insert(p1);
        testSET.insert(p2);
        testSET.insert(p3);
        testSET.insert(p4);
        testSET.insert(p5);
        // testSET.insert(p6);
        // testSET.insert(p7);
        // testSET.insert(p8);
        // testSET.insert(p9);
        // testSET.insert(p10);

    }
}