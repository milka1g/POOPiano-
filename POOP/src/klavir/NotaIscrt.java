package klavir;

public class NotaIscrt {

	private String kodtxt;
	private String notaReal;
	private long timeStart = 0;
	private long timeEnd = 0;
	private int midiBr;
	public NotaIscrt(String kod, String note, int brmidi) {
		kodtxt = kod;
		notaReal = note;
		midiBr = brmidi;
	}
	
	
	public String getKodtxt() {
		return kodtxt;
	}
	public void setKodtxt(String kodtxt) {
		this.kodtxt = kodtxt;
	}
	public String getNotaReal() {
		return notaReal;
	}
	public void setNotaReal(String notaReal) {
		this.notaReal = notaReal;
	}
	public int getMidiBr() {
		return midiBr;
	}
	public void setMidiBr(int midiBr) {
		this.midiBr = midiBr;
	}
	
	public void setCurrStartTime() {
		timeStart = System.currentTimeMillis();
	}
	
public void setCurrEndTime() {
		timeEnd = System.currentTimeMillis();
	}
	
	public String toString() {
		return "nota: " + kodtxt + " " + notaReal + " " + midiBr + "." + timeStart + "/" + timeEnd + "=" + (timeEnd-timeStart);
	}
	
	public long trajanjeNote() {
		return timeEnd - timeStart;
	}
	
	public boolean jednako(NotaIscrt n) {
		if(kodtxt == n.kodtxt && notaReal == n.notaReal && midiBr ==n.midiBr) return true; return false;
	}


	public long getTimeStart() {
		return timeStart;
	}


	public long getTimeEnd() {
		return timeEnd;
	}
	
	
	

}
