package RBtree;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.awt.*;
import java.io.Serializable;

@JsonAutoDetect
public class MyRedBlackTree implements Serializable {
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
    public void addNode(int value) {
        RBTreeNode current = this.root;
        RBTreeNode parent = this.root;
        RBTreeNode newNode = new RBTreeNode(value);

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
            newNode.setParent(parent);
        }
        balanceTree(newNode);
    }

    @JsonIgnore
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
                    shortLeftRotate(node);
                    parent = node;
                }
                rightRotate(parent);
                node = parent;
            }
            else {
                if (node == parent.getLeft()) {
                    shortRightRotate(parent);
                    parent = node;
                }
                leftRotate(parent);
                node = parent;
            }
            parent.setColor(Color.BLACK);
            grFather.setColor(Color.RED);
        }
        this.root.setColor(Color.BLACK);
    }

    @JsonIgnore
    private void shortRightRotate(RBTreeNode parent) {
        RBTreeNode grParent = parent.getParent();
        RBTreeNode node = parent.getLeft();

        grParent.setRight(node);
        parent.setLeft(node.getRight());
        parent.setParent(node);
        node.setRight(parent);
        node.setParent(grParent);
    }

    @JsonIgnore
    private void shortLeftRotate(RBTreeNode parent) {
        RBTreeNode grParent = parent.getParent();
        RBTreeNode node = parent.getRight();

        grParent.setLeft(node);
        parent.setRight(node.getLeft());
        parent.setParent(node);
        node.setLeft(parent);
        node.setParent(grParent);
    }

    @JsonIgnore
    private void rightRotate(RBTreeNode parent) {
        RBTreeNode node = parent.getLeft();
        RBTreeNode brother = parent.getRight();
        RBTreeNode grFather = node.getGrParent();

        parent.setParent(grFather.getParent());
        grFather.setLeft(brother);
        if (brother != null)
            brother.setParent(grFather);
        if (grFather.getRight() == parent)
            grFather.setRight(null);
        parent.setRight(grFather);
        grFather.setParent(parent);

        relinkParentToGrParent(parent, grFather);
    }

    @JsonIgnore
    private void leftRotate(RBTreeNode parent) {
        RBTreeNode node = parent.getRight();
        RBTreeNode grFather = node.getGrParent();
        RBTreeNode brother = parent.getLeft();

        parent.setParent(grFather.getParent());
        grFather.setRight(brother);
        if (brother != null)
            brother.setParent(grFather);
        if (grFather.getLeft() == parent)
            grFather.setLeft(null);
        parent.setLeft(grFather);
        grFather.setParent(parent);

        relinkParentToGrParent(parent, grFather);
    }

    @JsonIgnore
    private void relinkParentToGrParent(RBTreeNode node, RBTreeNode grFather) {
        RBTreeNode parent = node.getParent();

        if (parent != null) {
            if (parent.getLeft() == grFather)
                parent.setLeft(node);
            else
                parent.setRight(node);
        }
        else
            this.root = node;
    }

    @JsonIgnore
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
    public void deleteNode(int value) {
        RBTreeNode node = findNode(value);

        if (node == null)
            return ;
        delete(node);
    }

    @JsonIgnore
    public RBTreeNode findNode(int value) {
        RBTreeNode tmp = this.root;

        while (tmp != null && tmp.getValue() != value) {
            if (tmp.getValue() > value)
                tmp = tmp.getLeft();
            else
                tmp = tmp.getRight();
        }
        return tmp;
    }

    @JsonIgnore
    private void delete(RBTreeNode node) {
        RBTreeNode leftSon = node.getLeft();;
        RBTreeNode rightSon  = node.getRight();
        RBTreeNode parent = node.getParent();;
        RBTreeNode replaceChild;

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
    private RBTreeNode findNextValue(RBTreeNode node) {
        RBTreeNode tmp = node.getLeft();

        while (tmp.getRight() != null)
            tmp = tmp.getRight();
        return tmp;
    }

    @JsonIgnore
    private RBTreeNode replaceOneChild(RBTreeNode node, RBTreeNode replaceChild) {
        RBTreeNode parent  = node.getParent();

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
}
