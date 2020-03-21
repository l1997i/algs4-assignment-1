import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
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

    /**
     * construct an empty set of points
     */
    public KdTree() {
    }

    private Node root; // root of BST

    private class Node {
        private Point2D key; // sorted by key
        private int val; // associated data
        private Node left, right; // left and right subtrees
        private int size; // number of nodes in subtree

        public Node(Point2D key, int val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    private int comparePoint2D(Point2D a, Point2D b, int times) {
        double aCompare = 0.0;
        double bCompare = 0.0;
        if (times % 2 == 0) {
            aCompare = a.x();
            bCompare = b.x();
        } else {
            aCompare = a.y();
            bCompare = b.y();
        }

        if (aCompare < bCompare) {
            return -1;
        } else if (aCompare > bCompare) {
            return 1;
        } else {
            return 0;
        }
    }

    private void add(Point2D key, int val) {
        if (key == null)
            throw new IllegalArgumentException("calls add() with a null key");
        root = add(root, key, val, 0);
    }

    private Node add(Node x, Point2D key, int val, int times) {
        if (x == null)
            return new Node(key, val, 1);

        int cmp = comparePoint2D(key, x.key, times);
        if (cmp < 0)
            x.left = add(x.left, key, val, ++times);
        else if (cmp > 0)
            x.right = add(x.right, key, val, ++times);
        else
            x.val = val;

        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    /**
     * is the set empty?
     * 
     * @return true if is empty, otherwise false
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * number of points in the set
     * 
     * @return number of points in the set
     */
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        } else {
            return x.size;
        }
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
            add(p, size());
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
        return find(root, p, 0) != -1;
    }

    private int find(Node x, Point2D currentp, int times) {
        if (currentp == null)
            throw new IllegalArgumentException("calls find() with a null point");
        if (x == null)
            return -1;
        int cmp = comparePoint2D(currentp, x.key, times);
        if (cmp < 0)
            return find(x.left, currentp, ++times);
        else if (cmp > 0)
            return find(x.right, currentp, ++times);
        else
            return x.val;
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
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        root.key.draw();
        StdDraw.setPenRadius();
        draw(root, null, 0);

    }

    private void draw(Node x, Point2D parentPoint, int times) {
        // BUG: There will be overlap between split lines

        if (x == null) {
            return;
        }

        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.key.draw();
        StdDraw.setPenRadius();

        double minSize = 0.0;
        double maxSize = 1.0;
        Point2D currentPoint = x.key;

        if (parentPoint == null) { // red lines for vertical splits
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(currentPoint.x(), minSize, currentPoint.x(), maxSize);
            draw(root.left, root.key, 0);
            draw(root.right, root.key, 0);
            return;
        }

        int cmpFlag = comparePoint2D(currentPoint, parentPoint, times);

        if (times % 2 == 0) { // blue lines for horizontal splits
            double currenty = currentPoint.y();
            double parentx = parentPoint.x();
            if (cmpFlag == -1) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(minSize, currenty, parentx, currenty);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(maxSize, currenty, parentx, currenty);
            }
        } else { // red lines for vertical splits
            double currentx = currentPoint.x();
            double parenty = parentPoint.y();
            if (cmpFlag == -1) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(currentx, minSize, currentx, parenty);
            } else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(currentx, maxSize, currentx, parenty);
            }
        }

        int currentTimes = times + 1;
        draw(x.left, x.key, currentTimes);
        draw(x.right, x.key, currentTimes);
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

        Bag<Point2D> pointsInRange = new Bag<>();

        if (rect == null) { // corner cases
            throw new IllegalArgumentException();
        }

        double xmin = rect.xmin();
        double xmax = rect.xmax();
        double ymin = rect.ymin();
        double ymax = rect.ymax();
        Point2D minPoint = new Point2D(xmin, ymin);
        Point2D maxPoint = new Point2D(xmax, ymax);

        search(pointsInRange, root, minPoint, maxPoint, 0);
        return pointsInRange;

    }

    private void search(Bag<Point2D> pointsInRange, Node x, Point2D minPoint, Point2D maxPoint, int times) {

        if (x == null) {
            // All node in the subtrees is searched
            return;
        }

        Point2D currentPoint = x.key;
        addInRange(pointsInRange, currentPoint, minPoint, maxPoint);

        int isLess = comparePoint2D(minPoint, currentPoint, times);
        int isGreater = comparePoint2D(maxPoint, currentPoint, times);

        if (isLess == -1) {
            if (x.left == null) {
                return;
            }
            search(pointsInRange, x.left, minPoint, maxPoint, ++times);
        }

        if (isGreater == 1) {
            if (x.right == null) {
                return;
            }
            search(pointsInRange, x.right, minPoint, maxPoint, ++times);
        }
    }

    private void addInRange(Bag<Point2D> pointsInRange, Point2D currentPoint, Point2D minPoint, Point2D maxPoint) {
        if ((currentPoint.x() < maxPoint.x() && currentPoint.x() > minPoint.x())
                && (currentPoint.y() < maxPoint.y() && currentPoint.y() > minPoint.y())) {
            pointsInRange.add(currentPoint);
        }
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
        if (p == null) { // corner cases
            throw new IllegalArgumentException();
        }
        return nearest(p, root.key, root, Double.POSITIVE_INFINITY, 0);

    }

    private Point2D nearest(Point2D p, Point2D nearestp, Node currentNode, double d, int times) {

        if (currentNode == null) {
            return nearestp;
        }

        Point2D currentp = currentNode.key;
        double curNearestD = 0.0;
        double curD = p.distanceTo(currentp);
        Point2D newPoint;

        curNearestD = Math.min(Math.abs(p.x() - currentp.x()), Math.abs(p.y() - currentp.y()));

        int cmpFlag = comparePoint2D(p, currentp, times);

        if (curNearestD >= d) { // pruning, search only one branch
            if (curD < d) { // update the nearest point and distance
                nearestp = currentp;
                d = curD;
            }
            if (cmpFlag == -1) { // search left/bottom branch
                newPoint = nearest(p, nearestp, currentNode.left, curD, ++times);
            } else { // search right/top branch
                newPoint = nearest(p, nearestp, currentNode.right, curD, ++times);
            }
        } else { // search both branches
            if (curD < d) { // update the nearest point and distance
                nearestp = currentp;
                d = curD;
            }
            int currTimes = times;
            newPoint = nearest(p, nearestp, currentNode.left, curD, currTimes + 1);
            newPoint = nearest(p, newPoint, currentNode.right, curD, currTimes + 1);
        }

        return newPoint;
    }

    // unit testing of the methods
    public static void main(String[] args) {
        KdTree testSET = new KdTree();
        Point2D p1 = new Point2D(0.372, 0.497);
        Point2D p2 = new Point2D(0.564, 0.413);
        Point2D p3 = new Point2D(0.226, 0.577);
        Point2D p4 = new Point2D(0.144, 0.179);
        Point2D p5 = new Point2D(0.083, 0.51);
        Point2D p6 = new Point2D(0.32, 0.708);
        Point2D p7 = new Point2D(0.417, 0.362);
        Point2D p8 = new Point2D(0.862, 0.825);
        Point2D p9 = new Point2D(0.785, 0.725);
        Point2D p10 = new Point2D(0.499, 0.208);

        Point2D p = new Point2D(0.29, 0.306);

        RectHV rect = new RectHV(0.1, 0.1, 0.6, 0.6);
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

        StdOut.println("Is p3 contains: " + testSET.contains(p3));
        StdOut.println("Is p1 contains: " + testSET.contains(p1));
        StdOut.println("Is p contains: " + testSET.contains(p));
        StdOut.println("p's nearest point: " + testSET.nearest(p));

        // test method: range(rect)
        for (Point2D point : testSET.range(rect)) {
            StdOut.println(point);
        }

        StdOut.println("nearest to p" + testSET.nearest(p3));

        testSET.draw();
    }
}