/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package farmpro;

/**
 *
 * @author Nyker
 */
public class Tree {
/* 
    Each meAndChildK contains a pointer to its 
    Works much like a linked-list version of the list-of-children implementation.
    The right sibling pointers behave like a linked list where the leftmost-child pointer simply refers to the head.
*/
    public static String WorkingPrinter = "";
    
    public Node root;
    public String printer;
    
    // Constructors
    public Tree(Node ugat) {
        this.root = ugat;
        this.root.parent = null;    // Cuts connection with ancestor and parent
        traverse(ugat);
        this.printer = ugat.getContentString() + " " + WorkingPrinter;
    }
    
    // Getters
    
    // Methods
    @Override 
    public String toString() {
        return printer;
    }
    
    // Functions
    public static Node traverse(Node n) {
        if(n != null) { // Avoid Null Pointer Exception Error
            if(!n.children.isEmpty()) {   // if Node n has child(ren)
                // Iterate its child(ren) stored in the LinkedList
                Node anak = n.children.removeFirst();
                WorkingPrinter += anak.getContentString() + " ";
                traverse(anak);
            }
            else {  // else if Node n has no more child(ren)
                traverse(n.parent);
            }
        }
        return n;
    }
}
