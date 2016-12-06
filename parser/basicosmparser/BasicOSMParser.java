package basicosmparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.xml.sax.SAXException;

import controller.OSMParser;
import order.Curve;

public class BasicOSMParser {

	public void Export_Edges() throws FileNotFoundException {
		Edges ed = new Edges();
		long start1 = System.nanoTime();
		ed.getEdges();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Exported edges successfully without errors.");
		return;
		// throw new FileNotFoundException();
	}

	public void DoProject(String[] args) {
		String[] argus = new String[1];
		// argus[0] = "egypt-latest.osm";
		argus[0] = "new-york-latest.osm";
		if (argus.length != 1) {
			System.out.println("Invalid parameters.\nCommand usage: basicosmparser <Input OSM XML> <Output folder>");
		} else {
			File input = new File(argus[0]);

			// Check input
			if (!input.exists() || !input.isFile() || !input.canRead()) {
				System.out.println("Can't read input OSM XML.\nCheck if file exists and is readable.");
			} else {
				// Convert data
				OSMParser parser = new OSMParser();
				try {
					long start1 = System.nanoTime();
					parser.parse(input);
					long time1 = System.nanoTime() - start1;
					System.out.println(time1 / 1e9);
					System.out.println("Exported data successfully without errors.");
				} catch (IOException | SAXException e) {
					System.err.println("Error during data parsing.");
					e.printStackTrace();
				}
			}
		}
	}

	public void CreateHashTable() {
		HashTable ht = new HashTable();
		long start1 = System.nanoTime();
		ht.constructTableMain();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Exported hash table successfully without errors.");
	}

	public void ZOrder() throws IOException {
		ZOrderFinder zof = new ZOrderFinder();
		zof.ZOrderFinderMain();
	}

	public void DoZOrder() throws IOException {
		Curve c = new Curve();
		long start1 = System.nanoTime();
		c.CurveMain();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out.println("Exported zorder successfully without errors.");
	}
	
	public void EditSorted() throws IOException{
		PrintWriter writer = new PrintWriter(new FileWriter("tableEdited.txt"));
		File sorted = new File("table.txt");
		Scanner scanner = new Scanner(sorted);
		int max = 0 ; 
		while (scanner.hasNext()) {
			String myString = scanner.nextLine();
//			if(myString.length()>max){
//				max = myString.length() ; 
//			}
//			System.out.println(max);
			while (myString.length() < 22)
				myString += " ";
			writer.println(myString);
		}
		writer.close();
		scanner.close();
//		System.out.println(max);
	}
	public static void main(String[] args) throws IOException, InterruptedException {
		//TO-DO Check arguments
		 BasicOSMParser bop = new BasicOSMParser();
//		 bop.DoProject(args); // Parse OSM File and export it as 4 files ( nodes.txt , ways.txt , rels.txt , nodesv2.txt(includes only nodes ids,lon and lat of each)
//		 bop.Export_Edges(); // Export Edges of the OSM file to edges.txt
//		 bop.CreateHashTable(); // Create the Hash Table of edges and export to table.txt
//		 bop.DoZOrder(); // Find Z-Order of each node and export to ScaledNodes.txt
//		 bop.EditSorted();
		 bop.ZOrder();
	}
}
