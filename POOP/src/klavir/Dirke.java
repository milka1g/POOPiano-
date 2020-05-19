package klavir;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;


public class Dirke extends JPanel {

	private Mapiranje mapa = new Mapiranje();
	private ArrayList<Dirka> dirke = new ArrayList<Dirka>();
	private JPanel bele = new JPanel(new GridLayout(1,0));
	private JPanel crne = new JPanel(new GridLayout(1,0));
	
	public Dirke() {
		super();
		bele.setBounds(0,0,1050,100);
		crne.setBounds(0,0,500,60);

		for(int i = 0; i<mapa.getAllNotes().size();i++) {
			if(mapa.getAllNotes().get(i).getNotaReal().contains("#")) {
				crne.add(new DirkaCrna(mapa.getAllNotes().get(i), 100));
			}
			else {
				bele.add(new DirkaBela(mapa.getAllNotes().get(i), 100));
			}
			
		}
		add(bele); add(crne);
		bele.setVisible(true); crne.setVisible(true);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Frame f = new Frame();
		f.setLayout(new BorderLayout());
		Dirke d = new Dirke();
		f.add(d, BorderLayout.CENTER);
		f.setBounds(100,100,1100,300);
		f.setVisible(true);
		
	}

}
