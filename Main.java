package abruce_package_PA2;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		part_a();
		part_b();
	}
	
	public static void part_a() {
		System.out.println("Part A, 2.1 Encoding message:");
		Abruce_pA21 pa21 = new Abruce_pA21();
		pa21.do_tasks();
		
		System.out.println("\nPart A, 2.2 Decoding message:");
		Abruce_pA22 pa22 = new Abruce_pA22();
		pa22.do_tasks();
	}
	
	public static void part_b() {
		Abruce_pB21 partB21 = new Abruce_pB21();
		
	}
	

}
