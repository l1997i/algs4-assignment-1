import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

public class Board {
    final private int[][] tiles;
    private final int size;
    private int d_hamming = -1;
    private int d_manhattan = -1;

    // create a board from and n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(final int[][] tiles) {
        this.tiles = tiles.clone();
        size = tiles[0].length;
    }

    // string representation of this board
    public String toString() {
        String boardString = "" + size + "\n";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardString += (tiles[i][j] + "\t");
            }
            boardString += "\n";
        }
        return boardString;
    }

    // board dimension
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        if (d_hamming != -1) {
            return d_hamming;
        }

        int distance = 0;
        int k = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (k != tiles[i][j] && tiles[i][j] != 0) {
                    distance++;
                }
                k++;
            }
        }
        d_hamming = distance;
        return d_hamming;
    }

    // sum of Manhattan distance between tiles and goal
    public int manhattan() {
        if (d_manhattan != -1) {
            return d_manhattan;
        }
        int distance = 0;
        int k = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (k != tiles[i][j]) {
                    int[] index = find(tiles, k);
                    distance += (Math.abs(index[0] - i) + Math.abs(index[1] - j));
                }
                k++;
                if (k == size * size) {
                    break;
                }
            }
        }
        d_manhattan = distance;
        return d_manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (d_hamming != -1) {
            return d_hamming == 0;
        }
        if (d_manhattan != -1) {
            return d_manhattan == 0;
        }

        int k = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (k != tiles[i][j] && tiles[i][j] != 0) {
                    return false;
                }
                k++;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(final Object y) {
        if (!(y instanceof Board)) {
            return false;
        }

        Board that = (Board) y;

        if (that.tiles.length != this.tiles.length || that.tiles[0].length != this.tiles[0].length) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (that.tiles[i][j] != tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private class IterableBoard implements Iterable<Board> {

        private final int location;
        private final int[] zeroIndex;

        public IterableBoard(final int[] index) {
            zeroIndex = index.clone();
            location = locateNeighbors(index[0], index[1]);
        }

        @Override
        public Iterator<Board> iterator() {
            return new BoardIterator(location);
        }

        private class BoardIterator implements Iterator<Board> {
            private int index = 0;
            private final int[][][] tilesIterator;

            public BoardIterator(final int location) {
                final int i = zeroIndex[0];
                final int j = zeroIndex[1];
                switch (location) {
                    case 1:
                        tilesIterator = new int[2][size][size];
                        tilesIterator[0] = moveTo(1, 0);
                        tilesIterator[1] = moveTo(0, 1);
                        break;
                    case 3:
                        tilesIterator = new int[2][size][size];
                        tilesIterator[0] = moveTo(0, size - 2);
                        tilesIterator[1] = moveTo(1, size - 1);
                        break;
                    case 7:
                        tilesIterator = new int[2][size][size];
                        tilesIterator[0] = moveTo(size - 2, 0);
                        tilesIterator[1] = moveTo(size - 1, 1);
                        break;
                    case 9:
                        tilesIterator = new int[2][size][size];
                        tilesIterator[0] = moveTo(size - 2, size - 1);
                        tilesIterator[1] = moveTo(size - 1, size - 2);
                        break;
                    case 2:
                        tilesIterator = new int[3][size][size];
                        tilesIterator[0] = moveTo(0, j + 1);
                        tilesIterator[1] = moveTo(0, j - 1);
                        tilesIterator[2] = moveTo(1, j);
                        break;
                    case 4:
                        tilesIterator = new int[3][size][size];
                        tilesIterator[0] = moveTo(i - 1, 0);
                        tilesIterator[1] = moveTo(i + 1, 0);
                        tilesIterator[2] = moveTo(i, 1);
                        break;
                    case 6:
                        tilesIterator = new int[3][size][size];
                        tilesIterator[0] = moveTo(i - 1, size - 1);
                        tilesIterator[1] = moveTo(i + 1, size - 1);
                        tilesIterator[2] = moveTo(i, size - 2);
                        break;
                    case 8:
                        tilesIterator = new int[3][size][size];
                        tilesIterator[0] = moveTo(size - 1, j - 1);
                        tilesIterator[1] = moveTo(size - 1, j + 1);
                        tilesIterator[2] = moveTo(size - 2, j);
                        break;
                    default:
                        tilesIterator = new int[4][size][size];
                        tilesIterator[0] = moveTo(i - 1, j);
                        tilesIterator[1] = moveTo(i + 1, j);
                        tilesIterator[2] = moveTo(i, j - 1);
                        tilesIterator[3] = moveTo(i, j + 1);
                        break;
                }
            }

            private int[][] moveTo(final int i, final int j) {
                final int[][] newTiles = new int[size][size];
                for (int a = 0; a < size; a++) {
                    int[] newTiles_a = newTiles[a];
                    int[] tiles_a = tiles[a];
                    for (int b = 0; b < size; b++) {
                        newTiles_a[b] = tiles_a[b];
                    }
                }
                newTiles[zeroIndex[0]][zeroIndex[1]] = newTiles[i][j];
                newTiles[i][j] = 0;
                return newTiles;
            }

            @Override
            public boolean hasNext() {
                return index < tilesIterator.length;
            }

            @Override
            public Board next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException(
                            "you cannot calls next() method in the iterator when there are no more items to return.");
                }
                final Board neighorBoard = new Board(tilesIterator[index++]);
                return neighorBoard;
            }
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        final int[] zeroIndex = find(tiles, 0);
        final IterableBoard a = new IterableBoard(zeroIndex);
        return a;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        final int[][] newTiles = new int[size][size];
        for (int a = 0; a < size; a++) {
            int[] newTiles_a = newTiles[a];
            int[] tiles_a = tiles[a];
            for (int b = 0; b < size; b++) {
                newTiles_a[b] = tiles_a[b];
            }
        }
        int index_i = 0;
        int index_j = 0;

        OutterLoop: for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (newTiles[i][j] != 0 && newTiles[i + 1][j] != 0) {
                    index_i = i;
                    index_j = j;
                    break OutterLoop;
                }
            }
        }
        int temp = newTiles[index_i][index_j];
        newTiles[index_i][index_j] = newTiles[index_i + 1][index_j];
        newTiles[index_i + 1][index_j] = temp;
        return new Board(newTiles);
    }

    private int[] find(final int[][] tiles, final int value) {
        final int[] index = new int[2];
        index[0] = -1;
        index[1] = -1;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == value) {
                    index[0] = i;
                    index[1] = j;
                    return index; // already find
                }
            }
        }
        return index; // not find
    }

    private int locateNeighbors(final int i, final int j) {
        final boolean isHead = i == 0;
        final boolean isFoot = i == size - 1;
        final boolean isLeft = j == 0;
        final boolean isRight = j == size - 1;

        if (isHead) {
            if (isLeft)
                return 1;
            if (isRight)
                return 3;
            return 2;
        }
        if (isFoot) {
            if (isLeft)
                return 7;
            if (isRight)
                return 9;
            return 8;
        }
        if (isLeft)
            return 4;
        if (isRight)
            return 6;
        return 5;
    }

    // unit testing
    public static void main(final String[] args) {
        final int[][] testTiles = { { 1, 3, 9, 4 }, { 11, 2, 5, 10 }, { 7, 8, 6, 12 }, { 15, 13, 14, 0 } };
        final int[][] testTilesB = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        final int[][] testTilesC = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        final Board testBoard = new Board(testTiles);
        final Board testBoardB = new Board(testTilesB);
        final Board testBoardC = new Board(testTilesC);
        StdOut.print(testBoard.toString());
        StdOut.println("Hamming: " + testBoard.hamming());
        StdOut.println("Manhattan: " + testBoard.manhattan());
        StdOut.println("Hamming B: " + testBoardB.hamming());
        StdOut.println("Manhattan B: " + testBoardB.manhattan());
        StdOut.println("testBoard is Goal? " + testBoard.isGoal());
        StdOut.println("testBoardC is Goal? " + testBoardC.isGoal());
        StdOut.println("testBoard is equal to testTilesB? " + testBoard.equals(testBoardB));
        StdOut.println(testBoard.dimension());
        for (final Board neighbor : testBoard.neighbors()) {
            StdOut.print(neighbor);
        }
        StdOut.println("testBoard twin: \n" + testBoard.twin());

    }
}