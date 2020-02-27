package edu.wit.cs.comp2350;

public class Edge {
	
	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/

	public Vertex src;
	public Vertex dst;
	public double cost;
	
	// creates an edge between two vertices
	Edge(Vertex s, Vertex d, double c) {
		src = s;
		dst = d;
		cost = c;
	}

}
