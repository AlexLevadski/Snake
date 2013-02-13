package alex.levadski.snake;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameSurface extends SurfaceView implements OnTouchListener
{
	Random random = new Random();
	
	Bitmap ship, bullet, enemy, pause, pauseRed, explosion, pauseBar, pauseText, restartText, exitText, rocketIndicator, laserIndicator,
			health;
	
	float x, y, MoveDown;
	
	public void setXY(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public GameSurface(Context context) 
	{
		super(context);
		ship = 		BitmapFactory.decodeResource(getResources(), R.drawable.spaceship);	
		bullet = 	BitmapFactory.decodeResource(getResources(), R.drawable.bullet);	
		enemy =  	BitmapFactory.decodeResource(getResources(), R.drawable.asteroid);	
		pause =  	BitmapFactory.decodeResource(getResources(), R.drawable.pause);	
		pauseRed =  BitmapFactory.decodeResource(getResources(), R.drawable.pause2);	
		explosion =  BitmapFactory.decodeResource(getResources(), R.drawable.explosion);	
		rocketIndicator =  BitmapFactory.decodeResource(getResources(), R.drawable.rocketindicator);	
		laserIndicator =  BitmapFactory.decodeResource(getResources(), R.drawable.laserindicator);	
		health =  BitmapFactory.decodeResource(getResources(), R.drawable.health);	
			
		
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
		
		///////////////////////////////////////////////////////////////////////////»Õ»÷»¿À»«¿÷»ﬂ PAINT-¿ » ¡»“Ã¿œŒ¬
		Paint paint = new Paint();
		
		Bitmap mShip = Bitmap.createScaledBitmap(ship,  GameManager.screenWidth/5, GameManager.screenHeight/6, true);
		Bitmap mBullet = Bitmap.createScaledBitmap(bullet, GameManager.screenWidth/48, GameManager.screenHeight/40, true);
		Bitmap mEnemy = Bitmap.createScaledBitmap(enemy, GameManager.screenWidth/9, GameManager.screenHeight/12, true);
		Bitmap mPause = Bitmap.createScaledBitmap(pause, GameManager.screenWidth/6, GameManager.screenHeight/8, true);
		Bitmap mExplosion = Bitmap.createScaledBitmap(explosion, GameManager.screenWidth/9, GameManager.screenHeight/12, true);
		Bitmap mLaserIndicator = Bitmap.createScaledBitmap(laserIndicator, GameManager.screenWidth/9 + GameManager.screenWidth/48, GameManager.screenHeight/12 + GameManager.screenHeight/64, true);
		Bitmap mRocketInditor = Bitmap.createScaledBitmap( rocketIndicator, GameManager.screenWidth/9 + GameManager.screenWidth/48,	GameManager.screenHeight/12 + GameManager.screenHeight/62, true);
		Bitmap mHealth = Bitmap.createScaledBitmap(health, GameManager.screenWidth/9, GameManager.screenHeight/12, true);
		
		////////////////////////////////////////////////////////////////////////////Œ“–»—Œ¬ ¿ ‘ŒÕ¿

		DrawBackground(c, paint);
		
		////////////////////////////////////////////////////////////////////////////œ¿”«¿
		if (GameManager.isPause) 
		{
			GameManager.isPause = false;
			Intent intent = new Intent();
	        intent.setClass(getContext(), PauseActivity.class);
	        ((Activity)getContext()).startActivity(intent);
		}
		////////////////////////////////////////////////////////////////////////////»√–¿
		else
		{
		paint.setAlpha(255);
			
		ShipPhysics(c, mShip, paint);	////Œ“–¿¡Œ“ ¿ ‘»«» » » Œ“–»—Œ¬ ¿ Õ¿ÿ≈√Œ  Œ–¿¡Àﬂ
		
		DrawStarsAndEnemies(c, paint, mEnemy, mHealth); ////√≈Õ≈–¿÷»ﬂ » Œ“–»—Œ¬ ¿  Œ–¿¡À≈… œ–Œ“»¬Õ» Œ¬ » «¬≈«ƒ
		
		DrawBottomPanel(c, paint, mBullet, mRocketInditor, mLaserIndicator); /////////Œ“–»—Œ¬ ¿ Õ»∆Õ≈… »Õƒ» ¿“Œ–ÕŒ… œ¿Õ≈À»
		
		ShotManager(c, paint, mBullet, mShip, mExplosion);  ///////////Œ“–¿¡Œ“ ¿ ¬€—“–≈ÀŒ¬
		
		DrawTopPanel(c, paint, mPause);//////////////Œ“–»—Œ¬ ¿ ¬≈–’Õ≈… »Õƒ» ¿“Œ–ÕŒ… œ¿Õ≈À»
		
		RefreshEnemyRects();
		
		RefreshBulletsRects();
		
		if (GameManager.isHealthBonus) DrawHealthBonus(c, paint, mHealth);
		
		CollisionDetector(c, paint, mExplosion, mShip);
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {/////////////////////////////////////////////////////////////////////ON TOUCH
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			
			MoveDown = event.getX();
			
			if ((event.getX() > (GameManager.screenWidth/6)*5) && (event.getY() < GameManager.screenWidth/6))
			{
				GameManager.isPause = !GameManager.isPause;
			}
			
			else GameManager.isFight = true;
			
			break;
		case MotionEvent.ACTION_UP:
			GameManager.isFight = false;
			
			if (event.getX() + GameManager.screenWidth/2 < MoveDown)
				GameManager.ChangeWeapon(1);
			
			if (event.getX() - GameManager.screenWidth/2 > MoveDown)
				GameManager.ChangeWeapon(-1);
			break;
		}
		return true;
	}
	
	public void ShipPhysics(Canvas c, Bitmap mShip, Paint paint)////////////////////////////////////////////////////////////SHIP PHYSSICS
	{
		if (GameManager.Xpos <= GameManager.screenWidth/24) GameManager.Xpos = GameManager.screenWidth/24;
		if (GameManager.Xpos >= (GameManager.screenWidth - GameManager.screenWidth/4)) GameManager.Xpos = GameManager.screenWidth - GameManager.screenWidth/4;
		if (GameManager.Ypos <= GameManager.screenHeight/32) GameManager.Ypos = GameManager.screenHeight/32;
		if (GameManager.Ypos >= (GameManager.screenHeight - GameManager.screenHeight/4)) GameManager.Ypos = GameManager.screenHeight - GameManager.screenHeight/4;
		
		if (GameManager.left)  GameManager.Xpos -= GameActivity.GyroX*4;
		if (GameManager.right) GameManager.Xpos -= GameActivity.GyroX*4;
		if (GameManager.up)    GameManager.Ypos += GameActivity.GyroY*2;
		if (GameManager.down)  GameManager.Ypos += GameActivity.GyroY*3;
		
		c.drawBitmap(mShip, GameManager.Xpos,  GameManager.Ypos, paint);
	}
	
	public void DrawStarsAndEnemies(Canvas c, Paint paint, Bitmap mEnemy, Bitmap mHealth)///////////////////////////////////////////////////DRAW STARS AND ENEMIES
	{
		//////////////////////////////////////////////////////////////////«¬≈«ƒ€
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
		
		//////////////////////////////////////////////////////////////////œ–Œ“»¬Õ» »
		for (int i = 0; i < GameManager.maxEnemies; i++)
		{
			if (GameManager.enemyMapY[i] > GameManager.screenHeight)
			{
				GameManager.enemyMapY[i] = 0;
				GameManager.enemyMapX[i] = random.nextInt(GameManager.screenWidth - enemy.getWidth());
			}
			c.drawBitmap(mEnemy,GameManager.enemyMapX[i], GameManager.enemyMapY[i], paint);
			GameManager.enemyMapY[i]+=GameManager.enemySpeed;
		}
		
		//////////////////////////////////////////////////////////////////¡ŒÕ”—€
				
		if (GameManager.isHealthBonus)
		{
			c.drawBitmap(mHealth, GameManager.healthBonusMapX, GameManager.healthBonusMapY, paint);
			GameManager.healthBonusMapY += GameManager.screenHeight/64;
		}
		
		if (GameManager.healthBonusMapY > GameManager.screenHeight) GameManager.isHealthBonus = false;
		
	}
	
	public void DrawBottomPanel(Canvas c, Paint paint, Bitmap mBullet, Bitmap mRocketIndicator, Bitmap mLaserIndicator)/////DRAW BOTTOM PANEL
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
		c.drawRect(GameManager.screenWidth/24, GameManager.screenHeight - GameManager.screenHeight/21 - 3, GameManager.BulletIndicator, 
					   GameManager.screenHeight - GameManager.screenHeight/21 - 1, paint);
		
		paint.setColor(Color.RED);
		
		c.drawRect(2, GameManager.screenHeight - GameManager.screenHeight/16 + GameManager.screenHeight/16*GameManager.health, 5, 
				GameManager.screenHeight - GameManager.screenHeight/16, paint);
		
		if (GameManager.weapon == 0)
		{
			c.drawBitmap(mRocketIndicator, GameManager.screenWidth/24 , GameManager.screenHeight - 50, paint);
		}
		else if (GameManager.weapon == 1)
		{
			c.drawBitmap(mLaserIndicator, GameManager.screenWidth/24 , GameManager.screenHeight - 50, paint);}
		}
	
	public void DrawTopPanel(Canvas c, Paint paint, Bitmap mPause)//////////////////////////////////////////////////////////DRAW TOP PANEL
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
		
		if (GameManager.distance % 500 == 0)
		{
			GameManager.healthBonusMapX = random.nextInt(GameManager.screenWidth);
			GameManager.isHealthBonus = true;
		}
			
	}
	
	public void DrawBackground(Canvas c, Paint paint)///////////////////////////////////////////////////////////////////////DRAW BACKGROUND
	{
		Bitmap space =  BitmapFactory.decodeResource(getResources(), R.drawable.space);	
		Bitmap mSpace = Bitmap.createScaledBitmap(space, c.getWidth(),c.getHeight(), true);
		c.drawBitmap(mSpace, 0, 0, paint);
	}
	
	public Rect getShipRect(Bitmap mShip)///////////////////////////////////////////////////////////////////////////////////GET SHIP RECT
	{
		return new Rect(GameManager.Xpos, GameManager.Ypos, GameManager.Xpos + mShip.getWidth(), GameManager.Ypos + mShip.getHeight());
	}
	
	public void RefreshEnemyRects()/////////////////////////////////////////////////////////////////////////////////////////REFRESH ENEMY RECTS
	{
		for (int i = 0; i < GameManager.maxEnemies;i++)
			{
				GameManager.enemyRects[i] = new Rect(GameManager.enemyMapX[i], GameManager.enemyMapY[i],
					GameManager.enemyMapX[i] + GameManager.screenWidth/9, GameManager.enemyMapY[i] + GameManager.screenHeight/12);
			}
	}
	
	public void RefreshBulletsRects()///////////////////////////////////////////////////////////////////////////////////////REFRESH BULLETS RECTS
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
	
	public void CollisionDetector(Canvas c, Paint paint, Bitmap mExplosion, Bitmap mShip)///////////////////////////////////COLLISION DETECTOR
	{
		for (int i = 0; i < GameManager.maxEnemies;i++) 
		{
			if (GameManager.enemyRects[i].intersect(getShipRect(mShip)))
				{
					//GameActivity.vibrator.vibrate(50);
				
					if (GameManager.health > 1) 
						{
							GameManager.health--;
						}
					else 
						{
					 		Intent intent = new Intent();
					 		intent.setClass(getContext(), GameOverActivity.class);
					 		((Activity)getContext()).startActivity(intent);
						}
					DisposeEnemy(i, c, paint, mExplosion);
				}
			
			if (GameManager.weapon == 0)
				{
					if (GameManager.enemyRects[i].intersect(GameManager.bullet)) 
						{
							DisposeEnemy(i, c, paint, mExplosion);
							GameManager.YBulletPosition = -20;
						}
				}
			if (GameManager.weapon == 1)
				{
					if (GameManager.isFight)
						{
							if (GameManager.enemyRects[i].intersect(getLaserRect(mShip)))
								{
									DisposeEnemy(i, c, paint, mExplosion);
								}
						}
				}
		}
	}
	
	public void DisposeEnemy(int i, Canvas c, Paint paint, Bitmap mExplosion)///////////////////////////////////////////////DISPOSE ENEMY
	{
		GameManager.score +=5;
		
		c.drawBitmap(mExplosion, GameManager.enemyMapX[i], GameManager.enemyMapY[i], paint);
		
		GameManager.enemyMapX[i] = random.nextInt(GameManager.screenWidth);
		GameManager.enemyMapY[i] = 0;	
	}
	
	public void ShotManager(Canvas c, Paint paint, Bitmap mBullet, Bitmap mShip, Bitmap mExplosion)/////////////////////////SHOT MANAGER
	{
		if (GameManager.weapon == 0)
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
		
		else if (GameManager.weapon == 1)
			{
				if (GameManager.isFight)
					{
						paint.setColor(Color.BLUE);
						c.drawLine(GameManager.Xpos + getShipRect(mShip).width()/2, GameManager.Ypos, GameManager.Xpos + getShipRect(mShip).width()/2, 0, paint);
					}
			}
	}
	
	public Rect getLaserRect(Bitmap mShip)//////////////////////////////////////////////////////////////////////////////////GET LASER RECT
	{
		return new Rect(GameManager.Xpos + getShipRect(mShip).width()/2 -1,0,GameManager.Xpos + getShipRect(mShip).width()/2 + 1, GameManager.Ypos);
	}

	public void DrawHealthBonus(Canvas c, Paint paint, Bitmap mHealth)
	{
		c.drawBitmap(mHealth, GameManager.healthBonusMapX, GameManager.healthBonusMapY, paint);
		GameManager.healthBonusMapY+=10;
		
		if (GameManager.healthBonusMapY > GameManager.screenHeight) GameManager.isHealthBonus = false;
	}
}