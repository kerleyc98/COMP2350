package edu.wit.cs.comp2350;

// provides an interface for any graph
public interface GraphInterface {
	
	void addVertex(double x, double y);
	void addEdge(Vertex v, Vertex w);
	
	Vertex[] getVertices();
	Edge[] getEdges();
	
	double getTotalEdgeWeight();
}
