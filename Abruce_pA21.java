package abruce_package_PA2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import Jama.Matrix;

public class Abruce_pA21 {
	private Matrix mat_A;// Encoding matrix
	private Matrix mat_B;// Plaintext matrix (ASCII numbers for chars)
	private Matrix mat_C;// Resulting coded matrix.

	private File input_file;
	private Scanner file_scanner;

	public Abruce_pA21() {
		input_file = new File("input-A21.txt");

		try {
			file_scanner = new Scanner(input_file);
		} catch (FileNotFoundException f) {
			System.out.println("Missing input-A21.txt file.");
		}

	}// end constructor

	public void do_tasks() {
		// Initialize mat_A
		double[][] temp_encode_mat = { { 1, -1, -1, 1 }, { 2, -3, -5, 4 }, { -2, -1, -2, 2 }, { 3, -3, -1, 2 } };
		mat_A = new Matrix(temp_encode_mat);

		//Initialize mat_B
		fill_mat_B(read_input_file());
		
		//Initialize mat_C
		mat_C = new Matrix(mat_A.getRowDimension(), mat_B.getColumnDimension());
		fill_mat_C();
		
		
		//Print mat_B and mat_C
		//print mat_B(column width, decimal points)
		System.out.print("Matrix B:");
		mat_B.print(2, 0);
		//Print mat_C(column width, decimal points)
		System.out.print("\nMatrix C:");
		mat_C.print(2, 0);
	}

	//Take input
	public String read_input_file() {
		String extra_words = "The password is: ";
		String password = "";

		//Read the file
		while (file_scanner.hasNext()) {
			password += file_scanner.nextLine();
		}

		//If the string contains the extra_words, remove it
		if (password.startsWith(extra_words)) {
			password = password.replaceFirst(extra_words, "");
		}
		password.strip();
		return password;
	}

	//Convert input to ASCII matrix of size 4x#
	public void fill_mat_B(String sample) {
		// Too difficult to write a concise loop to process a char and put into
		//a 1x4 int array, then do this for an unknown number of said 
		//arrays and put them into an ArrayList. Instead, I'm doing the 
		//tasks separately.
		ArrayList<Integer> converted_sample = new ArrayList<Integer>();

		//Convert and store the sample string's decimal equivalent
		for (int index = 0; index < sample.length(); index++) {
			// Get and convert char to int
			int value = sample.codePointAt(index);

			//Place into a single ArrayList<Integer>
			converted_sample.add((Integer) value);
		} // end for loop

		//Transfer the data into mat_B
		fill_mat_B_helper(converted_sample);
	}

	public void fill_mat_B_helper(ArrayList<Integer> unformatted) {
		// Determine the number of 4x1 arrays needed. Round up.
		int num_cols = 0;
		if (unformatted.size() % 4 == 0) {
			num_cols = unformatted.size() / 4;
		} else {
			num_cols = ((int) (unformatted.size() / 4)) + 1;
		}

		//Fill temp_mat_B with 0s
		double[][] temp_mat_B = new double[4][num_cols];
		for (int row = 0; row < temp_mat_B.length; row++) {
			for (int col = 0; col < num_cols; col++) {
				temp_mat_B[row][col] = 0.0;
			}
		}

		//Organize the data. Data will go into mat_B.get(arr_num)[inner_arr_counter]
		int r = 0;
		int c = 0;
		for (int index = 0; index < unformatted.size(); index++) {
			temp_mat_B[r][c] = unformatted.get(index).doubleValue();
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

		//Pass the double[][] into mat_B object
		mat_B = new Matrix(temp_mat_B);

	}// end fill_mat_B_helper

	//Do matrix multiplication to obtain mat_C of size 4x#
	public void fill_mat_C() {
		// Multiply mat_A by mat_B
		mat_C = mat_A.times(mat_B);
	}

//	public String mat_to_str(int[][] matrix) {
//		String output = "";
//		for(int col = 0; col < matrix[0].length; col++) {
//			for(int row = 0; row < matrix.length; row++) {
//				//Iterating rows on the inside to print columns grouped together.
//				output += matrix[col][row];
//			}//end inner
//		}//end outer
//		return output;
//	}//end print_matrix(int[][])
}
