package byow.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private class Node<T> {
        T val;
        double priority;

        Node(T val, double priority) {
            this.val = val;
            this.priority = priority;
        }

    }

    private ArrayList<Node<T>> heap;
    private HashMap<T, Integer> index;
    private int size;

    public ArrayHeapMinPQ() {
        heap = new ArrayList();
        index = new HashMap();
    }

    private int parentLocation(int i) {
        return (i - 1) / 2;
    }

    private int leftChild(int i) {
        int child = (i * 2) + 1;
        if (child >= size()) {
            return i;
        }
        return child;
    }

    private int rightChild(int i) {
        int child = (i * 2) + 2;
        if (child >= size()) {
            return i;
        }
        return child;
    }

    private void swim(int i) {
        if (heap.get(parentLocation(i)).priority > heap.get(i).priority) {
            swap(parentLocation(i), i);
            swim(parentLocation(i));
        }
    }

    private void sink(int i) {
        Node<T> right = heap.get(rightChild(i));
        Node<T> left = heap.get(leftChild(i));
        if (right.priority < left.priority) {
            if (right.priority < heap.get(i).priority) {
                swap(rightChild(i), i);
                sink(rightChild(i));
            }
        } else {
            if (left.priority < heap.get(i).priority) {
                swap(leftChild(i), i);
                sink(leftChild(i));
            }
        }
    }

    private void swap(int index1, int index2) {
        Node<T> n1 = heap.get(index1);
        Node<T> n2 = heap.get(index2);
        index.put(n1.val, index2);
        index.put(n2.val, index1);
        heap.set(index1, n2);
        heap.set(index2, n1);
    }

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentExceptionb if item is already present. */
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("" + item + " is already present");
        }
        index.put(item, size);
        Node newNode = new Node(item, priority);
        heap.add(newNode);
        size++;
        swim(size - 1);
    }

    /* Returns true if the PQ contains the given item. */
    public boolean contains(T item) {
        return index.containsKey(item);
    }

    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T getSmallest() {
        if (size() < 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        return heap.get(0).val;
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    public T removeSmallest() {
        if (size() <= 0) {
            throw new NoSuchElementException("PQ is empty");
        }
        size--;
        Node<T> n = heap.get(0);
        swap(0, size);
        heap.remove(size);
        index.remove(n.val);
        if (size > 0) {
            sink(0);
        }
        return n.val;
    }

    /* Returns the number of items in the PQ. */
    public int size() {
        return size;
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException("Element " + item + " is not found.");
        }
        int i = index.get(item);
        Node<T> node = heap.get(i);
        node.priority = priority;
        sink(i);
        swim(i);
    }

}
