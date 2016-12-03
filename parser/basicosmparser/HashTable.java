package basicosmparser;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class HashTable {
	private static final int FLUSH_AMOUNT = 10000;

	public static void constructTable(File edges, File table) throws IOException {
		Scanner scanner = new Scanner(edges);
		PrintWriter tableWriter = new PrintWriter(new FileWriter(table));
		String cmp = "-1";
		int index = 0;
		int ref=0;
		String str = new String();
		int nbEdges = 0;
		while (scanner.hasNext()) {
			str = scanner.next();
			String[] matches = str.split(";");
			String nodeID = matches[0];
			
			if (!nodeID.equals(cmp)) {
				tableWriter.println(index-ref);
				cmp = nodeID;
				tableWriter.print(cmp + " " + index + " ");
				ref=index;
			}
			index++;
			nbEdges++;
			if (nbEdges % FLUSH_AMOUNT == 0) {
				tableWriter.flush();
			}
		}
		tableWriter.flush();
		tableWriter.close();
		scanner.close();
	}

	public void constructTableMain() {
		File edges = new File("sorted.txt");
		File table = new File("table.txt");
		try {
			constructTable(edges, table);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
