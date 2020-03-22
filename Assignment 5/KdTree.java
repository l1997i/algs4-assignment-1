import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

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
        private float xmin = 0;
        private float xmax = 1;
        private float ymin = 0;
        private float ymax = 1;

        public Node(Point2D key, int val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    private int comparePoint2D(Point2D a, Point2D b, int times) {
        double aCompare = 0.0;
        double bCompare = 0.0;
        double optionCompare = 0.0;
        double eps = 1e-7;
        if (times % 2 == 0) {
            aCompare = a.x();
            bCompare = b.x();
            if (Math.abs(aCompare - bCompare) < eps) {
                optionCompare = a.y() - b.y();
            }
        } else {
            aCompare = a.y();
            bCompare = b.y();

            if (Math.abs(aCompare - bCompare) < eps) {
                optionCompare = a.x() - b.x();
            }
        }

        if (aCompare - bCompare < 0) {
            return -1;
        } else if (aCompare - bCompare > 0) {
            return 1;
        } else if (optionCompare < 0) {
            return -1;
        } else if (optionCompare > 0) {
            return 1;
        }
        return 0;
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
        if (cmp < 0) {
            x.left = add(x.left, key, val, ++times);
            updateSplitRange(x, times - 1, cmp);
        } else if (cmp > 0) {
            x.right = add(x.right, key, val, ++times);
            updateSplitRange(x, times - 1, cmp);
        } else
            x.val = val;

        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    /**
     * update the range which is created by line splits
     * 
     * @param x
     * @param times
     * @param flag  1 for right/top branch, -1 for left/bottom branch
     */
    private void updateSplitRange(Node parentNode, int times, int flag) {

        // if (parentNode != null && parentNode.left != null) { // initialize left child
        // node
        // parentNode.left.xmax = parentNode.xmax;
        // parentNode.left.xmin = parentNode.xmin;
        // parentNode.left.ymax = parentNode.ymax;
        // parentNode.left.ymin = parentNode.ymin;
        // }
        // if (parentNode != null && parentNode.right != null) { // initialize right
        // child node
        // parentNode.right.xmax = parentNode.xmax;
        // parentNode.right.xmin = parentNode.xmin;
        // parentNode.right.ymax = parentNode.ymax;
        // parentNode.right.ymin = parentNode.ymin;
        // }

        if (times % 2 == 0) { // vertical splits
            float parentx = (float) parentNode.key.x();
            if (flag < 0) {
                parentNode.left.xmax = parentx;

                parentNode.left.xmin = parentNode.xmin;
                parentNode.left.ymax = parentNode.ymax;
                parentNode.left.ymin = parentNode.ymin;
            } else if (flag > 0) {
                parentNode.right.xmin = parentx;

                parentNode.right.xmax = parentNode.xmax;
                parentNode.right.ymax = parentNode.ymax;
                parentNode.right.ymin = parentNode.ymin;
            }
        } else { // horizontal splits
            float parenty = (float) parentNode.key.y();
            if (flag < 0) {
                parentNode.left.ymax = parenty;

                parentNode.left.xmax = parentNode.xmax;
                parentNode.left.xmin = parentNode.xmin;
                parentNode.left.ymin = parentNode.ymin;
            } else if (flag > 0) {
                parentNode.right.ymin = parenty;

                parentNode.right.xmax = parentNode.xmax;
                parentNode.right.xmin = parentNode.xmin;
                parentNode.right.ymax = parentNode.ymax;
            }
        }
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
        if (x == null) {
            return;
        }
        StdDraw.setPenColor();
        StdDraw.setFont(StdDraw.getFont().deriveFont(8));
        StdDraw.text(x.key.x(), x.key.y() - 0.025, x.val + x.key.toString());

        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.key.draw();
        StdDraw.setPenRadius();
        StdDraw.setPenColor();

        Point2D currentPoint = x.key;

        if (parentPoint == null) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(currentPoint.x(), x.ymin, currentPoint.x(), x.ymax);
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
                StdDraw.line(x.xmin, currenty, parentx, currenty);
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(x.xmax, currenty, parentx, currenty);
            }
        } else { // red lines for vertical splits
            double currentx = currentPoint.x();
            double parenty = parentPoint.y();
            if (cmpFlag == -1) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(currentx, x.ymin, currentx, parenty);
            } else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(currentx, x.ymax, currentx, parenty);
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
        // all points that are inside the rectangle (or on the boundary)
        if (rect == null)
            throw new IllegalArgumentException("RectHV rect is not illegal!");
        if (root != null)
            return range(root, rect);
        else
            return new ArrayList<Point2D>();
    }

    private ArrayList<Point2D> range(Node x, RectHV rect) {

        RectHV xRect = new RectHV(x.xmin, x.ymin, x.xmax, x.ymax);

        ArrayList<Point2D> points = new ArrayList<Point2D>();
        if (xRect.intersects(rect)) {
            if (rect.contains(x.key))
                points.add(x.key);
            if (x.left != null)
                points.addAll(range(x.left, rect));
            if (x.right != null)
                points.addAll(range(x.right, rect));
        }
        return points;
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
     * subtrees to go down, <b>you always choose the subtree that is on the same
     * side</b> of the splitting line as the query point as the first subtree to
     * explore—the closest point found while exploring the first subtree may enable
     * pruning of the second subtree.
     * </p>
     * 
     * @param p the point which is in the set
     * @throws IllegalArgumentException if {@code p} is {@code null}
     * @return null if the set is empty, otherwise a {@code Point2D} p
     */

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("Point2D p is not illegal!");
        if (root != null)
            return nearest(root, p, root.key, 0);
        return null;
    }

    private Point2D nearest(Node x, Point2D p, Point2D currNearPoint, int times) {

        if (x.key.equals(p))
            return x.key;
        double currMinDistance = currNearPoint.distanceTo(p);
        RectHV xRect = new RectHV(x.xmin, x.ymin, x.xmax, x.ymax);
        if (Double.compare(xRect.distanceTo(p), currMinDistance) >= 0)
            return currNearPoint;
        else {
            double distance = x.key.distanceTo(p);
            if (Double.compare(distance, currMinDistance) == -1) {
                currNearPoint = x.key;
                currMinDistance = distance;
            }
            RectHV xLeftRect = null;
            RectHV xRightRect = null;
            if (x.left != null) {
                xLeftRect = new RectHV(x.left.xmin, x.left.ymin, x.left.xmax, x.left.ymax);
            }
            if (x.right != null) {
                xRightRect = new RectHV(x.right.xmin, x.right.ymin, x.right.xmax, x.right.ymax);
            }

            float side = comparePoint2D(p, x.key, times);

            if (xLeftRect != null && side == -1) {
                if (x.left != null && Double.compare(xLeftRect.distanceTo(p), currNearPoint.distanceTo(p)) < 0) {
                    currNearPoint = nearest(x.left, p, currNearPoint, times + 1);
                }

                if (x.right != null && Double.compare(xRightRect.distanceTo(p), currNearPoint.distanceTo(p)) < 0)
                    currNearPoint = nearest(x.right, p, currNearPoint, times + 1);
            } else {
                if (x.right != null && Double.compare(xRightRect.distanceTo(p), currNearPoint.distanceTo(p)) < 0) {
                    currNearPoint = nearest(x.right, p, currNearPoint, times + 1);
                }
                if (x.left != null && Double.compare(xLeftRect.distanceTo(p), currNearPoint.distanceTo(p)) < 0)
                    currNearPoint = nearest(x.left, p, currNearPoint, times + 1);
            }

        }
        return currNearPoint;
    }

    // unit testing of the methods
    public static void main(String[] args) {

    }

}