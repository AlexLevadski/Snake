package alex.levadski.snake;

import java.util.Random;

import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameSurface extends SurfaceView implements OnTouchListener
{

	GameManager mField;
	
	Random random = new Random();
	
	Bitmap ship, bullet, enemy, pause, pauseRed, explosion;
	
	float x, y;
	
	
	public void setXY(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public GameSurface(Context context) 
	{
		super(context);
		mField = new GameManager();
		ship = 		BitmapFactory.decodeResource(getResources(), R.drawable.shipcool);	
		bullet = 	BitmapFactory.decodeResource(getResources(), R.drawable.bullet);	
		enemy =  	BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);	
		pause =  	BitmapFactory.decodeResource(getResources(), R.drawable.pause);	
		pauseRed =  BitmapFactory.decodeResource(getResources(), R.drawable.pause2);	
		explosion =  BitmapFactory.decodeResource(getResources(), R.drawable.explosion);	
		
		
		for (int i = 0; i < GameManager.maxStars; i++)
		{
			GameManager.starMapX[i] = random.nextInt(240);
			GameManager.starMapY[i] = random.nextInt(320);
		}

		for (int i = 0; i < GameManager.maxEnemies; i++)
		{
			GameManager.enemyMapX[i] = random.nextInt(240);
			GameManager.enemyMapY[i] = random.nextInt(320);
		}
		
		this.setOnTouchListener(this);
		}

	void drawGame(Canvas c)
	{
		GameManager.screenWidth  = c.getWidth();
		GameManager.screenHeight = c.getHeight();
		
		///////////////////////////////////////////////////////////////////////////ÈÍÈÖÈÀËÈÇÀÖÈß PAINT-À È ÁÈÒÌÀÏÎÂ
		Paint paint = new Paint();
		
		Bitmap mShip = Bitmap.createScaledBitmap(ship,  GameManager.screenWidth/36*4, GameManager.screenHeight/48*5, true);
		Bitmap mBullet = Bitmap.createScaledBitmap(bullet, GameManager.screenWidth/48, GameManager.screenHeight/40, true);
		Bitmap mEnemy = Bitmap.createScaledBitmap(enemy, GameManager.screenWidth/9, GameManager.screenHeight/12, true);
		Bitmap mPause = Bitmap.createScaledBitmap(pause, GameManager.screenWidth/6, GameManager.screenHeight/8, true);
		Bitmap mPauseBig = Bitmap.createScaledBitmap(pauseRed, GameManager.screenWidth/2, GameManager.screenHeight/2, true);
		Bitmap mExplosion = Bitmap.createScaledBitmap(explosion, GameManager.screenWidth/9, GameManager.screenHeight/12, true);
		
		////////////////////////////////////////////////////////////////////////////ÎÒÐÈÑÎÂÊÀ ÔÎÍÀ

		DrawBackground(c, paint);
		
		////////////////////////////////////////////////////////////////////////////ÏÀÓÇÀ
		if (GameManager.isPause) 
		{
			c.drawBitmap(mPauseBig,GameManager.screenWidth/4, GameManager.screenHeight/4, paint);
		}
		////////////////////////////////////////////////////////////////////////////ÈÃÐÀ
		else
		{
		paint.setAlpha(255);
			
		ShipPhysics(c, mShip, paint);	////ÎÒÐÀÁÎÒÊÀ ÔÈÇÈÊÈ È ÎÒÐÈÑÎÂÊÀ ÍÀØÅÃÎ ÊÎÐÀÁËß
		
		DrawStarsAndEnemies(c, paint, mEnemy); ////ÃÅÍÅÐÀÖÈß È ÎÒÐÈÑÎÂÊÀ ÊÎÐÀÁËÅÉ ÏÐÎÒÈÂÍÈÊÎÂ È ÇÂÅÇÄ
		
		DrawBottomPanel(c, paint, mBullet); /////////ÎÒÐÈÑÎÂÊÀ ÍÈÆÍÅÉ ÈÍÄÈÊÀÒÎÐÍÎÉ ÏÀÍÅËÈ
		
//		switch (GameManager.weapon)
//		{ case 0:
		{ rocketShotManager(c, paint, mBullet, mShip); }  ///////////ÎÒÐÀÁÎÒÊÀ ÂÛÑÒÐÅËÎÂ
//		case 1: { laserShotManager(c, paint, mShip); }  ///////////ÎÒÐÀÁÎÒÊÀ ÂÛÑÒÐÅËÎÂ
//		}
		
		DrawTopPanel(c, paint, mPause);//////////////ÎÒÐÈÑÎÂÊÀ ÂÅÐÕÍÅÉ ÈÍÄÈÊÀÒÎÐÍÎÉ ÏÀÍÅËÈ
		
		RefreshEnemyRects();
		
		RefreshBulletsRects();
		
		CollisionDetector(c, paint, mExplosion, mShip);
		
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			
			if ((event.getX() > (GameManager.screenWidth/6)*5) && (event.getY() < GameManager.screenWidth/6))
			{
				GameManager.isPause = !GameManager.isPause;
			}
			else if (GameManager.isPause)
				{
				GameManager.health = GameManager.maxHealth;
				GameManager.isPause = !GameManager.isPause;
				}
			else GameManager.isFight = true;
			
			break;
		case MotionEvent.ACTION_UP:
			GameManager.isFight = false;
			break;
			
		}
		return true;
	}
	
	public void ShipPhysics(Canvas c, Bitmap mShip, Paint paint)
	{
		if (GameManager.Xpos <= GameManager.screenWidth/24) GameManager.Xpos = GameManager.screenWidth/24;
		if (GameManager.Xpos >= (GameManager.screenWidth - GameManager.screenWidth/4)) GameManager.Xpos = GameManager.screenWidth - GameManager.screenWidth/4;
		if (GameManager.Ypos <= GameManager.screenHeight/32) GameManager.Ypos = GameManager.screenHeight/32;
		if (GameManager.Ypos >= (GameManager.screenHeight - GameManager.screenHeight/48*5)) GameManager.Ypos = GameManager.screenHeight - GameManager.screenHeight/48*5;
		
		if (GameManager.left)  GameManager.Xpos -= GameActivity.GyroX*4;
		if (GameManager.right) GameManager.Xpos -= GameActivity.GyroX*4;
		if (GameManager.up)    GameManager.Ypos += GameActivity.GyroY*2;
		if (GameManager.down)  GameManager.Ypos += GameActivity.GyroY*3;
		
		c.drawBitmap(mShip, GameManager.Xpos,  GameManager.Ypos, paint);
	}
	
	public void DrawStarsAndEnemies(Canvas c, Paint paint, Bitmap mEnemy)
	{
		paint.setColor(Color.WHITE);

		for (int i = 0; i < GameManager.maxStars; i++)
		{
			if (GameManager.starMapY[i] > GameManager.screenHeight)
			{
				GameManager.starMapY[i] = 0;
				GameManager.starMapX[i] = random.nextInt(GameManager.screenWidth);
			}
			
			int k = random.nextInt(5);
			switch (k)
			{
			case 0 :
				paint.setColor(Color.YELLOW);
				break;
			case 1 :
				paint.setColor(Color.rgb(255, 255, 155));
				break;
			
			case 2 :
				paint.setColor(Color.rgb(255, 255, 170));
				break;
				
			case 3 :
				paint.setColor(Color.rgb(255, 255, 210));
				break;
				
			case 4 :
				paint.setColor(Color.rgb(255, 255, 240));
				break;
			}
			
			c.drawLine(GameManager.starMapX[i], GameManager.starMapY[i], GameManager.starMapX[i], GameManager.starMapY[i] + 1, paint);
			GameManager.starMapY[i]+=GameManager.starSpeed;
		}
		
		for (int i = 0; i < GameManager.maxEnemies; i++)
		{
			if (GameManager.enemyMapY[i] > GameManager.screenHeight)
			{
				GameManager.enemyMapY[i] = 0;
				GameManager.enemyMapX[i] = random.nextInt(GameManager.screenWidth);
			}
			c.drawBitmap(mEnemy,GameManager.enemyMapX[i], GameManager.enemyMapY[i], paint);
			GameManager.enemyMapY[i]+=GameManager.enemySpeed;
		}
	}
	
	public void DrawBottomPanel(Canvas c, Paint paint, Bitmap mBullet)
	{
		paint.setColor(Color.rgb(45, 57, 0));
		
		c.drawRect(0, GameManager.screenHeight - GameManager.screenHeight/21, GameManager.screenWidth, GameManager.screenHeight, paint);
		
		paint.setColor(Color.rgb(45, 80, 0));
		
		c.drawLine(0, GameManager.screenHeight - GameManager.screenHeight/21, GameManager.screenWidth,
				GameManager.screenHeight - GameManager.screenHeight/21, paint);
		
		c.drawLine(0, GameManager.screenHeight - GameManager.screenHeight/21 - 1, GameManager.screenWidth,
				GameManager.screenHeight - GameManager.screenHeight/21 - 1, paint);
		
		for (int i = 1; i <= GameManager.bulletCounter;i++)
		{
			c.drawBitmap(mBullet, i*GameManager.screenWidth/24, GameManager.screenHeight - GameManager.screenHeight/32, paint);	
		}
			
		paint.setColor(Color.GREEN);
		c.drawRect(GameManager.screenWidth/80, GameManager.screenHeight - GameManager.screenHeight/21 - 3, GameManager.BulletIndicator, 
					   GameManager.screenHeight - GameManager.screenHeight/21 - 1, paint);
		
		paint.setColor(Color.RED);
		
		c.drawRect(2, GameManager.screenHeight - GameManager.screenHeight/16 + GameManager.screenHeight/16*GameManager.health, 5, 
				GameManager.screenHeight - GameManager.screenHeight/16, paint);
		}
	
	
	
	public void DrawTopPanel(Canvas c, Paint paint, Bitmap mPause)
	{
		paint.setColor(Color.rgb(45, 80, 0));
		
		c.drawText("Distanse: " + GameManager.distance, 2, 10, paint);
		GameManager.distance++;
		if ((GameManager.distance % 50 == 0) && (GameManager.bulletCounter < 24)) GameManager.bulletCounter += 2;
		
		paint.setColor(Color.RED);
		c.drawText("Health: " + GameManager.health, 2, 25, paint);
		
		paint.setColor(Color.RED);
		c.drawText("Score: " + GameManager.score, 2, 40, paint);
		
		paint.setColor(Color.RED);
		c.drawText("Last: " + GameManager.lastScore, 2, 55, paint);
		
		paint.setColor(Color.RED);
		c.drawText("Best: " + GameManager.bestScore, 2, 70, paint);
		
		paint.setColor(Color.RED);
		
		c.drawRect(2, 300 - 10*GameManager.health, 5, 300, paint);
		
		c.drawBitmap(mPause, (GameManager.screenWidth/6)*5, 0, paint);
		
		if (GameManager.distance % 50 == 0) 
			{
			GameManager.score += 10;
			}
		if (GameManager.distance % 200 == 0)
			{
			GameManager.enemySpeed++;
			GameManager.bulletCounter++;
			}
		if (GameManager.distance % 1000 == 0) 
			{
			GameManager.health++;
			}
		
		
	}
	
	
	public void DrawBackground(Canvas c, Paint paint)
	{
		Bitmap space =  BitmapFactory.decodeResource(getResources(), R.drawable.space);	
		Bitmap mSpace = Bitmap.createScaledBitmap(space, c.getWidth(),c.getHeight(), true);
		c.drawBitmap(mSpace, 0, 0, paint);
	}
	
	public Rect getShipRect(Bitmap mShip)
	{
		return new Rect(GameManager.Xpos, GameManager.Ypos, GameManager.Xpos + mShip.getWidth(), GameManager.Ypos + mShip.getHeight());
	}
	
	public void RefreshEnemyRects()
	{
		for (int i = 0; i < GameManager.maxEnemies;i++)
		{
			GameManager.enemyRects[i] = new Rect(GameManager.enemyMapX[i], GameManager.enemyMapY[i],
					GameManager.enemyMapX[i] + GameManager.screenWidth/9, GameManager.enemyMapY[i] + GameManager.screenHeight/12);
		}
	}
	
	public void RefreshBulletsRects()
	{
		
		for (int i = 0; i < GameManager.maxEnemies;i++)
		{
			GameManager.enemyRects[i] = new Rect(GameManager.enemyMapX[i], GameManager.enemyMapY[i],
					GameManager.enemyMapX[i] + GameManager.screenWidth/9, GameManager.enemyMapY[i] + GameManager.screenHeight/12);
		}
		
		GameManager.bullet = new Rect(GameManager.XBulletPosition,GameManager.YBulletPosition,
											GameManager.XBulletPosition + GameManager.screenWidth/48, 
											GameManager.YBulletPosition+GameManager.screenHeight/40);
	}
	
	public void CollisionDetector(Canvas c, Paint paint, Bitmap mExplosion, Bitmap mShip)
	{
		for (int i = 0; i < GameManager.maxEnemies;i++) 
		{
			if (GameManager.enemyRects[i].intersect(getShipRect(mShip)))
			{
				GameActivity.vibrator.vibrate(100);
				
				if (GameManager.health > 1) 
				{
					
					GameManager.health--;
				}
				else 
					{

					GameManager.isPause = true;
					GameManager.ResetAchievements();
					}
					
					DisposeEnemy(i, c, paint, mExplosion);
			}
			
			if (GameManager.enemyRects[i].intersect(GameManager.bullet)) 
			{
				DisposeEnemy(i, c, paint, mExplosion);
				GameManager.YBulletPosition = -20;
				GameManager.score +=5;
			}
			
			
		}
	}
	
	public void DisposeEnemy(int i, Canvas c, Paint paint, Bitmap mExplosion)
	{
		c.drawBitmap(mExplosion, GameManager.enemyMapX[i], GameManager.enemyMapY[i], paint);
		
		GameManager.enemyMapX[i] = random.nextInt(GameManager.screenWidth);
		GameManager.enemyMapY[i] = 0;	
	}
	
	public void rocketShotManager(Canvas c, Paint paint, Bitmap mBullet, Bitmap mShip)
	{
		if (GameManager.isFight)
		{
			if (!GameManager.isBulletOnMap && GameManager.bulletCounter > 0)
			{
			GameManager.isFight = false;
			GameManager.isBulletOnMap = true;
			GameManager.YBulletPosition = GameManager.Ypos;
			GameManager.XBulletPosition = GameManager.Xpos + getShipRect(mShip).width()/2;
			GameManager.bulletCounter--;
			}
		}
		
		if (GameManager.isBulletOnMap)
		{
			if (GameManager.YBulletPosition <= 0) GameManager.isBulletOnMap = false;
			
			c.drawBitmap(mBullet, GameManager.XBulletPosition, GameManager.YBulletPosition, paint);
						
			GameManager.BulletIndicator = GameManager.YBulletPosition;
			
			GameManager.YBulletPosition -= 25;
		}
	}
	
	public void laserShotManager(Canvas c, Paint paint, Bitmap mShip)
	{
		if (GameManager.isFight)
		{
			if (GameManager.laserCounter > 0)
			{
			GameManager.isFight = false;
			
			paint.setColor(Color.rgb(9, 255, 255));
			c.drawLine(GameManager.Xpos + getShipRect(mShip).width()/2, GameManager.Ypos, GameManager.Xpos + getShipRect(mShip).width()/2, 0, paint);
			
			GameManager.laserCounter--;
			}
		}
	}
}





















