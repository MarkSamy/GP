package basicosmparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class ZOrderFinder {

	private static final int FLUSH_AMOUNT = 10000;

	public static void getOrder(File zorder, File table) throws IOException {

		String str = new String();
		Scanner zorderScanner = new Scanner(zorder);
		Scanner tableScanner = new Scanner(table);
		PrintWriter listWriter;
		File listFile = new File("list.txt");
		listWriter = new PrintWriter(new FileWriter(listFile));

		while (zorderScanner.hasNext()) {
			str = zorderScanner.next();
			String[] matches = str.split(" ");
			String nodeID = matches[0];
			int currIndex = 0;
			int size = 0;
			int linesNum = 0;
			while (tableScanner.hasNext()) {
				String row = tableScanner.nextLine();
				String[] tableNode = row.split(" ");
				if (nodeID.equals(tableNode[0])) {
					currIndex = Integer.parseInt(tableNode[1]);
					size = Integer.parseInt(tableNode[2]);
					break;
				}
			}
			
			for (int i = currIndex; i < currIndex + size; i++) {
				Stream<String> lines = Files.lines(Paths.get("sorted.txt"));
				lines = lines.skip(i).limit(size);
				List<String> asList = lines.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
				for (String string : asList) {
					String[] nextNode = string.split(";");
					listWriter.print(nextNode[1] + " ");
				}
				listWriter.println();
			}
			linesNum++;
			if (linesNum % FLUSH_AMOUNT == 0) {
				listWriter.flush();
			}

		}
		tableScanner.close();
		listWriter.close();

		zorderScanner.close();

	}

	public void ZOrderFinderMain() throws IOException {
		File zorder = new File("ScaledNodes.txt");
		File table = new File("table.txt");
		getOrder(zorder, table);

	}
}
