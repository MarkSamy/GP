package basicosmparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xml.sax.SAXException;

import controller.OSMParser;
import controller.Edges;
public class BasicOSMParser {
	// OTHER METHODS
	public static void main(String[] args) throws FileNotFoundException {
		// Check arguments
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
//				Edges ed = new Edges();
//				ed.getEdges();
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
}
