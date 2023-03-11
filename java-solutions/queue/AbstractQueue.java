package queue;

public abstract class AbstractQueue implements Queue {
    // :NOTE: это далеко не все общие части, нужно объединить минимум всю работу с size и contains с removeFirstOccurrence
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
