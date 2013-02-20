package alex.levadski.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class StartMenuActivity extends Activity{

	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}


	public void onClick(View view)
	{
		if (view.getId() == R.id.newGame)
		{
		intent = new Intent(this, GameActivity.class);
       
        this.startActivity(intent);
		}
	}
}
