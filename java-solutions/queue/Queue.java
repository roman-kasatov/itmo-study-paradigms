package queue;

public interface Queue {
    // Model: a[0]..a[n]
    // Invariant: for i=0..(n-1): a[i] != null

    // Let immutPref(n): for i=0..(n-1): a'[i] == a[i]


    // Pred: element != null
    // Post: n' == n + 1 && a[n'] == element && immutPref(n)
    // enqueue(element) – добавить элемент в очередь;
    void enqueue(Object element);

    // Pred: n > 0
    // Post: R == a[0] && n' == n && immutablePref(n)
    // element – первый элемент в очереди;
    Object element();

    // Pred: n > 0
    // Post: R == a[0] && n' == n - 1 && for i=0..(n-1) a'[i] == a[i + 1]
    // dequeue – удалить и вернуть первый элемент в очереди;
    Object dequeue();

    // Pred: true
    // Post: R == n && n' == n && immutPref(n)
    // size – текущий размер очереди;
    int size();

    // Pred: true
    // Post: R == (n == 0)
    // isEmpty – является ли очередь пустой;
    boolean isEmpty();

    // Pred: true
    // Post: n' == 0
    // clear – удалить все элементы из очереди.
    void clear();

    // Pred: true
    // Post: R == (k exists: a[k] == element)
    // contains – проверяет, содержится ли элемент в очереди
    boolean contains(Object element);


    // Pred: true
    // Post: (R == false && (a[k] != element for k=0..(n-1)) && a[i] == a'[i] for i=0..(n-1)) ||
    // (R == true && (k exists: a[k] == element) && a[i] == a'[i] for i=0..(k-1) && a[i+1] = a'[i]
    // for i=k..(n-1))
    // removeFirstOccurrence - удаляет первое вхождение элемента в очередь и
    // возвращает было ли такое
    boolean removeFirstOccurrence(Object element);
}
