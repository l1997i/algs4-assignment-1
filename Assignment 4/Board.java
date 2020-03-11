import java.util.Arrays;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

public class Board {
    final private int[][] tiles;
    private final int[][] goal;
    private int hash = 0;
    private final int size;

    // create a board from and n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(final int[][] tiles) {
        this.tiles = tiles.clone();
        size = tiles[0].length;
        goal = new int[size][size];

        final int[] temp = new int[size * size - 1];
        int k = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i][j] != 0) {
                    temp[k] = tiles[i][j];
                    k++;
                }
            }
        }
        k = 0;

        Arrays.sort(temp);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (k < size * size - 1) {
                    goal[i][j] = temp[k];
                }
                k++;
            }
        }
        goal[size - 1][size - 1] = 0;

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
        int distance = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (goal[i][j] != tiles[i][j] && tiles[i][j] != 0) {
                    distance++;
                }
            }
        }
        return distance;
    }

    // sum of Manhattan distance between tiles and goal
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (goal[i][j] != tiles[i][j] && tiles[i][j] != 0) {
                    final int[] goalIndex = find(goal, tiles[i][j]);
                    distance += (Math.abs(goalIndex[0] - i) + Math.abs(goalIndex[1] - j));
                }
            }
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(final Object y) {
        Board that = (Board)y;
        for (int i=0; i<size; i++){
            for (int j = 0; j<size; j++){
                if (that.tiles[i][j]!=tiles[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    public class IterableBoard implements Iterable<Board> {

        private int location = -2;
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
            private int[][][] tilesIterator;

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
                    for (int b = 0; b < size; b++) {
                        newTiles[a][b] = tiles[a][b];
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
        // TODO:
        return this;
    }

    public int[] find(final int[][] tiles, final int value) {
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

    public int locateNeighbors(final int i, final int j) {
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

    public int hashCode() {
        int h = hash;
        if (h != 0) {
            return h;
        }
        h = 3;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                h = 5 * h + tiles[i][j];
            }
        }
        hash = h;
        return h;
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
        StdOut.println("testBoard is Goal? " + testBoard.isGoal());
        StdOut.println("testBoardC is Goal? " + testBoardC.isGoal());
        StdOut.println("testBoard is equal to testTilesB? " + testBoard.equals(testBoardB));
        StdOut.println(testBoard.dimension());
        StdOut.println("testBoard hash: " + testBoard.hashCode());
        StdOut.println("testBoardB hash: " + testBoardB.hashCode());
        for (final Board neighbor : testBoard.neighbors()) {
            StdOut.print(neighbor);
        }

    }
}