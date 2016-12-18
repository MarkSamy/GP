package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class WeightedEdges {

	private static final int FLUSH_AMOUNT = 10000;

	public static void main(String[] args) throws IOException {
		File join = new File("Join.txt");
		Scanner scanner = new Scanner(join);
		long startTime = System.currentTimeMillis();
		PrintWriter tableWriter = new PrintWriter(new FileWriter("weightedEdges.txt"));

		DistanceCaltulator distanceCaltulator = new DistanceCaltulator();
		int nbEdges = 0;
		while (scanner.hasNext()) {
			String myString = scanner.nextLine();
			String[] matches = myString.split(";");
			tableWriter.print(matches[0] + ";" + matches[3] + ";");
			tableWriter.println(distanceCaltulator.getDistanceFromLatLonInKm(Double.valueOf(matches[1]),
					Double.valueOf(matches[2]), Double.valueOf(matches[4]), Double.valueOf(matches[5])));

			if (nbEdges % FLUSH_AMOUNT == 0) {
				tableWriter.flush();
				System.out.println(nbEdges);
			}
			nbEdges = nbEdges + 2;
		}
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);
		tableWriter.close();
		scanner.close();
	}
}
