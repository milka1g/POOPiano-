package klavir;

import java.io.*;
import java.time.Duration;
import java.util.logging.Formatter;

import javax.sound.midi.*;

public class MIDIFormatter {
	private static Trajanje four=new Trajanje(1,4);
	private static Trajanje eight=new Trajanje(1,8);
	private Kompozicija komp;
	private String im = "";
	private Mapiranje mapa = new Mapiranje();
	private long actionTime=0, tpq=48;
	
	MIDIFormatter(Kompozicija k, String ime) {
		komp = k;
		im = ime;
	}
	
	public void export(String directory) {	
		try {
			Sequence s=new Sequence(Sequence.PPQ,24); //
			Track t=s.createTrack();
			byte[] b= {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
			SysexMessage sm=new SysexMessage();
			sm.setMessage(b, 6);
			MidiEvent me=new MidiEvent(sm, actionTime);
			t.add(me);
			
			MetaMessage mt=new MetaMessage(); //tempo 
			byte[] bt= {0x02,(byte)0x00,0x00};
			mt.setMessage(0x51,bt,3);
			me=new MidiEvent(mt,actionTime);
			t.add(me);
			
			ShortMessage mm=new ShortMessage(); //klavir instrument 
			mm.setMessage(0xC0,0x00,0x00);
			me=new MidiEvent(mm,(long)0);
			t.add(me);
			
			actionTime=1;
			//System.out.println(c);
			for (int i=0; i<komp.getSimboli().size();i++) {
				MuzickiSimbol m=komp.getSimboli().get(i);
				int rhythm;
				if (m.trajanje.jednako(four)) rhythm=2;  //fixxxxxx
				else rhythm = 1;
				if (m instanceof Nota) { //ako je obicna nota, znaci nije marker ili pauza onda je ubacim po trajanju samo
					if (m.trajanje.jednako(four)) rhythm=2; 
					else rhythm = 1;
					Nota n=(Nota)m;
					String noteString=n.toString();
					int midiNumber=Integer.parseInt(mapa.getMIDI(noteString)); 
					mm=new ShortMessage();
					mm.setMessage(0x90,midiNumber,100);
					me=new MidiEvent(mm,actionTime);
					t.add(me);
					mm=new ShortMessage();
					mm.setMessage(0x80,midiNumber,100);
					me=new MidiEvent(mm,actionTime+tpq*rhythm);
					t.add(me);
				}
				else if (m instanceof Marker) {
					//Marker multi=(MultiNote)m;
					int p = i+1; boolean foundPauza = true;
					while(p<komp.getSimboli().size() && !komp.getSimboli().get(p).isMarker()) {
						if(komp.getSimboli().get(p).getIstovremeno()) foundPauza = false;
						p++;
					}
					//if(i+1<komp.getSimboli().size())i++; else break; FIIIIIIIIIIIIIIIIIIIIIIIIIIIIIXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
					i++;
					if(foundPauza) {
						while(!komp.getSimboli().get(i).isMarker() && i<komp.getSimboli().size()) { // ima razmak to su 1/8 sve jedna za drugom
							rhythm = 1;
							if(!(komp.getSimboli().get(i) instanceof Pauza)) {
							String noteString=komp.getSimboli().get(i).toString();
							int midiNumber=Integer.parseInt(mapa.getMIDI(noteString)); 
							mm=new ShortMessage();
							mm.setMessage(0x90,midiNumber,100);
							me=new MidiEvent(mm,actionTime);
							t.add(me);
							mm=new ShortMessage();
							mm.setMessage(0x80,midiNumber,100);
							me=new MidiEvent(mm,actionTime+tpq*rhythm);
							t.add(me);
							actionTime+=tpq*rhythm;
							}
							i++;
						} continue;
					} else {
					while(!komp.getSimboli().get(i).isMarker() && i<komp.getSimboli().size()) { //ako nema razmaka svira se istovremeno 
						rhythm = 2;
						Nota noteMulti = (Nota)komp.getSimboli().get(i);
						String noteString = noteMulti.toString();
						int midiNumber=Integer.parseInt(mapa.getMIDI(noteString)); 
						long actionMulti = actionTime;
						mm=new ShortMessage();
						mm.setMessage(0x90,midiNumber,100);
						me=new MidiEvent(mm,actionMulti);
						t.add(me);
						mm=new ShortMessage();
						mm.setMessage(0x80,midiNumber,100);
						me=new MidiEvent(mm,actionMulti + tpq*rhythm);
						t.add(me);
						i++;
					}
				}
				}
				
				actionTime+=tpq*rhythm; 
			}
			actionTime+=tpq;
			mt=new MetaMessage();
			byte[] bet={};
			mt.setMessage(0x2F,bet,0);
			me=new MidiEvent(mt,actionTime);
			t.add(me);
			File f=new File(im+".mid");
			MidiSystem.write(s, 1, f);
		} catch (InvalidMidiDataException imde) {
		} catch (IOException e) {
		}
	}
}