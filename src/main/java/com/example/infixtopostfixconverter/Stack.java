package com.example.infixtopostfixconverter;

public class Stack {
    private cursorArray cursorArray = new cursorArray(50);
    private int index;
    private int size;

    public Stack() {
        index = cursorArray.create();
    }

    public void push(String data) throws IndexOutOfBoundsException {
        this.size++;
        cursorArray.addFirst(data, index);
    }

    public void clear() {
        cursorArray = new cursorArray(index);
        size = 0;

    }

    public boolean isEmpty() {
        return cursorArray.isEmptiy(index);
    }


    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    public String pop() {
        this.size--;
        return cursorArray.deleteFirst(index);
    }

    public String peek() {
        return cursorArray.FirstRevel(index);
    }

    /**
     * @return the cursorArray
     */
    public cursorArray getCursorArray() {
        return cursorArray;
    }

    /**
     * @param cursorArray the cursorArray to set
     */
    public void setCursorArray(cursorArray cursorArray) {
        this.cursorArray = cursorArray;
    }


}
