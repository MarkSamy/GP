package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Edges {
	
	private static final int FLUSH_AMOUNT = 10000;
	
	
	public void getEdges() {
		
		try {
			File ways = new File("ways.txt");
			File edges = new File("edges.txt");
			PrintWriter edgesWriter = new PrintWriter(new FileWriter(edges));
			Scanner scanner = new Scanner(ways);
			int nbEdges = 0;
			while (scanner.hasNext()) {
				String myString = scanner.nextLine();
				String[] matches = myString.split(";");
				if(matches.length <= 6){
					continue;
				}
				String nodes = matches[6];
				String nodestrimmed = nodes.trim().substring(2, nodes.length() - 2);
				String[] nodesOfWay = nodestrimmed.split(",");
				for (int j = 0; j < nodesOfWay.length - 1; j++) {
					//assuming all ways to be two_way if one_way used the use second line only
					if(! (nodesOfWay[j + 1].compareTo(nodesOfWay[j]) == 0) ){
						edgesWriter.println(nodesOfWay[j + 1] + ";" + nodesOfWay[j]);
						edgesWriter.println(nodesOfWay[j] + ";" + nodesOfWay[j + 1]);
						nbEdges+=2;
					}
				}
				if(nbEdges%FLUSH_AMOUNT == 0){
					edgesWriter.flush();
				}
			}
			scanner.close();
			edgesWriter.close();
			System.out.println(nbEdges);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}