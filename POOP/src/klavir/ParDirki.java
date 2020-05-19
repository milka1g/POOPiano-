package klavir;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.text.html.HTMLDocument.Iterator;

public class ParDirki extends Canvas implements MouseListener, Runnable, KeyListener {
	
	private static final int len_short = 200;
	private static final int len_long = 400;
	private Mapiranje mapa = new Mapiranje();
	private boolean dum, keys = false; 
	private MidiPlayer mp;
	private boolean radi = true, shutdown = false;
	private ArrayList<MuzickiSimbol> toPaint = new ArrayList<MuzickiSimbol>();
	private ArrayList<MuzickiSimbol> simboli;
	private HashMap<NotaIscrt,Tacka> dirkeMapa = new HashMap<NotaIscrt,Tacka>();
	private LinkedList<MuzickiSimbol> forPaint = new LinkedList<MuzickiSimbol>();
	//private LinkedList<MuzickiSimbol> jaKlikno = new LinkedList<MuzickiSimbol>();
	private Klavir k = null; 

	public ParDirki() {
		super();
		mapirajDirkeNaTacke();
		//simboli = new ArrayList<MuzickiSimbol>(k.as.k.getSimboli());
		this.setBounds(100,300,1400,200);
		//setSize(new Dimension(1400,200));
		setVisible(true);
		this.setBackground(Color.white);
		setVisible(true);
		repaint();
		try {
			mp = new MidiPlayer();
		} catch (MidiUnavailableException e) {}
	}
	
	public ParDirki(Klavir k){
		//tip sredi
		super();
		this.k = k;
		simboli = new ArrayList<MuzickiSimbol>(k.as.k.simboli);
		mapirajDirkeNaTacke();
		//simboli = new ArrayList<MuzickiSimbol>(k.as.k.getSimboli());
		this.setBounds(100,300,1400,200);
		//setSize(new Dimension(1400,200));
		setVisible(true);
		this.setBackground(Color.white);
		this.addMouseListener(this);
		this.addKeyListener(this);
		setVisible(true);
		repaint();
		try {
			mp = new MidiPlayer();
		} catch (MidiUnavailableException e) {}
		
		//for(int i = 0;i<dirkeMapa.size();i++) {
			//System.out.print(mapa.getAllNotes().get(i) + "  ");
			//System.out.println(dirkeMapa.get(mapa.getAllNotes().get(i))); 
		//}
	}
	
	@Override
	public void paint(Graphics g) {
		dum = false;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		g.setColor(Color.black);
		int base = 0;
		int j = 0;
		
		for(int i = 0; i<1400;i+=40) {
			g.setColor(Color.white);
			for(int it = 0; it < forPaint.size(); it++) {
				if(j==1 || j==3 || j==6 || j==8 || j==10) j++;
				if(mapa.getAllNotes().get(j+base).getKodtxt().equals(forPaint.get(it).getNaziv())) {
				    g.setColor(Color.red);
				    g.fillRect(i, 0, 40, 150);
				    g.setColor(Color.black);
				    if(!keys)
					g.drawString(mapa.getAllNotes().get(j+base).getNotaReal(), i+12 ,90);
				    else
				    	g.drawString(mapa.getAllNotes().get(j+base).getKodtxt(), i+12 ,90);
				    forPaint.remove(it);
				    dum = true;
				    break;
				};
			}
			if(!dum) {
				g.fillRect(i, 0, 40, 150);
				g.setColor(Color.black);
				g.drawLine(i, 0, i, 150);
				if(j==1 || j==3 || j==6 || j==8 || j==10) j++;
				if(!keys)
				g.drawString(mapa.getAllNotes().get(j+base).getNotaReal(), i+12 ,90);
				else
				g.drawString(mapa.getAllNotes().get(j+base).getKodtxt(), i+12 ,90);
			}
			j++;
			if(j==12) {j=0;base+=12;}
			dum = false;
		}
		g.drawLine(1400, 0, 1400, 150);
		g.drawLine(0, 0, 1400, 0);
		g.drawLine(0, 150, 1400, 150);
		
		
		base = 0;
		int baseCrtanje = 0;
		j = 0;
		dum = false;
		for(int i = 0 ; i+baseCrtanje<35; i++) {
			if(i==0 || i==1 || i==3 || i==4 || i==5){
				if(!(j==1 || j==3 || j==6 || j==8 || j==10))j++;
				for(int it = 0; it < forPaint.size(); it++) {
					if(mapa.getAllNotes().get(j+base).getKodtxt().equals(forPaint.get(it).getNaziv())) {
					    g.setColor(Color.red);
					    g.fillRect(dirkeMapa.get(mapa.getAllNotes().get(base+j)).getX(), 0, 30, 60);
					    g.setColor(Color.black);
					    
					    if(!keys)
					    g.drawString(mapa.getNota(forPaint.get(it).getNaziv()), dirkeMapa.get(mapa.getAllNotes().get(base+j)).getX()+3,30);
					    else g.drawString(mapa.MIDIToCode(String.valueOf(mapa.getMIDI(forPaint.get(it).toString()))),
					    		dirkeMapa.get(mapa.getAllNotes().get(base+j)).getX()+3,30);
					    forPaint.remove(it);
					    dum = true;
					    break;
					};
				}
			
				g.setColor(Color.black);
				if(!dum) {
					g.fillRect((baseCrtanje+i)*40+25, 0, 30, 60);
					g.setColor(Color.white);
					if(!(j==1 || j==3 || j==6 || j==8 || j==10))j++;
					if(!keys)
					g.drawString(mapa.getAllNotes().get(j+base).getNotaReal(), (i+baseCrtanje)*40+28,30);
					else g.drawString(mapa.getAllNotes().get(j+base).getKodtxt(), (i+baseCrtanje)*40+28,30);
					}
			}
			j++;
			if(j==12) {
				base+=12;
				j=0;
			}
			if(i==6) {i=-1; baseCrtanje+=7;}
			dum = false;
		}
		dum = false;
	}
	
	public static void main(String[] args) {
		Frame f = new Frame();
		f.setLayout(new BorderLayout());
		//ParDirki d = new ParDirki(new Klavir());
		//f.add(d,BorderLayout.CENTER);
		f.setBounds(100,100,1500,300);
		f.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(k!=null)
			if(k.snimanje) {
				if(!k.recording.isEmpty())
				k.recording.get(k.recording.size()-1).setCurrEndTime();
			}
		int midibr = 0;
		for(Map.Entry<NotaIscrt, Tacka> entry : dirkeMapa.entrySet()) {
			if(entry.getValue().getHeight() == 60) {
				if(e.getX()>= entry.getValue().getX() && e.getX() <= entry.getValue().getX()+30 && e.getY()>=entry.getValue().getY() &&
						e.getY() <= entry.getValue().getY()+60) {
						midibr = entry.getKey().getMidiBr();
						mp.play(midibr);
						
						forPaint.add(new Nota(1,4,mapa.MIDIToCode(String.valueOf(midibr))));
						repaint();
				}
			}
		}
		if(!forPaint.isEmpty()) return;
		for(Map.Entry<NotaIscrt, Tacka> entry : dirkeMapa.entrySet()) {
			if(entry.getValue().getHeight() == 150) {
				if(e.getX()>= entry.getValue().getX() && e.getX() <= entry.getValue().getX()+40 && e.getY()>=entry.getValue().getY() &&
						e.getY() <= entry.getValue().getY()+150) {
						midibr = entry.getKey().getMidiBr();
						mp.play(midibr);
						forPaint.add(new Nota(1,4,mapa.MIDIToCode(String.valueOf(midibr))));
						repaint();
				}
			}
		}
		if(k!=null)
		if(k.snimanje) {
			NotaIscrt nova = null;
			k.recording.add(nova = new NotaIscrt(mapa.MIDIToCode(String.valueOf(midibr)),mapa.getNota(mapa.MIDIToCode(String.valueOf(midibr))),midibr));
			nova.setCurrStartTime();
			}
		
		if(k!=null) 
			{
				k.pressedSimbol.add(new NotaIscrt(mapa.MIDIToCode(String.valueOf(midibr)),mapa.getNota(mapa.MIDIToCode(String.valueOf(midibr))),midibr));
			}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int midibr = 0;
		for(Map.Entry<NotaIscrt, Tacka> entry : dirkeMapa.entrySet()) {
			if(entry.getValue().getHeight() == 60) {
				if(e.getX()>= entry.getValue().getX() && e.getX() <= entry.getValue().getX()+30 && e.getY()>=entry.getValue().getY() &&
						e.getY() <= entry.getValue().getY()+60) {
						midibr = entry.getKey().getMidiBr();
						mp.release(midibr);
						
						forPaint.remove(new Nota(1,4,mapa.MIDIToCode(String.valueOf(midibr))));
						repaint();
				}
			}
		}
		for(Map.Entry<NotaIscrt, Tacka> entry : dirkeMapa.entrySet()) {
			if(entry.getValue().getHeight() == 150) {
				if(e.getX()>= entry.getValue().getX() && e.getX() <= entry.getValue().getX()+40 && e.getY()>=entry.getValue().getY() &&
						e.getY() <= entry.getValue().getY()+150) {
						midibr = entry.getKey().getMidiBr();
						mp.release(midibr);
						forPaint.remove(new Nota(1,4,mapa.MIDIToCode(String.valueOf(midibr))));
						repaint();
				}
			}
		}
		if(k!=null)
		if(k.snimanje) {
			for(NotaIscrt n: k.recording) {
				if(n.getMidiBr() == midibr)
					n.setCurrEndTime();
			}
			NotaIscrt nova = null;
			k.recording.add(nova = new NotaIscrt("", "", 100));
			nova.setCurrStartTime();
		}
		
		if(k!=null)
			if(k.ucitano==true) 
				if(k.manual.getState()) {
					if(k.pressedSimbol.get(k.pressedSimbol.size()-1).getKodtxt() == k.crtez.simbCrtez.get(0).getNaziv())
						k.crtez.kreni(); 
						k.parDirki.kreni();
					//k.clearPressedSimbol();
					}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void run() {
		try {
		while(!Thread.interrupted() && !shutdown) {
		for(int i = 0; i<simboli.size();i++) {
			if(k.manual.getState())stani();
			synchronized (this) {
				if(!radi) {wait();}
			}
			if(!simboli.get(i).getIstovremeno() && !simboli.get(i).toString().equals(" ") && !simboli.get(i).isMarker()) {
				forPaint.add(simboli.get(i));
				System.out.println("---SIMBOLI---");
				System.out.println(forPaint);
				repaint();
				if(simboli.get(i).trajanje.jednako(new Trajanje(1,4))) Thread.sleep(len_long); else Thread.sleep(len_short);
			}
			else if(!simboli.get(i).getIstovremeno() && simboli.get(i).toString().equals(" ")
					&& !simboli.get(i).isMarker()) {
				repaint();
				if(simboli.get(i).trajanje.jednako(new Trajanje(1,4))) Thread.sleep(len_long); else Thread.sleep(len_short);
				System.out.println("---PAUZAK---");
			}
			else if(simboli.get(i).isMarker()) {
				i++;
				if(simboli.get(i).getIstovremeno() == false) {
					while(!simboli.get(i).getIstovremeno() && !simboli.get(i).isMarker() && i<simboli.size()) {
						forPaint.add(simboli.get(i)); 
						System.out.println("---SIMBOLIspec 1 samo---");
						System.out.println(forPaint);
						repaint();
						Thread.sleep(len_short);
						forPaint.clear();
						i++;
					}
				} else { 
				while(simboli.get(i).getIstovremeno() && !simboli.get(i).isMarker() && i<simboli.size()) {
					forPaint.add(simboli.get(i)); i++;
				}
				System.out.println("---SIMBOLImulti---");
				System.out.println(forPaint);
				repaint();
				Thread.sleep(len_long);
			}
			}
			
			forPaint.clear();
		}
		break;	
		}
		
		}catch(InterruptedException e) {}
		
	}
	
	private void mapirajDirkeNaTacke() { //TEST IT 
		int xNormal = 0;
		int j = 0, base = 0, baseCrtanje = 0;
		for(int i = 0; i < mapa.getAllNotes().size();i++) {
			if(j==1 || j==3 || j==6 || j==8 || j==10) {
				int dodatak = 0;
				if(j==1) dodatak = 25; else if(j==3) dodatak = 65; else if(j==6) dodatak = 145; else if(j==8) dodatak = 185; else if (j==10) dodatak = 225;
				dirkeMapa.put(mapa.getAllNotes().get(i), new Tacka(dodatak+baseCrtanje,0,30,60));
			}else {
				dirkeMapa.put(mapa.getAllNotes().get(i), new Tacka(xNormal+baseCrtanje,0,40,150));
				xNormal+=40;
			}
			j++;
			if(j==12) {j=0;base+=12; baseCrtanje+=280; xNormal=0;}
		}
		
		
	}
	
	public synchronized void stani() throws InterruptedException {radi = false;}
	public synchronized void kreni() {radi = true; notifyAll();}
	public void zavrsi() { shutdown = true;  
	//interrupt();? 
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(k!=null)
			if(k.snimanje) {
				if(!k.recording.isEmpty())
				k.recording.get(k.recording.size()-1).setCurrEndTime();
			}
		String midiStr = mapa.getMIDI(String.valueOf(e.getKeyChar()));
		if(midiStr==null) return;
		int midiBr = Integer.parseInt(midiStr);
		mp.play(midiBr);
		
		
		forPaint.add(new Nota(1,4,String.valueOf(e.getKeyChar())));
		repaint();
		
		
		
		if(k!=null)
		if(k.snimanje) {
		NotaIscrt nova = null;
		k.recording.add(nova = new NotaIscrt(mapa.MIDIToCode(String.valueOf(midiBr)),mapa.getNota(mapa.MIDIToCode(String.valueOf(midiBr))),midiBr));
		nova.setCurrStartTime();
		}
		//if(k.manual.getState()) {
		//	if(k.crtez.getSimbCrtez().get(0).equals(k.getPressedSimbol().get(0)) && k.ucitano == true)
		//		k.crtez.kreni();
		//}
		
		if(k!=null) 
			if(k.ucitano==true)
				if(k.manual.getState())
			{	k.pressedSimbol.add(new NotaIscrt(mapa.MIDIToCode(String.valueOf(midiBr)),mapa.getNota(mapa.MIDIToCode(String.valueOf(midiBr))),midiBr));
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		String midiStr = mapa.getMIDI(String.valueOf(e.getKeyChar()));
		if(midiStr==null) return;
		int midiBr = Integer.parseInt(midiStr);
		mp.release(midiBr);
		forPaint.clear();
		repaint();
		
		if(k!=null)
		if(k.snimanje) {
			for(NotaIscrt n: k.recording) {
				if(n.getMidiBr() == midiBr)
					n.setCurrEndTime();
			}
			NotaIscrt nova = null;
			k.recording.add(nova = new NotaIscrt("", "", 100));
			nova.setCurrStartTime();
		}
		
		if(k!=null)
			if(k.ucitano==true) 
				if(k.manual.getState())
					k.clearPressedSimbol();
		
	}
	
	public void changeKeys() {
		keys = !keys;
	}

	public Klavir getK() {
		return k;
	}
	
	


}
