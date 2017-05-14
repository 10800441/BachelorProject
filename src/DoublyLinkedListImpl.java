

import java.util.ArrayList;
import java.util.NoSuchElementException;

/****************************************************
 **                Bachelor project                **
 ** DrawGraph.java                                 **
 ** Source: http://stackoverflow.com/questions/8693342/drawing-a-simple-line-graph-in-java
 ** Adjusted to work for a TSP instantiation       **
 ****************************************************
 */

public class DoublyLinkedListImpl<E> {

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedListImpl() {
        size = 0;
    }
    /**
     * this class keeps track of each element information
     * @author java2novice
     * https://www.java2novice.com/data-structures-in-java/linked-list/doubly-linked-list/
     */
    private class Node {
        E element;
        Node next;
        Node prev;

        public Node(E element, Node next, Node prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }
    /**
     * returns the size of the linked list
     * @return
     */
    public int size() { return size; }

    /**
     * return whether the list is empty or not
     * @return
     */
    public boolean isEmpty() { return size == 0; }

    /**
     * adds element at the starting of the linked list
     * @param element
     */


    public void addFirst(E element) {
        Node tmp = new Node(element, head, null);
        if(head != null ) {head.prev = tmp;}
        head = tmp;
        if(tail == null) { tail = tmp;}
        size++;

    }

    /**
     * adds element at the end of the linked list
     * @param element
     */
    public void addLast(E element) {

        Node tmp = new Node(element, null, tail);
        if(tail != null) {tail.next = tmp;}
        tail = tmp;
        if(head == null) { head = tmp;}
        size++;

    }

    /**
     * this method walks forward through the linked list
     */
    public ArrayList<E> iterateForward(){
ArrayList<E> elements= new ArrayList<>();
           Node tmp = head;
        while(tmp != null){
            elements.add(tmp.element);
            tmp = tmp.next;
        }
        return elements;
    }

    /**
     * this method walks backward through the linked list
     */
    public void iterateBackward(){

        System.out.println("iterating backword..");
        Node tmp = tail;
        while(tmp != null){
            System.out.println(tmp.element);
            tmp = tmp.prev;
        }
    }

    /**
     * this method removes element from the start of the linked list
     * @return
     */
    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        Node tmp = head;
        head = head.next;
        head.prev = null;
        size--;
        System.out.println("deleted: "+tmp.element);
        return tmp.element;
    }

    /**
     * this method removes element from the end of the linked list
     * @return
     */
    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        Node tmp = tail;
        tail = tail.prev;
        tail.next = null;
        size--;
        System.out.println("deleted: "+tmp.element);
        return tmp.element;
    }

    // Added functionality "elementAt"
    // source: http://algorithms.tutorialhorizon.com/doubly-linked-list-complete-implementation/
    public E elementAt(int index){
        if(index>size) throw new NoSuchElementException();
        Node n = head;
        while(index-1!=0){
            n=n.next;
            index--;
        }
        return n.element;
    }

    // Given a city yhis operation computes the next city in the list
    public E next(E key) {
        Node tmp = head;
        while (tmp != null && !tmp.element.toString().equals(key.toString()) && !tmp.element.toString().equals(tail.element.toString())) {
          tmp = tmp.next;
        }

        if (tmp == null)
            throw new NoSuchElementException();

        if(tail.element.toString().equals(key.toString()))
            return head.element;
        tmp = tmp.next;
        return tmp.element;

    }

    // Given a city this operation computes the next city in the list
    public E prev(E key) {
        Node tmp = tail;
        while (tmp != null && !tmp.element.toString().equals(key.toString())) {
            tmp = tmp.prev;
        }

        if (tmp == null)
            throw new NoSuchElementException();

        if(head.element.toString().equals(key.toString()))
            return tail.element;
        tmp = tmp.prev;
        return tmp.element;

    }

    public static void main(String a[]){


    }
}