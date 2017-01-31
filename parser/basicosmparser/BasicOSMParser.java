package basicosmparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.xml.sax.SAXException;

import controller.*;
import order.*;

public class BasicOSMParser {

	private void DoProject(String[] args) throws IOException, SAXException {
		String[] argus = new String[1];
//		 argus[0] = "egypt-latest.osm";
		argus[0] = "new-york-latest.osm";
//		 argus[0] = "maldives-latest.osm";
		if (argus.length != 1) {
			System.out.println("[ERROR] Invalid parameters.\nCommand usage: basicosmparser <Input OSM XML>");
		} else {
			File input = new File(argus[0]);
			OSMParser parser = new OSMParser();
			long start1 = System.nanoTime();
			parser.parse(input);
			long time1 = System.nanoTime() - start1;
			System.out.println("[INFO] Exported data ................... SUCCESS ["+ (time1 / 1e9) +"]");
		}
	}

	private void DoZOrder() throws IOException {
		Curve c = new Curve();
		long start1 = System.nanoTime();
		c.CurveMain();
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Exported ZOrder ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void SortEdges() throws IOException {
		ExternalSort es = new ExternalSort();
		long start1 = System.nanoTime();
		es.ExternalSortMain(0);
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Sorted edges ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void SortZOrder() throws IOException {
		ExternalSort es = new ExternalSort();
		long start1 = System.nanoTime();
		es.ExternalSortMain(1);
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Sorted ZOrder ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void SmallGraph() throws FileNotFoundException, IOException {
		SmallGraphBuilder sg = new SmallGraphBuilder();
		long start1 = System.nanoTime();
		sg.generateSmallGraph();
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Exported small graph ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void SuperGraph() throws IOException {
		SuperGraphBuilder sg = new SuperGraphBuilder();
		long start1 = System.nanoTime();
		sg.SuperGraphBuilderMain();
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Exported super graph ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	public static void main(String[] args) throws IOException, InterruptedException, SAXException {
		// TODO Check arguments
		BasicOSMParser bop = new BasicOSMParser();
//		/*
//		 * Parse OSM File and export it as 5 files ( nodes.txt , ways.txt ,
//		 * rels.txt , nodesv2.txt(includes only nodes ids,lon and lat of each)
//		 * and edges.txt
//		 */
		bop.DoProject(args);
		bop.SortEdges();
		/* Create the Hash Table of edges and export to table.txt */
//		bop.CreateHashTable(); 
								/*
								 * Find Z-Order of each node and export to
								 * ScaledNodes.txt
								 */
		bop.DoZOrder();
		bop.AdjacencyList();
		bop.SortZOrder();
		bop.SmallGraph();
		bop.SuperGraph();
//		bop.FindWeights();
	}

	private void AdjacencyList() throws IOException {
		// TODO Auto-generated method stub
		AdjacencyListBuilder tp = new AdjacencyListBuilder();
		long start1 = System.nanoTime();
		tp.TP_Main();
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Exported Adjacency list ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void FindWeights() throws IOException {
		WeightedEdges we = new WeightedEdges();
		long start1 = System.nanoTime();
		we.WeightedEdgesMain();
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Exported weighted edges ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}
	
	
}
