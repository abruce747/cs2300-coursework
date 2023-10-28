package abruce_package_PA2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import Jama.Matrix;

public class Abruce_pA22 {
	private File input_file;
	private Scanner input_reader;
	
	private Matrix encoded_Mat;
	private Matrix mat_A;
	private Matrix mat_A_inv;//decoding matrix
	private Matrix decoded_Mat;
	
	public Abruce_pA22() {
		input_file = new File("input-A22.txt");
		try {
			input_reader = new Scanner(input_file);
		}catch(FileNotFoundException f) {
			System.out.println("File input-A22.txt is missing.");
		}
		
		// Initialize mat_A
		double[][] temp_encode_mat = { { 1, -1, -1, 1 }, { 2, -3, -5, 4 }, { -2, -1, -2, 2 }, { 3, -3, -1, 2 } };
		mat_A = new Matrix(temp_encode_mat);
		mat_A_inv = mat_A.inverse();
	}//end constructor
	
	public void do_tasks() {
		/**Need to:
		 * 	1. Read the data and enter it into encoded_Mat.
		 *  2. Multiply mat_A_inv by encoded_Mat and store in decoded_Mat.
		 *  3. Put decoded_Mat into a list and convert the data to characters.
		 *  4. Print the message.
		 */	
		
		//1:
		load_encoded();//encoded_Mat is loaded
		
		//2:
		decode();//decoded_Mat is loaded
		
		//3:
		ArrayList<Character> converted_list = convert_to_chars(mat_to_list(decoded_Mat));
		
		//4:
		print_list(converted_list);
	}
	
	public void print_list(ArrayList<Character> list) {
		String line = "";
		
		for(int index = 0; index < list.size(); index++) {
			line += list.get(index).charValue();
		}
		
		System.out.println("Decoded message is:\n\t" + line);
	}
	
	public ArrayList<Character> convert_to_chars(ArrayList<Integer> num_list){
		ArrayList<Character> list = new ArrayList<Character>();
		
		for (int index = 0;  index < num_list.size(); index++) {
			list.add((char)num_list.get(index).intValue());
		}
		
		return list;
	}
	
	public ArrayList<Integer> mat_to_list(Matrix mat) {
		//Iterate columns, then rows
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for(int c = 0; c < mat.getColumnDimension(); c++) {
			for(int r = 0; r < mat.getRowDimension(); r++) {
				list.add((int)mat.get(r, c));
			}
		}//end loops
		
		return list;
	}
	

	public void decode() {
		//Multiply mat_A_inv by encoded_Mat, store in decoded_Mat
		decoded_Mat = mat_A_inv.times(encoded_Mat);
	}
	
	public void load_encoded() {
		ArrayList<Double> encoded_sequence = new ArrayList<Double>();
		
		//Store the numbers to determine how many there are. 
		while(input_reader.hasNext()) {
			encoded_sequence.add((Double)input_reader.nextDouble());
		}
		
		//Determine the size of the double[4][] column count. Round up.
		int num_cols = 0;
		if (encoded_sequence.size() % 4 == 0) {
			num_cols = encoded_sequence.size() / 4;
		} else {
			num_cols = ((int)(encoded_sequence.size() / 4)) + 1;
		}
		
		//Create the double[][]
		double[][] temp_encoded_mat = new double[4][num_cols];
		
		//Load the double[][] with 0s by columns first, rows second
		for(int row = 0; row < temp_encoded_mat.length; row++) {
			for(int col = 0; col < num_cols; col++) {
				temp_encoded_mat[row][col] = 0.0;
			}
		}
		
		//Overwrite the 0s with actual data
		int r = 0;
		int c = 0;
		for (int index = 0; index < encoded_sequence.size(); index++) {
			temp_encoded_mat[r][c] = encoded_sequence.get(index).doubleValue();
			switch (r) {
				case 0, 1, 2:
					r++;
					break;
				case 3:
					// Reset r to 0
					r = 0;
					// Advance c by 1
					c++;
					break;
			}// end switch block
		} // end for loop
		
		//Load the data into the encoded_Mat object
		encoded_Mat = new Matrix(temp_encoded_mat);
	}//end load_encoded() method
	
}//end Abruce_pA22 class
