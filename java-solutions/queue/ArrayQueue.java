package queue;

import java.util.Objects;

/*
    Model: a[0]..a[n]
    Invariant: for i=0..(n-1): a[i] != null

    Let immutPref(n): for i=0..(n-1): a'[i] == a[i]
 */

public class ArrayQueue {
    private Object[] arr;
    private int head;
    private int size;

    public ArrayQueue() {
        arr = new Object[10];
        head = 0;
        size = 0;
    }

    // Pred: element != null
    // Post: n' == n + 1 && a[n'] == element && immutPref(n)
    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        if (size == arr.length) {
            doubleArray();
        }
        arr[(size + head) % arr.length] = element;
        size++;
    }

    private void doubleArray() {
        Object[] newArray = new Object[size * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = arr[(i + head) % arr.length];
        }
        arr = newArray;
        head = 0;
    }

    // Pred: n > 0
    // Post: R == a[0] && n' == n && immutablePref(n)
    public Object element() {
        return arr[head];
    }

    // Pred: n > 0
    // Post: R == a[0] && n' == n - 1 && for i=0..(n-1) a'[i] == a[i + 1]
    public Object dequeue() {
        Object ret = arr[head];
        arr[head] = null;
        head = (head + 1) % arr.length;
        size--;
        return ret;
    }

    // Pred: true
    // Post: R == n && n' == n && immutPref(n)
    public int size() {
        return size;
    }

    // Pred: true
    // Post: R == (n == 0)

    public boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: n' == 0
    public void clear() {
        arr = new Object[10];
        head = 0;
        size = 0;
    }

    // Pred: element != null
    // Post n' == n + 1 && for i=0..(n - 1) a'[i + 1] == a[i] && a'[n] = element
    public void push(Object element) {
        Objects.requireNonNull(element);
        if (size == arr.length) {
            doubleArray();
        }
        arr[(arr.length + head - 1) % arr.length] = element;
        size++;
        head = (head + arr.length - 1) % arr.length;
    }

    // Pred: n > 0
    // Post: R = a[n - 1]
    public Object peek() {
        return arr[(head + size - 1) % arr.length];
    }

    // Pred: n > 0
    // Post:  R == a[n - 1] && immutPref(n - 1) && n' == n - 1
    public Object remove() {
        int pos = (head + size - 1) % arr.length;
        Object ret = arr[pos];
        arr[pos] = 0;
        size--;
        return ret;
    }

    // Pred: true
    // Post: R.length == n, for i=0..(n-1) R[i] == a[i]
    public Object[] toArray() {
        // :NOTE: объединить с doubleArray
        Object[] ret = new Object[size];
        for (int i = 0; i < size; i++) {
            ret[i] = arr[(i + head) % arr.length];
        }

        return ret;
    }
}
