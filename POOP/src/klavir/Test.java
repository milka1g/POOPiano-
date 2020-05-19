package klavir;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		MuzickiSimbol m = new Nota(1,4,"adf");
		
		boolean f1 = m instanceof Pauza;
		boolean f2 = m instanceof Nota;
		boolean f3 = m instanceof MuzickiSimbol;
		
		
		System.out.println(f1 + " -- " + f2 + " -- " + f3);
	}

}
