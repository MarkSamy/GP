package basicosmparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.xml.sax.SAXException;

import controller.*;
import order.*;

public class BasicOSMParser {

	private void DoProject(String[] args) throws IOException, SAXException {
		String[] argus = new String[1];
		// argus[0] = "egypt-latest.osm";
//		argus[0] = "new-york-latest.osm";
		 argus[0] = "maldives-latest.osm";
		if (argus.length != 1) {
			System.out.println("Invalid parameters.\nCommand usage: basicosmparser <Input OSM XML>");
		} else {
			File input = new File(argus[0]);
			OSMParser parser = new OSMParser();
			long start1 = System.nanoTime();
			parser.parse(input);
			long time1 = System.nanoTime() - start1;
			System.out.println(time1 / 1e9);
			System.out.println("Exported data successfully without errors.");
		}
	}

	private void CreateHashTable() {
		HashTable ht = new HashTable();
		long start1 = System.nanoTime();
		ht.constructTableMain();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Exported hash table successfully without errors.");
	}

	private void AdjacencyList(int maxLenTable, int maxLenEdges) throws IOException {
		AdjacencyListBuilder alb = new AdjacencyListBuilder();
		long start1 = System.nanoTime();
		alb.AdjacencyListBuilderMain(maxLenTable, maxLenEdges);
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Exported adjacency list successfully without errors.");
	}

	private void DoZOrder() throws IOException {
		Curve c = new Curve();
		long start1 = System.nanoTime();
		c.CurveMain();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Exported zorder successfully without errors.");
	}

	private int Edit(int caller) throws IOException {
		PrintWriter writer;
		File sorted;
		if (caller == 0) {
			writer = new PrintWriter(new FileWriter("sortedEdgesEdited.txt"));
			sorted = new File("sortedEdges.txt");
		} else {
			writer = new PrintWriter(new FileWriter("tableEdited.txt"));
			sorted = new File("table.txt");
		}
		BufferedReader br = new BufferedReader(new FileReader(sorted));
		int maxLen = 0;
		long start1 = System.nanoTime();
		String myString = br.readLine();
		while (myString != null) {
			if (myString.length() > maxLen) {
				maxLen = myString.length();
			}
			myString = br.readLine();
		}
		int noLines = 0;
		br.close();
		br = new BufferedReader(new FileReader(sorted));
		myString = br.readLine();
		while (myString != null) {
			while (myString.length() < maxLen) {
				myString += " ";
			}
			writer.println(myString);
			noLines++;
			if (noLines % 100000 == 0) {
				writer.flush();
			}
			myString = br.readLine();
		}
		br.close();
		writer.close();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		if (caller == 0) {
			System.out.println("Edited edges successfully without errors.");
		} else {
			System.out.println("Edited hashtable successfully without errors.");
		}
		return maxLen;
	}

	private void SortEdges() throws IOException {
		ExternalSort es = new ExternalSort();
		long start1 = System.nanoTime();
		es.ExternalSortMain(0);
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Sorted edges successfully without errors.");
	}

	private void SortZOrder() throws IOException {
		ExternalSort es = new ExternalSort();
		long start1 = System.nanoTime();
		es.ExternalSortMain(1);
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Sorted zorder successfully without errors.");

	}

	private void SmallGraph() throws FileNotFoundException, IOException {
		SmallGraphBuilder sg = new SmallGraphBuilder();
		long start1 = System.nanoTime();
		sg.generateSmallGraph();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Exported small graph successfully without errors.");
	}

	private void SuperGraph() throws IOException {
		SuperGraphBuilder sg = new SuperGraphBuilder();
		long start1 = System.nanoTime();
		sg.SuperGraphBuilderMain();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Exported super graph successfully without errors.");
	}

	public static void main(String[] args) throws IOException, InterruptedException, SAXException {
		// TODO Check arguments
		BasicOSMParser bop = new BasicOSMParser();
		/*
		 * Parse OSM File and export it as 5 files ( nodes.txt , ways.txt ,
		 * rels.txt , nodesv2.txt(includes only nodes ids,lon and lat of each)
		 * and edges.txt
		 */
		bop.DoProject(args);
		bop.FindWeights();
		bop.SortEdges();
		/* Create the Hash Table of edges and export to table.txt */
		bop.CreateHashTable(); /*
								 * Find Z-Order of each node and export to
								 * ScaledNodes.txt
								 */
		bop.DoZOrder();
		bop.SortZOrder();
		int maxLenEdges = bop.Edit(0);
		int maxLenTable = bop.Edit(1);
		// System.out.println(maxLenTable + " " + maxLenEdges);
		bop.AdjacencyList(maxLenTable, maxLenEdges); // Egypt 21 21
		bop.SmallGraph();
		bop.SuperGraph();
	}

	private void FindWeights() throws IOException {
		WeightedEdges we = new WeightedEdges();
		long start1 = System.nanoTime();
		we.WeightedEdgesMain();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Computed edges weights successfully without errors.");
	}
}
