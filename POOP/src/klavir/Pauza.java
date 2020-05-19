package klavir;

public class Pauza extends MuzickiSimbol {
	private String naziv = " ";
	
	public Pauza(int br, int im) {
		super(br, im);
		istovremeno = false;
	}
	
	@Override
	public void setIstovremeno() {}
	@Override
	public boolean getIstovremeno() {return false;}
	
	public String toString() {
		return naziv;
	}
	
	public boolean isMarker(){return false;}
	public void setNaziv(String s) {}
	
	public String getNaziv() {return " ";}


}
