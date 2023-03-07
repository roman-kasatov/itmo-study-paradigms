package queue;

public abstract class AbstractQueue implements Queue {
    public Object element() {
        assert !isEmpty();
        return elementImpl();
    }
    protected abstract Object elementImpl();

    public Object dequeue() {
        assert !isEmpty();
        return dequeueImpl();
    }
    protected abstract Object dequeueImpl();

}
