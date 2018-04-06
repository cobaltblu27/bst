import java.util.*;

public class BST { // Binary Search Tree implementation

    protected boolean NOBSTified = false;
    protected boolean OBSTified = false;
    protected String element;
    protected BST left;
    protected BST right;
    protected int frequency;
    protected int accessCount;
    private int length;

    //used only in building obst
    private static ArrayList<BST> nodeArr;
    private static MyHashMap<Integer> rootMap, costMap, freqSum;

    public BST() {
        element = null;
        frequency = 0;
        accessCount = 0;
        left = null;
        right = null;
    }

    public void insert(String key) {
        if (element == null) {
            element = key;
            frequency = 1;
        } else if (element.compareTo(key) > 0) {
            if (left == null)
                left = new BST();
            left.insert(key);
        } else if (element.compareTo(key) < 0) {
            if (right == null)
                right = new BST();
            right.insert(key);
        } else {
            frequency++;
        }
    }

    public boolean find(String key) {
        accessCount++;
        if (element.compareTo(key) > 0)
            return left != null && left.find(key);
        else if (element.compareTo(key) < 0)
            return right != null && right.find(key);
        return true;
    }

    public int size() {
        return inorder(tree -> 1, this);
    }

    public int sumFreq() {
        return inorder(tree -> tree.frequency, this);
    }

    public int sumProbes() {
        return inorder(tree -> tree.accessCount, this);
    }

    public void resetCounters() {
        inorder(tree -> {
            tree.accessCount = 0;
            tree.frequency = 0;
            return 0;
        }, this);
    }

    public int sumWeightedPath() {//TODO BROKEN
        length = 1;
        return inorder(tree -> {
            if (tree.right != null) tree.right.length = tree.length + 1;
            if (tree.left != null) tree.left.length = tree.length + 1;
            return tree.length * tree.frequency;
        }, this);
    }

    public void nobst() {//TODO

    }    // Set NOBSTified to true.

    public void obst() {
        int s = size();
        nodeArr = new ArrayList<>();
        rootMap = new MyHashMap<>();
        costMap = new MyHashMap<>();
        freqSum = new MyHashMap<>();
        nodeArr.add(null);

        BST newRoot = new BST();
        newRoot.copy(this);

        newRoot.getArr(nodeArr);
        //nodeArr consists of every BST in order
        //index 0 of nodeArr will have null pointer
        //null element will be used to make empty tree point to null

        for (int left = 1; left <= s; left++) {//adds frequency for every range possible
            int sumCost = nodeArr.get(left).frequency;
            freqSum.put(left, left, sumCost);
            costMap.put(left, left, sumCost);//also initialize cost for single or empty tree
            costMap.put(left, left - 1, 0);
            rootMap.put(left, left - 1, 0);
            rootMap.put(left, left, left);
            for (int right = left + 1; right <= s; right++) {
                sumCost += nodeArr.get(right).frequency;
                freqSum.put(left, right, sumCost);
            }
        }
        costMap.put(s + 1, s, 0);
        rootMap.put(s + 1, s, 0);


        for (int r = 2; r <= s; r++) {
            int lBound, rBound;
            for (int l = r - 1; l > 0; l--) {
                lBound = rootMap.get(l, r - 1);
                rBound = rootMap.get(l + 1, r);
                int minCost = Integer.MAX_VALUE;
                int cost, index = l;

                for (int i = lBound; i <= rBound; i++) {
                    cost = costMap.get(l, i - 1) + costMap.get(i + 1, r) + freqSum.get(l, r);
                    if (cost < minCost) {
                        minCost = cost;
                        index = i;
                    }
                }
                costMap.put(l, r, minCost);
                rootMap.put(l, r, index);
            }
        }


        build(1, s);
        BST newOBSTRoot = nodeArr.get(rootMap.get(1, s));
        copy(newOBSTRoot);
        OBSTified = true;
    }    // Set OBSTified to true.


    private BST build(int left, int right) {
        if (left > right)
            return null;
        BST root;
        int index;
        index = rootMap.get(left, right);
        root = nodeArr.get(index);

        if (root == null)
            return null;

        root.right = build(index + 1, right);
        root.left = build(left, index - 1);

        return root;
    }

    private void getArr(ArrayList<BST> arr) {
        if (left != null)
            left.getArr(arr);
        arr.add(this);
        if (right != null)
            right.getArr(arr);
    }

    public void print() {
        inorder(tree -> {
            System.out.println("[" + tree.element + ":" + tree.frequency + ":" + tree.accessCount + "]");
            return 0;
        }, this);
    }

    private int inorder(getVal g, BST tree) {//recursively visits every nodes in ascending order
        int leftSum, thisElement, rightSum;
        leftSum = (tree.left == null) ? 0 : inorder(g, tree.left);
        thisElement = g.get(tree);
        rightSum = (tree.right == null) ? 0 : inorder(g, tree.right);
        return leftSum + rightSum + thisElement;
    }

    protected void copy(BST tree) {
        element = tree.element;
        frequency = tree.frequency;
        accessCount = tree.accessCount;
        left = tree.left;
        right = tree.right;
    }

    private class MyHashMap<V> extends HashMap<Long, V> {//takes two keys instead of one

        //TODO this class seems to be slowing down

        private V get(int key1, int key2) {
            return super.get(getKey(key1, key2));
        }

        private void put(int key1, int key2, V obj) {
            super.put(getKey(key1, key2), obj);
        }

        private long getKey(int x, int y) {// Cantor pairing function
            // produces one unique key from two nonnegative integers
            return (x + y) * (x + y + 1) / 2 + y;
        }
    }

    private interface getVal {//used to recursively add certain element of all nodes

        int get(BST tree);
    }
}

