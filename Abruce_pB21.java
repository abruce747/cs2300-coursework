package abruce_package_PA2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.ejml.dense.row.misc.RrefGaussJordanRowPivot_DDRM;
import org.ejml.data.DMatrixRMaj;

public class Abruce_pB21 {
	
	private int flow_1;
	private int flow_2;
	private int flow_3;
	private int flow_4;
	
	private File input_file;
	private Scanner input_reader;
	
	private RrefGaussJordanRowPivot_DDRM rref_ed_mat;
	private DMatrixRMaj mat_A;
	
	public Abruce_pB21() {
		input_file = new File(prompt_filename());
		try {
			input_reader = new Scanner(input_file);
			flow_1 = input_reader.nextInt();
			flow_2 = input_reader.nextInt();
			flow_3 = input_reader.nextInt();
			flow_4 = input_reader.nextInt();
		
			double[][] temp_arr = {
					{1,0,0,-1,flow_4},
					{1,-1,0,0,flow_3},
					{0,-1,1,0,flow_2},
					{0,0,1,-1,flow_1}
					};
			
			mat_A = new DMatrixRMaj(temp_arr);
			
			print_mat_A(temp_arr);
		}catch(FileNotFoundException f) {
			System.out.println("Missing file for Part B 2.1");
		}
		
	}
	
	public void print_mat_A(double[][] mat) {	
        System.out.println("Printing the matrix:");
		for (int r = 0; r < mat.length; r++) {
            for (int c = 0; c < mat[r].length; c++) {
            	System.out.print(mat[r][c] + " ");
            }
            System.out.println();
        }
	}
	
	public String prompt_filename() {
		String file_name = "";
		
		while(file_name.equals("")) {
			System.out.println("Enter a file name with the proper extension .txt."
					+ "\nAlso, this code only works if the file has 4 integer numbers with spaces on the same line."
					+ "\nI was not provided an example input or output file for this part.:");
			Scanner input = new Scanner(System.in);
			String temp = input.nextLine();
			if(temp.endsWith(".txt")) {
				file_name = temp;
			}
		}
		
		return file_name;
	}
	
}
