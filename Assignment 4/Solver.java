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

        public SearchNode(Board initial) {
            board = initial;
        }

        @Override
        public int compareTo(SearchNode that) {
            // Override compareTo method using the Manhattan priority function
            if (this.board.manhattan() + this.moves < that.board.manhattan() + that.moves) {
                return -1;
            } else if (this.board.manhattan() + this.moves > that.board.manhattan() + that.moves) {
                return 1;
            } else { // priority is equal
                if (this.moves < that.moves)
                    return -1;
                else if (this.moves > that.moves)
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
        SearchNode initialSearchNodeA = new SearchNode(initial);
        SearchNode initialSearchNodeB = new SearchNode(initial.twin());
        solutionA.insert(initialSearchNodeA);
        solutionB.insert(initialSearchNodeB);
        SearchNode currentNodeA = initialSearchNodeA;
        SearchNode currentNodeB = initialSearchNodeB;
        do {
            currentNodeA = solutionA.delMin();
            currentNodeB = solutionB.delMin();
            Iterator<Board> searchA = currentNodeA.board.neighbors().iterator();
            Iterator<Board> searchB = currentNodeB.board.neighbors().iterator();
            while (searchA.hasNext() || searchB.hasNext()) {

                if (searchA.hasNext()) {
                    Board nextA = searchA.next();
                    if (!(currentNodeA.previous != null && nextA.equals(currentNodeA.previous.board))) {
                        SearchNode neighborNodeA = new SearchNode(nextA);
                        neighborNodeA.previous = currentNodeA;
                        neighborNodeA.moves = currentNodeA.moves + 1;
                        solutionA.insert(neighborNodeA);
                    }
                }

                if (searchB.hasNext()) {
                    Board nextB = searchB.next();
                    if (!(currentNodeB.previous != null && nextB.equals(currentNodeB.previous.board))) {
                        SearchNode neighborNodeB = new SearchNode(nextB);
                        neighborNodeB.previous = currentNodeB;
                        neighborNodeB.moves = currentNodeB.moves + 1;
                        solutionB.insert(neighborNodeB);
                    }
                }
            }
        } while (!currentNodeA.board.isGoal() && !currentNodeB.board.isGoal());
        if (currentNodeA.board.isGoal()) {
            finalNode = currentNodeA;
            solvable = true;
        } else {
            finalNode = currentNodeB;
            solvable = false;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (!solvable){
            return -1;
        }

        return finalNode.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!solvable){
            return null;
        }

        SearchNode[] iteratorSolution = new SearchNode[moves()+1];
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
                int k = iteratorSolution.length-1;

                @Override
                public boolean hasNext() {
                    return k>=0;
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