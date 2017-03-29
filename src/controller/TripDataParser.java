package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import model.Trip;

public class TripDataParser {
	
	public TripDataParser(String[] filesNames) throws IOException{
		for (String file : filesNames) {
			parse(file);
		}
	}
	
	private void parse(String fileName) throws IOException{
		long start1 = System.nanoTime();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = br.readLine();
		while(true){
			line = br.readLine();//skip first line which contains csv headers
			if(line == null || line.equals(null)){
				break;
			}
			Trip t = new Trip(line);
			//TODO check that distance equals to Dijkstra
			/*
			 * Get point from (t.getPickLat() , t.getPickLon() )
			 * Get point to (t.getDropLat() , t.getDropLon() )
			 * Distance d = Dikjstra(from,to);
			 * Path p Actual path with the given t.getDistance()
			 * if d not equal to t.getDistance()
			 * 		flagJam = true;
			 */
		}
		br.close();
		long time1 = System.nanoTime() - start1;
		System.out.println("[INFO] Trip Data parse ................... SUCCESS [" + (time1 / 1e9) + "]");
	}

}
