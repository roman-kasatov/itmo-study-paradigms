package queue;

import java.util.Objects;

/*
    Model: a[0]..a[n]
    Invariant: for i=0..(n-1): a[i] != null

    Let immutPref(n): for i=0..(n-1): a'[i] == a[i]
 */

public class ArrayQueue extends AbstractQueue {

    private Object[] arr = new Object[10];
    private int head = 0;


    // Pred: element != null
    // Post: n' == n + 1 && a[n'] == emlement && immutPref(n)
    public void enqueueImpl(Object element) {
        if (size == arr.length) {
            doubleArray();
        }
        arr[(size + head) % arr.length] = element;
    }

    private void doubleArray() {
        arr = toArray(size * 2);
        head = 0;
    }

    // Pred: n > 0
    // Post: R == a[0] && n' == n && immutablePref(n)
    protected Object elementImpl() {
        return arr[head];
    }

    // Pred: n > 0
    // Post: R == a[0] && n' == n - 1 && for i=0..(n-1) a'[i] == a[i + 1]
    protected Object dequeueImpl() {
        Object ret = arr[head];
        arr[head] = null;
        head = (head + 1) % arr.length;
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
    public void clearImpl() {
        arr = new Object[10];
        head = 0;
    }

    @Override
    public boolean contains(Object element) {
        for (int i = 0; i < size; i++) {
            if (arr[(i + head) % arr.length].equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeFirstOccurrenceImpl(Object element) {
        Object[] allElements = toArray();
        boolean foundFlag = false;
        for (int i = 0; i < size; i++) {
            if (!foundFlag && allElements[i].equals(element)) {
                foundFlag = true;
            } else {
                arr[i + (foundFlag ? -1 : 0)] = allElements[i];
            }
        }
        head = 0;
        return foundFlag;
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

    // Pred: true
    // Post: R.length == n, for i=0..(n-1) R[i] == a[i]
    public Object[] toArray() {
        return toArray(size);
    }

    private Object[] toArray(int lenght) {
        Object[] ret = new Object[lenght];
        int lengthOfBeginning = Math.min(size, arr.length - head),
                lengthOfEnding = size - lengthOfBeginning;
        System.arraycopy(arr, head, ret, 0, lengthOfBeginning);
        if (lengthOfEnding > 0) {
            System.arraycopy(arr, 0, ret, lengthOfBeginning, lengthOfEnding);
        }
        return ret;
    }
}
