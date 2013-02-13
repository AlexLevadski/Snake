package alex.levadski.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class PauseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pause);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pause, menu);
		return true;
	}

	
	public void onClick(View view)
	{
		switch (view.getId())
		{
		case R.id.exit:
			Intent intent = new Intent(PauseActivity.this, StartMenuActivity.class);
			startActivity(intent);
			break;
		case R.id.restart:
			GameManager.ResetAchievements();
			GameManager.isPause = false;
			Intent restartGame = new Intent(PauseActivity.this, GameActivity.class);
			startActivity(restartGame);
			break;
		case R.id.resume:
			GameManager.isPause = false;
			Intent resumeGame = new Intent(PauseActivity.this, GameActivity.class);
			startActivity(resumeGame);
			break;
		}
	}
}
