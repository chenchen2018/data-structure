package com.cchen.avltree;

/**
 * The class representing AVL tree.
 * @param <T>: generic type that should be comparable.
 */
public class AVLTree<T extends Comparable<T>> {

    /**
     * The class representing AVL tree node.
     * @param <T>: generic type that should be comparable.
     */
    class AVLTreeNode<T extends Comparable<T>> {
        T value;                    // value that node is storing
        int height;                 // height of the node
        AVLTreeNode<T> left, right; // left child and right child

        public AVLTreeNode(T value, AVLTreeNode left, AVLTreeNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.height = 0;
        }
    }

    private AVLTreeNode<T> root;

    public AVLTree() {
        this.root = null;
    }

    /**
     * Get AVL tree's height.
     * @return
     */
    public int height() {
        return height(root);
    }

    public void insert(T value) {
        this.root = insert(this.root, value);
    }

    // helper methods here

    /**
     * Insert a value into AVL tree.
     * @param tree: AVL tree.
     * @param value: value to be inserted.
     * @return new root.
     */
    private AVLTreeNode<T> insert(AVLTreeNode<T> tree, T value) {
        if (tree == null) {
            tree = new AVLTreeNode<>(value, null, null);
        } else {
            int comp = value.compareTo(tree.value);
            if (comp < 0) { // new value goes to left subtree
                tree.left = insert(tree.left, value);
                if (height(tree.left) - height(tree.right) == 2) {  // if tree is unbalanced because of the insertion
                    if (value.compareTo(tree.left.value) < 0) {
                        // left-left rotation because new node is inserted into left child's left subtree.
                        tree = leftLeftRotation(tree);
                    } else {
                        // left-right rotation because nwe node is inserted into left child's right subtree.
                        tree = leftRightRotation(tree);
                    }
                }
            } else if (comp > 0) {
                tree.right = insert(tree.right, value);
                if (height(tree.right) - height(tree.left) == 2) {
                    if (value.compareTo(tree.right.value) < 0) {
                        // right-left rotation because new node is inserted into right child's left subtree
                        tree = rightLeftRotation(tree);
                    } else {
                        // right-right rotation because new node is inserted into right child's right subtree
                        tree= rightRightRotation(tree);
                    }
                }
            } else {
                System.out.println("Failed to insert: cannot add same value twice.");
            }
        }
        tree.height = maxHeight(height(tree.left), height(tree.right)) + 1;
        return tree;
    }

    private int maxHeight(int height1, int height2) {
        return height1 > height2 ? height1 : height2;
    }

    private int height(AVLTreeNode<T> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /**
     * Perform rotation when tree is unbalanced due to left-left scenario.
     * What is left-left? When adding or removing a node, the root(not necessary the root of the whole tree)'s left
     * subtree's left subtree still has non-null node, which leads to left subtree's height greater than right subtree.
     * The solution for rebalancing: perform a right rotation on root's left child.
     * @param node: root node to perform left-left rotation on.
     * @return new root.
     */
    private AVLTreeNode<T> leftLeftRotation(AVLTreeNode<T> node) {
        AVLTreeNode<T> newRoot = rotateToRight(node.left);
        // After rotation, perform a height updating.
        newRoot.height = maxHeight(height(newRoot.left), height(newRoot.right)) + 1;
        node.height = maxHeight(height(node.left), height(node.right)) + 1;
        return newRoot;
    }

    /**
     * Perform rotation when tree is unbalanced due to left-right scenario.
     * What is left-right? When adding or removing a node, the root(not necessary the root of the whole tree)'s left
     * subtree's right subtree still has non-null node, which leads to left subtree's height greater than right subtree.
     * The steps for rebalancing:
     * 1) Perform a R-R rotation on root's left child(let left child's balance factor's sign be consistent with root's)
     * 2) Perform a L-L rotation on root
     * @param node: root node to perform left-right rotation on.
     * @return new root.
     */
    private AVLTreeNode<T> leftRightRotation(AVLTreeNode<T> node) {
        node.left = rightRightRotation(node.left);
        return leftLeftRotation(node);
    }

    /**
     * Perform rotation when tree is unbalanced due to right-left scenario.
     * What is right-left? When adding or removing a node, the root(not necessary the root of the whole tree)'s right
     * subtree's left subtree still has non-null node, which leads to right subtree's height greater than left subtree.
     * The steps for rebalancing:
     * 1) Perform a L-L rotation on root's right child(let right child's balance factor's sign be consistent with root's)
     * 2) Perform a R-R rotation on root
     * @param node: root node to perform right-left rotation on.
     * @return new root.
     */
    private AVLTreeNode<T> rightLeftRotation(AVLTreeNode<T> node) {
        node.right = leftLeftRotation(node.right);
        return rightRightRotation(node);
    }

    /**
     * Perform rotation when tree is unbalanced due to left-left scenario.
     * What is right-right? When adding or removing a node, the root(not necessary the root of the whole tree)'s right
     * subtree's right subtree still has non-null node, which leads to right subtree's height greater than left subtree.
     * The solution for rebalancing: perform a left rotation on root's right child.
     * @param node: root node to perform left-left rotation on.
     * @return new root.
     */
    private AVLTreeNode<T> rightRightRotation(AVLTreeNode<T> node) {
        AVLTreeNode<T> newRoot = rotateToLeft(node.right);
        // After rotation, perform a height updating.
        newRoot.height = maxHeight(height(newRoot.left), height(newRoot.right)) + 1;
        node.height = maxHeight(height(node.left), height(node.right)) + 1;
        return newRoot;
    }

    /**
     * Perform one left rotation on a node.
     * @param node: node to perform left-rotation on.
     * @return new root.
     */
    private AVLTreeNode<T> rotateToLeft(AVLTreeNode<T> node) {
        AVLTreeNode<T> right = node.right;
        node.right = right.left;
        right.left = node;
        return right;
    }

    /**
     * Perform one right rotation on a node.
     * @param node: node to perform right-rotation on.
     * @return new root.
     */
    private AVLTreeNode<T> rotateToRight(AVLTreeNode<T> node) {
        AVLTreeNode<T> left = node.left;
        node.left = left.right;
        left.right = node;
        return left;
    }
}