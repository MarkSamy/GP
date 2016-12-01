package order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Curve {
	static Integer[] ids;
	static long[] values = new long[5000000];
	static long[] index = new long[5000000];
	static int sz = 0;
	long calcZOrder(int x, int y){
		String xx = Integer.toBinaryString(x) ;
		String yy = Integer.toBinaryString(y) ; 
		StringBuilder zero = new StringBuilder("0000000000000000000000000000000000000000000000000000000000000000") ;
		int xl = 63 ;
		for (int i = xx.length()-1 ; i >= 0; i--) {
			zero.setCharAt(xl, xx.charAt(i));
			xl = xl-2 ;
		}
		xl =  62 ;
		for (int j = yy.length()-1 ; j >= 0; j--) {
			zero.setCharAt(xl, yy.charAt(j));
			xl = xl-2 ;
		}
		String str =zero.toString();
		long sum=0;
		for (int i=str.length()-1;i>=0;i--){
			if(str.charAt(i)=='1'){
				sum+=Math.pow(2,63-i);
			}
		}
		return sum;
	}	
	public void read() throws IOException{
	    	File input = new File("nodesv2.txt");
	     //   File input = new File("test");

	        Scanner scanner2 = new Scanner(input);
	        while(scanner2.hasNext())
	        {
	        		String myString2 = scanner2.nextLine();
	        		String[] matches2 = myString2.split(";");
	        		String id = matches2[0];
	        		index[sz] = Long.valueOf(id);
	        		int Lat = (int) (10000000 * Double.valueOf(matches2[1]));
	        		int Longt = (int) (10000000 * Double.valueOf(matches2[2]));
	        		long Zvalue = calcZOrder(Lat,  Longt);
	        		values[sz] = Zvalue;
	        		sz++;
	        }
	        ids = new Integer[sz];
	        for(int i = 0; i < sz; i++)
	        	ids[i] = i;
	}
	static Comparator<Integer> comp = new Comparator<Integer>() {
		public int compare(Integer i, Integer j) {
			if(values[i] == values[j])
				return 0;
			if(values[i] < values[j])
				return -1;
			return 1;
		}
	};
	public void writer() throws IOException
	{
		Arrays.sort(ids, comp);
        FileWriter output = new FileWriter("ScaledNodes.txt");
        for(int i = 0; i < sz; i++)
        {
        	if(i > 0 && values[ids[i]] < values[ids[i - 1]])
        	{
        		System.out.println(i);
        		continue;
        	}
        	output.write(Long.toString(index[ids[i]]) + " " + Long.toString(values[ids[i]]) + "\n");
        }
        output.close();
	}
	public static void main(String[] args) throws IOException{
		Curve c = new Curve();
		c.read();
		c.writer();
	}
	
}