import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import Jama.Matrix;


public class Abruce_p1 {
	private File input_file;
	private Scanner input_reader;
	private File output_file;
	private FileWriter output_writer;
	
	private Matrix mat_D; //input-output for the economic system
	private Matrix mat_E; //external demands matrix
	private Matrix mat_X; //output matrix x
	private Matrix mat_I; //identity matrix
	private Matrix mat_ID_sub; //result of mat_I - mat_D
	private Matrix mat_ID_sub_inv; //result of (mat_I - mat_D)^(-1)
	
	
	public Abruce_p1(String input_file_name, String output_file_name) {
		try {
			input_file = new File(input_file_name);
			input_reader = new Scanner(input_file);
			
			output_file = new File(output_file_name);
			output_writer = new FileWriter(output_file);
			
			//Initialize and loads mat_D, mat_E, and mat_I
			read_input_mats();
			mat_I = Matrix.identity(3,3);
			
			//Initialize mat_X and default to 0s
			mat_X = new Matrix(3,1);
			
		}catch(FileNotFoundException fnfe) {
			System.out.println("Missing input file: " + input_file_name);
		}catch(IOException ioe){
			System.out.println("Missing output file: " + output_file_name);
		}
	}//end constructor
	
	public void do_tasks() {
		//(mat_I - mat_D) * mat_X = mat_E
		
		//1: Minus operation
		mat_ID_sub = mat_I.minus(mat_D);
		
		//2: 
		//Figure out how to do Gauss Jordan elim or if it's the same as LU
		//Or implement what seems best, then compare outputs with what Pam's file has.
		mat_ID_sub_inv = mat_ID_sub.inverse();
		
		mat_X = mat_ID_sub_inv.times(mat_E);
		
		write_to_file();
	}//end do_tasks
		
	public void read_input_mats() {
		//Read and load mat_D, then mat_E
		boolean mat_d_turn = true;
		
		//Determine dimensions
		int d_row = 0;
		int d_col = 0;
		int e_row = 0;
		int e_col = 0;
		while (input_reader.hasNextLine()) {
			//Account for blank line separating matrices
			String line = input_reader.nextLine();
			if(line.isBlank()) {
				mat_d_turn = false;
				continue;
			}
			
			//Account for which matrix is being read:
			if(mat_d_turn) {
				d_row++;
				String[] values = line.strip().split(" ");
				d_col = values.length;
			}else {
				e_row++;
				String[] values = line.strip().split(" ");
				e_col = values.length;
			}
		}//end loop
		
		//Create matrix
		Matrix d_temp_mat = new Matrix(d_row,d_col);
		Matrix e_temp_mat = new Matrix(e_row,e_col);
		
		//Load the matrix (need new Scanner)
		try {
			Scanner temp_scanner = new Scanner(input_file);
			int d_row_count = 0;
			int e_row_count = 0;
			boolean mat_d_turn_2 = true;
			
			while(temp_scanner.hasNextLine()) {
				String line = temp_scanner.nextLine();	
				
				if(line.isBlank()) {
					mat_d_turn_2 = false;
					continue;
				}
				
				if(mat_d_turn_2){
					String[] r = line.strip().split(" ");
					for(int c = 0; c < r.length; c++) {
						d_temp_mat.set(d_row_count, c, Double.valueOf(r[c]).doubleValue());
					}//end inner loop
					d_row_count++;
				}else {
					String[] r = line.strip().split(" ");
					for(int c = 0; c < r.length; c++) {
						e_temp_mat.set(e_row_count, c, Double.valueOf(r[c]).doubleValue());
					}//end inner loop
					e_row_count++;
				}//end turn making
				
			}//end outer loop
			
		}catch(FileNotFoundException fnfe) {
			System.out.println("Input file not found while loading input matrix");
		}//end try/catch
		
		//Set mat_D, mat_E
		mat_D = d_temp_mat;
		mat_E = e_temp_mat;
	
	}//end read_input_mat method 
	
	public void write_to_file(){
		String data = "";
		
		//Iterate through mat_X, add to string, format
		for(int r = 0; r < mat_X.getRowDimension(); r++) {
			for(int c = 0;  c < mat_X.getColumnDimension(); c++) {
				data += String.format("%.1f", mat_X.get(r, c)) + " ";
			}//end inner loop
			data += "\n";
		}//end outer loop
		
		try {
			output_writer.append(data);
			output_writer.close();
		}catch(IOException ioe) {
			System.out.println("Trouble writing equation to output file in Abruce_p1: write_to_file()");
		}//end try/catch block
	}//end write_to_file() method
	
	public void print_mats(int precision) {	
		System.out.println("\nMatrix D:");
		mat_D.print(3, precision);
		
		System.out.println("Matrix E:");
		mat_E.print(3, precision);
		
		System.out.println("Matrix I:");
		mat_I.print(3, precision);
		
		System.out.println("Matrix ID_sub:");
		mat_ID_sub.print(3, precision);
		
		System.out.println("Matrix ID_sub_inv:");
		mat_ID_sub_inv.print(3, precision);
		
		System.out.println("Matrix X:");
		mat_X.print(3, precision);
		
	}//end print_mats method
	
}//end Abruce_p1 class
