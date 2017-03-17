package basicosmparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import org.xml.sax.SAXException;
import controller.*;
import database.Database;
import model.Files;

public class BasicOSMParser {

	private void parseOSM(String[] args) throws IOException, SAXException {
		if (args.length != 1) {
			Error e = new Error("\n[ERROR] Invalid parameters.\nCommand usage: basicosmparser <Input OSM XML>");
			throw e;
		} else {
			File input = new File(args[0]);
			OSMParser parser = new OSMParser();
			long start1 = System.nanoTime();
			parser.parse(input);
			long time1 = System.nanoTime() - start1;
			System.out.println("[INFO] Exported data ................... SUCCESS ["+ (time1 / 1e9) +"]");
		}
	}

	private void calcZOrder() throws IOException {
		Curve c = new Curve();
		long start1 = System.nanoTime();
		c.CurveMain(new String[]{Files.NODES_LON_LAT,Files.ZORDER_FILE});
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Exported ZOrder ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void sortEdges() throws IOException {
		ExternalSort es = new ExternalSort();
		long start1 = System.nanoTime();
		es.ExternalSortMain(new String[]{Files.EDGES_FILE,Files.SORTED_EDGES});
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Sorted edges ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void sortAdjacencyList() throws IOException {
		ExternalSort es = new ExternalSort();
		long start1 = System.nanoTime();
		es.ExternalSortMain(new String[]{Files.ADJACENCY_LIST,Files.SORTED_ADJACENCY_LIST});
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Sorted adjacency list ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void generateSmallGraph() throws FileNotFoundException, IOException {
		SmallGraphBuilder sg = new SmallGraphBuilder();
		long start1 = System.nanoTime();
		sg.generateSmallGraph(new String[]{Files.SORTED_ADJACENCY_LIST,Files.SMALL_GRAPH});
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Exported small graph ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void generateSuperGraph() throws IOException {
		SuperGraphBuilder sg = new SuperGraphBuilder();
		long start1 = System.nanoTime();
		sg.SuperGraphBuilderMain(new String[]{Files.SMALL_GRAPH, Files.SUPER_GRAPH_EXTERNAL,Files.SUPER_GRAPH_INTERNAL});
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Exported super graph ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}
	
	private void sortWeightedEdges() throws IOException {
		ExternalSort es = new ExternalSort();
		long start1 = System.nanoTime();
		es.ExternalSortMain(new String[]{Files.WEIGHTED_EDGES_FILE,Files.WEIGHTED_EDGES_FILE_SORTED});
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Sorted weighted edges ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException, SAXException, ClassNotFoundException, SQLException {
		//TODO Remove this line in production
		args = new String[]{Files.MALDIVES_FILE};
		
		BasicOSMParser bop = new BasicOSMParser();
		/**
		 * Parse OSM File and export it as 5 files ( nodes.txt , ways.txt ,
		 * rels.txt , nodesv2.txt(includes only nodes ids,lon and lat of each)
		 * and edges.txt
		 */
		bop.parseOSM(args);
		bop.sortEdges();
		bop.joinFiles();
		bop.calcZOrder(); // Find Z-Order of each node and export to ZOrder.txt
		bop.generateAdjacencyList();
		bop.sortAdjacencyList(); //Sort on ZOrder
		bop.generateSmallGraph();
		bop.generateSuperGraph();
		bop.findWeights();
		bop.sortWeightedEdges();
		bop.dijkstra("2939387830","3132175255");
	}

	private void dijkstra(String source, String dest) throws ClassNotFoundException, SQLException, IOException {
		long start1 = System.nanoTime();
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
		dijkstra.execute(source);
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Dijkstra ................... SUCCESS ["+ (time1 / 1e9) +"]");
		LinkedList<String> path = dijkstra.getPath(dest);
		System.out.println("Vertices : ");
		for (String vertex : path) {
			System.out.println(vertex);
		}
		System.out.println("Distance : ");
		System.out.println(dijkstra.getShortestDistance(dest));
	}

	private void joinFiles() throws ClassNotFoundException, SQLException, IOException {
		File f = new File("test.db");
		Database db = new Database();
		if(!f.exists() || (f.exists() && f.length()==0)){
			db.createTables();
			db.loadEdges();
			db.loadNodes();
		}
		long start1 = System.nanoTime();
		db.join();
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Join ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void generateAdjacencyList() throws IOException {
		AdjacencyListBuilder tp = new AdjacencyListBuilder();
		long start1 = System.nanoTime();
		tp.TP_Main();
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Exported Adjacency list ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}

	private void findWeights() throws IOException {
		WeightedEdges we = new WeightedEdges();
		long start1 = System.nanoTime();
		we.WeightedEdgesMain();
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Exported weighted edges ................... SUCCESS ["+ (time1 / 1e9) +"]");
	}
	
	
}
