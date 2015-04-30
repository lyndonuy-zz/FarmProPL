/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lyndon
 */
public class StackNode {
    static SyntaxTreeNode n;
    static int state;
    static String move;
    public StackNode(int s, String m, SyntaxTreeNode node){
        n = node;
        state = s;
        move = m;
    }
}
