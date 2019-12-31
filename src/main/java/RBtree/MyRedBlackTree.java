package RBtree;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.*;

public class MyRedBlackTree {
    private static MyRedBlackTree instance = new MyRedBlackTree();
    private RBTreeNode root;

    private MyRedBlackTree() { }

    public static MyRedBlackTree getInstance(){
        return instance;
    }

    public RBTreeNode getRoot() {
        return root;
    }
    @JsonIgnore
    public RBTreeNode findNode(int value) {
        RBTreeNode tmp;

        tmp = this.root;
        while (tmp != null && tmp.getValue() != value) {
            if (tmp.getValue() > value)
                tmp = tmp.getLeft();
            else
                tmp = tmp.getRight();
        }
        if (tmp.getValue() == value)
            return null;
        return tmp;
    }
    @JsonIgnore
    public void addNode(int value) {
        RBTreeNode current;
        RBTreeNode parent;
        RBTreeNode newNode;

        newNode = new RBTreeNode(value);

        current = this.root;
        parent = this.root;
        while (current != null) {
            parent = current;
            current = (value < current.getValue()) ?
                    current.getLeft() : current.getRight();
        }
        if (parent == null)
            this.root = newNode;
        else {
            if (value < parent.getValue())
                parent.setLeft(newNode);
            else
                parent.setRight(newNode);
        }
        balanceTree(newNode);
    }

    private void rightRotate(RBTreeNode parent) {
        RBTreeNode node;
        RBTreeNode grFather;

        node = parent.getLeft();
        grFather = node.getGrParent();

        grFather.setLeft(parent.getRight());
        parent.setParent(grFather.getParent());
        parent.setRight(grFather);

        node = parent;
        parent = node.getParent();
        if (parent != null) {
            if (parent.getLeft() == grFather)
                parent.setLeft(node);
            else
                parent.setRight(node);
        }
        else
            this.root = node;
    }

    private void leftRotate(RBTreeNode parent) {
        RBTreeNode node;
        RBTreeNode grFather;

        node = parent.getRight();
        grFather = node.getGrParent();

        parent.setParent(grFather.getParent());
        grFather.setRight(parent.getLeft());
        parent.setLeft(grFather);

        node = parent;
        parent = node.getParent();
        if (parent != null) {
            if (parent.getLeft() == grFather)
                parent.setLeft(node);
            else
                parent.setRight(node);
        }
        else
            this.root = node;
    }

    private void balanceTree(RBTreeNode node) {
        RBTreeNode uncle;
        RBTreeNode grFather;
        RBTreeNode parent;

        while (node != null && node.getParent() != null
                && node.getParent().isRed()
                && node.getGrParent() != null) {
            uncle = node.getUncle();
            grFather = node.getGrParent();
            parent = node.getParent();
            if (uncle != null && uncle.isRed()) {
                uncle.setColor(Color.BLACK);
                parent.setColor(Color.BLACK);
                grFather.setColor(Color.RED);
                node = grFather;
            }
            else if (parent == grFather.getLeft()) {
                if (node == parent.getRight()) {
                    leftRotate(parent);
                    node.setColor(Color.BLACK);
                    node.getParent().setColor(Color.RED);
                    parent = node;
                }
                else {
                    parent.setColor(Color.BLACK);
                    grFather.setColor(Color.RED);
                }
                rightRotate(parent);
                node = parent;
            }
            else {
                if (node == parent.getLeft()) {
                    rightRotate(parent);
                    node.setColor(Color.BLACK);
                    node.getParent().setColor(Color.RED);
                    parent = node;
                }
                else {
                    parent.setColor(Color.BLACK);
                    grFather.setColor(Color.RED);
                }
                leftRotate(parent);
                node = parent;
            }
        }
        this.root.setColor(Color.BLACK);
    }

    private void balanceTreeAfterDelete(RBTreeNode node, RBTreeNode replacedNode) {
        RBTreeNode brother;
        RBTreeNode leftSon;
        RBTreeNode rightSon;

        while ((replacedNode == null || replacedNode.isBlack())
                && replacedNode != this.root) {
            if (node.getLeft() == replacedNode) {
                brother = node.getRight();
                if (brother.isRed()) {
                    brother.setColor(Color.BLACK);
                    node.setColor(Color.RED);
                    leftRotate(brother);
                    brother = node.getRight();
                }
                leftSon = brother.getLeft();
                rightSon = brother.getRight();
                if ((leftSon == null || leftSon.isBlack())
                    && (rightSon == null || rightSon.isBlack())) {
                    brother.setColor(Color.RED);
                    if (node.isRed()) {
                        node.setColor(Color.BLACK);
                        return ;
                    }
                    replacedNode = node;
                    node = replacedNode.getParent();
                }
                else {
                    if (rightSon == null || rightSon.isBlack()) {
                        rightRotate(brother);
                        brother.setColor(Color.RED);
                        if (leftSon != null)
                            leftSon.setColor(Color.BLACK);
                        brother = node.getRight();
                    }
                    brother.setColor(node.getColor());
                    brother.getRight().setColor(Color.BLACK);
                    node.setColor(Color.BLACK);
                    leftRotate(node);
                    replacedNode = node;
                    node = replacedNode.getParent();
                }
            }
            else {
                brother = node.getLeft();
                if (brother.isRed()) {
                    brother.setColor(Color.BLACK);
                    node.setColor(Color.RED);
                    rightRotate(brother);
                    brother = node.getLeft();
                }
                leftSon = brother.getLeft();
                rightSon = brother.getRight();
                if ((leftSon == null || leftSon.isBlack())
                        && (rightSon == null || rightSon.isBlack())) {
                    brother.setColor(Color.RED);
                    if (node.isRed()) {
                        node.setColor(Color.BLACK);
                        return ;
                    }
                    replacedNode = node;
                    node = replacedNode.getParent();
                }
                else {
                    if (leftSon == null || leftSon.isBlack()) {
                        leftRotate(brother);
                        brother.setColor(Color.RED);
                        if (rightSon != null)
                            rightSon.setColor(Color.BLACK);
                        brother = node.getRight();
                    }
                    brother.setColor(node.getColor());
                    brother.getLeft().setColor(Color.BLACK);
                    node.setColor(Color.BLACK);
                    rightRotate(node);
                    replacedNode = node;
                    node = replacedNode.getParent();
                }
            }
        }
    }
    @JsonIgnore
    private RBTreeNode findNextValue(RBTreeNode node) {
        RBTreeNode tmp;

        tmp = node.getLeft();
        while (tmp.getRight() != null)
            tmp = tmp.getRight();
        return tmp;
    }
    @JsonIgnore
    private RBTreeNode replaceOneChild(RBTreeNode node, RBTreeNode replaceChild) {
        RBTreeNode parent;

        parent = node.getParent();
        if (parent == null) {
            this.root = replaceChild;
            if (this.root != null)
                this.root.setColor(Color.BLACK);
            return replaceChild;
        }
        if (parent.getLeft() == node)
            parent.setLeft(replaceChild);
        else
            parent.setRight(replaceChild);
        return replaceChild;
    }
    @JsonIgnore
    private void delete(RBTreeNode node) {
        RBTreeNode leftSon;
        RBTreeNode rightSon;
        RBTreeNode parent;
        RBTreeNode replaceChild;

        parent = node.getParent();
        leftSon = node.getLeft();
        rightSon = node.getRight();
        if (leftSon != null && rightSon != null){
            replaceChild = findNextValue(node);
            node.setValue(replaceChild.getValue());
            if (replaceChild.isRed()) {
                if (replaceChild.getParent().getLeft() == replaceChild)
                    replaceChild.getParent().setLeft(null);
                else
                    replaceChild.getParent().setRight(null);
            }
            else
                delete(replaceChild);
            return ;
        }
        else if (leftSon != null)
            replaceChild = replaceOneChild(node, leftSon);
        else if (rightSon != null)
            replaceChild = replaceOneChild(node, rightSon);
        else
            replaceChild = replaceOneChild(node, null);
        if (replaceChild != null && replaceChild.isRed())
            replaceChild.setColor(Color.BLACK);
        else if (node.isBlack() && this.root != replaceChild)
            balanceTreeAfterDelete(parent, replaceChild);
    }
    @JsonIgnore
    public void deleteNode(int value) {
        RBTreeNode node;

        node = findNode(value);
        if (node == null)
            return ;
        delete(node);
    }
}
