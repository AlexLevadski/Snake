package alex.levadski.snake;
import android.graphics.Rect;

public class GameManager 
{
	public static int Xpos = 120, Ypos = 135;
	public static boolean left = false, right = false, up = false, down = false, touchDown = false;
	
	public static boolean isFight = false, isBulletOnMap = false, isPause = false, isHealthBonus = false, isShield = false;
	
	public static int XMapSize = 12;
	public static int YMapSize = 16;
	
	public static int YBulletPosition = 0;
	public static int XBulletPosition = 0;
	

	public static int bulletCounter = 10, laserCounter = 10, touchDownCounter = 0;
	
	public static int distance = 0, maxStars = 30, maxEnemies = 7, starSpeed = 15, enemySpeed = 5, enemyMinSpeed = 3;
	
	public static int[] starMapX = new int[maxStars];
	public static int[] starMapY = new int[maxStars];
	
	public static int[] enemyMapX = new int[maxEnemies];
	public static int[] enemyMapY = new int[maxEnemies];
	
	
	public static int BulletIndicator = 0;
	
	public static final int maxHealth = 10;
	
	public static int screenWidth = 0, screenHeight = 0, health = maxHealth, score = 0, lastScore = 0, bestScore = 0, weapon = 0, maxWeapon = 1;
	
	public static float SwipeDistance = screenWidth/2;
	
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
	
	public static void ChangeWeapon(int direction)
	{
		if (direction == 1)
			{
				if (weapon < maxWeapon) 
					weapon++;
				else
					weapon = 0;
			}	
		else if (direction == 2)
			if (weapon > 0) weapon--;
			else weapon = maxWeapon;
	
	}
	///////////////////////////////////////////////////////////////////////////////////������
	
	public static boolean bonusTime = false;
	
	public static int healthBonusMapX = 0;
	public static int healthBonusMapY = 0;
	
	///////////////////////////////////////////////////////////////////////////////////��������� ��������
	public static int[] parabolicX = new int[11];
}























