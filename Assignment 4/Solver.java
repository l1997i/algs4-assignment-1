import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private SearchNode finalNode;
    private boolean solvable = false;

    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        SearchNode previous = null;
        int moves = 0;
        private final int priority;

        public SearchNode(Board initial, SearchNode previous) {
            this.previous = previous;
            this.board = initial;
            if (this.previous != null) {
                this.moves = previous.moves + 1;
            }
            priority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            // Override compareTo method using the Manhattan priority function
            if (this.priority < that.priority) {
                return -1;
            } else if (this.priority > that.priority) {
                return 1;
            }
            return 0;
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("The argument in the constructor is null!");
        }
        MinPQ<SearchNode> solutionA = new MinPQ<>();
        MinPQ<SearchNode> solutionB = new MinPQ<>();
        SearchNode initialSearchNodeA = new SearchNode(initial, null);
        SearchNode initialSearchNodeB = new SearchNode(initial.twin(), null);
        solutionA.insert(initialSearchNodeA);
        solutionB.insert(initialSearchNodeB);
        SearchNode currentNodeA = initialSearchNodeA;
        SearchNode currentNodeB = initialSearchNodeB;

        while (true) {

            currentNodeA = solutionA.delMin();
            currentNodeB = solutionB.delMin();

            if (currentNodeA.board.isGoal() || currentNodeB.board.isGoal()) {
                break;
            }

            insertNeighborNode(currentNodeA, solutionA);
            insertNeighborNode(currentNodeB, solutionB);
        }
        // } while (!currentNodeA.board.isGoal() && !currentNodeB.board.isGoal());
        if (currentNodeA.board.isGoal()) {
            finalNode = currentNodeA;
            solvable = true;
        } else {
            finalNode = currentNodeB;
            solvable = false;
        }
    }

    private void insertNeighborNode(SearchNode currentNode, MinPQ<SearchNode> solution) {
        for (Board neighborBoard : currentNode.board.neighbors()) {
            if (currentNode.previous != null && neighborBoard.equals(currentNode.previous.board)) {
                continue;
            }
            SearchNode neighborNode = new SearchNode(neighborBoard, currentNode);
            solution.insert(neighborNode);
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (!solvable) {
            return -1;
        }

        return finalNode.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }

        SearchNode[] iteratorSolution = new SearchNode[moves() + 1];
        int i = 1;
        iteratorSolution[0] = finalNode;
        SearchNode iteratorNode = finalNode;
        while (iteratorNode.previous != null) {
            iteratorNode = iteratorNode.previous;
            iteratorSolution[i] = iteratorNode;
            i++;
        }

        class IterableSolution implements Iterable<Board> {
            @Override
            public Iterator<Board> iterator() {
                return new BoardIterator();
            }

            class BoardIterator implements Iterator<Board> {
                int k = iteratorSolution.length - 1;

                @Override
                public boolean hasNext() {
                    return k >= 0;
                }

                @Override
                public Board next() {
                    if (!hasNext()) {
                        throw new java.util.NoSuchElementException(
                                "you cannot calls next() method in the iterator when there are no more items to return.");
                    }
                    return iteratorSolution[k--].board;
                }
            }
        }

        return new IterableSolution();

    }

    // test client
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}