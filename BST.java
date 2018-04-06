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
        accessCount++;//TODO accessCount is half the answer
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

    public void obst() {//TODO MUST make it O(n^2)
        //TODO BROKEN
        int s = size();
        ArrayList<BST> nodeArr;
        MyHashMap<Integer> rootMap, costMap, freqSum;
        nodeArr = new ArrayList<>();
        rootMap = new MyHashMap<>();
        costMap = new MyHashMap<>();
        freqSum = new MyHashMap<>();
        nodeArr.add(null);

        BST newroot = new BST();
        newroot.element = element;
        newroot.frequency = frequency;
        newroot.right = right;
        newroot.left = left;

        newroot.getArr(nodeArr);
        //nodeArr consists of every BST in order
        //index 0 of nodeArr will have null pointer
        //null element will be used to make empty tree point to null

        for (int left = 1; left <= s; left++) {//adds frequency for every range possible
            freqSum.put(left, left, nodeArr.get(left).frequency);
            costMap.put(left, left, nodeArr.get(left).frequency);
            rootMap.put(left, left, left);
            for (int right = left + 1; right <= s; right++)
                freqSum.put(left, right, freqSum.get(left, right - 1) + nodeArr.get(right).frequency);
        }

        getRootMap(1, 1, s, freqSum, rootMap, costMap);



        build(1, s, nodeArr, rootMap);
        BST newOBSTRoot = nodeArr.get(rootMap.get(1, s));
        element = newOBSTRoot.element;
        frequency = newOBSTRoot.frequency;
        right = newOBSTRoot.right;
        left = newOBSTRoot.left;
        OBSTified = true;
    }    // Set OBSTified to true.

    private void getRootMap(int bound, int l, int r, MyHashMap<Integer> freqSum, MyHashMap<Integer> rootMap, MyHashMap<Integer> costMap) {
        if (l > r) {
            rootMap.put(l, r, 0);
            costMap.put(l, r, 0);
            return;
        }
        int minCost = Integer.MAX_VALUE;
        if (bound < l)
            bound = l;
        int leftRootBound = l, rightRootBound = bound;
        int cost , index = l;
        for (int i = bound; i <= r; i++) {//TODO bounds unused for correcting algorithm first
            if (!costMap.containsKey(l, i - 1))
                getRootMap(leftRootBound, l, i - 1, freqSum, rootMap, costMap);

            if (!costMap.containsKey(i + 1, r))
                getRootMap(rightRootBound, i + 1, r, freqSum, rootMap, costMap);

            leftRootBound = rootMap.get(l, i - 1);
            rightRootBound = rootMap.get(i + 1, r);
            cost = costMap.get(l, i - 1) + costMap.get(i + 1, r) + freqSum.get(l, r);
            if (cost < minCost) {
                minCost = cost;
                index = i;
            }
        }
        costMap.put(l, r, minCost);
        rootMap.put(l, r, index);

    }

    private BST build(int left, int right, ArrayList<BST> nodeArr, MyHashMap<Integer> rootMap) {
        if (left > right)
            return null;
        BST root;
        int index;
        index = rootMap.get(left, right);
        root = nodeArr.get(index);


        if (root == null)
            return null;

        root.right = build(index + 1, right, nodeArr, rootMap);
        root.left = build(left, index - 1, nodeArr, rootMap);

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

        private V get(int key1, int key2) {
            return super.get(getKey(key1, key2));
        }

        private boolean containsKey(int key1, int key2) {
            return keySet().contains(getKey(key1, key2));
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

