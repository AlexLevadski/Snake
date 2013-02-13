package alex.levadski.snake;

import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class GameActivity extends Activity implements SensorEventListener, OnTouchListener {
	 
    GameSurface surf;
    Timer t;
    int width, height;
 
    public static Vibrator vibrator;
    
    public static float GyroX = 0, GyroY = 0;
    
    SensorManager mSensorManager;
    Sensor mAccelerometerSensor;
 
    float SSX = 0, SSY = 0;
    float SX = 0, SY = 0;
    boolean firstTime;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surf = new GameSurface(this);
        this.setContentView(surf);
        t = new Timer();
        height = this.getWindowManager().getDefaultDisplay().getHeight();
        width = this.getWindowManager().getDefaultDisplay().getWidth();
 
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
        mSensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        if (sensors.size() > 0) {
            for (Sensor sensor : sensors) {
                if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    if (mAccelerometerSensor == null)
                        mAccelerometerSensor = sensor;
                }
            }
        }
    }
 
    @Override
    public void onStart() {
        super.onStart();
        t.scheduleAtFixedRate(new GraphUpdater(surf), 0, 100);
        t.scheduleAtFixedRate(new StepUpdater(this), 0, 500);
        mSensorManager.registerListener(this, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
        this.firstTime = true;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
 
     @Override
    public void onStop() {
        super.onStop();
        t.cancel();
        t.purge();
        mSensorManager.unregisterListener(this);
    }
 
//  @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing!
    }
 
   
 
   //  @Override
    public void onSensorChanged(SensorEvent event) 
    {
        
    float x = event.values[0];
    float y = event.values[1];
    
    GyroX = x;
    GyroY = y;
    
    if (x > 0) 
    	{
    	GameManager.right = true;
    	GameManager.left = false;
    	}
    
    if (x < 0) 
    	{
    	GameManager.left = true;
    	GameManager.right = false;
    	}
    
    if (y > 0) 
	{
	GameManager.down = true;
	GameManager.up = false;
	}
    
    if (y < 0) 
	{
	GameManager.up = true;
	GameManager.down = false;
	}
    
}
 
     public void Step() 
    {
        
    }
     
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			GameManager.isFight = true;
			break;
		case MotionEvent.ACTION_UP:
		break;
			
		}
		return true;
	}
	
	
}