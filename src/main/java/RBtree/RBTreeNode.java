package RBtree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.*;
import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RBTreeNode implements Serializable {
    private RBTreeNode  parent;
    @JsonProperty("right")
    private RBTreeNode  right;
    @JsonProperty("left")
    private RBTreeNode  left;

    @JsonProperty("value")
    private int         value;
    @JsonProperty("x")
    private int         x;
    @JsonProperty("y")
    private int         y;
    @JsonProperty("lvl")
    private int         lvl;
    @JsonProperty("Color")
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

    @JsonIgnore
    void setRight(RBTreeNode right) {
        this.right = right;
        if (right != null)
            this.right.parent = this;
    }

    @JsonIgnore
    public RBTreeNode getRight() {
        return right;
    }
    @JsonIgnore
    void setLeft(RBTreeNode left) {
        this.left = left;
        if (left != null)
            this.left.parent = this;
    }

    @JsonIgnore
    public RBTreeNode getLeft() {
        return left;
    }

    @JsonIgnore
    public RBTreeNode getGrParent(){
        if (this.parent != null)
            return this.parent.getParent();
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
