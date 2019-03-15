package au.pkj.life;

import java.awt.EventQueue;

public class GameLife {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new GameUI().setVisible(true);
			}
		});
	}

}
