package alex.levadski.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends Activity 
	{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		TextView scores = (TextView) findViewById(R.id.scoreText);
		scores.setText("Your score is " + GameManager.score);
		
		TextView distance = (TextView) findViewById(R.id.distance);
		distance.setText("Your distance is " + GameManager.distance);
		
		GameManager.ResetAchievements();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_game_over, menu);
		return true;
	}

	public void onClick(View view)
	{
		switch (view.getId())
		{
		case R.id.exit:
			Intent intent = new Intent(GameOverActivity.this, StartMenuActivity.class);
			startActivity(intent);
			break;
		case R.id.restart:
			Intent intent2 = new Intent(GameOverActivity.this, GameActivity.class);
			startActivity(intent2);
			break;
		}
	}
	
}
