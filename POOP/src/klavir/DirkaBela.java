package klavir;

import java.awt.*;

public class DirkaBela extends Dirka {

	
	public DirkaBela(NotaIscrt s, int time){
		//tip sredi
		super(s,time);
		platno.setSize(25, 100);
		add(platno);
		platno.setVisible(true);
		this.setBackground(Color.white);
				
		this.setBounds(0,0,25,100);
		setVisible(true);
	}
	@Override
	public void paint(Graphics g) {
		g.setFont(new Font ("Serif", Font.BOLD, 12));
		if(fromRed) { 
			g.setColor(new Color(255,155,155)); 
			g.fillRect(0, 0, 25, 100);
			g.setColor(Color.black);
			g.drawRect(0, 0, 25, 100);
			g.drawString(simbol.getNotaReal(),3,50);
			try {
				Thread.currentThread().sleep(redTime);
			} catch (InterruptedException e) {}
		
		}
			g.setColor(Color.white);
			g.fillRect(0, 0, 25, 100);
			g.setColor(Color.black);
			g.drawRect(0, 0, 25, 100);

			g.drawString(simbol.getNotaReal(),3,50);
		fromRed = false;
	}

	
	public static void main(String[] args) {
		Frame f = new Frame();
		f.setLayout(new BorderLayout());
		DirkaBela d = new DirkaBela(new NotaIscrt("t","C4",45), 2000);
		f.add(d,BorderLayout.CENTER);
		f.setBounds(100,100,300,200);
		f.setVisible(true);
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {}
		d.red();
	}

}
