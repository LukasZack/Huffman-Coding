package p2.DataStructures.SortedList;
/**
 * Implementation of a Sorted List using a Singly Linked List structure
 * 
 * @author Fernando J. Bermudez - bermed28
 * @author Lucas Maldonado Montanez - 841 18 0561
 * @version 3.0
 * @since 03/28/2023
 * @param <E> 
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}				
	} // End of Node class

	
	private Node<E> head; // First DATA node (This is NOT a dummy header node)
	
	/*
	 * Constructor for the SortedLinkedList class
	 */
	
	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}

	/*
	 * Adds an element to the sorted linked list.
	 *
	 * @param e The element to add
	 */
	
	@Override
	public void add(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the new value is the smallest */
	    if (e == null) {
	        throw new IllegalArgumentException("Value cannot be null");
	    }

	    Node<E> newNode = new Node<>(e);
	    Node<E> prevNode = null;
	    Node<E> curNode = head;

	    while (curNode != null && e.compareTo(curNode.getValue()) >= 0) {
	        prevNode = curNode;
	        curNode = curNode.getNext();
	    }

	    if (prevNode == null) {
	        head = newNode;
	    } else {
	        prevNode.setNext(newNode);
	    }
	    newNode.setNext(curNode);
	    currentSize++;
	}

	/*
	 * Removes an element from the sorted linked list.
	 *
	 * @param e The element to remove
	 * @return true if the element was removed, false otherwise
	 */
	
	@Override
	public boolean remove(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the value is found at the head node */
		Node<E> prevNode = null;
		Node<E> curNode = head;

		while (curNode != null && !curNode.getValue().equals(e)) {
			prevNode = curNode;
			curNode = curNode.getNext();
		}

		if (curNode == null) {
			return false;
		}

		if (prevNode == null) {
			head = curNode.getNext();
		} else {
			prevNode.setNext(curNode.getNext());
		}
		curNode.clear();
		currentSize--;

		return true;
	}
	
	/*
	 * Removes the element at the specified index.
	 *
	 * @param index The index of the element to remove
	 * @return The removed element
	 */

	@Override
	public E removeIndex(int index) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when index = 0 */
		if (index < 0 || index >= currentSize) {
			throw new IndexOutOfBoundsException("Invalid index");
		}

		Node<E> prevNode = null;
		Node<E> curNode = head;

		for (int i = 0; i < index; i++) {
			prevNode = curNode;
			curNode = curNode.getNext();
		}

		E value = curNode.getValue();

		if (prevNode == null) {
			head = curNode.getNext();
		} else {
			prevNode.setNext(curNode.getNext());
		}
		curNode.clear();
		currentSize--;

		return value;
	}
	
	/*
	 * Returns the first index of the specified element.
	 *
	 * @param e The element to search for
	 * @return The index of the first occurrence of the element, or -1 if not found
	 */

	@Override
	public int firstIndex(E e) {
		/* TODO ADD CODE HERE */
		Node<E> curNode = head;
		int index = 0;

		while (curNode != null) {
			if (curNode.getValue().equals(e)) {
				return index;
			}
			curNode = curNode.getNext();
			index++;
		}

		return -1;
	}
	
	/*
	 * Returns the element at the specified index.
	 *
	 * @param index The index of the element to retrieve
	 * @return The element at the specified index
	 */

	@Override
	public E get(int index) {
		/* TODO ADD CODE HERE */
		if (index < 0 || index >= currentSize) {
			throw new IndexOutOfBoundsException("Invalid index");
		}

		Node<E> curNode = head;

		for (int i = 0; i < index; i++) {
			curNode = curNode.getNext();
		}

		return curNode.getValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Comparable<E>[] toArray() {
		int index = 0;
		Comparable[] theArray = new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}

}