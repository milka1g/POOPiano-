package klavir;

public abstract class MuzickiSimbol {
	
	protected Trajanje trajanje;
	protected boolean istovremeno;

	public MuzickiSimbol(int br, int im) {
		trajanje = new Trajanje(br,im);
	}
	
	public abstract void setIstovremeno();
	public abstract boolean getIstovremeno();
	public abstract boolean isMarker();
	public abstract void setNaziv(String s);
	public abstract String getNaziv();

}
