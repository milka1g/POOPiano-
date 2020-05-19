package klavir;

import java.awt.*;

import javax.swing.*;

public class Dirka extends Panel {
	
	protected static int len_short = 200;
	protected static int len_long = 400;
	protected Canvas platno = new Canvas();
	protected int redTime; 
	protected boolean tip = true; //sredi
	protected boolean fromRed = false;
	protected Mapiranje mapa = new Mapiranje();
	protected NotaIscrt simbol;

	public Dirka(NotaIscrt s, int time) {
		//tip sredi
		redTime = time;
		simbol = s;

	}


	
	public void red() {
		fromRed = true;
		repaint();
	}
	
	public static void main(String[] args) {
		Frame f = new Frame();
		f.setLayout(new BorderLayout());
		Dirka d = new Dirka(new NotaIscrt("t","C4",45), 2000);
		f.add(d,BorderLayout.CENTER);
		f.setBounds(100,100,300,200);
		f.setVisible(true);
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {}
		d.red();
	}

}
