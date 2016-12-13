package order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Scanner;

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

	private void read() throws IOException {
		File input = new File("edges.txt");
		File output = new File("edgesZOrder.txt");
		PrintWriter scaleWriter = new PrintWriter(new FileWriter(output));
		Scanner scanner = new Scanner(input);
		int sz = 0;
		while (scanner.hasNext()) {
			String node = scanner.nextLine();
			String[] matches = node.split(";");
			int latValue = (int) (10000000 * Double.valueOf(matches[1]));
			int lonValue = (int) (10000000 * Double.valueOf(matches[2]));
			BigInteger Zvalue = calcZOrder(latValue, lonValue);
			scaleWriter.write(matches[0] + ";");
			scaleWriter.println(Zvalue.toString());
			sz++;
			if (sz % FLUSH_AMOUNT == 0) {
				scaleWriter.flush();
			}
		}
		scaleWriter.close();
		scanner.close();
	}

	public void CurveMain() throws IOException {
		Curve c = new Curve();
		c.read();
	}
}
