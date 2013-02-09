package alex.levadski.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SimpleSnakeActivity extends Activity {

	Button button;
	TextView tv;
	
	public static int GAME_MODE = 0;
	public static int GAME_SCORE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	public void onClick(View view)
	{
		Intent i = new Intent(this, GameActivity.class);
        GAME_MODE=0;
        GAME_SCORE=0;
        this.startActivity(i);
	}

}
