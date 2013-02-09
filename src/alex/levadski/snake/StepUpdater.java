package alex.levadski.snake;

import java.util.TimerTask;

public class StepUpdater extends TimerTask {

	GameActivity act;
	
	StepUpdater(GameActivity act)
	{
		this.act = act;
	}
	
	@Override
	public void run() 
	{
		act.Step();
	}

}
