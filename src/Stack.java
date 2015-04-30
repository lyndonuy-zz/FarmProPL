/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */
//Lyndon A. Uy
//3CSD, 3csd-47
//Lab Exercise 7
public class Stack<T> {
    private int top=0; 
    private final static int stackMax = 100; 
    private Object[] stk = new Object[stackMax+1]; 
    
    public Stack(){
        
    }
    public void clear() 
    { 
        top=0; 
    } 
    public boolean isEmpty() 
    { 
        if(top==0){return true;} 
        else
            return false; 
    } 
    public void push(T el) 
    { 
        if(top==stackMax) 
            System.out.print("Stack Push Overflow Error"); 
        else
        { 
            top=top+1; 
            stk[top]=el; 
            //System.out.print(stk[top] + " ");
        } 
    } 
    public T pop() 
    { 
        if(isEmpty()) 
        { 
            System.out.println("Stack Underflow Error"); 
            return null; 
        } 
        else
        { 
            top=top-1; 
            return (T)stk[top+1]; 
        } 
    } 
    public T top() 
    { 
        if(isEmpty()) 
        { 
            System.out.println("Stack is empty"); 
            return null; 
        } 
        else
        { 
            return (T)stk[top]; 
        } 
    } 
    public int FindMinimum(){
        if(!isEmpty()){
            Stack<Integer> st1 = new Stack();
            int t, min = Integer.parseInt(this.top().toString());
            if(this.isEmpty()){
                
            }
            else{
                while(!isEmpty()){
                    st1.push(Integer.parseInt(this.pop().toString()));
                    t = Integer.parseInt(st1.top().toString());
                    System.out.println(t);
                    if(t < min){
                        min = t;
                        
                    }
                }
                while(st1.isEmpty() == false){
                    this.push((T)st1.pop());
                }
            }
            return min;
        }
        else{
            return 0;
        }
    }
}
