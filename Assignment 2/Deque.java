import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

/**
 * Deque
 */
public class Deque<Item> implements Iterable<Item> {
    // private Item[] s;
    private int N;
    private Node first;
    private Node last;

    // construct an empty deque
    public Deque() {
        N = 0;
        first = null;
        last = null;

    }

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "you cannot calls next() method in the iterator because it is an unsupported operation.");
        }

        public Item next() {

            if (!hasNext()) {
                throw new java.util.NoSuchElementException(
                        "you cannot calls next() method in the iterator when there are no more items to return..");
            }

            final Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // is the deque empty
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return N;
    }

    // add the item to the front
    public void addFirst(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You cannot call addFirst() with a null argument.");
        }

        Node oldfirst;

        if (isEmpty()) {
            first = new Node();
            last = first;
        } else {
            oldfirst = first;
            first = new Node();
            first.next = oldfirst;
            oldfirst.previous = first;
        }

        first.item = item;
        first.previous = null;
        N++;
    }

    // add the item to the back
    public void addLast(final Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You cannot call addLast() with a null argument.");
        }

        final Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.previous = oldlast;
        if (isEmpty()) {
            first = last;
        } else {
            oldlast.next = last;
        }
        N++;

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("you cannot calls removeFirst() when the deque is empty.");
        }

        final Item item = first.item;

        if (N == 1) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.previous = null;
        }

        if (isEmpty()) {
            last = null;
        }
        N--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("you cannot calls removeLast() when the deque is empty.");
        }

        final Item item = last.item;
        if (N > 1) {
            last = last.previous;
            last.next = null;
            if (isEmpty()) {
                last = null;
            }
        } else {
            first = null;
            last = null;
        }

        N--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    public static void main(final String[] args) {
        final Deque<String> myTest = new Deque<String>();
        myTest.addFirst("B");
        myTest.addFirst("A");
        myTest.addLast("C1");
        myTest.removeLast();
        myTest.addLast("C");
        myTest.addFirst("Head");
        myTest.addLast("D");
        final String head = myTest.removeFirst();
        for (final String s : myTest) {
            StdOut.println(s);
        }
        StdOut.println(head);
        StdOut.println(myTest.size());
    }

}