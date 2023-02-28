package queue;

/*
    Model: a[0]..a[n]
    Invariant: for i=0..(n-1): a[i] != null

    Let immutPref(n): for i=0..(n-1): a'[i] == a[i]


    Pred: element != null
    Post: n' == n + 1 && a[n'] == element && immutPref(n)
        enqueue(element) – добавить элемент в очередь;

    Pred: n > 0
    Post: R == a[0] && n' == n && immutablePref(n)
        element – первый элемент в очереди;

    Pred: n > 0
    Post: R == a[0] && n' == n - 1 && for i=0..(n-1) a'[i] == a[i + 1]
        dequeue – удалить и вернуть первый элемент в очереди;

    Pred: true
    Post: R == n && n' == n && immutPref(n)
        size – текущий размер очереди;

    Pred: true
    Post: R == (n == 0)
        isEmpty – является ли очередь пустой;

    Pred: true
    Post: n' == 0
        clear – удалить все элементы из очереди.

    DEQUE:

    Pred: element != null
    Post n' == n + 1 && for i=0..(n - 1) a'[i + 1] == a[i] && a'[n] = element
        push(element) – добавить элемент в начало очереди;

    Pred: n > 0
    Post: R = a[n - 1]
        peek – вернуть последний элемент в очереди;

    Pred: n > 0
    Post:  R == a[n - 1] && immutPref(n - 1) && n' == n - 1
        remove – вернуть и удалить последний элемент из очереди.

 */


import java.util.Objects;

public class ArrayQueueModule {
    private static Object[] arr = new Object[1];
    private static int head = 0;
    private static int size = 0;

    // Pred: element != null
    // Post: n' == n + 1 && a[n'] == element && immutPref(n)
    public static void enqueue(Object element) {
        Objects.requireNonNull(element);
        if (size == arr.length) {
            doubleArray();
        }
        arr[(size + head) % arr.length] = element;
        size++;
    }

    private static void doubleArray() {
        Object[] newArray = new Object[size * 2];
        for (int i = 0; i < size; i++) {
            newArray[i] = arr[(i + head) % arr.length];
        }
        arr = newArray;
        head = 0;
    }

    // Pred: n > 0
    // Post: R == a[0] && n' == n && immutablePref(n)
    public static Object element() {
        return arr[head];
    }

    // Pred: n > 0
    // Post: R == a[0] && n' == n - 1 && for i=0..(n-1) a'[i] == a[i + 1]
    public static Object dequeue() {
        Object ret = arr[head];
        arr[head] = null;
        head = (head + 1) % arr.length;
        size--;
        return ret;
    }

    // Pred: true
    // Post: R == n && n' == n && immutPref(n)
    public static int size() {
        return size;
    }

    // Pred: true
    // Post: R == (n == 0)

    public static boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: n' == 0
    public static void clear() {
        arr = new Object[1];
        head = 0;
        size = 0;
    }

    // Pred: element != null
    // Post n' == n + 1 && for i=0..(n - 1) a'[i + 1] == a[i] && a'[n] = element
    public static void push(Object element) {
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
    public static Object peek() {
        return arr[(head + size - 1) % arr.length];
    }


    // Pred: n > 0
    // Post:  R == a[n - 1] && immutPref(n - 1) && n' == n - 1
    public static Object remove() {
        int pos = (head + size - 1) % arr.length;
        Object ret = arr[pos];
        arr[pos] = 0;
        size--;
        return ret;
    }

    // Pred: true
    // Post: R.length == n, for i=0..(n-1) R[i] == a[i]
    public static Object[] toArray() {
        Object[] ret = new Object[size];
        for (int i = 0; i < size; i++) {
            ret[i] = arr[(i + head) % arr.length];
        }

        return ret;
    }
}
