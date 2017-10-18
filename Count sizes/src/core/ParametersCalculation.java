package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParametersCalculation {
	public static double h = 7;

	public static double k1 = 7;
	public static double k2 = 7;
	public static double k3 = 7;
	public static List<List<Double>> parametersLists = null;
	private static BufferedReader reader = null;

	public static double realh = 7;
	public static double realw = 7;
	public static double realz = 7;

	public static double w = 7;

	// object for writing to file if the user choose to write to a file
	private static BufferedWriter writer = null;

	public static double z = 7;

	public static String fileToPath(String filename) throws UnsupportedEncodingException {
		URL url = ParametersCalculation.class.getResource(filename);
		return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
	}

	public static double getH() {
		return h;
	}

	public static double getK1() {
		return k1;
	}

	public static double getK2() {
		return k2;
	}

	public static double getK3() {
		return k3;
	}

	public static double getRealh() {
		return realh;
	}

	public static double getRealw() {
		return realw;
	}

	public static double getRealz() {
		return realz;
	}

	public static double getW() {
		return w;
	}

	public static double getZ() {
		return z;
	}
	public static List<Double> Select(double z)
	{
		List<Double> listK = new ArrayList<Double>();
		double absMin = 100000;
		int index = 0;
		for (int i = 0; i < parametersLists.size(); i++) {// (List<Double> list :
															// parametersList) {
			if (Math.abs(parametersLists.get(i).get(5) - z) < absMin) {
				absMin = Math.abs(parametersLists.get(i).get(5) - z);
				index = i;
			}
		}
		listK.add(parametersLists.get(index).get(6));
		listK.add(parametersLists.get(index).get(7));
		k1 = parametersLists.get(index).get(6);
		k2 = parametersLists.get(index).get(7);
		return listK;
	}
	public static List<Double> CalculateK (double realH, double realW)
	{
		List<Double> listK = new ArrayList<Double>();
		listK.add(realW/w);
		listK.add(realH/h);
		
		return listK;
	}
	public static List<Double> CalculateK (double realH, double realW, double w, double h)
	{
		List<Double> listK = new ArrayList<Double>();
		listK.add(realW/w);
		listK.add(realH/h);
		return listK;
	}
	public static List<Double> CalculateK ()
	{
		List<Double> listK = new ArrayList<Double>();
		listK.add(realw/w);
		listK.add(realh/h);
		return listK;
	}

	public static void Read(String input) throws NumberFormatException, IOException {
		if (input == null) {
			reader = null;
		} else { // if the user want to save the result to a file
			reader = new BufferedReader(new FileReader(input));
		}

		String line;

		parametersLists = new ArrayList<List<Double>>();
		while (((line = reader.readLine()) != null)) { // for each transaction

			String[] lineSplited = line.split(" ");
			// for each token (item)
			int i = 0;
			List<Double> paramsList = new ArrayList<Double>();
			for (String parameter : lineSplited) {
				// convert from string item to integer
				Double item = Double.parseDouble(parameter);
				if (i == 0)
					setRealh(item);
				else if (i == 1)
					setRealw(item);
				else if (i == 2)
					setRealz(item);
				else if (i == 3)
					setH(item);
				else if (i == 4)
					setW(item);
				else if (i == 5)
					setZ(item);
				else if (i == 6)
					setK1(item);
				else if (i == 7)
					setK2(item);
				else if (i == 8)
					setK3(item);
				paramsList.add(item);
				i++;
			}
			parametersLists.add(paramsList); // increment the tid number
		}
		reader.close(); // close the input file
	}

	public static void ReadAndWrite() throws IOException {
		String output = System.getProperty("user.dir")+"\\ParametersCalculation.txt";
		String input = System.getProperty("user.dir")+"\\ParametersCalculation.txt";
		writer = new BufferedWriter(new FileWriter(output));
		if(reader==null)
			Read(input);

		Write();

		if (writer != null) {
			writer.close();
		}
	}

	public static void setH(double h) {
		ParametersCalculation.h = h;
	}

	public static void setK1(double k1) {
		ParametersCalculation.k1 = k1;
	}

	public static void setK2(double k2) {
		ParametersCalculation.k2 = k2;
	}

	public static void setK3(double k3) {
		ParametersCalculation.k3 = k3;
	}

	public static void setRealh(double realh) {
		ParametersCalculation.realh = realh;
	}

	public static void setRealw(double realw) {
		ParametersCalculation.realw = realw;
	}

	public static void setRealz(double realz) {
		ParametersCalculation.realz = realz;
	}

	public static void setW(double w) {
		ParametersCalculation.w = w;
	}

	public static void setZ(double z) {
		ParametersCalculation.z = z;
	}

	public static void Write() throws IOException {
		if (writer != null) {
			
			for(List<Double>parametersList : parametersLists)
			{
				System.out.println(parametersList.size());
				writer.write(parametersList.get(0)+" "+parametersList.get(1)+" "+parametersList.get(2)+" "+parametersList.get(3)+" "+
						parametersList.get(4)+" "+parametersList.get(5)+" "+parametersList.get(6)+" "+parametersList.get(7)+" "+parametersList.get(8));
				writer.newLine();
			}
			writer.write(getRealh() + " " + getRealw() + " " + getRealz() + " " + getH() + " " + getW() + " " + getZ()
					+ " " + getK1() + " " + getK2() + " " + getK3());
			writer.newLine();
		}
	}

	public ParametersCalculation() {

	}
}
