import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int size;
    private final WeightedQuickUnionUF site;
    private final WeightedQuickUnionUF site1;
    private int openSiteNum = 0;
    private boolean state[]; // blocked = false; open = true;

    // create n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Int n cannot less than 0!");
        }
        size = n;
        int N = n * n + 2;
        state = new boolean[N];
        site = new WeightedQuickUnionUF(N);
        site1 = new WeightedQuickUnionUF(N);

        state[0] = true;
        state[N - 1] = true;
        for (int i = 0; i < size; i++) {
            site.union(0, i + 1);
            site1.union(0, i+1);
            site.union(N - 1, N - 1 - i - 1);
            // site.union(0, N-1);
            // open(1, i + 1);
            // open(n, i + 1);
        }

    }

    private int toIndex(int row, int col) {
        return size * (row - 1) + col;
    }

    // Opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row > size || col > size || row <= 0 || col <= 0) {
            throw new IllegalArgumentException("Col or row index is outside its prescribed range!");
        }

        int currentIndex = toIndex(row, col);

        // if the site (row, col) is already open
        if (state[currentIndex]) {
            return; // do nothing
        }

        state[currentIndex] = true;
        openSiteNum++;
        if (row > 1 && state[toIndex(row - 1, col)]) {
            site.union(toIndex(row - 1, col), currentIndex);
            site1.union(toIndex(row - 1, col), currentIndex);
        }

        if (row < size && state[toIndex(row + 1, col)]) {
            site.union(toIndex(row + 1, col), currentIndex);
            site1.union(toIndex(row + 1, col), currentIndex);
        }
        if (col > 1 && state[toIndex(row, col - 1)]) {
            site.union(toIndex(row, col - 1), currentIndex);
            site1.union(toIndex(row, col - 1), currentIndex);
        }
        if (col < size && state[toIndex(row, col + 1)]) {
            site.union(toIndex(row, col + 1), currentIndex);
            site1.union(toIndex(row, col + 1), currentIndex);
        }
        if (row == size)
            site.union(toIndex(row, col), currentIndex);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row > size || col > size || row <= 0 || col <= 0) {
            throw new IllegalArgumentException("Col or row index is outside its prescribed range!");
        }
        return state[toIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row > size || col > size || row <= 0 || col <= 0) {
            throw new IllegalArgumentException("Col or row index is outside its prescribed range!");
        }
        if (!isOpen(row, col)) {
            return false;
        }

        return site1.connected(toIndex(row, col), 0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSiteNum;
    }

    // does the system percolate?
    public boolean percolates() {
        if (size == 1) {
            return state[1];
        }

        return site.connected(0, size * size + 1);
    }

    // test client (opional)
    public static void main(String[] args) {
        int n = 5;
        Percolation myTest = new Percolation(n);
        myTest.open(1, 2);
        myTest.open(2, 2);
        myTest.open(3, 2);
        myTest.open(3, 3);
        myTest.open(4, 3);
        myTest.open(5, 1);
        System.out.println("isFull: "+myTest.isFull(5, 4));
        System.out.println("percolates: "+myTest.percolates());
        System.out.println(myTest.numberOfOpenSites());
        double x = (1.0 * myTest.numberOfOpenSites()) / (1.0 * n * n);
        System.out.println(x);
    }

}