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
	
	Bitmap ship, bullet, enemy, enemy2, pause, explosion, pauseBar, pauseText, restartText, exitText, rocketIndicator,
			laserIndicator, greenLaserIndicator, health, topPanel;
	
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
		bullet = 	BitmapFactory.decodeResource(getResources(), R.drawable.fire);	
		enemy =  	BitmapFactory.decodeResource(getResources(), R.drawable.asteroideasy);	
		enemy2 =  	BitmapFactory.decodeResource(getResources(), R.drawable.asteroid2);	
		pause =  	BitmapFactory.decodeResource(getResources(), R.drawable.pauseneweasy);	
		explosion =  BitmapFactory.decodeResource(getResources(), R.drawable.explosion);	
		rocketIndicator =  BitmapFactory.decodeResource(getResources(), R.drawable.rocketindicator);	
		laserIndicator =  BitmapFactory.decodeResource(getResources(), R.drawable.laserindicator);	
		health =  BitmapFactory.decodeResource(getResources(), R.drawable.health);	
		greenLaserIndicator =  BitmapFactory.decodeResource(getResources(), R.drawable.greenlaserindicator);	
		topPanel =  BitmapFactory.decodeResource(getResources(), R.drawable.toppanel3);	
			
		
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
		
		///////////////////////////////////////////////////////////////////////////������������� PAINT-� � ��������
		Paint paint = new Paint();
		
		Bitmap mShip = Bitmap.createScaledBitmap(ship,  GameManager.screenWidth/5, GameManager.screenHeight/6, false);
		Bitmap mBullet = Bitmap.createScaledBitmap(bullet, GameManager.screenWidth/48, GameManager.screenHeight/40, false);
		Bitmap mEnemy = Bitmap.createScaledBitmap(enemy, GameManager.screenWidth/9, GameManager.screenHeight/12, false);
		Bitmap mEnemy2 = Bitmap.createScaledBitmap(enemy2, GameManager.screenWidth/9, GameManager.screenHeight/12, false);
		Bitmap mPause = Bitmap.createScaledBitmap(pause, GameManager.screenWidth/6, GameManager.screenHeight/8, false);
		Bitmap mExplosion = Bitmap.createScaledBitmap(explosion, GameManager.screenWidth/9, GameManager.screenHeight/12, false);
		Bitmap mLaserIndicator = Bitmap.createScaledBitmap(laserIndicator, GameManager.screenWidth/9 + GameManager.screenWidth/48, GameManager.screenHeight/12 + GameManager.screenHeight/64, false);
		Bitmap mRocketInditor = Bitmap.createScaledBitmap( rocketIndicator, GameManager.screenWidth/9 + GameManager.screenWidth/48,	GameManager.screenHeight/12 + GameManager.screenHeight/62, false);
		Bitmap mGreenLaserIndicator = Bitmap.createScaledBitmap(greenLaserIndicator, GameManager.screenWidth/9 + GameManager.screenWidth/48, GameManager.screenHeight/12 + GameManager.screenHeight/64, false);
		Bitmap mTopPanel = Bitmap.createScaledBitmap(topPanel, GameManager.screenWidth - GameManager.screenWidth/6, GameManager.screenHeight/16, false);
		
		////////////////////////////////////////////////////////////////////////////��������� ����

		DrawBackground(c, paint);
		
		////////////////////////////////////////////////////////////////////////////�����
		if (GameManager.isPause) 
		{
			GameManager.isPause = false;
			Intent intent = new Intent();
	        intent.setClass(getContext(), PauseActivity.class);
	        ((Activity)getContext()).startActivity(intent);
		}
		////////////////////////////////////////////////////////////////////////////����
		else
		{
		paint.setAlpha(255);
			
		ShipPhysics(c, mShip, paint);	////��������� ������ � ��������� ������ �������
		
		DrawStarsAndEnemies(c, paint, mEnemy, mEnemy2); ////��������� � ��������� �������� ����������� � �����
		
		DrawTopPanel(c, paint, mPause, mTopPanel);//////////////��������� ������� ������������ ������
		
		DrawBottomPanel(c, paint, mBullet, mRocketInditor, mLaserIndicator, mGreenLaserIndicator); /////////��������� ������ ������������ ������
		
		
		ShotManager(c, paint, mBullet, mShip, mExplosion);  ///////////��������� ���������
		
		RefreshEnemyRects();
		
		RefreshBulletsRects();
		
		CollisionDetector(c, paint, mExplosion, mShip, mEnemy);
		
		DistanceCheckout();
		/*	
		if (GameManager.touchDown)
		{
			paint.setColor(Color.BLUE);
			c.drawCircle(GameManager.Xpos, GameManager.Ypos, GameManager.screenHeight/6, paint);
		}
		*/
			}	
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {/////////////////////////////////////////////////////////////////////ON TOUCH
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			
			 GameManager.touchDown = true; 
			MoveDown = event.getX();
			
			if ((event.getX() > (GameManager.screenWidth/6)*5) && (event.getY() < GameManager.screenWidth/6))
			{
				GameManager.isPause = !GameManager.isPause;
			}
			
			else GameManager.isFight = true;
			
			break;
		case MotionEvent.ACTION_UP:
			GameManager.isFight = false;
			GameManager.touchDown = false;
			
			if (event.getX() + 120 < MoveDown)
				GameManager.ChangeWeapon(2);
			
			if (event.getX() - 120 > MoveDown)
				GameManager.ChangeWeapon(1);
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
	
	public void DrawStarsAndEnemies(Canvas c, Paint paint, Bitmap mEnemy, Bitmap mEnemy2)///////////////////////////////////////////////////DRAW STARS AND ENEMIES
	{
		//////////////////////////////////////////////////////////////////������
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
		
		//////////////////////////////////////////////////////////////////����������
		for (int i = 0; i < GameManager.maxEnemies; i++)
		{
			if (GameManager.enemyMapY[i] > GameManager.screenHeight)
			{
				GameManager.enemyMapY[i] = 0;
				GameManager.enemyMapX[i] = random.nextInt(GameManager.screenWidth - mEnemy.getWidth());
			}
				c.drawBitmap(mEnemy2,GameManager.enemyMapX[i], GameManager.enemyMapY[i], paint);
			
			GameManager.enemyMapY[i]+=GameManager.enemySpeed;
		}
	}
	
	public void DrawBonus(Canvas c, Paint paint, Bitmap bonus)///////////////////////////////////////////////////DRAW BONUS
	{		
			if (GameManager.healthBonusMapY > GameManager.screenHeight)	GameManager.healthBonusMapX = random.nextInt(GameManager.screenWidth - bonus.getWidth());
			c.drawBitmap(bonus, GameManager.healthBonusMapX, GameManager.healthBonusMapY, paint);
			GameManager.healthBonusMapY += 10;
	}
	
	public void DrawBottomPanel(Canvas c,Paint paint,Bitmap mBullet,Bitmap mRocketIndicator,Bitmap mLaserIndicator,Bitmap mGreenLaserIndicator)/////DRAW BOTTOM PANEL
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
			c.drawBitmap(mLaserIndicator, GameManager.screenWidth/24 , GameManager.screenHeight - 50, paint);
		}
		if (GameManager.weapon == 2)
		{
			c.drawBitmap(mGreenLaserIndicator, GameManager.screenWidth/24 , GameManager.screenHeight - 50, paint);	
		}
		
		}
	
	public void DrawTopPanel(Canvas c, Paint paint, Bitmap mPause, Bitmap mTopPanel)//////////////////////////////////////////////////////////DRAW TOP PANEL
	{
		c.drawBitmap(mTopPanel, 0, 0, paint);
		
		paint.setColor(Color.rgb(45, 80, 0));
		
		c.drawText("Distanse: " + GameManager.distance, 2, 10, paint);
		GameManager.distance++;
		
		if ((GameManager.distance % 50 == 0) && (GameManager.bulletCounter < 24)) GameManager.bulletCounter += 2;
		
		
		paint.setColor(Color.RED);
		c.drawText("Score: " + GameManager.score, 2, 40, paint);
		
		paint.setColor(Color.RED);
		c.drawText("Last: " + GameManager.lastScore, 2, 55, paint);
		
		paint.setColor(Color.RED);
		c.drawText("Best: " + GameManager.bestScore, 2, 70, paint);
		
		paint.setColor(Color.RED);
		
		c.drawRect(2, 300 - 10*GameManager.health, 5, 300, paint);
		
		c.drawBitmap(mPause, (GameManager.screenWidth/6)*5, 0, paint);
	}
	
	public void DistanceCheckout()//////////////////////////////////////////////////////////////////////////////////////////DISTANCE CHECKOUT
	{
		if (GameManager.distance % 50 == 0) 
			{
				GameManager.score += 10;
				GameManager.bonusTime = true;
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
		if (GameManager.distance % 1000 == 0) 
		{
			GameManager.health++;
		}
		if (GameManager.distance % 500 == 0) 
		{
			GameManager.bonusTime = true;
		}

}
	
	public void DrawBackground(Canvas c, Paint paint)///////////////////////////////////////////////////////////////////////DRAW BACKGROUND
	{
		Bitmap space =  BitmapFactory.decodeResource(getResources(), R.drawable.stars);	
		Bitmap mSpace = Bitmap.createScaledBitmap(space, c.getWidth(),c.getHeight(), false);
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
	
	public void CollisionDetector(Canvas c, Paint paint, Bitmap mExplosion, Bitmap mShip, Bitmap mEnemy)///////////////////////////////////COLLISION DETECTOR
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
					DisposeEnemy(i, c, paint, mExplosion, mEnemy);
				}
			
			if (GameManager.weapon == 0)
				{
					if (GameManager.enemyRects[i].intersect(GameManager.bullet)) 
						{
							DisposeEnemy(i, c, paint, mExplosion, mEnemy);
							GameManager.YBulletPosition = -20;
						}
				}
			if (GameManager.weapon == 1)
				{
					if (GameManager.isFight)
						{
							if (GameManager.enemyRects[i].intersect(getLaserRect(mShip)))
								{
									DisposeEnemy(i, c, paint, mExplosion, mEnemy);
								}
						}
				}
		}
	}
	
	public void DisposeEnemy(int i, Canvas c, Paint paint, Bitmap mExplosion, Bitmap mEnemy)///////////////////////////////////////////////DISPOSE ENEMY
	{
		GameManager.score +=5;
		
		c.drawBitmap(mExplosion, GameManager.enemyMapX[i], GameManager.enemyMapY[i], paint);
		
		GameManager.enemyMapX[i] = random.nextInt(GameManager.screenWidth - mEnemy.getWidth());
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
	
	public void drawPointByCenter(int x, int y,Paint paint, Canvas c)///////////////////////////////////////////////////////DRAW POINT BY CENTER
	{
		c.drawCircle(x, y, 1, paint);
	}
}















