package basicosmparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class ZOrderFinder {

	private static final int FLUSH_AMOUNT = 10000;
	public static String search(String s) throws IOException
	{
		long A = Long.parseLong(s);
		RandomAccessFile raf = new RandomAccessFile("tableEdited.txt", "rw");
		int l = 0, r = 15344734, mid, it = 30;
		while(it-- > 0)
		{
			mid = (l + r) / 2;
			raf.seek(24 * mid);
			String alt = raf.readLine();
			String id[] = alt.split(" ");
			
			long B = 0; 

			B = Long.parseLong(id[0]);
			if(A > B)
					l = mid;
			else if(A == B)
					return alt;
			else	r = mid;
		}
		return "";
	}

	public static void getOrder(File zorder, File table) throws IOException {

		String str = new String();
		Scanner zorderScanner = new Scanner(zorder);
		Scanner tableScanner = new Scanner(table);
		PrintWriter listWriter;
		File listFile = new File("list.txt");
		listWriter = new PrintWriter(new FileWriter(listFile));

		
		
		
		int count=0;

		while (zorderScanner.hasNext()) {
			str = zorderScanner.next();
			String[] matches = str.split(";");
			String nodeID = matches[0];
			int currIndex = 0;
			int size = 0;
			int linesNum = 0;
			int numberOfLine =0 ;
			String ans = search(nodeID);
			RandomAccessFile raf = new RandomAccessFile("sortedEdited.txt", "rw");
			if (ans=="") {
				count++;
			}
			else
			{
				String[]ansMatches=ans.split(" ");
				currIndex=Integer.parseInt(ansMatches[1]);
				size=Integer.parseInt(ansMatches[2]);
			}
			listWriter.print(nodeID+";");
			numberOfLine = currIndex ;
			for (int i = numberOfLine ; i < size+numberOfLine ; i++) {
				raf.seek(24*i);
				String c = raf.readLine();
				String[] st = c.split(";");
				listWriter.print(st[1] + ";" );
			}
			listWriter.println();
			raf.close();
			linesNum++;
			if (linesNum % FLUSH_AMOUNT == 0) {
				listWriter.flush();
			}


		}
		System.out.println(count);

		tableScanner.close();
		listWriter.close();

		zorderScanner.close();

	}

	public void ZOrderFinderMain() throws IOException {
		File zorder = new File("ZOrderSorted.txt");

		File table = new File("tableEdited.txt");

		getOrder(zorder, table);

	}
}
