import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private MinPQ<SearchNode> solutionMinPQ = new MinPQ<>();
    private MinPQ<SearchNode> finalSolution = new MinPQ<>();
    private SearchNode finalNode;

    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        SearchNode previous = null;
        int moves = 0;

        public SearchNode(Board initial) {
            board = initial;
        }

        @Override
        public int compareTo(SearchNode that) {
            // Override compareTo method using the Manhattan priority function
            if (this.board.manhattan()+this.moves < that.board.manhattan()+that.moves) {
                return -1;
            } else if (this.board.manhattan()+this.moves > that.board.manhattan()+that.moves) {
                return 1;
            } else {    // priority is equal
                if (this.moves < that.moves)
                    return -1;
                else if (this.moves>that.moves)
                    return 1;    
            }
            return 0;
        }
    }

    // find a solution to the initial board (using A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("The argument in the constructor is null!");
        }
        SearchNode initialSearchNode = new SearchNode(initial);
        solutionMinPQ.insert(initialSearchNode);
        // solutionMinPQ.delMin();
        // for (Board board : initialSearchNode.board.neighbors()) {
        // SearchNode child = new SearchNode(board);
        // child.previous = initialSearchNode;
        // child.moves = 1;
        // }
        SearchNode currentNode = initialSearchNode;
        do {
            currentNode = solutionMinPQ.delMin();
            for (Board neighborBoard : currentNode.board.neighbors()) {
                SearchNode neighborNode = new SearchNode(neighborBoard);
                neighborNode.previous = currentNode;
                neighborNode.moves = currentNode.moves + 1;
                solutionMinPQ.insert(neighborNode);
            }
        } while (!currentNode.board.isGoal());
        finalNode = currentNode;

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        // TODO:
        return true;
    }

    // min number of moves to solve initial board
    public int moves() {
        return finalNode.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        finalSolution.insert(finalNode);
        SearchNode iteratorNode = finalNode;
        while (iteratorNode.previous!=null){
            iteratorNode = iteratorNode.previous;
            finalSolution.insert(iteratorNode);
        }
        return new IterableSolution();

    }

    public class IterableSolution implements Iterable<Board> {

        @Override
        public Iterator<Board> iterator() {
            return new BoardIterator();
        }

        private class BoardIterator implements Iterator<Board> {

            @Override
            public boolean hasNext() {
                return !finalSolution.isEmpty();
            }

            @Override
            public Board next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException(
                            "you cannot calls next() method in the iterator when there are no more items to return.");
                }
                return finalSolution.delMin().board;
            }
        }
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