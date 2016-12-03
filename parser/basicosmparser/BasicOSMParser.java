package basicosmparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.xml.sax.SAXException;

import controller.OSMParser;
import order.Curve;

public class BasicOSMParser {
	
	public void Export_Edges() throws FileNotFoundException{
		Edges ed = new Edges();
		long start1 = System.nanoTime();
		ed.getEdges();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out
				.println("Exported edges successfully without errors.");
		return;
//		throw new FileNotFoundException();
	}
	
	public void DoProject(String[] args){
		String[] argus = new String[1];
		argus[0] = "egypt-latest.osm";
		//914436ms  15min 63711
		//Only Reading 15.231915983sec
//		argus[0] = "map.osm"; //12260ms 13sec   1751
//		argus[0] = "new-york-latest.osm";
		//67.10631337 sec for reading only
		if (argus.length != 1) {
			System.out
					.println("Invalid parameters.\nCommand usage: basicosmparser <Input OSM XML> <Output folder>");
		} else {
			File input = new File(argus[0]);

			// Check input
			if (!input.exists() || !input.isFile() || !input.canRead()) {
				System.out
						.println("Can't read input OSM XML.\nCheck if file exists and is readable.");
			} else {
				// Convert data
				OSMParser parser = new OSMParser();
				try {
					long start1 = System.nanoTime();
					parser.parse(input);
					long time1 = System.nanoTime() - start1;
					System.out.println(time1 / 1e9);
					System.out
							.println("Exported data successfully without errors.");
				} catch (IOException | SAXException e) {
					System.err.println("Error during data parsing.");
					e.printStackTrace();
				}
			}
		}
	}
	
	public void CreateHashTable(){
		HashTable ht = new HashTable();
		long start1 = System.nanoTime();
		ht.constructTableMain();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out
				.println("Exported hash table successfully without errors.");
	}
	
	public void ZOrder() throws IOException{
		ZOrderFinder zof = new ZOrderFinder();
		zof.ZOrderFinderMain();
	}
	
	public void DoZOrder(){
		Curve c = new Curve();
		long start1 = System.nanoTime();
		c.CurveMain();
		long time1 = System.nanoTime() - start1;
		System.out.println(time1 / 1e9);
		System.out
				.println("Exported zorder successfully without errors.");
	}
	
	public static void main(String[] args) throws IOException {
		// Check arguments
//		BasicOSMParser bop = new BasicOSMParser();
//		bop.DoProject(args); //Parse OSM File and export it as 3 files ( nodes.txt , ways.txt , rels.txt )
//		bop.Export_Edges();  //Export Edges of the OSM file to edges.txt
//		bop.CreateHashTable(); //Create the Hash Table of edges and export to table.txt
//		bop.DoZOrder(); //Find Z-Order of each node and export to ScaledNodes.txt
//		bop.ZOrder();
//		String fourthLine;
//		String whatever = "unsorted.txt";
//		try {
//			Process process = new ProcessBuilder("sort --field-separator=';' -n -k 1,1 -k 2,2 unsortedtest.txt").start();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		RandomAccessFile raf = new RandomAccessFile("rels.txt", "r");
		raf.seek(5);
		System.out.println("" + raf.readUTF());
	}
}
