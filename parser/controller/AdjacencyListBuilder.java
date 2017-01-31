package controller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AdjacencyListBuilder {
	
	public void TP_Main() throws IOException {
		File file1 = new File("ZOrder.txt");
		File file2 = new File("sortedEdges.txt");
		FileWriter fileWriter = new FileWriter("list.txt");
		merge(file1, file2, fileWriter);
	}

	private void merge(File file1, File file2, FileWriter out) throws IOException {
		Scanner nodesScanner = new Scanner(file1);
		Scanner edgesScanner = new Scanner(file2);
		String[] nodesMatches;
		String[] edgesMatches = null;
		String nodeId = null;
		String nodeZOrder = null;
		String edgeId = null;
		String str2;
		boolean flag = true;
		boolean prflag = false;
		while (nodesScanner.hasNext()) {
			String str1 = nodesScanner.nextLine();
			nodesMatches = str1.split(";");
			nodeZOrder = nodesMatches[0];
			if (flag == true) {
				str2 = edgesScanner.nextLine();
				edgesMatches = str2.split(";");
				nodeId = nodesMatches[1];
				edgeId = edgesMatches[0];
				flag = false;
			} else {
				nodeId = nodesMatches[1];
			}

			if (nodeId.equals(edgeId)) {
				out.write(nodeZOrder + ";" + nodeId + ";");
				while (nodeId.equals(edgeId)) {
					out.write(edgesMatches[1]);

					if (edgesScanner.hasNext()) {

						str2 = edgesScanner.nextLine();
						edgesMatches = str2.split(";");
						edgeId = edgesMatches[0];
						if (nodeId.equals(edgeId)) {
							out.write(";");
						}
					} else {
						prflag = true;
						break;
					}
				}
				if (prflag) {

				} else
					out.write("\n");
			}
		}
		nodesScanner.close();
		edgesScanner.close();
		out.close();
	}

}
