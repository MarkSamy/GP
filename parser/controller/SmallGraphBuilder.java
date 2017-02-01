package controller;

import java.io.*;
import java.util.HashMap;

public class SmallGraphBuilder {

	private static final int NODES_PER_PAGE = 4000;
	private static final int FLUSH_AMOUNT = 10000;

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
		String file = "sortedlistTP.txt";
		int maxLen = setOffsets(file);
		int size = countLines(file);
		// //// System.out.println(maxLen + " " + size);//253 15344731
		int pageNo = 0;
		PrintWriter smallGraphWriter = new PrintWriter(new FileWriter("smallgraph.txt"));
		while (size > 0) {
			loadPage("listTableEdited.txt", maxLen, pageNo, smallGraphWriter);
			pageNo++;
			smallGraphWriter.flush();
			size -= NODES_PER_PAGE;
		}
		smallGraphWriter.close();
	}

	private int setOffsets(String file) throws IOException {
		PrintWriter writer = new PrintWriter(new FileWriter("listTable.txt"));
		BufferedReader brOne = new BufferedReader(new FileReader(file));
		long sizeInBytes = 0;
		writer.print(sizeInBytes + ";");
		long start1 = System.nanoTime();
		String s = brOne.readLine();
		int index = 0;
		int flush = 0;
		while (s != null) {
			sizeInBytes += (s.length() + 1);
			index++;
			if (index % NODES_PER_PAGE == 0) {
				writer.println(sizeInBytes);
				writer.print(sizeInBytes + ";");
				index = 0;
			}
			flush += 2;
			if (flush % FLUSH_AMOUNT == 0) {
				writer.flush();
			}
			s = brOne.readLine();
		}
		writer.println(sizeInBytes);
		brOne.close();
		writer.close();
		int maxLen = 0;
		BufferedReader brTwo = new BufferedReader(new FileReader("listTable.txt"));
		String line = brTwo.readLine();
		while (line != null) {
			if (line.length() > maxLen) {
				maxLen = line.length();
			}
			line = brTwo.readLine();
		}
		brTwo.close();
		PrintWriter lTableWriter = new PrintWriter(new FileWriter("listTableEdited.txt"));
		BufferedReader brThree = new BufferedReader(new FileReader("listTable.txt"));
		String myString = brThree.readLine();
		int noLines = 0;
		while (myString != null) {
			while (myString.length() < maxLen) {
				myString += " ";
			}
			lTableWriter.println(myString);
			noLines++;
			if (noLines % 100000 == 0) {
				lTableWriter.flush();
			}
			myString = brThree.readLine();
		}
		brThree.close();
		lTableWriter.close();
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Set file offsets ................... SUCCESS [" + (time1 / 1e9) + "]");
		return maxLen;
	}

	private void loadPage(String file, int maxLen, int page, PrintWriter pw) throws IOException {
		HashMap<String, Integer> hm = new HashMap<>();
		StringBuilder sb = new StringBuilder();
		OptimizedRandomAccessFile raf = new OptimizedRandomAccessFile(new File(file), "r");
		long startOffset = -1, endOffset = -1;
		raf.seek((maxLen + 2) * page); // Get line that includes my offsets
		String offsets = raf.readLine().trim();
		raf.close();
		String[] offsetsMatches = offsets.split(";");
		startOffset = Long.parseLong(offsetsMatches[0]);
		endOffset = Long.parseLong(offsetsMatches[1]);
		OptimizedRandomAccessFile rafList = new OptimizedRandomAccessFile("list.txt", "r");
		long temp = startOffset;
		while (temp != endOffset) {
			rafList.seek(temp);
			String s = rafList.readLine();
			if (s != null) {
				String[] nodes = s.split(";");
				hm.put(nodes[1], 0);
			}
			temp += (s.length() + 1);
		}
		temp = startOffset;
		while (temp != endOffset) {
			rafList.seek(temp);
			String s = rafList.readLine();
			if (s != null) {
				String[] nodes = s.split(";");
				pw.print(nodes[1]);
				for (int j = 2; j < nodes.length; j++) {
					if (!hm.containsKey(nodes[j])) {
						sb.append(";" + nodes[j]);
					} else {
						pw.print(";" + nodes[j]);
					}
				}
				if (sb.length() > 0) {
					pw.print(";external");
					pw.print(sb);
				}
				pw.println();
				sb = new StringBuilder();
			}
			temp += (s.length() + 1);
		}
		rafList.close();
	}
	
}
