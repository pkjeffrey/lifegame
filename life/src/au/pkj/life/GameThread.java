package au.pkj.life;

public class GameThread extends Thread {
	private volatile boolean _running;
	private volatile int _framePeriod;
	private GameUI _gameUI;
	
	public GameThread(GameUI gameUI) {
		super();
		_running = false;
		_framePeriod = 100;
		_gameUI = gameUI;
	}
	
	public void setRunning(boolean running) {
		_running = running;
	}
	
	public void setFramePeriod(int framePeriod) {
		_framePeriod = framePeriod;
	}
	
	@Override
	public void run() {
		while (_running) {
			long beginTime = System.currentTimeMillis();
			_gameUI.update();
			_gameUI.render();
			long timeDiff = System.currentTimeMillis() - beginTime;
			long sleepTime = _framePeriod - timeDiff;
			
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ex) {}
			}
		}
	}
}
