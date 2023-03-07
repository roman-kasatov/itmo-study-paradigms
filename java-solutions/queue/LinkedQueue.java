package queue;

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        final Object value;
        Node next;

        Node(Object value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size = 0;

    @Override
    public void enqueue(Object element) {
        Node node = new Node(element);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = tail.next;
        }
        size++;
    }

    @Override
    protected Object elementImpl() {
        return head.value;
    }

    @Override
    protected Object dequeueImpl() {
        Object value = head.value;
        if (tail == head) {
            tail = null;
        }
        head = head.next;
        size--;
        return value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        size = 0;
        head = tail = null;
    }

    @Override
    public boolean contains(Object element) {
        Node node = head;
        while (node != null) {
            if (node.value.equals(element)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean removeFirstOccurrence(Object element) {
        if (head == null) {
            return false;
        } else if (head.value.equals(element)) {
            dequeue();
            return true;
        } else {
            for (Node node = head; node.next != null; node=node.next) {
                if (node.next.value.equals(element)) {
                    tail = (tail == node.next) ? node : tail;
                    node.next = node.next.next;
                    size--;
                    return true;
                }
            }
            return false;
        }
    }
}
