/**
 * Note: the input file for this part must be in the format:
 * 	x1 y1
 * 	x2 y2
 * 	x3 y3
 *  ...
 *  and its name must be written into the Main method
 */

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import Jama.Matrix;

import org.jfree.chart.ui.*;
import org.jfree.chart.util.*;
import org.jfree.chart.ChartPanel;


public class Abruce_p2 {
	private File input_file;
	private File output_file;

	private Scanner input_reader;

	private FileWriter output_writer;

	private Matrix mat_X;//col 0 is line spacing, col 1 is x coordinates
	private Matrix mat_Y;//y coordinates
	private Matrix mat_A;//coeff mat A

	private double const_b;
	private double coeff_m;

	private PlotExample graph; 

	public Abruce_p2(String input_file_name, String output_file_name) {
		try {
			input_file = new File(input_file_name);
			output_file = new File(output_file_name);//Likely need to change extension
			input_reader = new Scanner(input_file);
			output_writer = new FileWriter(output_file);

			//Method to initialize matrices:
			init_mats();

			const_b = 0.0;
			coeff_m = 0.0;

		}catch(FileNotFoundException fnf) {
			System.out.println("Error raised in Abruce_p2 constructor:"
					+ "\nInput file is missing with name:" + input_file_name);
		}catch(IOException ioe) {
			System.out.println("Error raised in Abruce_p2 constructor:"
					+ "\nOutput file error with name: \"p2_output\"");
		}//end try/catch block

	}//end constructor

	public void do_tasks() {
		//Data has been read in during Abruce_p2 instance creation.

		//Perform matrix calculations:
		mat_A = do_calculations();

		//Interpret results for equation:
		const_b = mat_A.get(0, 0);
		coeff_m = mat_A.get(1, 0);

		//Write the equation into a text file
		write_to_file();

		//Graph the equation and given points
		display_charts();

	}//end do_tasks() method

	public Matrix do_calculations() {
		//Calculate X^(T) * X:
		Matrix mat_Xt = mat_X.transpose();
		Matrix mat_XtX = mat_Xt.times(mat_X);

		//Calculate X^(T) * Y:
		Matrix mat_XtY = mat_Xt.times(mat_Y);

		//Invert XtX:
		Matrix mat_XtXi = mat_XtX.inverse();

		//Find coeff mat_A = mat_XtXi * mat_XtY
		return mat_XtXi.times(mat_XtY);
	}

	public void init_mats() {
		//Read the number of rows
		//If row_count == -1, error.
		int row_count = count_rows(input_file);
		if (row_count == -1) {
			return;
		}

		//Assume row_count != -1.
		//Create the matrices:
		//mat_X: load with 1s
		mat_X = new Matrix(row_count, 2, 1.0);

		//mat_Y: load with 0s
		mat_Y = new Matrix(row_count, 1);

		//Read the data into the matrices:
		read_input();

	}//end init_mats() method

	public void read_input(){
		/**Input written as data points in format:
		 * 	x1 y1
		 * 	x2 y2
		 * 	x3 y3
		 * 	...
		 * 
		 * 	Will need to read in the line, split, 
		 * 		send index 0 to mat_X(row, 2) 
		 * 		and index 1 to mat_Y(row, 1).
		 * 	Row value is stored in the matrices' 
		 * 		row variables and they're the same value.
		 */
		for(int row = 0; row < mat_X.getRowDimension(); row++) {
			if(input_reader.hasNextLine()) {
				//Read input line and split into String array
				String[] split_line = input_reader.nextLine().trim().split(" ");

				if(split_line.length != 2) {
					System.out.println("p2: read_input():"
							+ "\n\tError with split_line[] length. mat_X and mat_Y not loaded.");
					continue;
				}else {
					//Pass index 0 to mat_X(row, 1)
					mat_X.set(row, 1, Double.valueOf(split_line[0]).doubleValue());

					//Pass index 1 to mat_Y(row, 0)
					mat_Y.set(row, 0, Double.valueOf(split_line[1]).doubleValue());
				}//end length if/else block

			}else {
				System.out.println("p2: read_input():"
						+ "\n\tInput line number mismatch. Either counted or read incorrectly.");
			}//end input_reader.hasNextLint() if/else block
		}//end row for loop

	}//end read_input() method

	//Temp objects used to avoid messing up the global Scanner object.
	public int count_rows(File temp_file) {
		try {
			int row_count = 0;
			Scanner temp_reader = new Scanner(temp_file);

			while(temp_reader.hasNextLine()) {
				temp_reader.nextLine();
				row_count++;
			}

			return row_count;
		}catch(FileNotFoundException fnfe) {
			System.out.println("Error raised in Abruce_p2 count_rows(File):"
					+ "\nScanner can't find the file.");
			return -1;
		}//end try/catch block
	}//end count_rows(File) method

	public void display_charts(){
		//Graph the equation and given points and put the equation and graph into output file.
		graph = new PlotExample(mat_X, 
				mat_Y, 
				const_b, 
				coeff_m, 
				get_largest_x(), 
				500,
				eq_string(),
				input_file.getName());
//		graph.print_dataset_line();
//		graph.print_dataset_point();
		graph.setAlwaysOnTop(true);
		
		graph.pack();
		UIUtils.centerFrameOnScreen(graph);
		graph.setVisible(true);
	}//end display_chart method

	public int get_largest_x(){
		//Return largest X value, truncated, and increased by some arbitrary buffer.
		int largest = 0;
		for(int r = 0; r < mat_X.getRowDimension(); r++) {
			if (mat_X.get(r, 1) > largest) {
				largest = (int)mat_X.get(r, 1);
			}
		}//end loop
		largest += 5;
		
		return largest;
	}//end get_largest_x() method
	
	public String eq_string(){
		if(const_b >= 0) {
			return String.format("\n\ty = %.1fx + %.1f", 
					coeff_m, 
					const_b);
		}else {
			return String.format("\n\ty = %.1fx - %.1f", 
					coeff_m, 
					Math.abs(const_b));
		}
	}//end eq_string method
	
	public void write_to_file(){
		//Write the equation to the file
		//Proper formatting for positive/negative, but couldn't find another way to put in spaces.
		String equation = "Equation, rounded to the nearest tenth:"; 		
		if(const_b >= 0) {
			equation = String.format("\ny = %.1fx + %.1f", 
					coeff_m, 
					const_b);
		}else {
			equation = String.format("\ny = %.1fx - %.1f", 
					coeff_m, 
					Math.abs(const_b));
		}

		equation += "\nThe graph should appear as a window when running the program."
				+ "\nI couldn't figure out the location or name of the class files "
				+ "\n\tI'd need in order to create a JPEG of the graph.";

		try {
			output_writer.append(equation);
			output_writer.close();
		}catch(IOException ioe) {
			System.out.println("Trouble writing equation to output file in Abruce_p2: write_to_file()");
		}
	}//end write_to_file method

	//Use this for testing and checking matrices
	public void print_mats() {		
		System.out.println("Matrix X:");
		mat_X.print(3, 1);

		System.out.println("Matrix Y:");
		mat_Y.print(3, 1);

		System.out.println("Matrix A:");
		mat_A.print(3, 1);

		//Proper formatting for positive/negative, but couldn't find another way to put in spaces.
		if(const_b >= 0) {
			System.out.printf("Linear equation in y=mx+b form, "
					+ "rounded to the nearest tenth:"
					+ "\n\ty = %.1fx + %.1f\n", coeff_m, const_b);
		}else {
			System.out.printf("Linear equation in y=mx+b form, "
					+ "rounded to the nearest tenth:"
					+ "\n\ty = %.1fx - %.1f\n", coeff_m, Math.abs(const_b));

		}//end if/else block
	}//end print_mats() method

}//end Abruce_p2 class
