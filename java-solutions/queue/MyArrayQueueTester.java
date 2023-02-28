package queue;

import java.util.*;

public class MyArrayQueueTester {
    private static final Random random = new Random();

    public static void main(String[] args) {

        System.out.println("Tests ArrayQueueADT");
        for (double freq = 0.2; freq < 1; freq+= 0.2) {
            System.out.println("Add requency = " + freq);
            Deque<Object> deque = new ArrayDeque<>();
            ArrayQueueADT queueADT = new ArrayQueueADT();

            for (int i = 0; i < 100; i++) {
                if (random.nextDouble() <= freq) {
                    Object value = random.nextInt();
                    ArrayQueueADT.enqueue(queueADT, value);
                    deque.addLast(value);
                } else {
                    if (!deque.isEmpty()) {
                        ArrayQueueADT.dequeue(queueADT);
                        deque.pollFirst();
                    }
                }
                assert Arrays.equals(deque.toArray(), ArrayQueueADT.toArray(queueADT));
            }
        }

        System.out.println("Tests ArrayQueueModule");
        for (double freq = 0.2; freq < 1; freq+= 0.2) {
            System.out.println("Add requency = " + freq);
            Deque<Object> deque = new ArrayDeque<>();
            ArrayQueueModule.clear();

            for (int i = 0; i < 100; i++) {

                if (random.nextDouble() <= freq) {
                    Object value = random.nextInt();
                    ArrayQueueModule.enqueue(value);
                    deque.addLast(value);
                } else {
                    if (!deque.isEmpty()) {
                        ArrayQueueModule.dequeue();
                        deque.pollFirst();
                    }
                }
                assert Arrays.equals(deque.toArray(), ArrayQueueModule.toArray());
            }
        }

        System.out.println("Tests ArrayQueue");
        for (double freq = 0.2; freq < 1; freq+= 0.2) {
            System.out.println("Add requency = " + freq);
            Deque<Object> deque = new ArrayDeque<>();
            ArrayQueue queue = new ArrayQueue();

            for (int i = 0; i < 100; i++) {
                if (random.nextDouble() <= freq) {
                    Object value = random.nextInt();
                    queue.enqueue(value);
                    deque.addLast(value);
                } else {
                    if (!deque.isEmpty()) {
                        queue.dequeue();
                        deque.pollFirst();
                    }
                }
                assert Arrays.equals(deque.toArray(), queue.toArray());
            }
        }
    }
}
