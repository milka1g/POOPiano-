package klavir;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.Scanner;
public class MidiPlayer {
 private static final int DEFAULT_INSTRUMENT = 1;
 private MidiChannel channel;


 
 public MidiPlayer() throws MidiUnavailableException {
 this(DEFAULT_INSTRUMENT);
 }
 
 public MidiPlayer(int instrument) throws
MidiUnavailableException {
 channel = getChannel(instrument);
 }
 
 public void play(final int note) {
 channel.noteOn(note, 50);
 
 }
 
 public void release(final int note) {
 channel.noteOff(note, 50);
 
 }
 
 public void play(final int note, final long length)
throws InterruptedException {		
	play(note);
	//System.out.println(note);
	Thread.sleep(length);
	release(note);
 }
 
 public void play(int[] note, final long length)
throws InterruptedException {	
	for(int i = 0; i<note.length; i++) {
	play(note[i]);
	//System.out.println(note[i]);
	}
	Thread.sleep(length);
	for(int i = 0; i<note.length; i++)
	release(note[i]);
 }
 
 public void playpauza(final long length) throws InterruptedException {
	 Thread.sleep(length);
 }
 
 private static MidiChannel getChannel(int instrument)
throws MidiUnavailableException {
 Synthesizer synthesizer = MidiSystem.getSynthesizer();
 synthesizer.open();
 return synthesizer.getChannels()[instrument];
 }
 
 /*public void run() {
	 try {
	 while(!Thread.interrupted()) {
				synchronized (this) {
					if(!radi) wait();
				}
	 }
	 }catch(InterruptedException e) {}
 }
 
 public void stani() throws InterruptedException {radi = false; interrupt();}
 public void kreni() {radi = true; notifyAll();}
 public void zavrsi() {interrupt();}*/
 
 
 
 public static void main(String[] args) throws Exception {
 MidiPlayer player = new MidiPlayer();
 Scanner scanner = new Scanner(System.in);
 int note;
 while (!Thread.currentThread().isInterrupted()) {
 System.out.print("Note (1..127) : ");
 note = scanner.nextInt();
 }
 scanner.close();
 }
}