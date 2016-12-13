package controller;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class SmallGraphBuilder {

	private static final int NODES_PER_PAGE = 4000;

	private int countLines(String file) throws IOException {
		int i = 0;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = br.readLine();
		while (line != null) {
			i++;
			line = br.readLine();
		}
		br.close();
		return i;
	}

	public void generateSmallGraph() throws FileNotFoundException, IOException {
		String file = "list.txt";
		int maxLen = Edit(file);
		int size = countLines(file);
		int pageNo = 0;
		PrintWriter smallGraphWriter = new PrintWriter(new FileWriter("smallgraph.txt"));
		while (size > 0) {
			loadPage("listEdited.txt", maxLen, pageNo, smallGraphWriter);
			pageNo++;
			smallGraphWriter.flush();
			size -= NODES_PER_PAGE;
		}
		smallGraphWriter.close();
	}

	private int Edit(String file) throws IOException {
		PrintWriter writer = new PrintWriter(new FileWriter("listEdited.txt"));
		Scanner scanner = new Scanner(new File(file));
		int maxLen = 0;
		long start1 = System.nanoTime();
		while (scanner.hasNext()) {
			String myString = scanner.nextLine();
			if (myString.length() > maxLen) {
				maxLen = myString.length();
			}
		}
		int noLines = 0;
		Scanner scannerTwo = new Scanner(new File(file));
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
		System.out.println("Edited list successfully without errors.");
		return maxLen;
	}

	private void loadPage(String file, int maxLineLen, int offset, PrintWriter pw) throws IOException {
		HashMap<String, Integer> hm = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		RandomAccessFile raf = new RandomAccessFile(new File(file), "r");
		for (int i = (offset * NODES_PER_PAGE); i < ((offset * NODES_PER_PAGE) + NODES_PER_PAGE); i++) {
			raf.seek((maxLineLen + 2) * i);
			String s = raf.readLine();
			if (s.trim() != null) {
				String[] matches = s.split(";");
				hm.put(matches[0], 0);
			}
		}
		for (int i = (offset * NODES_PER_PAGE); i < ((offset * NODES_PER_PAGE) + NODES_PER_PAGE); i++) {
			raf.seek((maxLineLen + 2) * i);
			String s = raf.readLine();
			if (s.trim() != null) {
				String[] matches = s.trim().split(";");
				pw.print(matches[0]);
				for (int j = 1; j < matches.length; j++) {
					if (!hm.containsKey(matches[j])) {
						sb.append(";" + matches[j]);
					} else {
						pw.print(";" + matches[j]);
					}
				}
			}
			if (sb.length() > 0) {
				pw.print(";external");
				pw.print(sb);
			}
			pw.println();
			sb = new StringBuilder();
		}
		raf.close();
	}
}