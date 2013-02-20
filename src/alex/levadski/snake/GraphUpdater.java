package alex.levadski.snake;

import java.util.TimerTask;

import android.graphics.Canvas;
import android.graphics.Color;

public class GraphUpdater extends TimerTask {

	GameSurface surf;
	
	GraphUpdater (GameSurface surf)
	{
		this.surf = surf;
	}
	
	@Override
	public void run()
	{
		Canvas c = surf.getHolder().lockCanvas();
		if (c!=null)
		{
			//c.drawColor(Color.WHITE);
			surf.drawGame(c);
			surf.getHolder().unlockCanvasAndPost(c);
		}
	}
}
