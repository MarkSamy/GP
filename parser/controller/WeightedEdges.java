package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import model.Files;

public class WeightedEdges {

	private static final int FLUSH_AMOUNT = 10000;

	public void WeightedEdgesMain() throws IOException {
		File join = new File(Files.JOIN_FILE);
		BufferedReader br = new BufferedReader(new FileReader(join));
		PrintWriter tableWriter = new PrintWriter(new FileWriter(Files.WEIGHTED_EDGES_FILE));
		DistanceCaltulator distanceCaltulator = new DistanceCaltulator();
		int nbEdges = 0;
		String myString = br.readLine();
		while (myString != null) {
			String[] matches = myString.split(";");
			tableWriter.print(matches[0] + ";" + matches[3] + ";");
			tableWriter.println(distanceCaltulator.getDistanceFromLatLonInKm(Double.valueOf(matches[1]),
					Double.valueOf(matches[2]), Double.valueOf(matches[4]), Double.valueOf(matches[5])));

			if (nbEdges % FLUSH_AMOUNT == 0) {
				tableWriter.flush();
			}
			nbEdges = nbEdges + 2;
			myString = br.readLine();
		}
		tableWriter.close();
		br.close();
//		if(join.delete()){
//			System.out.println("Deleted " + join.getName() + " successfully");
//		}
	}
}
