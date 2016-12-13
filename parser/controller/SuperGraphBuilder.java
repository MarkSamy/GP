package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @inputs the whole adjacency list file (test2.txt)
 * @outputs SuperGraphExternalAdjacencyList.txt that contains external edges,
 *          edges connecting two different super-nodes and
 *          SuperGraphInternalAdjacencyList.txt, that contains internal edges,
 *          edges connecting two different nodes in the same super-node
 */

public class SuperGraphBuilder {

	private final static int FLUSH_AMOUNT = 10000;

	public void SuperGraphBuilderMain() throws IOException {
		File file = new File("smallgraph.txt");
		PrintWriter externalGraphWriter = new PrintWriter(
				new FileWriter(new File("SuperGraphExternalAdjacencyList.txt")));
		PrintWriter internalGraphWriter = new PrintWriter(
				new FileWriter(new File("SuperGraphInternalAdjacencyList.txt")));
		Scanner scanner = new Scanner(file);
		int nbLines = 0;
		int pageNo = 0;
		externalGraphWriter.println("page " + pageNo);
		internalGraphWriter.println("page " + pageNo);
		while (scanner.hasNext()) {
			String Edge = scanner.nextLine();
			if (Edge.contains("external")) {
				String[] nodes = Edge.split(";");
				int i = 0;
				while (nodes[i].compareTo("external") != 0) {
					i++;
				}
				externalGraphWriter.print(nodes[0]);
				for (int j = i + 1; j < nodes.length; j++) {

					externalGraphWriter.print(";" + nodes[j]);
				}
				externalGraphWriter.println();
			} else {
				String[] nodes = Edge.split(";");
				internalGraphWriter.print(nodes[0]);
				for (int j = 1; j < nodes.length; j++) {
					internalGraphWriter.print(";" + nodes[j]);
				}
				internalGraphWriter.println();
			}
			nbLines++;
			if (nbLines % 4000 == 0) {
				pageNo++;
				externalGraphWriter.println("page " + pageNo);
				internalGraphWriter.println("page " + pageNo);
			}
			if (nbLines % FLUSH_AMOUNT == 0) {
				externalGraphWriter.flush();
				internalGraphWriter.flush();
			}
		}
		externalGraphWriter.close();
		internalGraphWriter.close();
		scanner.close();
	}
}
