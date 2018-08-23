package queue.array;

public class CircularQueue<E> implements Queue<E> {

    private E[] data;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public CircularQueue(int capacity) {
        this.data = (E[]) new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
        this.capacity = capacity;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * 判断队列是否为空
     *
     * @return 若为空返回 true
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 判断队列是否已满
     *
     * @return 若已满返回 true
     */
    private boolean isFull() {
        return size == capacity;
    }

    /**
     * 在尾部入队
     *
     * @param e 入队元素
     */
    @Override
    public void enqueue(E e) {
        /* 队列已满，扩容 */
        if (isFull()) {
            resize(2 * getCapacity());
        }

        rear++;
        rear %= data.length;
        data[rear] = e;

        size++;
    }

    /**
     * 从队首出队
     *
     * @return 返回出队的元素
     */
    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new IllegalArgumentException("Cannot dequeue from an empty queue.");
        }

        E tmpCell = data[front];
        data[front] = null;

        front++;
        front %= data.length;
        size--;

        /* 队列大小占容量的 1/4，容量减半，这样可防止出队方法的复杂度震荡 */
        if (size == getCapacity() / 4) {
            resize(getCapacity() / 2);
        }

        return tmpCell;
    }

    @Override
    public E peekFront() {
        return null;
    }

    /**
     * 容量动态调整
     */
    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        E[] newData = (E[]) new Object[capacity];

        for (int i = 0; i < size; i++) {
            newData[i] = data[(i + front) % data.length];
        }

        this.data = newData;
        this.front = 0;
        this.rear = -1;
        this.capacity = capacity;
    }
}
