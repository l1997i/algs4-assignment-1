import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int N;

    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[1];
        N = 0;

    }

    private class ReverseArrayIterator implements Iterator<Item> {

        private int i = N;
        private Item[] random_s;

        public ReverseArrayIterator() {
            random_s = (Item[]) new Object[N];

            for (int i = 0; i < N; i++) {
                random_s[i] = s[i];
            }
            StdRandom.shuffle(random_s);
        }

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "you cannot calls remove() method in the iterator because it is an unsupported operation.");
        }

        public Item next() {

            if (!hasNext()) {
                throw new java.util.NoSuchElementException(
                        "you cannot calls next() method in the iterator when there are no more items to return.");
            }

            return random_s[--i];
        }
    }

    // if array is full, create a new array of twice the size, and copy items
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int j = 0;
        for (int i = 0; i < N; i++) {
            while (j < N) {
                if (s[j] != null) {
                    copy[i] = s[j];
                    j++;
                    break;
                } else {
                    j++;
                    continue;
                }
            }
        }
        s = copy;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {

        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {

        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You cannot call enqueue() with a null argument.");
        }

        if (N == s.length) {
            resize(2 * s.length);

        }
        s[N++] = item;

    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("you cannot calls dequeue() when the deque is empty.");
        }

        int randIndex = StdRandom.uniform(0, N);
        Item tmp = s[randIndex];

        s[randIndex] = s[--N];
        s[N] = null;
        if (N > 0 && N == s.length / 4) {
            resize(s.length / 2);
        }
        return tmp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("you cannot calls sample() when the deque is empty.");
        }

        int randIndex = StdRandom.uniform(0, N);
        return s[randIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    public static void main(String[] args) {
        final RandomizedQueue<String> myTest = new RandomizedQueue<String>();
        myTest.enqueue("B");
        myTest.enqueue("A");
        myTest.enqueue("C");
        final String dqItem1 = myTest.dequeue();
        myTest.enqueue("D");
        myTest.enqueue("E");
        myTest.enqueue("Good");
        myTest.enqueue("F");
        final String dqItem2 = myTest.dequeue();
        final String sample1 = myTest.sample();
        for (final String s : myTest) {
            StdOut.println(s);
        }
        StdOut.println("Dequeue 1: " + dqItem1);
        StdOut.println("Dequeue 2: " + dqItem2);
        StdOut.println("Sample 1: " + sample1);
        StdOut.println(myTest.size());

    }

}