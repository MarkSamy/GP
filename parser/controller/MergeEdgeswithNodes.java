package basicosmparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MergeEdgeswithNodes {

	public static void main(String[] args) throws IOException {
		MergeEdgeswithNodes m = new MergeEdgeswithNodes();
		File fOne = new File("nodesv2.txt");
		File fTwo = new File("sortedEdges.txt");
		m.mergeFiles(fOne, fTwo);
	}

	private void mergeFiles(File fOne, File fTwo) throws IOException {
		BufferedReader brOne = new BufferedReader(new FileReader(fOne));
		BufferedReader brTwo = new BufferedReader(new FileReader(fTwo));
		File listFile = new File("latLng.txt");
		PrintWriter listWriter = new PrintWriter(new FileWriter(listFile));
		int count =0;
		while (true) {
			String sOne = brOne.readLine();
			if(sOne==null)
				break;
			String nodeArrOne[] = sOne.split(";");
			String nodeIdOne = nodeArrOne[0];
			long lastTwo=-1;
			long id1=Long.parseLong(nodeIdOne);

			while(true)
			{
				if(lastTwo!=-1&&lastTwo==id1)
				{
					listWriter.print(id1+";"+nodeArrOne[1]+";"+nodeArrOne[2]+"\n");
				}
				String sTwo = brTwo.readLine();
				if(sTwo==null)
					break;
				String nodeArrTwo[] = sTwo.split(";");
				String nodeIdTwo = nodeArrTwo[0];
				long id2=Long.parseLong(nodeIdTwo);

				if (id1==id2) {
					listWriter.print(id1+";"+nodeArrTwo[1]+";"+nodeArrOne[1]+";"+nodeArrOne[2]+"\n");
					count++;
				}
				if(id1<id2)
				{
					lastTwo=id2;
					break;
				}
				
			
			}
		}
		System.out.println(count);
	}
}