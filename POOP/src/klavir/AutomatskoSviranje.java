package klavir;

import java.util.ArrayList;
import java.util.concurrent.*;

import javax.sound.midi.MidiUnavailableException;

public class AutomatskoSviranje extends Thread {
	
	private static final int len_short = 200;
	private static final int len_long = 400;
	protected Kompozicija k;
	private volatile boolean manual = false;
	private boolean radi = true;
	private Mapiranje mapa = new Mapiranje();
	private MidiPlayer mp;
	boolean pop = false;
	private Klavir klavir;
	ArrayList<MuzickiSimbol> simb = new ArrayList<MuzickiSimbol>();
	public AutomatskoSviranje(Klavir kl ) {
		klavir = kl;
		k = new Kompozicija(klavir.ime);
		try {
			mp = new MidiPlayer();
		} catch (MidiUnavailableException e) {}
	}

	
	private void popuniSimb() {
		
		for(int i = 0; i < k.simboli.size();i++) {
			
			if(!k.simboli.get(i).isMarker()) 
				simb.add(k.simboli.get(i));

		}
		pop = true;
	}
	
	public void run() {
		popuniSimb();
		//System.out.println(simb + "AS");
		try {
		while(!interrupted()) {
			
			for(int i = 0; i < k.simboli.size();i++) {
				if(klavir.manual.getState()) stani();
				synchronized (this) {
					if(!radi) wait();
				}
				if(!k.simboli.get(i).getIstovremeno() && !k.simboli.get(i).toString().equals(" ") && !k.simboli.get(i).isMarker()) {
					final long len;
					if(k.simboli.get(i).trajanje.jednako(new Trajanje(1,8))) len = len_short; else len = len_long;
					try {
						mp.play(Integer.parseInt(mapa.getMIDI(k.simboli.get(i).toString())), len);
					} catch (NumberFormatException e) {} catch (InterruptedException e) {throw e;}
				}
				else if(!k.simboli.get(i).getIstovremeno() && k.simboli.get(i).toString().equals(" ")
						&& !k.simboli.get(i).isMarker()) {
					long len;
					if(k.simboli.get(i).trajanje.jednako(new Trajanje(1,8))) len = len_short; else len = len_long;
					try {
						mp.playpauza(len);
					} catch (InterruptedException e) { throw e; }
				}
				else if(k.simboli.get(i).isMarker()) {
					int j = 0;
					int p = i+1; 
					i++; 
					if(p>=k.simboli.size()) break; //tacno jedino ako je komp ...tgrftgr[ wtf
					
					if(k.simboli.get(i).getIstovremeno() == false) {
						while(!k.simboli.get(i).getIstovremeno() && !k.simboli.get(i).isMarker() && i<k.simboli.size()) {
							try {
								mp.play(Integer.parseInt(mapa.getMIDI(k.simboli.get(i).toString())), len_short); //svira se 1/8 uvek
							} catch (NumberFormatException e) {} catch (InterruptedException e) {throw e;}
							i++;
						}
					} else { 
					while(k.simboli.get(p).getIstovremeno() && !k.simboli.get(p).isMarker() && p<k.simboli.size()) {j++; p++;}
					int[] pom = new int[j];
					final long len = len_long;
					j = 0;
					while(k.simboli.get(i).getIstovremeno() && i < k.simboli.size() && !k.simboli.get(i).isMarker()) {
						pom[j] = Integer.parseInt(mapa.getMIDI(k.simboli.get(i).toString()));
						j++;
						i++;
					}
					try {
						mp.play(pom,len);
					} catch (InterruptedException e) {throw e; }
				}
				}
			}
			this.interrupt(); //xexe
		  }
		}catch(InterruptedException e) { //throw new RuntimeException("Thread interrupted..."); 
			 System.out.println("INTERRUPTED");
			 
		}
		
		
	}
	
	public synchronized void stani() throws InterruptedException {radi = false;}
	public synchronized void kreni() {radi = true; notifyAll();}
	public void zavrsi() {this.interrupt();}
	public void setManual() {
		manual = true;
	}
	public void setAuto() {
		manual = false;
		kreni();
		
	}
	
	public static void main(String args[]) {
		//AutomatskoSviranje a = new AutomatskoSviranje("ode.txt"); a.start();
		
		
	}

}
