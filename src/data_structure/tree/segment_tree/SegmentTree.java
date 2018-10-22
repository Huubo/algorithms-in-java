package data_structure.tree.segment_tree;

public class SegmentTree<E> {

    private class Node {
        int l;
        int r;
        int mid;
        E value;

        Node(int l, int r, E value) {
            this.l = l;
            this.r = r;
            this.value = value;
        }

        Node(int l, E value) {
            this(l, l, value);
        }
    }

    private Node[] tree;
    private E[] data;

    private Merger<E> merger;

    @SuppressWarnings("unchecked")
    public SegmentTree(E[] arr, Merger<E> merger) {
        this.merger = merger;

        int len = arr.length;
        data = (E[]) new Object[len];
        for (int i = 0; i < len; i++) {
            data[i] = arr[i];
        }

        tree = (Node[]) new Object[4 * len];
        buildSegmentTree(0, 0, len - 1);
    }

    private void buildSegmentTree(int treeIndex, int l, int r) {
        if (l == r) {
            tree[treeIndex] = new Node(l, data[l]);
            return;
        }

        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = leftTreeIndex + 1;
        int mid = l + (l - r) / 2;

        buildSegmentTree(leftTreeIndex, l, mid);
        buildSegmentTree(rightTreeIndex, mid + 1, r);

        E value = merger.merge(tree[leftTreeIndex].value, tree[rightTreeIndex].value);
        tree[treeIndex] = new Node(l, r, value);
    }

    /**
     * 获取线段树所存储数据的大小
     */
    public int size() {
        return data.length;
    }

    /**
     * 获取指定索引处的元素
     */
    public E get(int index) {
        rangeCheck(index);
        return data[index];
    }

    /**
     * 查询区间[rangeL, rangeR]内的值
     */
    public E query(int queryL, int queryR) {
        rangeCheckForQuery(queryL, queryR);

        return query(0, queryL, queryR);
    }

    private E query(int treeIndex, int queryL, int queryR) {
        int nodeL = tree[treeIndex].l;
        int nodeR = tree[treeIndex].r;
        if (nodeL == queryL && nodeR == queryR) {
            return tree[treeIndex].value;
        }

        int mid = nodeL + (nodeR - nodeL) / 2;

        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = leftTreeIndex + 1;

        if (queryL >= mid + 1) {
            return query(rightTreeIndex, queryL, queryR);
        } else if (queryR <= mid) {
            return query(leftTreeIndex, queryL, queryR);
        } else { // 待查询区间横跨两个子节点的区间
            E leftResult = query(leftTreeIndex, queryL, mid);
            E rightResult = query(rightTreeIndex, mid + 1, queryR);
            return merger.merge(leftResult, rightResult);
        }
    }

    private void rangeCheck(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private void rangeCheckForQuery(int l, int r) {
        if (l < 0 || r >= size() || r < 0 || r >= size() || l > r) {
            throw new IllegalArgumentException("Index range is illegal.");
        }
    }

    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size();
    }

    /**
     * 获取在完全二叉树的数组表示中，指定索引元素的左子节点的索引
     */
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (int i = 0; i < tree.length; i++) {
            if (tree[i] != null) {
                builder.append(tree[i].value);
            } else {
                builder.append("null");
            }

            if (i != tree.length - 1) {
                builder.append(" ,");
            }
        }

        return builder.toString();
    }
}