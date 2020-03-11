import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    // private final Percolation site; 不要在这里定义，不然内存会变大
    // Hints: If you failed this test, it may be because you are holding an
    // unnecessary reference to a (large) Percolation object in an instance
    // variable. Would storing it in a local variable suffice?
    private final double[] x;
    private final int T;
    private final double mean;
    private final double stddev;

    // perform independent trails on an n-by-n grid
    public PercolationStats(int n, int trials) {
        // Percolation site;
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Int n or trials cannot less than 0!");
        }
        T = trials;
        x = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation site = new Percolation(n);

            while (!site.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                site.open(row, col);
            }

            x[i] = (1.0 * site.numberOfOpenSites()) / (1.0 * n * n);
        }

        mean = StdStats.mean(x);
        stddev = StdStats.stddev(x);

    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return mean - 1.96 * stddev / Math.sqrt(T);
    }

    public double confidenceHi() {
        return mean + 1.96 * stddev / Math.sqrt(T);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats siteTest = new PercolationStats(n, T);
        System.out.println("mean\t\t\t= " + siteTest.mean());
        System.out.println("stddev\t\t\t= " + siteTest.stddev());
        System.out.println(
                "95% confidence interval\t = [" + siteTest.confidenceLo() + ", " + siteTest.confidenceHi() + "]");
    }
}