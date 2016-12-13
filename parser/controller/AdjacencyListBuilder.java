package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class AdjacencyListBuilder {

	private static final int FLUSH_AMOUNT = 10000;

	private static int countLines(File table) throws IOException {
		int i = 0;
		BufferedReader br = new BufferedReader(new FileReader(table));
		String line = "";
		while (line != null) {
			line = br.readLine();
			i++;
		}
		br.close();
		return (i-1);
	}

	private String search(String s, int maxLen, int rBinSearch, int maxComp, RandomAccessFile raf) throws IOException {
		long A = Long.parseLong(s);
		// TODO Change it value
		int l = 0, mid, it = maxComp, r = rBinSearch;
		while (it-- > 0) {
			mid = (l + r) / 2;
			raf.seek((maxLen + 2) * mid);
			String alt = raf.readLine();
			String id[] = alt.split(" ");
			long B = Long.parseLong(id[0]);
			if (A > B) {
				l = mid;
			} else if (A == B) {
				raf.close();
				return alt;
			} else {
				r = mid;
			}
		}
		raf.close();
		return "";
	}
	
	private void getOrder(File zorder, File table, int maxLenTable, int maxLenEdges) throws IOException {
		Scanner zorderScanner = new Scanner(zorder);
		File listFile = new File("list.txt");
		PrintWriter listWriter = new PrintWriter(new FileWriter(listFile));
		int r = countLines(table);
		System.out.println("count " + r);
		int linesNum = 0;
		int maxComp = (int) (Math.log(r) / Math.log(2)) + 1;
		System.out.println(maxComp);
		while (zorderScanner.hasNext()) {
			String line = zorderScanner.nextLine();
			String[] matches = line.split(";");
			String nodeID = matches[0];
			int currIndex = 0;
			int size = 0;
			int numberOfLine = 0;
			RandomAccessFile searchRaf = new RandomAccessFile("tableEdited.txt", "r");
			String ans = search(nodeID, maxLenTable, r, maxComp, searchRaf);
			StringBuilder sb;
			RandomAccessFile raf = new RandomAccessFile("sortedEdgesEdited.txt", "r");
			if (!(ans.compareToIgnoreCase("") == 0)) {
				String[] ansMatches = ans.split(" ");
				currIndex = Integer.parseInt(ansMatches[1]);
				size = Integer.parseInt(ansMatches[2]);
				sb = new StringBuilder();
				sb.append(nodeID + ";");
				// listWriter.print(nodeID + ";");
				numberOfLine = currIndex;
				for (int i = numberOfLine; i < size + numberOfLine; i++) {
					raf.seek((maxLenEdges + 2) * i);
					String c = raf.readLine();
					String[] st = c.split(";");
					// listWriter.print(st[1] + ";");
					sb.append(st[1].trim() + ";");
				}
				listWriter.println(sb);
				raf.close();
				linesNum++;
				if (linesNum % FLUSH_AMOUNT == 0) {
					listWriter.flush();
					linesNum = 0;
				}
			}
		}
		listWriter.close();
		zorderScanner.close();
	}

	public void AdjacencyListBuilderMain(int maxLenTable, int maxLenEdges) throws IOException {
		File zorder = new File("sortedZOrder.txt");
		File table = new File("tableEdited.txt");
		getOrder(zorder, table, maxLenTable, maxLenEdges);
	}
}
