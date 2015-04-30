/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//package sequencethesizer;
/**
 *
 * @author Lyndon
 */
import java.util.*;

public class SyntaxTree {

    protected SyntaxTreeNode root;

    public SyntaxTree() {
        super();
    }

    public void setRoot(SyntaxTreeNode n) {
        this.root = n;
    }

    public SyntaxTreeNode getRoot() {
        return this.root;
    }

    public int getNumberNodes() {
        int n = 0;
        if (root != null) {
            n = subGetNumberNodes(root) + 1;
        }
        return n;
    }

    private int subGetNumberNodes(SyntaxTreeNode node) {
        int n = node.getNumberChildren();
        for (SyntaxTreeNode x : node.getChildren()) {
            n += subGetNumberNodes(x);
        }
        return n;
    }

    public boolean exists(SyntaxTreeNode n) {
        return (find(n) != null);
    }

    public SyntaxTreeNode find(SyntaxTreeNode node) {
        SyntaxTreeNode n = null;
        if (root != null) {
            n = subFind(root, node);
        }
        return n;
    }

    private SyntaxTreeNode subFind(SyntaxTreeNode r, SyntaxTreeNode n) {
        SyntaxTreeNode node = null;
        int index = 0;

        if (((Comparable) r.tk).compareTo("") == 0) {
            node = r;
        } else if (r.childrenExist()) {
            while (node == null && index < r.getNumberChildren()) {
                node = subFind(r.getChildIndex(index), n);
                index++;
            }
        }
        return node;
    }

    public SyntaxTreeNode find(String s) {
        SyntaxTreeNode n = null;
        if (root != null) {
            n = findStr(root, s);
        }
        return n;
    }

    private SyntaxTreeNode findStr(SyntaxTreeNode r, String s) {
        SyntaxTreeNode node = null;
        int i = 0;
        if (r.tk.getTokenType().contains(s)) {
            node = r;
        } else if (r.childrenExist()) {
            while (node == null && i < r.getNumberChildren()) {
                node = findStr(r.getChildIndex(i), s);
                i++;
            }
        }
        return node;
    }

    public boolean isEmpty() {
        return (root == null);
    }

    public enum kTreeTraversalOrder {

        PRE_ORDER,
        POST_ORDER;
    }

    public List<SyntaxTreeNode> build(kTreeTraversalOrder order) {
        List<SyntaxTreeNode> l = null;
        if (root != null) {
            l = build(root, order);
        }
        return l;
    }

    private List<SyntaxTreeNode> build(SyntaxTreeNode node, kTreeTraversalOrder o) {
        List<SyntaxTreeNode> traverse = new ArrayList<SyntaxTreeNode>();
        if (o == kTreeTraversalOrder.PRE_ORDER) {
            buildPreOrder(node, traverse);
        } else if (o == kTreeTraversalOrder.POST_ORDER) {
            buildPostOrder(node, traverse);
        }
        return traverse;
    }

    private void buildPreOrder(SyntaxTreeNode node, List<SyntaxTreeNode> traverse) {
        traverse.add(node);
        for (SyntaxTreeNode t : node.getChildren()) {
            if(t != null){
                buildPreOrder(t, traverse);
            }
            else{
                
            }
        }
    }

    private void buildPostOrder(SyntaxTreeNode node, List<SyntaxTreeNode> traverse) {
        for (SyntaxTreeNode t : node.getChildren()) {
            buildPostOrder(node, traverse);
        }
        traverse.add(node);
    }

    public Map<SyntaxTreeNode, Integer> buildWithDepth(kTreeTraversalOrder o) {
        Map<SyntaxTreeNode, Integer> map = null;
        if (root != null) {
            map = buildWithDepth(root, o);
        }
        return map;
    }

    private Map<SyntaxTreeNode, Integer> buildWithDepth(SyntaxTreeNode node, kTreeTraversalOrder o) {
        Map<SyntaxTreeNode, Integer> traverse = new LinkedHashMap<SyntaxTreeNode, Integer>();
        if (o == kTreeTraversalOrder.POST_ORDER) {
            buildPostOrderDepth(node, traverse, 0);
        } else if (o == kTreeTraversalOrder.PRE_ORDER) {
            buildPreOrderDepth(node, traverse, 0);
        }
        return traverse;
    }

    private void buildPreOrderDepth(SyntaxTreeNode node, Map<SyntaxTreeNode, Integer> traverse, int depth) {
        traverse.put(node, depth);
        for (SyntaxTreeNode t : node.getChildren()) {
            buildPreOrderDepth(t, traverse, depth + 1);
        }
    }

    private void buildPostOrderDepth(SyntaxTreeNode node, Map<SyntaxTreeNode, Integer> traverse, int depth) {

        for (SyntaxTreeNode t : node.getChildren()) {
            buildPreOrderDepth(t, traverse, depth + 1);
        }
        traverse.put(node, depth);
    }

    public String toString() {
        String s = "";
        if (root != null) {
            s = build(kTreeTraversalOrder.PRE_ORDER).toString();
        }
        return s;
    }

    public String toStringWithDepth() {
        String s = "";
        if (root != null) {
            s = buildWithDepth(kTreeTraversalOrder.PRE_ORDER).toString();
        }
        return s;
    }
}
