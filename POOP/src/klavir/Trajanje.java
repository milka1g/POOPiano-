package klavir;

public class Trajanje {
	
	private int brojilac, imenilac;

	public Trajanje(int br,int im) {
		brojilac = br; 
		imenilac = im;
	}
	

	public int getBrojilac() {
		return brojilac;
	}



	public void setBrojilac(int brojilac) {
		this.brojilac = brojilac;
	}



	public int getImenilac() {
		return imenilac;
	}



	public void setImenilac(int imenilac) {
		this.imenilac = imenilac;
	}
	
	public String toString() {
		return brojilac+"/"+imenilac;
	}
	
	public boolean jednako(Trajanje t) {
		if(t.imenilac == imenilac && t.brojilac == brojilac) return true; else return false;
	}


}
