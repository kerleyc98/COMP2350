package edu.wit.cs.comp2350.tests;

import java.io.File;
import java.io.IOException;
import java.security.Permission;
import java.util.Random;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import edu.wit.cs.comp2350.Edge;
import edu.wit.cs.comp2350.Graph;
import edu.wit.cs.comp2350.LAB9;

import static org.junit.Assert.*;


public class LAB9TestCase{

	@Rule
	public Timeout globalTimeout = Timeout.seconds(15);
	
	@SuppressWarnings("serial")
	private static class ExitException extends SecurityException {}
	
	private static class NoExitSecurityManager extends SecurityManager 
    {
        @Override
        public void checkPermission(Permission perm) {}
        
        @Override
        public void checkPermission(Permission perm, Object context) {}
        
        @Override
        public void checkExit(int status) { super.checkExit(status); throw new ExitException(); }
    }
	
	@Before
    public void setUp() throws Exception 
    {
        System.setSecurityManager(new NoExitSecurityManager());
    }
	
	@After
    public void tearDown() throws Exception 
    {
        System.setSecurityManager(null);
    }

	private void _testMST(Graph g, double expectedWeight) {
		
		try {
			LAB9.FindMST(g);
		} catch (ExitException e) {}
		
		boolean[] contains = new boolean[g.size()];
		
		Edge[] edges = g.getEdges();
		
		assertEquals("MST must have " + (g.size() - 1) + " edges.", g.size() - 1, edges.length);
		
		double actualWeight = 0;
		for (Edge e: edges) {
			actualWeight += e.cost;
			contains[e.src.ID] = true;
			contains[e.dst.ID] = true;
		}
		
		assertEquals("Returned edges don't match expected weight.", expectedWeight, actualWeight, 1E-4);
		assertEquals("total edge weight doesn't match expected weight.", expectedWeight, g.getTotalEdgeWeight(), 1E-4);
		
		// make sure all vertices are spanned
		for (int i = 0; i < contains.length; i++)
			assertTrue("Missing vertex with ID " + i + ".", contains[i]);
	}
	
	private void _testST(Graph g) {
		
		try {
			LAB9.FindMST(g);
		} catch (ExitException e) {}
		
		boolean[] contains = new boolean[g.size()];
		
		Edge[] edges = g.getEdges();
		
		assertEquals("ST must have " + (g.size() - 1) + " edges",
				g.size() - 1, edges.length);
		
		for (Edge e: edges) {
			contains[e.src.ID] = true;
			contains[e.dst.ID] = true;
		}

		// make sure all vertices are spanned
		for (int i = 0; i < contains.length; i++)
			assertTrue("missing vertex with ID " + i, contains[i]);
		
	}

	private void _testRandMST(Graph g) {
		
		try {
			LAB9.FindMST(g);
		} catch (ExitException e) {}
		
		boolean[] contains = new boolean[g.size()];
		
		Edge[] edges = g.getEdges();
		
		assertEquals("MST must have " + (g.size() - 1) + " edges",
				g.size() - 1, edges.length);
		
		double actualWeight = 0;
		for (Edge e: edges) {
			actualWeight += e.cost;
			contains[e.src.ID] = true;
			contains[e.dst.ID] = true;
		}
		
		assertEquals(actualWeight, g.getTotalEdgeWeight(), 1E-4);
		
		// make sure all vertices are spanned
		for (int i = 0; i < contains.length; i++)
			assertTrue("missing vertex with ID " + i, contains[i]);
	}
	
	private void _testFileST(String file, double expectedWeight, double epsilon, boolean minimal) {
		Graph g = new Graph(epsilon);
		try (Scanner f = new Scanner(new File(file))) {
			while(f.hasNextDouble()) // each node listing
				g.addVertex(f.nextDouble(), f.nextDouble());
		} catch (IOException e) {
			System.err.println("Cannot open file " + file + ". Exiting.");
			System.exit(0);
		}
		if (minimal)
			_testMST(g, expectedWeight);
		else
			_testST(g);
	}
	
	private Graph _genRandGraph(int size) {
		Graph g = new Graph(1.5);
		Random r = new Random();
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < i; j++) {
				g.addVertex(r.nextDouble(), r.nextDouble());
			}
		}
		
		return g;
	}
	
	@Test
	public void testSpanTiny() {
		_testFileST("points/tiny", 2.0, 1.5, false);
	}
	
	@Test
	public void testSpanSmall() {
		_testFileST("points/small1", 2.118, 1.5, false);
		_testFileST("points/small2", 1.5, 1.5, false);
	}
	
	@Test
	public void testSpanPicasso() {
		_testFileST("points/picasso", 3.2167, .3, false);
	}
	
	@Test
	public void testTiny() {
		_testFileST("points/tiny", 2.0, 1.5, true);
	}
	
	@Test
	public void testSmall() {
		_testFileST("points/small1", 2.118, 1.5, true);
		_testFileST("points/small2", 1.5, 1.5, true);
	}
	
	@Test
	public void testPicasso() {
		_testFileST("points/picasso", 3.2167, .3, true);
	}
	
	@Test
	public void testRandSmall() {
		for (int i = 0; i < 10; i++)
			_testRandMST(_genRandGraph(5));
	}
	
	
}