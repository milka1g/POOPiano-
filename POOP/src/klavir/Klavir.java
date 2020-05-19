package klavir;
import java.lang.Thread.State;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Klavir extends JFrame {
  private JTextField filename = new JTextField(), dir = new JTextField();
  String ime, im1, directory;

  private JButton open = new JButton("Open"), save = new JButton("Save"), play = new JButton("Play"), stop = new JButton("Stop/Continue"), exit = new JButton("Exit");
  AutomatskoSviranje as;
  Crtez crtez = null;
  boolean running = true, snimanje = false;
  boolean ucitano = false;
  ParDirki parDirki = null;
  private Thread crta = null, klavijatura = null;
  private MIDIFormatter midi = null;
  private CheckboxGroup grupaDugmica = new CheckboxGroup();
  private CheckboxGroup tipSviranja = new CheckboxGroup();
  private CheckboxGroup rucnoMisTastatura = new CheckboxGroup(), export = new CheckboxGroup();
  Checkbox auto, manual, nota, code, txt,mid;
  ArrayList<NotaIscrt> pressedSimbol = new ArrayList<NotaIscrt>();
  ArrayList<NotaIscrt> recording = new ArrayList<NotaIscrt>();
  private JButton pokaziTastere = new JButton("Show keys"), record = new JButton("Start/Stop recording");
  
  


  public Klavir() {
	super("Klavir");
	parDirki = new ParDirki();
	parDirki.addKeyListener(parDirki);
	parDirki.addMouseListener(parDirki);
	add(parDirki, BorderLayout.CENTER);
	parDirki.setFocusable(true);
	parDirki.requestFocus();
	popuniAutomatski();
	JOptionPane.showMessageDialog(Klavir.this,
		    "Za pun set funkcionalnosti, ucitajte kompoziciju.",
		    "Molim za razumevanje",
		    JOptionPane.INFORMATION_MESSAGE);
    
  }
  
  private void popuniAutomatski() {
	  
	  	setLayout(new BorderLayout());
	    JPanel pom = new JPanel(new GridLayout(0,2)); // pomocni, u njega trpam buttone
	    open.addActionListener(new OpenL());
	    pom.add(open);
	    save.addActionListener(new SaveL());
	    pom.add(save);
	    play.setEnabled(false);
	    play.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		Klavir.this.remove(parDirki);
	    		stop.setEnabled(true);
	    		exit.setEnabled(true);
	    		
	    		try {
	    		as = new AutomatskoSviranje(Klavir.this);
	    		}catch(RuntimeException g) { play.setEnabled(true); stop.setEnabled(false);}
	    		as.start();
	    		while(!as.pop); //dok u as nesto nije spremno, cekaj hahah
	    		parDirki = new ParDirki(Klavir.this);
	    		//parDirki.addKeyListener(parDirki);
	    		parDirki.addMouseListener(parDirki);
	    		parDirki.setFocusable(true);
	    		parDirki.requestFocus();
	    		crtez = new Crtez(Klavir.this);
	    		add(parDirki, BorderLayout.CENTER);
	    		add(crtez,BorderLayout.NORTH);
	    		//if(as.simb.isEmpty()) System.out.println("PRAZAN BAJO"); else System.out.println("PUN BAJO");
	    		crta = new Thread(crtez); ////ako je vec bio thread znaci samo ga rr
	    		klavijatura = new Thread(parDirki);
	    		crta.start(); ////
	    		klavijatura.start();
	    		//midi = new MIDIFormatter(Klavir.this); midi.export(im1);
	    		play.setEnabled(false);
	    	}
	    });
	    stop.setEnabled(false);
	    stop.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		if(running) { 
	    		if(crta!=null && klavijatura!=null)
	    		try {
					as.stani();
					crtez.stani();  ////
					parDirki.stani();
					running = false;
				} catch (InterruptedException e1) {}
	    		//stop.setText("Continue");
	    		}else {
	    			if(crta!=null && klavijatura!=null) {
	    			as.kreni();
	    			crtez.kreni();////
	    			parDirki.kreni();
	    			//stop.setText("Stop");
	    			running = true;
	    			}
	    		}
	    	 
	    	}
	    });
	    exit.setEnabled(false);
	    exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			as.zavrsi();
			crtez.zavrsi(); /////
			crta.interrupt();
			parDirki.zavrsi();
			klavijatura.interrupt();
			crta = null;
			klavijatura = null;
			stop.setEnabled(false);
			play.setEnabled(true);
			Klavir.this.remove(parDirki);
			Klavir.this.remove(crtez);
			}
		});
	    pokaziTastere.setEnabled(true);
	    pokaziTastere.addActionListener(new ActionListener() {
	    	@Override 
	    	public void actionPerformed(ActionEvent e) {
	    		if(parDirki!=null) {
	    			parDirki.changeKeys();
	    			parDirki.repaint();
	    			crtez.changeKeys();
	    			crtez.repaint();
	    			}
	    	}
	    });
	    record.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		if(parDirki.getK()==null) {
	    			JOptionPane.showMessageDialog(Klavir.this,
	    				    "Ovde pokretanje kompozicije znaci otvaranje poklopca klavira, zato Vas molim ucitajte kompoziciju, startujte je i pauzirajte"
	    				    + ", a zatim pokrenite snimanje. Biranje opcije Text ili Midi govori u kom formatu ce se eksportovati. Hvala.",
	    				    "Molim za razumevanje",
	    				    JOptionPane.WARNING_MESSAGE);
	    		} else {
	    		if(!snimanje) {
	    			snimanje = true;
	    			JOptionPane.showMessageDialog(Klavir.this,
	    				    "Snimanje pokrenuto",
	    				    "Snimanje",
	    				    JOptionPane.PLAIN_MESSAGE);
	    		} else {
	    			snimanje = false;
	    			JOptionPane.showMessageDialog(Klavir.this,
	    				    "Snimanje zavrseno",
	    				    "Snimanje",
	    				    JOptionPane.PLAIN_MESSAGE);
	    			System.out.println("Evo sta si snimio:");
	    			for(NotaIscrt n : recording)
	    				System.out.println(n.toString());
	    			if(mid.getState()) {
	    				String str = "";
	    				BufferedWriter writer = null;
						try {
							writer = new BufferedWriter(new FileWriter("tekstVerzija.txt"));
						} catch (IOException e1) {}
	    				
	    				for(int i = 0; i < recording.size()-1;i++) {
	    					if((recording.get(i).getMidiBr()==100 && recording.get(i).trajanjeNote()<0) || recording.get(i).trajanjeNote()<0
	    							|| recording.get(i).getKodtxt() == null) continue;
	    					if(recording.get(i+1).getTimeStart()-recording.get(i).getTimeStart() < 100) {
	    						str+="[";
	    						while(recording.get(i+1).getTimeStart()-recording.get(i).getTimeStart() < 100) {
	    							str+=recording.get(i).getKodtxt(); i++;
	    						}
	    						str+=recording.get(i).getKodtxt();
	    						str+="]";
	    					} else if(recording.get(i).getMidiBr()==100) {
	    						if(recording.get(i).trajanjeNote() < 1000)
	    							str+=" "; else str+="|";
	    					} else {
	    						str+=recording.get(i).getKodtxt();
	    					}
	    				}
	    				
	    				try {
						writer.write(str);
	    				writer.close();
	    			} catch (IOException e1) {}
	    			Kompozicija k = new Kompozicija("tekstVerzija.txt");
	    			midi = new MIDIFormatter(k, Klavir.this.ime); midi.export(Klavir.this.ime);
	    				
	    				//napravi da radi txt dobar, pa onda ucitas u kompoziciju taj txt i odatle saljes komp midif ormateru jbg
	    				//midi = new MIDIFormatter(Klavir.this); midi.export(im1);
	    			} else if(txt.getState()) {
	    				String str = "";
	    				BufferedWriter writer = null;
						try {
							writer = new BufferedWriter(new FileWriter("tekstVerzija.txt"));
						} catch (IOException e1) {}
	    				
	    				for(int i = 0; i < recording.size()-1;i++) {
	    					if((recording.get(i).getMidiBr()==100 && recording.get(i).trajanjeNote()<0) || recording.get(i).trajanjeNote()<0
	    							|| recording.get(i).getKodtxt() == null) continue;
	    					if(recording.get(i+1).getTimeStart()-recording.get(i).getTimeStart() < 100) {
	    						str+="[";
	    						while(recording.get(i+1).getTimeStart()-recording.get(i).getTimeStart() < 100) {
	    							str+=recording.get(i).getKodtxt(); i++;
	    						}
	    						str+=recording.get(i).getKodtxt();
	    						str+="]";
	    					} else if(recording.get(i).getMidiBr()==100) {
	    						if(recording.get(i).trajanjeNote() < 1000)
	    							str+=" "; else str+="|";
	    					} else {
	    						str+=recording.get(i).getKodtxt();
	    					}
	    				}
	    				
	    				try {
						writer.write(str);
	    				writer.close();
	    			} catch (IOException e1) {}
	    			}
	    		}
	    		}
	    	}
	    });
	    Panel dugmad = new Panel(new GridLayout(2,2)); //panel za dugmice auto i manual
	    dugmad.add(auto = new Checkbox("Auto", grupaDugmica, true)); //dugmad.add(new Label("Auto")); auto.getState() ako je onda nesto radi
	    dugmad.add(manual = new Checkbox("Manual", grupaDugmica, false));
	    auto.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(auto.getState()) {
					as.kreni();
					crtez.kreni();
					parDirki.kreni();
				}
			}
	    	
	    });
	    manual.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(manual.getState()) {
					
				}
			}
	    	
	    });
	    Panel sviranje = new Panel(new GridLayout(2,2)); //panel za sviranje nota ili sifra
	    sviranje.add(nota = new Checkbox("Note", tipSviranja, false)); //dugmad.add(new Label("Auto")); auto.getState() ako je onda nesto radi
	   sviranje.add(code = new Checkbox("Code note", tipSviranja, true));
	   // Panel milit = new Panel(new GridLayout(2,2)); //panel da li je mis ili tastatura
	   // milit.add(mis = new Checkbox("Mouse", rucnoMisTastatura, true));
	   // milit.add(tastatura = new Checkbox("Keyboard", rucnoMisTastatura, false));
	    Panel exp = new Panel(new GridLayout(2,2)); 
	    exp.add(mid = new Checkbox("Midi", export, true));
	    exp.add(txt = new Checkbox("Text", export, false));
	    pom.add(exit);
	    pom.add(play);
	    pom.add(stop);
	    pom.add(pokaziTastere);
	    pom.add(record);
	    pom.add(exp);
	    pom.add(dugmad);
	    pom.add(sviranje);
	    //pom.add(milit);
	    add(pom,BorderLayout.SOUTH);
  }

  //===listeneri za otvaranje i cuvanje fajla, cuvanje nzm kako radi jos===
  class OpenL implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    play.setEnabled(true);
      JFileChooser c = new JFileChooser();
      // Demonstrate "Open" dialog:
      int rVal = c.showOpenDialog(Klavir.this);
      if (rVal == JFileChooser.APPROVE_OPTION) {
        filename.setText(c.getSelectedFile().getName());
        dir.setText(c.getCurrentDirectory().toString());
        ime = c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName();
        directory = c.getCurrentDirectory().toString();
        ucitano = true;
        im1 = c.getSelectedFile().getName();
      }
      if (rVal == JFileChooser.CANCEL_OPTION) {
        filename.setText("You pressed cancel");
        dir.setText("");
      }
    }
  }

  class SaveL implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      JFileChooser c = new JFileChooser();
      // Demonstrate "Save" dialog:
      int rVal = c.showSaveDialog(Klavir.this);
      if (rVal == JFileChooser.APPROVE_OPTION) {
        filename.setText(c.getSelectedFile().getName());
        dir.setText(c.getCurrentDirectory().toString()); 
      }
      if (rVal == JFileChooser.CANCEL_OPTION) {
        filename.setText("You pressed cancel");
        dir.setText("");
      }
    }
  }

  public static void main(String[] args) {
	  Klavir k;
	  runs(k = new Klavir(), 1600, 800);
	  
    
  }
  
  public void putPressedSimbol(NotaIscrt m) {
	  pressedSimbol.add(m);
  }
  
  public void clearPressedSimbol() {
	  pressedSimbol.clear();
  }
  
  public ArrayList<NotaIscrt> getPressedSimbol(){
	  return pressedSimbol;
  }

  public static void runs(JFrame frame, int width, int height) {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBounds(100, 200, width, height);
    frame.setVisible(true);
  }

public boolean isUcitano() {
	return ucitano;
}
  
  
}