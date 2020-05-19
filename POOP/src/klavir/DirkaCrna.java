package klavir;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;

public class DirkaCrna extends Dirka {

	public DirkaCrna(NotaIscrt s,int time) {
		super(s,time);
		this.setBackground(Color.white);
		platno.setSize(20,60);
		add(platno);
		platno.setVisible(true);
		this.setBounds(0,0,20,60);
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setFont(new Font ("Serif", Font.BOLD, 12));
		if(fromRed) { 
			g.setColor(new Color(255,155,155)); 
			g.fillRect(0, 0, 20, 60);
			g.drawRect(0, 0, 20, 60);
			g.setColor(Color.white);
			g.drawString(simbol.getNotaReal(),3,30);
			try {
				Thread.currentThread().sleep(redTime);
			} catch (InterruptedException e) {}
		
		}
			g.setColor(Color.black);
			g.fillRect(0, 0, 20, 60);
			g.setColor(Color.black);
			g.drawRect(0, 0, 20, 60);
			g.setColor(Color.white);
			g.drawString(simbol.getNotaReal(),3,30);
		fromRed = false;
	}
	
	public static void main(String[] args) {
		Frame f = new Frame();
		f.setLayout(new BorderLayout());
		DirkaCrna d = new DirkaCrna(new NotaIscrt("t","C4",45), 2000);
		f.add(d,BorderLayout.CENTER);
		f.setBounds(100,100,300,200);
		f.setVisible(true);
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {}
		d.red();
	}

}
