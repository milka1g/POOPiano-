package klavir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mapiranje {
	
	private ArrayList<NotaIscrt> allNotes = new ArrayList<NotaIscrt>();
	private HashMap<String,String> codeToNota = new HashMap<String,String>();
	private HashMap<String,String> codeToMIDI = new HashMap<String,String>();
	private HashMap<String,String> MIDItoCode = new HashMap<String,String>();
	private ArrayList<String> codes = new ArrayList<String>();
	

	public Mapiranje() {
		String line = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("map.csv"));

			while ((line = br.readLine()) != null) {
			       parsiraj(line);
			    }
			br.close();
		} catch (IOException e) {}
		   
	}
	
	
	private void parsiraj(String s) {
	
		Pattern p = Pattern.compile("^(.),({1}[A-Z]#?{1}[0-9]),(\\d{2})$");
		Matcher m = p.matcher(s);
		if(m.matches()) {
			codeToNota.put(m.group(1),m.group(2));
			codeToMIDI.put(m.group(1),m.group(3));
			MIDItoCode.put(m.group(3),m.group(1));
			allNotes.add(new NotaIscrt(m.group(1), m.group(2), Integer.parseInt(m.group(3))));
		}
	}
	
	public String getNota(String code) {
		return codeToNota.get(code);
	}
	
	public String getMIDI(String code) {
		return codeToMIDI.get(code);
	}
	
	public String MIDIToCode(String midi) {
		return MIDItoCode.get(midi);
	}
	
	
	
	public HashMap<String, String> getCodeToNota() {
		return codeToNota;
	}


	public HashMap<String, String> getCodeToMIDI() {
		return codeToMIDI;
	}


	public ArrayList<String> getCodes() {
		return codes;
	}

	

	public ArrayList<NotaIscrt> getAllNotes() {
		return allNotes;
	}


	

	public static void main(String[] args) {
		Mapiranje m = new Mapiranje();
		System.out.println(m.getCodes());
		System.out.println("++++");
		System.out.println(m.getCodeToMIDI());
		System.out.println("++++");
		System.out.println(m.getCodeToNota());
		System.out.println("++++");
		System.out.println(m.allNotes);
		
		
		
	}
	
	

}
