package klavir;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Crtez extends Canvas implements Runnable {
	
	private static int br = 1; 
	private Mapiranje mapa = new Mapiranje();
	private static final int len_short = 200;
	private static final int len_long = 400;
	private volatile boolean shutdown = false;
	private volatile boolean tip = true;  //true znaci sviraj PRAVU NOTU, false znaci sviraj KOD
	protected Klavir klavir;
	private ArrayList<MuzickiSimbol> simboli;
	private ArrayList<MuzickiSimbol> saved;
	private ArrayList<MuzickiSimbol> simboliIscrtavanje = new ArrayList<MuzickiSimbol>();
	private boolean radi = true;
	protected LinkedList<MuzickiSimbol> simbCrtez = new LinkedList<MuzickiSimbol>();

	public Crtez(Klavir klavir) {
		super();
		this.klavir = klavir;
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds(600,0,400,150);
		simboli = new ArrayList<MuzickiSimbol>(klavir.as.k.getSimboli());
		saved = new ArrayList<MuzickiSimbol>(klavir.as.k.getSimboli());
		if(klavir.nota.getState())tip = true; else tip = false;
		setVisible(true);
		prepraviSimb();
		//System.out.println( "SIMBOLI STOT SE CRTATJU " + simboliIscrtavanje + "AAA");
		//System.out.println( "SIMBOLI " + simboli + "AAA");
		
		//System.out.println(simboliIscrtavanje.size() + "size iscrt");
		//System.out.println(simboli.size() + "size simboli");
		//System.out.println(simbCrtez.size() + "size crtanje");
		//System.out.println(simbCrtez);
		
		
	}
	
	public Crtez() {
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds(600,0,400,150);
		if(klavir.nota.getState())tip = true; else tip = false;
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g) {
		int x = 10, y = 10, x1 = 10, y1 = 100;
		int max = simbCrtez.size()>=12?12:simbCrtez.size();
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		for(int j = 0; j<12; j++) {
			g.drawLine(x1, y1, x1, y1+10);
			x1+=40;
		}
		for(int i = 0; i < max; i++) {
			if(simbCrtez.get(i).trajanje.jednako(new Trajanje(1,8))) {
				//if(simbCrtez.get(i) instanceof Pauza) System.out.println("MALA PAUZA" + br);
				if(simbCrtez.get(i) instanceof Pauza) g.setColor(Color.orange); else
				g.setColor(Color.GREEN); g.setFont(new Font ("Serif", Font.BOLD, 11));
				int br = 0;
				if(simbCrtez.get(i).toString().contains("\n")) br = simbCrtez.get(i).toString().split("\n", -1).length-1; else br = 1;
				g.fillRect(x, y, 20, br*20);
				if(simbCrtez.get(i) instanceof Pauza) { g.setColor(Color.cyan); g.drawRect(x, y, 20, br*20); x+=20;  continue;}
				g.setColor(Color.BLACK);
				if(simbCrtez.get(i).toString().contains("\n")) {
					drawStringL(g,simbCrtez.get(i).toString(),x+8, y); 	
				} else {
					if(!tip)
					g.drawString(mapa.getNota(simbCrtez.get(i).toString()), x+8, y+12);
					else g.drawString(simbCrtez.get(i).toString(), x+8, br*y+12);
				}
				g.setColor(Color.cyan);
				g.drawRect(x, y, 20, br*20);
				x+=20;
			
		}else if(simbCrtez.get(i).trajanje.jednako(new Trajanje(1,4))) {
			//if(simbCrtez.get(i) instanceof Pauza) System.out.println("VELIKA PAUZA" + br);
			if(simbCrtez.get(i) instanceof Pauza) g.setColor(Color.MAGENTA); else
			g.setColor(Color.RED); g.setFont(new Font ("Serif", Font.BOLD, 11));
			int br = 0;
			if(simbCrtez.get(i).toString().contains("\n")) br = simbCrtez.get(i).toString().split("\n", -1).length-1; else br = 1;
			g.fillRect(x, y, 40, br*20);
			if(simbCrtez.get(i) instanceof Pauza) { g.setColor(Color.cyan); g.drawRect(x, y, 40, br*20); x+=40; continue;}
			g.setColor(Color.BLACK);
			if(simbCrtez.get(i).toString().contains("\n")) {
				drawStringL(g,simbCrtez.get(i).toString(),x+18,y);
			} else {
				if(!tip)
				g.drawString(mapa.getNota(simbCrtez.get(i).toString()), x+18, br*y+12);
				else g.drawString(simbCrtez.get(i).toString(), x+18, y+12);
			}
			g.setColor(Color.cyan);
			g.drawRect(x, y, 40, br*20);
			x+=40; 
		}
		
		}
		//br++;
		//System.out.println("==================");
	}
	
	@Override
	public void run() {
		try {
		while(!Thread.interrupted() && !shutdown) {
			if(klavir.manual.getState()) {
				repaint();
				stani();
			}
			synchronized (this) {
				if(!radi) wait();}
			if(simbCrtez.isEmpty()) {
				int max = simboliIscrtavanje.size()>=12?12:simboliIscrtavanje.size();
				for(int i = 0; i < max;i++) { 
					simbCrtez.addLast(simboliIscrtavanje.get(0));
					simboliIscrtavanje.remove(0);
				}
			}
			//crtanje
			repaint();
			long sleep = 0;
			if(simbCrtez.get(0).trajanje.jednako(new Trajanje(1,8))) sleep = len_short; else sleep = len_long;
			Thread.sleep(sleep);
			if(simboliIscrtavanje.isEmpty()) {
				int time;
				while(!simbCrtez.isEmpty()) {
				repaint();
				if(simbCrtez.getFirst().trajanje.jednako(new Trajanje(1,8))) time = len_short; else time = len_long;
				Thread.sleep(time);
				simbCrtez.remove();
				}
				break;
				//reset(); repaint();
				//radi = false; continue;
			}
			simbCrtez.remove();
			//System.out.println(simbCrtez.size()+" SAJZ simbCrtez");
			//System.out.println(simboliIscrtavanje.size()+" SAJZ simboliIscrtavanje");
			simbCrtez.addLast(simboliIscrtavanje.get(0));
			simboliIscrtavanje.remove(0);
		}
		}catch(InterruptedException e) { }
	}
	
	private void prepraviSimb() {
		if(klavir.nota.getState())tip = true; else tip = false;
		for(int i = 0; i < simboli.size();i++) {
			if(!simboli.get(i).getIstovremeno() && !simboli.get(i).isMarker()) {
				simboliIscrtavanje.add(simboli.get(i));
				}
			else if(simboli.get(i).getIstovremeno()) {
				String naziv = "";
				while(simboli.get(i).getIstovremeno() && i<simboli.size() && !simboli.get(i).isMarker()) {
					if(tip) {
						naziv += mapa.getNota(simboli.get(i).toString()); naziv += "\n";
					}
					else {
						naziv += simboli.get(i).toString(); naziv += "\n";
					}
					i++;
				}
				simboliIscrtavanje.add(new Nota(1,4,naziv));
			}
		}
		
	}

	public void reset() {
		simboli = new ArrayList<MuzickiSimbol>(saved);
		prepraviSimb();
		radi = false;
	}
	
	public synchronized void stani() throws InterruptedException {radi = false;}
	public synchronized void kreni() {radi = true; notifyAll();}
	public void zavrsi() { shutdown = true;  
	//interrupt();? 
	}
	
	void drawStringL(Graphics g, String text, int x, int y) {
	    for (String line : text.split("\n"))
	    	if(tip)
	        g.drawString(line, x, y += g.getFontMetrics().getHeight());
	    	else 
	    	g.drawString(mapa.getNota(line), x, y += g.getFontMetrics().getHeight());
	}
	
	public void changeKeys() {
		tip = !tip;
	}
	
	

	
	public ArrayList<MuzickiSimbol> getSimboliIscrtavanje() {
		return simboliIscrtavanje;
	}

	public LinkedList<MuzickiSimbol> getSimbCrtez() {
		return simbCrtez;
	}

	public static void main(String[] args) {
		/*ArrayList<MuzickiSimbol> sim = new ArrayList<MuzickiSimbol>();
		MuzickiSimbol m;
		sim.add(new Nota(1,4,"w"));
		sim.add(new Pauza(1,8));
		sim.add(new Marker());
		sim.add(m = new Nota(1,4,"8"));
		m.setIstovremeno();
		sim.add(m = new Nota(1,4,"w"));
		m.setIstovremeno();
		sim.add(new Marker());
		sim.add(new Pauza(1,8));
		sim.add(new Nota(1,4,"u"));
		sim.add(new Pauza(1,4));
		sim.add(new Marker());
		sim.add(m = new Nota(1,4,"5"));
		m.setIstovremeno();
		sim.add(m = new Nota(1,4,"y"));
		m.setIstovremeno();
		sim.add(new Marker());
		sim.add(new Nota(1,4,"t"));
		
		
		Frame f = new Frame();
		f.setLayout(new BorderLayout());
		Crtez c = new Crtez(sim);
		Thread nit = new Thread(c);
		f.add(c,BorderLayout.CENTER);
		f.setBounds(100,100,300,200);
		f.setVisible(true);
		nit.start();*/
		
		
	}

}
