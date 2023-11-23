//NOTE:
//	Each assignment part has its own object here. Change the filenames in the constructors if needed.
//  Output prints to both the console and output files to meet assignment requirements and to help 
//		show the process as the code works through the calculations. 



public class Main {

	public static void main(String[] args) {
		//Part 1:

		System.out.println("Start output for Abruce_p1==============================");

		Abruce_p1 p1_obj = new Abruce_p1("input_1.txt", "output_1.txt");
		p1_obj.do_tasks();
		p1_obj.print_mats(1);

		System.out.println("\nEnd output for Abruce_p1==============================");

//Commented out testing object (used provided example for code validation):
		//		System.out.println("Start output for Abruce_p1 test file==============================");
		//
		//		Abruce_p1 p1_obj_test = new Abruce_p1("input_1_test.txt", "output_1_test.txt");
		//		p1_obj_test.do_tasks();
		//		p1_obj_test.print_mats(1);
		//
		//		System.out.println("\nEnd output for Abruce_p1 test file==============================");



		//Part 2:

		System.out.println("\nStart output for Abruce_p2============================");

		Abruce_p2 p2_obj = new Abruce_p2("input_2.txt", "output_2.txt");
		p2_obj.do_tasks();
		p2_obj.print_mats();

		System.out.println("\nEnd output for Abruce_p2============================");


//Commented out testing object (used provided example for code validation):
		//		System.out.println("\nStart output for Abruce_p2 test file============================");
		//		
		//		Abruce_p2 p2_obj_test = new Abruce_p2("input_2_test.txt", "output_2_test.txt");
		//		p2_obj_test.do_tasks();
		//		p2_obj_test.print_mats();
		//		
		//		System.out.println("\nEnd output for Abruce_p2 test file============================");


	}//end main method

}
