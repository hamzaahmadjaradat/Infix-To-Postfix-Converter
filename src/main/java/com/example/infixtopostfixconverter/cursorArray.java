package com.example.infixtopostfixconverter;

public class cursorArray {
    private int size = 50;
    Node[] arrayCursor;

    public cursorArray(int size) {
        this.size = size;
        arrayCursor = new Node[size];
        initializing();
    }

    public int getSize() {
        return size;
    }

    private void initializing() {
        for (int i = 0; i < arrayCursor.length - 1; i++)
            arrayCursor[i] = new Node(null, i + 1);
        arrayCursor[arrayCursor.length - 1] = new Node(null, 0);
    }

    public int alloc() {
        int p = arrayCursor[0].next;
        arrayCursor[0].next = arrayCursor[p].next;
        return p;
    }

    public void reset(int i) {
        while (!isEmptiy(i))
            deleteFirst(i);

    }

    public boolean isLast(int p) {
        return arrayCursor[p].next == 0;
    }

    public void Cursorfree(int p) {
        arrayCursor[p] = new Node(null, arrayCursor[0].next);
        arrayCursor[0].setNext(p);
    }

    public boolean isNull(int p) {
        return arrayCursor[p] == null;
    }

    public boolean isEmptiy(int p) {
        return arrayCursor[p].next == 0;
    }

    public int create() {
        int p = alloc();
        if (p != 0)
            arrayCursor[p].next = 0;
        else
            p = -1;
        return p;
    }

    public void addFirst(String data, int list) {
        if (isNull(list))
            return;
        int p = alloc();
        if (p != 0) {
            arrayCursor[p] = new Node(data, arrayCursor[list].next);
            arrayCursor[list].next = p;
        } else
            System.out.println("Full");
    }

    public String deleteFirst(int list) {
        if (!isEmptiy(list)) {
            int temp = arrayCursor[list].next;
            Node flag = arrayCursor[temp];
            arrayCursor[list].next = flag.next;
            Cursorfree(temp);
            return flag.data;
        }
        return null;

    }

    public String FirstRevel(int pos) {

        int flag = arrayCursor[pos].next;
        Node temp = arrayCursor[flag];

        return temp.data;
    }

    public Node[] getArrayCursor() {
        return arrayCursor;
    }

    public void setArrayCursor(Node[] arrayCursor) {
        this.arrayCursor = arrayCursor;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
