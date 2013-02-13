package alex.levadski.snake;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StartMenuActivity extends Activity implements OnTouchListener{

	Button button;
	TextView tv;
	
	public static int GAME_MODE = 0;
	public static int GAME_SCORE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ImageView button = (ImageView) findViewById(R.id.newGame);
		
		button.setOnTouchListener(this);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	public void onClick(View view)
	{
		if (view.getId() == R.id.newGame)
		{
		Intent i = new Intent(this, GameActivity.class);
       
        this.startActivity(i);
		}
	}


	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch (v.getId())
		{
		case R.id.newGame:
			
			switch (event.getAction())
			{
			case MotionEvent.ACTION_DOWN:
				ImageView button = (ImageView) findViewById(R.id.newGame);
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.startbuttonpressed);
				@SuppressWarnings("deprecation")
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);

				button.setImageDrawable(bitmapDrawable);
				break;
			case MotionEvent.ACTION_UP:
				ImageView button1 = (ImageView) findViewById(R.id.newGame);
				Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.startbutton);
				@SuppressWarnings("deprecation")
				
				BitmapDrawable bitmapDrawable1 = new BitmapDrawable(bitmap1);

				button1.setImageDrawable(bitmapDrawable1);
				
				Intent i = new Intent(this, GameActivity.class);
			       
		        this.startActivity(i);
				break;
			}
			break;
		}
		
		return true;
	}

}
