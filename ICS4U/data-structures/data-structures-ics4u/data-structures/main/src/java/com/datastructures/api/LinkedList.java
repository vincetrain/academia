package com.datastructures.api;

public class LinkedList {
	private Node head;	// The head of the tail

	/**
	 * Constructor for Linked List.
	 */
	public LinkedList() {
		head = null;
	}

	/**
	 * Adds new Node to front of Linked List and feeds given paramters into new Node.
	 * 
	 * @param name	The name of the country
	 * @param population	The population of the country
	 */
	public void addAtFront(String name, int population) {
		Node node = new Node(name, population);
		node.setNext(head);
		head = node;
	}
	
	/**
	 * Iterates between each Linked List node and concatenates to String str, where it is then returned.
	 * 
	 * @return	Returns string containing contents of the Linked List 
	 */
	public String toString() {
		Node c_node = head;
		String str = "";
		// While the current Node is not null,
		while (c_node != null) {
			// Iterate through each present Node and add information into string
			str += ("[" + c_node.getName() + ", " + c_node.getPopulation() + "] ");
			c_node = c_node.getNext();
		}
		// Prints linked list is empty if no information is put into str
		if (str.equals("")) {
			System.out.println("Country list is empty.");	
		}
		return str;
	}

	/**
	 * Iterates between each Linked List node and determines what is the smallest populated country.
	 * 
	 * @return Returns name of smallest population country
	 */
	public String smallestPopulation() {
		Node c_node = head;
		String c_country = c_node.getName();
		int c_population = c_node.getPopulation();
		while (c_node != null) {
			// Iterate through each present Node and add information into string
			if (c_node.getPopulation() < c_population) {
				c_country = c_node.getName();
				c_population = c_node.getPopulation();
			}
			c_node = c_node.getNext();
		}
		// Prints linked list is empty if no information is put into str
		if (c_country.equals("")) {
			System.out.println("Country list is empty.");	
		}
		return c_country + ", " + c_population;
	}

	/**
	 * Iterates between each Linked List node and determines what is the country with longest name.
	 * 
	 * @return Returns country with longest name
	 */
	public String longestName() {
		Node c_node = head;
		String c_country = "";
		String temp;
		while (c_node != null) {
			// Iterate through each present Node and add information into string
			temp = c_node.getName();
			if (temp.length() > c_country.length()) {
				c_country = temp;
				System.out.println(c_country);
			}
			c_node = c_node.getNext();
		}
		// Prints linked list is empty if no information is put into str
		if (c_country.equals("")) {
			System.out.println("Country list is empty.");	
			System.out.println(c_country);
		}
		return c_country;
	}

	/**
	 * Reverses the Linked List
	 */
	public void reverse() {
		Node c_node = head;	// Sets current node to head
		Node p_node = null;	// Previous node to null
		Node n_node = null;	// Next node to null
		// Loops while c_node is not null
		while (c_node != null) {
			n_node = c_node.getNext();	// Sets n_node as next node in Linked List
			c_node.setNext(p_node);	// Sets next node in Linked List to previous node
			p_node = c_node;	// Sets p_node to current c_node
			c_node = n_node;	// Sets c_node to n_node, which is the old next node of Linked List
		}
		head = p_node;	// Sets head to null
	}

	// sub-class Node structure for Linked List
	public class Node {
		private String name;
        private int population;
		private Node next;	// recursively links Node to keep track of what next Node is.

		/**
		 * Constructor for Node.
		 * 
		 * @param name	The name of the country
		 * @param population	The population of the country
		 */
		public Node(String name, int population) {
			this.name = name;
            this.population = population;
			next = null;
		}

		/**
		 * Returns next Node to be accessed.
		 * 
		 * @return	Returns next Node if found or null if current Node is the tail
		 */
		public Node getNext() {
			return (next);
		}

		/**
		 * Sets next variable to next node when called.
		 * 
		 * @param next	Next variable of a Node
		 */
		public void setNext(Node next) {
			this.next = next;
		}

		/**
		 * Returns the name of the current Node.
		 * 
		 * @return	Returns name of current Node
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the population of the current Node.
		 * 
		 * @return	Returns population of current Node
		 */
        public int getPopulation() {
            return population;
        }
	}
}
