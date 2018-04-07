public class AVL extends BST {
    private int height;

    public AVL() {
        element = null;
        left = null;
        right = null;
        frequency = 0;
        accessCount = 0;
        height = 0;
    }

    public void insert(String key) {
        if (element == null) {
            element = key;
            frequency = 1;
        } else if (element.compareTo(key) > 0) {
            if (left == null)
                left = new AVL();
            left.insert(key);
            if (getHeight(left) - getHeight(right) == 2) {
                if (left.element.compareTo(key) > 0)
                    rightSpin();
                else
                    leftRightSpin();
            }
            getHeight(this);
        } else if (element.compareTo(key) < 0) {
            if (right == null)
                right = new AVL();
            right.insert(key);
            if (getHeight(left) - getHeight(right) == -2) {
                if (right.element.compareTo(key) < 0)
                    leftSpin();
                else rightLeftSpin();
            }
            getHeight(this);
        } else {
            frequency++;
        }
    }

    private void rightLeftSpin() {
        AVL rightAVL = (AVL) right;
        rightAVL.rightSpin();
        leftSpin();
    }

    private void leftRightSpin() {
        AVL leftAVL = (AVL) left;
        leftAVL.leftSpin();
        rightSpin();
    }

    private void rightSpin() {
        AVL newRight = new AVL();
        newRight.copy(this);
        AVL leftRight = (AVL) left.right;
        left.right = null;
        element = left.element;
        frequency = left.frequency;
        accessCount = left.accessCount;
        left = left.left;
        right = newRight;
        right.left = leftRight;
    }

    private void leftSpin() {
        AVL newLeft = new AVL();
        newLeft.copy(this);
        AVL rightLeft = (AVL) right.left;
        right.left = null;
        element = right.element;
        frequency = right.frequency;
        accessCount = right.accessCount;
        right = right.right;
        left = newLeft;
        left.right = rightLeft;
    }

    private int getHeight(BST tree) {
        if (tree == null)
            return 0;
        int leftH, rightH;
        if(right != null && left != null && Math.max(((AVL)left).height,((AVL)right).height) +1==height)
            return height;
        leftH = getHeight(((AVL)tree).left);
        rightH = getHeight(((AVL)tree).right);
        ((AVL)tree).height = Math.max(leftH,rightH) + 1;
        return ((AVL)tree).height;
    }

}

