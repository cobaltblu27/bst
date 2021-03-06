import java.util.*;

public class BST { // Binary Search Tree implementation

    protected boolean NOBSTified = false;
    protected boolean OBSTified = false;
    protected String element;
    protected BST left;
    protected BST right;
    protected int frequency;
    protected int accessCount;

    //used only in building obst
    private static ArrayList<BST> nodeArr;
    private static int[][] rootMap;

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

    public int sumWeightedPath() {
        return inorder(BST::sumFreq, this);
    }

    public void nobst() {//TODO
        int s = size();
        nodeArr = new ArrayList<>();
        rootMap = new int[s + 1][];//int[high][low]

        int[] freqSum = new int[s + 1];

        BST newRoot = new BST();
        newRoot.copy(this);

        nodeArr.add(null);
        inorder(tree -> {
            nodeArr.add(tree);
            return 0;
        }, newRoot);

        freqSum[0] = 0;

        for (int i = 1; i <= s; i++) {
            rootMap[i] = new int[i + 2];
            freqSum[i] = nodeArr.get(i).frequency + freqSum[i - 1];
        }

        nobstify(freqSum, 1, s);

        build(1, s);

        BST newOBSTRoot = nodeArr.get(rootMap[s][1]);
        copy(newOBSTRoot);

        NOBSTified = true;
    }    // Set NOBSTified to true.

    private void nobstify(int[] freqSum, int left, int right) {
        if (left > right)
            return;
        int leftSum, rightSum;
        int i = left;
        while(i <= right){
            leftSum = freqSum[i - 1] - freqSum[left - 1];
            rightSum = freqSum[right] - freqSum[i];
            if (i == right ||
                    Math.abs(leftSum - rightSum) <= Math.abs(leftSum - rightSum + nodeArr.get(i).frequency + nodeArr.get(i + 1).frequency))
                break;
            i++;
        }

        rootMap[right][left] = i;
        nobstify(freqSum, left, i - 1);
        nobstify(freqSum, i + 1, right);
    }

    public void obst() {
        int s = size();
        nodeArr = new ArrayList<>();
        rootMap = new int[s + 1][];//int[high][low]
        int[][] costMap = new int[s + 1][];
        int[][] freqSum = new int[s + 1][];
        nodeArr.add(null);

        BST newRoot = new BST();
        newRoot.copy(this);

        inorder(tree -> {
            nodeArr.add(tree);
            return 0;
        }, newRoot);

        //nodeArr consists of every BST in order
        //index 0 of nodeArr will have null pointer
        //null element will be used to make empty tree point to null


        for (int high = 0; high <= s; high++) {//adds frequency for every range possible
            freqSum[high] = new int[high + 2];
            rootMap[high] = new int[high + 2];
            costMap[high] = new int[high + 2];

            int sumCost = (high == 0) ? 0 : nodeArr.get(high).frequency;

            freqSum[high][high] = sumCost;
            costMap[high][high] = sumCost;//also initialize cost for single or empty tree
            rootMap[high][high] = high;
            for (int low = high - 1; low > 0; low--) {
                sumCost += nodeArr.get(low).frequency;
                freqSum[high][low] = sumCost;
            }
        }

        for (int r = 2; r <= s; r++) {//stores optimal root in array for every range possible
            int lBound, rBound;
            for (int l = r - 1; l > 0; l--) {
                lBound = rootMap[r - 1][l];
                rBound = rootMap[r][l + 1];
                int minCost = Integer.MAX_VALUE;
                int cost, index = l;

                for (int i = lBound; i <= rBound; i++) {
                    cost = costMap[i - 1][l] + costMap[r][i + 1] + freqSum[r][l];
                    if (cost < minCost) {
                        minCost = cost;
                        index = i;
                    }
                }
                costMap[r][l] = minCost;
                rootMap[r][l] = index;
            }
        }


        build(1, s);//build tree out of optimal roots recursively
        BST newOBSTRoot = nodeArr.get(rootMap[s][1]);
        copy(newOBSTRoot);
        OBSTified = true;
    }    // Set OBSTified to true.


    private BST build(int left, int right) {
        if (left > right)
            return null;
        BST root;
        int index;
        index = rootMap[right][left];
        root = nodeArr.get(index);

        if (root == null)
            return null;

        root.right = build(index + 1, right);
        root.left = build(left, index - 1);

        return root;
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


    private interface getVal {//used to recursively add certain element of all nodes

        int get(BST tree);
    }
}

