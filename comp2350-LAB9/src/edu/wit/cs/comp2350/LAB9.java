package edu.wit.cs.comp2350;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/* Calculates the minimal spanning tree of a graph 
 * 
 * Wentworth Institute of Technology
 * COMP 2350
 * Lab Assignment 9
 * 
 */

public class LAB9 {
	

	public static ArrayList<Edge> allEdges = new ArrayList<Edge>();
	public static void FindMST(Graph g) {
	
		ArrayList<Edge> T = new ArrayList<Edge>();
		//initialize all the single-node trees
		for(Vertex v : g.getVertices())
		{
			makeSet(v);
			//make some edges
			for(Vertex u : g.getVertices())
			{
				//for every other vertex, if the distance between the two is less than
				//epsilon, make the edge and add it to the list	
				if(getDist(v, u) < g.getEpsilon() && v != u)
				{
					//places edges in sorted order as they're created
					Edge e = new Edge (v,u, getDist(v,u));
					allEdges.add(findEdgeIndex(e), e);
				}
			}
		}
		for(Edge e : allEdges)
		{
			if(findSet(e.src) != findSet(e.dst))
			{
				T.add(e);
				Union(e.src, e.dst);
			}
		}
		for(Edge e : T)
		{
			g.addEdge(e.src, e.dst);
		}
	}
	
	public static int findEdgeIndex(Edge e)
	{
		if(allEdges.isEmpty())
		{
			return 0;
		} else {
			//go until we find an edge that weighs more than the input
			for(int i=0; i<allEdges.size(); i++)
			{
				if(allEdges.get(i).cost > e.cost)
				{
					return i;
				}
			}
		}
		return allEdges.size()-1;
	}
	
	public static void makeSet(Vertex v)
	{
		v.predecessor = v;
		v.rank = 0;
	}
	
	public static Vertex findSet(Vertex v)
	{
		if(v != v.predecessor)
		{
			v.predecessor = findSet(v.predecessor);
		}
		return v.predecessor;
	}
	
	public static void Union(Vertex v, Vertex u)
	{
		Vertex x = findSet(v);
		Vertex y = findSet(u);
		if(x.rank > y.rank)
		{
			y.predecessor = x;
		} else {
			x.predecessor = y;
			if(x.rank == y.rank)
			{
				y.rank++;	
			}
		}
	}
	
	public static double getDist(Vertex v, Vertex u)
	{
		double x = Math.pow(Math.abs(v.x-u.x), 2);
		double y = Math.pow(Math.abs(v.y-u.y), 2);
		return Math.sqrt(x+y);
	}
	
	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/
	

	// reads in an undirected graph from a specific file formatted with one
	// x/y node coordinate per line:
	private static Graph InputGraph(String file1, double epsilon) {
		
		Graph g = new Graph(epsilon);
		try (Scanner f = new Scanner(new File(file1))) {
			while(f.hasNextDouble()) // each vertex listing
				g.addVertex(f.nextDouble(), f.nextDouble());
		} catch (IOException e) {
			System.err.println("Cannot open file " + file1 + ". Exiting.");
			System.exit(0);
		}
		
		return g;
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1;
		
		System.out.printf("Enter <points file> <edge neighborhood>\n");
		System.out.printf("(e.g: points/small .5)\n");
		file1 = s.next();

		// read in vertices
		Graph g = InputGraph(file1, s.nextDouble());
		
		FindMST(g);

		s.close();

		System.out.printf("Weight of tree: %f\n", g.getTotalEdgeWeight());
	}

}
