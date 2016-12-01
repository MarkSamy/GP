package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Edges{
	public void getEdges() throws FileNotFoundException{
		ArrayList<ArrayList<String>> edges = new ArrayList<ArrayList<String>>();
		ArrayList<String> nodesindex = new ArrayList<String>();
		
		File levelOne = new File("ScaledNodes.txt");
		Scanner scanner = new Scanner(levelOne);
		while(scanner.hasNext())
        {
        		String myString = scanner.nextLine();
        		String[] matches = myString.split(" ");
        		String node = matches[0];
        		edges.add(new ArrayList<String>());
        		nodesindex.add(node);
        }
		
		File levelTwo = new File("ways.txt");
		scanner = new Scanner(levelTwo);
        
		while(scanner.hasNext())
        {
        		String myString = scanner.nextLine();
        		String[] matches = myString.split(";");
        		String nodes = matches[6];
        		String nodestrimmed = nodes.trim().substring(2, nodes.length()-2);
        		String[] nodesOfWay = nodestrimmed.split(",");
        		
        		for (int i = 0; i < nodesOfWay.length; i++) {
					
				}
        		
        }
	}
}