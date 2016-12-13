package basicosmparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import org.xml.sax.SAXException;

import controller.Edges;
import controller.ExternalSort;
import controller.HashTable;
import controller.OSMParser;
import controller.SmallGraphBuilder;
import controller.SuperGraphBuilder;
import controller.AdjacencyListBuilder;
import order.Curve;

public class BasicOSMParser {

	private void Export_Edges() throws FileNotFoundException {
		Edges ed = new Edges();
		long start1 = System.nanoTime();
		ed.getEdges();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Exported edges successfully without errors.");
		return;
	}

	private void DoProject(String[] args) throws IOException, SAXException {
		String[] argus = new String[1];
		// argus[0] = "egypt-latest.osm";
		argus[0] = "new-york-latest.osm";
		if (argus.length != 1) {
			System.out.println("Invalid parameters.\nCommand usage: basicosmparser <Input OSM XML>");
		} else {
			File input = new File(argus[0]);
			// Check input
			if (!input.exists() || !input.isFile() || !input.canRead()) {
				System.out.println("Can't read input OSM XML.\nCheck if file exists and is readable.");
			} else {
				// Convert data
				OSMParser parser = new OSMParser();
				long start1 = System.nanoTime();
				parser.parse(input);
				long time1 = System.nanoTime() - start1;
				System.out.println(time1 / 1e9);
				System.out.println("Exported data successfully without errors.");
			}
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
		AdjacencyListBuilder zof = new AdjacencyListBuilder();
		long start1 = System.nanoTime();
		zof.AdjacencyListBuilderMain(maxLenTable, maxLenEdges);
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
		Scanner scanner = new Scanner(sorted);
		int maxLen = 0;
		long start1 = System.nanoTime();
		while (scanner.hasNext()) {
			String myString = scanner.nextLine();
			if (myString.length() > maxLen) {
				maxLen = myString.length();
			}
		}
		int noLines = 0;
		Scanner scannerTwo = new Scanner(sorted);
		while (scannerTwo.hasNext()) {
			String myString = scannerTwo.nextLine();
			while (myString.length() < maxLen) {
				myString += " ";
			}
			writer.println(myString);
			noLines++;
			if (noLines % 100000 == 0) {
				writer.flush();
			}
		}
		writer.close();
		scanner.close();
		scannerTwo.close();
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
		// BasicOSMParser bop = new BasicOSMParser();
		/*
		 * Parse OSM File and export it as 4 files ( nodes.txt , ways.txt ,
		 * rels.txt , nodesv2.txt(includes only nodes ids,lon and lat of each)
		 */
		// bop.DoProject(args);
		// bop.Export_Edges(); // Export Edges of the OSM file to edges.txt
		// bop.SortEdges();
		// /* Create the Hash Table of edges and export to table.txt */
		// bop.CreateHashTable();
		// /* Find Z-Order of each node and export to ScaledNodes.txt */
		// bop.DoZOrder();
		// bop.SortZOrder();
		// int maxLenEdges = bop.Edit(0);
		// int maxLenTable = bop.Edit(1);
		// System.out.println(maxLenTable + " " + maxLenEdges);
		// bop.AdjacencyList(maxLenTable, maxLenEdges);
		// bop.AdjacencyList(21, 21); Egypt 21 21
		// bop.SmallGraph();
		// bop.SuperGraph();
	}
}
