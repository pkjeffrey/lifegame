package au.pkj.life;

import javax.swing.JFrame;

public class GameUI extends JFrame {
	private GameState _gameState;
	private GameThread _gameThread;
	
	public GameUI() {
		initComponents();
	}
	
	public void onSetup() {
		_gameState = new GameState(20, 10);
		_gameState.setup("");
	}
	
	public void onStart() {
		if (_gameState != null) {
			_gameThread = new GameThread(this);
			_gameThread.setRunning(true);
			_gameThread.run();
		}
	}
	
	public void onStop() {
		if (_gameThread != null) {
			_gameThread.setRunning(false);
			while (_gameThread != null) {
				try {
					_gameThread.join();
					_gameThread = null;
				} catch (InterruptedException ex) {}
			}
		}
	}
	
	public void render() {
		
	}
	
	public void update() {
		_gameState.update();
	}
	
	private void initComponents() {
		
	}
}
