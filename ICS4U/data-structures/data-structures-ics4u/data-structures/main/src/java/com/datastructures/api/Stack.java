package com.datastructures.api;

public class Stack {
    public int top;
    private int[] data;

    /**
     * Stack constructor.
     * 
     * @param max_items Maximum size of Stack
     */
    public Stack(int max_items) {
        data = new int[max_items];
        top = -1;
    }

    /**
     * Pushes a new Integer onto the top of the stack.
     * 
     * @param x Integer value to be pushed onto the top of the stack
     */
    public void push(int x) {
        if (top < data.length) {
            top++;
            data[top] = x;
        }
        else {
            System.out.println("Stack Overflowed!");
        }
    }

    /**
     * Removes and returns top most element of stack.
     * 
     * @return  Integer of top most element in stack or -1 + an error message if stack underflow
     */
    public int pop() {
        if (top > -1) {
            return data[top--];
        }
        System.out.println("Stack Underflow!");
        return -1;
    }

    /**
     * Returns the topmost item of stack.
     * 
     * @return  Top most integer value of stack, or -1 if empty.
     */
    public int peek() {
        if (top > -1) {
            return data[top];
        }
        System.out.println("Stack is empty");
        return -1;
    }

    /**
     * Gets the current size of the stack.
     * 
     * @return  Returns integer value of current size of stack
     */
    public int size() {
        return top+1;
    }

    /**
     * Extracts numbers from this stack into another stack until a negative is found.
     * 
     * @return  Returns a stack containing positive numbers until the first negative number is found
     */
    public Stack popNegative() {
        int stack_size = this.size();
        Stack new_stack = new Stack(stack_size);

        while (this.peek() > 0) {
            new_stack.push(this.pop());
        }
        return new_stack;
    }

    /**
     * Recursively iterates through Stack to calculate sum.
     * <p>
     * Pops and stores topmost element as c_element, sets sum equal to c_element + the next item by
     * recursively calling this.sum() until we reach the bottom of the stack. Then, push c_element
     * back onto the stack in the same order.
     * 
     * @return  Integer value containing sum or zero + an error message if Stack is empty
     */
    public int sum() {
        int sum = 0;
        int c_element;
        if (top > -1) {
            c_element = this.pop();
            sum = c_element + this.sum();
            this.push(c_element);
        }
        return sum;
    }

    /**
     * Recursively iterates through Stack and concatenates into String str.
     * <p>
     * Pops and stores topmost element as c_element, sets str equal to c_element + the next item by
     * recursively calling this.toString() until we reach the bottom of the stack. Then, push c_element
     * back onto the stack in the same order.
     * 
     * @return  String value containing contents of this Stack, or EMPTY if empty
     */
    public String toString() {
        String str = "";
        int c_element;
        if (top > -1) {
            c_element = this.pop();
            str = c_element + " " + this.toString();
            this.push(c_element);
        }
        return str;
    }

}
