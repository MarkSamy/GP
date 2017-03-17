package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class Curve {

	private static final int FLUSH_AMOUNT = 10000;

	private BigInteger calcZOrder(int x, int y) {
		String xBinValue = Integer.toBinaryString(x);
		String yBinValue = Integer.toBinaryString(y);
		StringBuilder zeroBinValue = new StringBuilder(
				"0000000000000000000000000000000000000000000000000000000000000000");
		int xl = 63;
		for (int i = xBinValue.length() - 1; i >= 0; i--) {
			zeroBinValue.setCharAt(xl, xBinValue.charAt(i));
			xl = xl - 2;
		}
		xl = 62;
		for (int j = yBinValue.length() - 1; j >= 0; j--) {
			zeroBinValue.setCharAt(xl, yBinValue.charAt(j));
			xl = xl - 2;
		}
		String str = zeroBinValue.toString();
		BigInteger sum = BigInteger.ZERO;
		for (int i = str.length() - 1; i >= 0; i--) {
			if (str.charAt(i) == '1') {
				// Math.pow(2, 63 - i)
				BigInteger temp = BigInteger.valueOf(2);
				sum = sum.add(temp.pow(63 - i));
			}
		}
		return sum;
	}

//TO BE USED IF NEEDED	
/**
	private BigInteger calcZOrder3D(int x, int y, int z) {
		String xBinValue = Integer.toBinaryString(x);
		String yBinValue = Integer.toBinaryString(y);
		String zBinValue = Integer.toBinaryString(z);
		StringBuilder zeroBinValue = new StringBuilder(
				"0000000000000000000000000000000000000000000000000000000000000000");
		int xl = 63;
		for (int i = xBinValue.length() - 1; i >= 0; i--) {
			zeroBinValue.setCharAt(xl, xBinValue.charAt(i));
			xl = xl - 3;
		}
		xl = 62;
		for (int j = yBinValue.length() - 1; j >= 0; j--) {
			zeroBinValue.setCharAt(xl, yBinValue.charAt(j));
			xl = xl - 3;
		}
		xl = 61;
		for (int j = zBinValue.length() - 1; j >= 0; j--) {
			zeroBinValue.setCharAt(xl, zBinValue.charAt(j));
			xl = xl - 3;
		}
		String str = zeroBinValue.toString();
		BigInteger sum = BigInteger.ZERO;
		for (int i = str.length() - 1; i >= 0; i--) {
			if (str.charAt(i) == '1') {
				// Math.pow(2, 63 - i)
				BigInteger temp = BigInteger.valueOf(2);
				sum = sum.add(temp.pow(63 - i));
			}
		}
		return sum;
	}
*/
	private void read(String[] args) throws IOException {
		File input = new File(args[0]);
		File output = new File(args[1]);
		PrintWriter scaleWriter = new PrintWriter(new FileWriter(output));
		BufferedReader br = new BufferedReader(new FileReader(input));
		String node = br.readLine();
		int sz = 0;
		while (node != null) {
			String[] matches = node.split(";");
			int latValue = (int) (10000000 * Double.valueOf(matches[1]));
			int lonValue = (int) (10000000 * Double.valueOf(matches[2]));
			BigInteger Zvalue = calcZOrder(latValue, lonValue);
			scaleWriter.write(Zvalue.toString() + ";");
			scaleWriter.println(matches[0]);
			sz++;
			if (sz % FLUSH_AMOUNT == 0) {
				scaleWriter.flush();
			}
			node = br.readLine();
		}
		scaleWriter.close();
		br.close();
//		if(input.delete()){
//			System.out.println(input.getName() + " is deleted!");
//		}else{
//			System.out.println("Delete " + input.getName()+ " failed.");
//		}
	}

	public void CurveMain(String[] args) throws IOException {
		Curve c = new Curve();
		c.read(args);
	}
}