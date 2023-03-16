package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    public boolean isEmpty() {
        return size() == 0;
    }

    public void enqueue(Object element) {
        Objects.requireNonNull(element);
        enqueueImpl(element);
        size++;
    }

    protected abstract void enqueueImpl(Object element);

    public boolean removeFirstOccurrence(Object element) {
        Objects.requireNonNull(element);
        boolean foundFlag = removeFirstOccurrenceImpl(element);
        size = foundFlag ? size - 1 : size;
        return foundFlag;
    }

    protected abstract boolean removeFirstOccurrenceImpl(Object element);

    public Object element() {
        assert !isEmpty();
        return elementImpl();
    }
    protected abstract Object elementImpl();

    public Object dequeue() {
        assert !isEmpty();
        Object element = dequeueImpl();
        size--;
        return element;
    }

    protected abstract Object dequeueImpl();

    public void clear() {
        clearImpl();
        size = 0;
    }

    protected abstract void clearImpl();
}
