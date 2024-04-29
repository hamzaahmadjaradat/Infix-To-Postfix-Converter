package com.example.infixtopostfixconverter;

public class Node {
    String data;
    int next;

    public Node(String data, int next) {
        this.data = data;
        this.next = next;
    }

    public Node() {
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String toString() {
        return "[" + data + "]";
    }
}
