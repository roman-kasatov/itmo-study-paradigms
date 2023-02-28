package queue;

import java.util.Objects;

/*
    Model: a[0]..a[n]
    Invariant: for i=0..(n-1): a[i] != null

    Let immutPref(n): for i=0..(n-1): a'[i] == a[i]
 */

public class ArrayQueueADT {
    private Object[] arr = new Object[10];
    private int head = 0;
    private int size = 0;


    // Pred: element != null
    // Post: n' == n + 1 && a[n'] == element && immutPref(n)
    public static void enqueue(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        if (queue.size == queue.arr.length) {
            doubleArray(queue);
        }
        queue.arr[(queue.size + queue.head) % queue.arr.length] = element;
        queue.size++;
    }

    private static void doubleArray(ArrayQueueADT queue) {
        queue.arr = toArray(queue, queue.size * 2);
        queue.head = 0;
    }

    // Pred: n > 0
    // Post: R == a[0] && n' == n && immutablePref(n)
    public static Object element(ArrayQueueADT queue) {
        return queue.arr[queue.head];
    }

    // Pred: n > 0
    // Post: R == a[0] && n' == n - 1 && for i=0..(n-1) a'[i] == a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        Object ret = queue.arr[queue.head];
        queue.arr[queue.head] = null;
        queue.head = (queue.head + 1) % queue.arr.length;
        queue.size--;
        return ret;
    }

    // Pred: true
    // Post: R == n && n' == n && immutPref(n)
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pred: true
    // Post: R == (n == 0)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pred: true
    // Post: n' == 0
    public static void clear(ArrayQueueADT queue) {
        queue.arr = new Object[10];
        queue.head = 0;
        queue.size = 0;
    }

    // Pred: element != null
    // Post n' == n + 1 && for i=0..(n - 1) a'[i + 1] == a[i] && a'[n] = element
    public static void push(ArrayQueueADT queue, Object element) {
        Objects.requireNonNull(element);
        if (queue.size == queue.arr.length) {
            doubleArray(queue);
        }
        queue.arr[(queue.arr.length + queue.head - 1) % queue.arr.length] = element;
        queue.size++;
        queue.head = (queue.head + queue.arr.length - 1) % queue.arr.length;
    }

    // Pred: n > 0
    // Post: R = a[n - 1]
    public static Object peek(ArrayQueueADT queue) {
        return queue.arr[(queue.head + queue.size - 1) % queue.arr.length];
    }

    // Pred: n > 0
    // Post:  R == a[n - 1] && immutPref(n - 1) && n' == n - 1
    public static Object remove(ArrayQueueADT queue) {
        int pos = (queue.head + queue.size - 1) % queue.arr.length;
        Object ret = queue.arr[pos];
        queue.arr[pos] = 0;
        queue.size--;
        return ret;
    }

    // Pred: true
    // Post: R.length == n, for i=0..(n-1) R[i] == a[i]
    public static Object[] toArray(ArrayQueueADT queue) {
        return toArray(queue, queue.size);
    }

    private static Object[] toArray(ArrayQueueADT queue, int lenght) {
        Object[] ret = new Object[lenght];
        for (int i = 0; i < queue.size; i++) {
            ret[i] = queue.arr[(i + queue.head) % queue.arr.length];
        }
        return ret;
    }
}
