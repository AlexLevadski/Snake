package alex.levadski.snake;

import android.graphics.Rect;

public class GameManager 
{
	public static int Xpos = 120, Ypos = 135;
	public static boolean left = false, right = false, up = false, down = false;
	
	public static boolean isFight = false, isBulletOnMap = false, isPause = false;
	
	public static int XMapSize = 12;
	public static int YMapSize = 16;
	
	public static int YBulletPosition = 0;
	public static int XBulletPosition = 0;
	

	public static int bulletCounter = 10;
	
	public static int distance = 0, maxStars = 10, maxEnemies = 5, starSpeed = 15, enemySpeed = 5, enemyMinSpeed = 5;
	
	public static int[] starMapX = new int[maxStars];
	public static int[] starMapY = new int[maxStars];
	
	public static int[] enemyMapX = new int[maxEnemies];
	public static int[] enemyMapY = new int[maxEnemies];
	
	public static int BulletIndicator = 0;
	
	public static final int maxHealth = 10;
	
	public static int screenWidth = 0, screenHeight = 0, health = maxHealth, score = 0, lastScore = 0, bestScore = 0;
	
	
	public static Rect[] enemyRects = new Rect[maxEnemies];
	
	public static Rect bullet;
	
	public static void ResetAchievements()
	{
		enemySpeed = enemyMinSpeed;
		health = 10;
		distance = 0;
		lastScore = score;
		if (score > bestScore) bestScore = score;
		score = 0;
	}
	
}























