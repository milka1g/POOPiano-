package klavir;

public class Nota extends MuzickiSimbol {
	
	private String naziv;
	
	public Nota(int br, int im, String s) {
		super(br,im);
		naziv = s;
		istovremeno = false;
	}
	
	@Override
	public void setIstovremeno() {
		istovremeno = true;
	}
	
	@Override 
	public boolean getIstovremeno() {
		return istovremeno;
	}
	
	public boolean isMarker(){return false;}
	
	
	public String toString() {
		return naziv;
	}
	
	public void setNaziv(String s) {naziv = s;}
	
	public String getNaziv() {return naziv;}
	

}
