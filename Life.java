/******************************************************************************
 *  Compilation:  javac *.java
 *  Execution:    java Life size iterations setting
 *  Dependencies: Picture.java
 *
 *  CS 3201
 *  Assignment 1
 *  Jeremy Eustace
 *  201049855
 *  
 *
 ******************************************************************************/

import java.awt.Color;

public class Life
{
	private static int boardSize; // The amount of pixels across are in each square
	
	private static int x,y, m;            // x-by-y grid of cells
    private int magnification;  // pixel-width of each cell
    private int[][] cells;      // cells to be randomly coloured
    private Picture pic;        // picture to be drawn on screen
    
    private static boolean[][] tempWorld; // False for a dead area, and True for a living cell
    private static boolean[][] world; // False for a dead area, and True for a living cell
    
    //constants
    private static float frameTime = 0.02f; // the time of each frame in seconds

    public Life(int x, int y, int magnification)
    {
        this.x = x;
        this.y = y;
        this.magnification = magnification;
        cells = new int[x][y];
        pic = new Picture(x * magnification, y * magnification);
    }
   
    // display (or update) the picture on screen
    public void show()
    {
        pic.show();     // without calling this the pic will not show
    }
    
    // Make a random assortment of pixels living
    public static void randomizeBoard(Life life)
    {
    	for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
            	life.randomizeCell(i,j);
            }
        }
    	life.show();
    }
    
    // randomize if this cell is alive or dead
    private void randomizeCell(int i, int j)
    {
    	float r = (float) Math.random() * 100;
    	int value = ((int) r) % 4;
        Color col;
        
        if (value == 0) 
        {
        	world[i][j] = true;
        	birthCell(i, j);
        	
        }
        else
        {
        	world[i][j] = false;
        	killCell(i, j);
        }
    }
    
    // Make a random assortment of pixels living
    public static void gosperGlider(Life life, int centerX, int centerY)
    {
    	for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
            	life.killCell(i,j);
            }
        }
    	
    	int[] positionsX = new int[] { 26, 24, 26, 14, 15, 22, 23, 36, 37, 13, 17, 22, 23, 36, 37, 2, 3, 12, 18, 22, 23, 2, 3, 12, 16, 18, 19, 24, 26, 12, 18, 26, 13, 17, 14, 15 };
    	int[] positionsY = new int[] {2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 9, 9, 10, 10};
    	
    	for (int i=0; i< positionsX.length;i++)
    	{
    		life.birthCell(centerX + positionsX[i], centerY + positionsY[i]);
    	}
    	
    	
    	life.show();
    }
    
    // Stamping neighborhood
    public static void continueLife(Life life)
    {
    	for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++)
            {
            	life.stampNeighborhood(i,j);
            }
        }
    	
    	exchangeWorlds();
    
    	life.show();
    }
    
    private static void exchangeWorlds()
    {
    	for (int i = 0; i < world.length; i++)
        {
            for (int j = 0; j < world.length; j++)
            {
            	world[i][j] = tempWorld[i][j];
            }
        }
    }
    
    private void stampNeighborhood(int thisX, int thisY)
    {
    	//if (thisX == (boardSize - 1) || thisX == 0 || thisY == 0 || thisY == (boardSize - 1)) 
    	//	return;
    	//System.out.println("Stamp for " + thisX + ", " + thisY);
    	
    	int count = 0;
    	for (int i=-1; i<2; i++)
    	{
    		for (int j=-1;j<2;j++)
    		{
    			if (i==0 && j==0)
    				continue;
    			
    			int checkX = thisX + i;
    			int checkY = thisY + j;
    			
    			
    			if (checkX >= boardSize)
    				checkX = 0;
    			else if (checkX < 0)
    				checkX = boardSize - 1;
    			if (checkY >= boardSize)
    				checkY = 0;
    			else if (checkY < 0)
    				checkY = boardSize - 1;
    			
    			
    			if (checkCell(checkX, checkY))
    			{
    				count++;
    			}
    			
    			//System.out.println("Count: " + count + ", ij is " + i + "," + j);
    		}
    	}
    	boolean alive = checkCell(thisX, thisY);
    	if (count != 2 && count != 3)
    		killCell(thisX, thisY);
    	else if (!alive && count == 3)
    		birthCell(thisX, thisY);
    	
    	//FOR TESTING
    	if (count > 8 || count < 0)
    	{
    		System.out.println("abnormal count " + count);
    	}
    	//System.out.println("Checking " + thisX + "," + thisY + ". Alive?" + alive + ", count=" + count);
    		
    }
    
    private boolean checkCell(int i, int j)
    {
    	if (world[i][j])
    		return true;
    	else
    		return false;
    }
    
    // kill the cell here
    private void killCell(int i, int j)
    {
    	tempWorld[i][j] = false;
    	Color col = new Color(255,255,255);
        
        for (int offsetX = 0; offsetX < magnification; offsetX++)
        {
            for (int offsetY = 0; offsetY < magnification; offsetY++)
            {
                // set() colours an individual pixel
                pic.set((i*magnification)+offsetX,
                        (j*magnification)+offsetY, col);
            }
        }
    }
    
    // birth a cell here
    private void birthCell(int i, int j)
    {
    	tempWorld[i][j] = true;
    	Color col = new Color(100,100,100);
        
        for (int offsetX = 0; offsetX < magnification; offsetX++)
        {
            for (int offsetY = 0; offsetY < magnification; offsetY++)
            {
                // set() colours an individual pixel
                pic.set((i*magnification)+offsetX,
                        (j*magnification)+offsetY, col);
            }
        }
    }
    
    // must provide grid size (x & y) and level of magnification (m)
    public static void main(String[] args) throws InterruptedException
    {
    	boardSize = Integer.parseInt(args[0]);
		int iterations = Integer.parseInt(args[1]);
		String setting = args[2];
    	
    	x = boardSize;
        y = boardSize;
        m = 5;
        
        Life life = new Life(x,y,m);
        world = new boolean[boardSize][boardSize];
        tempWorld = new boolean[boardSize][boardSize];
        
        if (setting.equals("R"))
        {
        	randomizeBoard(life); // Randomize each cell in the board to be alive or dead first
        }
        else {
        	int center = boardSize / 2;
        	//gosperGlider(life, center, center);
        	gosperGlider(life, 0, 0);
        }
        
        
        exchangeWorlds();
        
        for (int i=0; i<iterations;i++)
    	{
    		continueLife(life);
    		Thread.sleep(200);
    	}
    }
}
