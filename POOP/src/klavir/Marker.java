package klavir;

public class Marker extends MuzickiSimbol {

	public Marker() {
		super(0, 0);
		istovremeno = false;
	}

	@Override
	public void setIstovremeno() {

	}

	@Override
	public boolean getIstovremeno() {
		return false;
	}
	
	public boolean isMarker(){return true;}
	
	public String toString() {
		return "|";
	}
	
	public String getNaziv() {return "/";}
	public void setNaziv(String s) {}

}
