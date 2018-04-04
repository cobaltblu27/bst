import java.util.*;

public class BST { // Binary Search Tree implementation

    protected boolean NOBSTified = false;
    protected boolean OBSTified = false;
    protected String element;
    protected BST left;
    protected BST right;
    protected int frequency;
    protected int accessCount;
 //   private int length;


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

    public boolean find(String key) {//TODO accessCount is half the answer
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

    public int sumWeightedPath() {
        return inorder(new getVal() {
            @Override
            public int get(BST tree) {
                //TODO
            }
        }, this);
    }

    public void nobst() {//TODO

    }    // Set NOBSTified to true.

    public void obst() {//TODO
        int s = size();
        ArrayList<BST> nodeArr = new ArrayList<>();
        getArr(nodeArr);

        getRootMap(0,s-1);

        OBSTified = true;
    }    // Set OBSTified to true.

    private void getRootMap(int low, int high){

    }

    private void getArr(ArrayList<BST> arr) {
        if (left != null)
            left.getArr(arr);
        arr.add(this);
        if (right != null)
            right.getArr(arr);
    }

    public void print() {
        inorder(tree -> {//TODO accessCount doubled for comparing; must be erased
            System.out.println("[" + tree.element + ":" + tree.frequency + ":" + 2 * tree.accessCount + "]");
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

    private interface getVal {//used to recursively add certain element of all nodes

        int get(BST tree);
    }
}

