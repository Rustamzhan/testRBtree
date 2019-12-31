package RBtree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.awt.*;

@JsonPropertyOrder({ "value", "left", "right", "parent" })
public class RBTreeNode {
    private RBTreeNode  parent;
    @JsonIgnoreProperties("parent")
    private RBTreeNode  right;
    @JsonIgnoreProperties("parent")
    private RBTreeNode  left;

    private int         value;
    private int         x;
    private int         y;
    private int         lvl;
    @JsonIgnore
    private Color       color;

    RBTreeNode(int value) {
        this.value = value;
        this.color = Color.RED;
        this.parent = null;
        this.right = null;
        this.left = null;
    }

    RBTreeNode() {
        this.color = Color.RED;
        this.parent = null;
        this.right = null;
        this.left = null;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    @JsonIgnore
    void setParent(RBTreeNode parent) {
        this.parent = parent;
    }
    @JsonIgnore
    public RBTreeNode getParent() {
        return parent;
    }

    void setRight(RBTreeNode right) {
        this.right = right;
        if (right != null)
            this.right.parent = this;
    }

    public RBTreeNode getRight() {
        return right;
    }

    void setLeft(RBTreeNode left) {
        this.left = left;
        if (left != null)
            this.left.parent = this;
    }

    public RBTreeNode getLeft() {
        return left;
    }

    @JsonIgnore
    public RBTreeNode getGrParent(){
        if (this.parent != null)
            return this.parent.parent;
        return null;
    }

    @JsonIgnore
    public RBTreeNode getUncle() {
        RBTreeNode grParent = this.getGrParent();
        if (this.parent != null && grParent != null)
        {
            if (grParent.getLeft() == this.parent)
                return grParent.getRight();
            else
                return grParent.getLeft();
        }
        return null;
    }

    public int  getValue() {
        return this.value;
    }

    void setValue(int value) {
        this.value = value;
    }

    void setColor(Color color) {
        if (color != null)
           this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isBlack() {
        if (this.color == Color.BLACK)
            return true;
        return false;
    }

    public boolean isRed() {
        if (this.color == Color.RED)
            return true;
        return false;
    }
}
