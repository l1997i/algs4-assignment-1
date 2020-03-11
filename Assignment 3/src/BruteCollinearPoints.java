import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> line4pointsSegments = new ArrayList<>();
    private int n;

    /**
     * finds all line segments containing 4 points
     *
     * @param points Points set
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("The argument to the constructor cannot be null");
        }
        n = 0;
        Point[] pts = new Point[points.length];
        for (int p = 0; p < points.length; p++) {
            if (points[p] == null) {
                throw new IllegalArgumentException("The argument to the constructor cannot be null");
            }
            for (int j = p + 1; j < points.length; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException("The argument to the constructor cannot be null");
                }
                if (points[p].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("The argument to the constructor contains a repeated point!");
                }
            }
            pts[p] = points[p];
        }
        Arrays.sort(pts);

        double eps = 1e-7;  // define a very small value to compare two doubles if they are equal
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double k1 = pts[i].slopeTo(pts[j]);
                for (int k = j + 1; k < pts.length; k++) {
                    double k2 = pts[i].slopeTo(pts[k]);
                    if (Math.abs(k1 - k2) > eps) {
                        continue;
                    }
                    for (int l = k + 1; l < pts.length; l++) {
                        double k3 = pts[i].slopeTo(pts[l]);
                        if (Math.abs(k1 - k3) > eps) {
                            continue;
                        }
                        line4pointsSegments.add(new LineSegment(pts[i], pts[l]));
                        n++;
                    }
                }
            }
        }

    }

    /**
     * the number of line segments
     *
     * @return the number of line segments
     */
    public int numberOfSegments() {
        return n;
    }

    /**
     * the line segments
     *
     * @return the set of all of line segments containing 4 points exactly once
     */
    public LineSegment[] segments() {

        LineSegment[] myResult = new LineSegment[line4pointsSegments.size()];
        return line4pointsSegments.toArray(myResult);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);

        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
