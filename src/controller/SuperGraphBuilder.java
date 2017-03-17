package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @inputs the whole adjacency list file (test2.txt)
 * @outputs SuperGraphExternalAdjacencyList.txt that contains external edges,
 *          edges connecting two different super-nodes and
 *          SuperGraphInternalAdjacencyList.txt, that contains internal edges,
 *          edges connecting two different nodes in the same super-node
 */

public class SuperGraphBuilder {
	
	private static final int NODES_PER_PAGE = 4000;
	private final static int FLUSH_AMOUNT = 10000;

	public void SuperGraphBuilderMain(String[] args) throws IOException {
		File file = new File(args[0]);
		/*
		args[0] Files.SMALL_GRAPH
		args[1] Files.SUPER_GRAPH_EXTERNAL
		args[2] Files.SUPER_GRAPH_INTERNAL
		*/
		PrintWriter externalGraphWriter = new PrintWriter(
				new FileWriter(new File(args[1])));
		PrintWriter internalGraphWriter = new PrintWriter(
				new FileWriter(new File(args[2])));
		int nbLines = 0;
		int pageNo = 0;
		externalGraphWriter.println("page " + pageNo);
		internalGraphWriter.println("page " + pageNo);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String Edge = br.readLine();
		while (Edge!=null) {
			if (Edge.contains("external")) {
				String[] nodes = Edge.split(";");
				if(nodes[1].compareTo("external") != 0){
					int k = 1;
					internalGraphWriter.print(nodes[0]);
					while (nodes[k].compareTo("external") != 0) {
						internalGraphWriter.print(";" + nodes[k]);
						k++;
					}
					internalGraphWriter.println();
				}
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
			if (nbLines % NODES_PER_PAGE == 0) {
				pageNo++;
				externalGraphWriter.println("page " + pageNo);
				internalGraphWriter.println("page " + pageNo);
			}
			if (nbLines % FLUSH_AMOUNT == 0) {
				externalGraphWriter.flush();
				internalGraphWriter.flush();
			}
			Edge = br.readLine();
		}
		externalGraphWriter.close();
		internalGraphWriter.close();
		br.close();
	}
}
