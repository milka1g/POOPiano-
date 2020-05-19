package klavir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Kompozicija {
	
	private Mapiranje mapa = new Mapiranje();
	private String note = "", midi = ""; //bzvz testiranje
	protected ArrayList<String> Note = new ArrayList<String>(); //kodovi t y u w ! , ... sta sam dodao 
	ArrayList<MuzickiSimbol> simboli = new ArrayList<MuzickiSimbol>(); //trajanje i code tipa t y u w ... 
	protected ArrayList<String> codes = new ArrayList<String>(); // codes je matcher.group(1) stalno 
	protected ArrayList<String> NoteStvarne = new ArrayList<String>(); //not important ukljucujuci i markere BMK za ovo

	public Kompozicija(String s) {
		String line = null;
		String komp = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(s));
			while ((line = br.readLine()) != null) {
			       komp+=line;
			    }
			br.close();
		} catch (IOException e) {}
		parsiraj(komp);
		for(int i = 0; i<simboli.size();i++) {
			//System.out.println(simboli.get(i).toString());
			if(!simboli.get(i).toString().equals(" ")) {
			note+=mapa.getNota(simboli.get(i).toString());
			midi+=mapa.getMIDI(simboli.get(i).toString());
			Note.add(simboli.get(i).toString());
			NoteStvarne.add(mapa.getNota(simboli.get(i).toString()));
			}
			else {
				Note.add(" ");
				NoteStvarne.add(" ");
				note+=" ";
				midi+=" ";
			}
		}
		//System.out.println(simboli);
		//for(int i = 0; i < simboli.size(); i++)
			//System.out.println(simboli.get(i).getIstovremeno());
	}

	private void parsiraj(String s) {
		
		Pattern pattern = Pattern.compile("([^\\[\\]]{1}|\\[([^\\]]+)\\])");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			codes.add(matcher.group(1));
		    if(matcher.group(1).equals(" "))
		    	simboli.add(new Pauza(1,8));
		    else if (matcher.group(1).equals("|"))
		    	simboli.add(new Pauza(1,4));
		    else if(matcher.group(1).length()==1) {
		    	simboli.add(new Nota(1,4,matcher.group(1)));
		    }
		    else {
		    	simboli.add(new Marker());
		    	Pattern pat = Pattern.compile("\\s");
		    	Matcher mat = pat.matcher(matcher.group(1));
		    	boolean found = mat.find();
		    	//System.out.println(found);
		    	//System.out.println("OVO" + matcher.group(1));
		    	if(found) {
		    		for(int i = 1;i<matcher.group(1).length()-1;i++) {
		    			if(!(matcher.group(1).charAt(i) == ' '))
		    				simboli.add(new Nota(1,8,String.valueOf(matcher.group(1).charAt(i))));
		    		}
		    	}
		    	else {
		    		for(int j = 1;j<matcher.group(1).length()-1;j++) {
		    			MuzickiSimbol k;
		    			simboli.add(k = new Nota(1,4,String.valueOf(matcher.group(1).charAt(j))));
		    			k.setIstovremeno();
		    		}
		    	}
		    	simboli.add(new Marker());
		    }
			//System.out.println(matcher.group(1));
		}
		
	}
	
	
	public ArrayList<String> getNote() {
		return Note;
	}

	public ArrayList<MuzickiSimbol> getSimboli() {
		return simboli;
	}

	public ArrayList<String> getCodes() {
		return codes;
	}

	public ArrayList<String> getNoteStvarne() {
		return NoteStvarne;
	}

	public static void main(String[] args) {
		Kompozicija k = new Kompozicija("ode.txt");
		System.out.println(k.getCodes());
		System.out.println("============");
		System.out.println(k.getNote());
		System.out.println("============");
		System.out.println(k.getNoteStvarne());
		System.out.println("============");
		System.out.println(k.getSimboli());
	}

}
