package com.games.kulki;
import java.util.Random;
import android.graphics.Color;
import android.util.Log;
import android.graphics.Point;
import java.util.ArrayList;

class Ball
{
	private static final int MAXCOLORS = 7;
	private static final int EMPTY = 0;

	private int _value;

	Ball()
	{
		_value = EMPTY;
	}

	public boolean empty()
	{
		return _value == EMPTY;
	}

	public void clean()
	{
		_value = EMPTY;
	}

	public int color()
	{
		switch(_value)
		{
			case 1: return Color.RED;
			case 2: return Color.GREEN;
			case 3: return Color.BLUE;
			case 4: return Color.YELLOW;
			case 5: return Color.CYAN;
			case 6: return Color.MAGENTA;
			default: return Color.BLACK;
		}
	}

	public void randColor()
	{
		Random generator = new Random();
		_value = 1+generator.nextInt(MAXCOLORS-1);
	}

	public void set(Ball r)
	{
		_value = r._value;
	}

	public boolean eq(Ball r)
	{
		return _value == r._value;
	}
}

public class KulkiGame
{
	public static final int WIDTH = 9;
	public static final int HEIGHT = 9;
	private static final int INITBALLS = 3;
	private static final int MININLINE = 5;

	public enum Direction {LEFT, RIGTH, UP, DOWN};

	private Ball [][] _board = new Ball[WIDTH][HEIGHT];
	private Point _selected = new Point();
	private int _amount;
	private int _points;

	KulkiGame()
	{
		for(int i = 0; i < WIDTH; i++)
			for(int j = 0; j < WIDTH; j++)
				_board[i][j] = new Ball();

		restart();
		Log.d("qqq", "Game started");
	}

	public void restart()
	{
		for(int i = 0; i < WIDTH; i++)
			for(int j = 0; j < HEIGHT; j++)
				value(i,j).clean();

		_amount = 0;
		_points = 0;
		shuffleNew();
		_selected.set(-1, -1);
	}

	void shuffleNew()
	{
		Random generator = new Random();
		for(int i = 0; i < Math.min(INITBALLS, WIDTH*HEIGHT-_amount); i++)
		{
			int x = generator.nextInt(WIDTH);
			int y = generator.nextInt(HEIGHT);
			if(!value(x,y).empty()) {i--; continue;}
			value(x,y).randColor();
			_amount++;
			calculatePoints(x,y);
		}
	}

	private ArrayList<Direction> genPath(int [][]tab, Point from, int start)
	{
		ArrayList<Direction> path = new ArrayList<Direction>();
		int x = from.x, y = from.y;
		for(int no = start; no >= 1; no--)
		{
			if(x-1>=0 && tab[x-1][y] == no)     {path.add(Direction.LEFT); x--; continue;}
			if(x+1<WIDTH && tab[x+1][y] == no)  {path.add(Direction.RIGTH); x++; continue;}
			if(y-1>=0 && tab[x][y-1] == no)     {path.add(Direction.UP); y--; continue;}
			if(y+1<HEIGHT && tab[x][y+1] == no) {path.add(Direction.DOWN); y++; continue;}
		}
		return path;
	}

	private ArrayList<Direction> canMove(Point from, Point to)
	{
		int [][] tmp = new int[WIDTH][HEIGHT];

		for(int i = 0; i < WIDTH; i++)
			for(int j = 0; j < HEIGHT; j++)
				tmp[i][j] = value(i,j).empty()?0:-1;

		tmp[to.x][to.y] = 1;

		for(int p = 1; p < WIDTH*HEIGHT; p++)
		{
			boolean found = false;
			for(int i = 0; i < WIDTH; i++)
				for(int j = 0; j < HEIGHT; j++)
				{
					if(tmp[i][j] != p) continue;

					if(i-1>=0)
						if(i-1 == from.x && j == from.y) return genPath(tmp, from, p);
						else if(tmp[i-1][j] == 0) {tmp[i-1][j]=p+1; found=true;}
					if(i+1<WIDTH)
						if(i+1 == from.x && j == from.y) return genPath(tmp, from, p);
						else if(tmp[i+1][j] == 0) {tmp[i+1][j]=p+1; found=true;}
					if(j-1>=0)
						if(i == from.x && j-1 == from.y) return genPath(tmp, from, p);
						else if(tmp[i][j-1] == 0) {tmp[i][j-1]=p+1; found=true;}
					if(j+1<HEIGHT)
						if(i == from.x && j+1 == from.y) return genPath(tmp, from, p);
						else if(tmp[i][j+1] == 0) {tmp[i][j+1]=p+1; found=true;}
				}
			if(!found) break;
		}

		/* for(int i = 0; i < WIDTH; i++) */
		/* 	Log.d("qqq", tmp[0][i]+", "+tmp[1][i]+", "+tmp[2][i]+", "+tmp[3][i]+", "+ */
		/* 		tmp[4][i]+", "+tmp[5][i]+", "+tmp[6][i]+", "+tmp[7][i]+", "+ tmp[8][i]); */

		return null;
	}

	private void move(Point from, Point to, ArrayList<Direction> path)
	{
		/* value(to.x, to.y).set(value(from.x, from.y)); */
		/* value(from.x, from.y).clean(); */
		int x=0, y=0, nx = from.x, ny = from.y;
		for(Direction s : path)
		{
			switch(s)
			{
				case LEFT: x = nx; y = ny; nx--; break;
				case RIGTH: x = nx; y = ny; nx++; break;
				case UP: x = nx; y = ny; ny--; break;
				case DOWN: x = nx; y = ny; ny++; break;
			}
			value(nx, ny).set(value(x,y));
			value(x,y).clean();

			try{Thread.sleep(300);}
			catch(InterruptedException e){}
		}
	}

	private int calculatePoints(int x, int y)
	{
		/* Log.d("qqq", "CalcualtePoints x:"+x+" y:"+y); */
		Ball checked = new Ball();
		checked.set(value(x,y));
		int v = 1;
		for(int i = y+1; i < WIDTH; i++)
			if(!value(x,i).eq(checked)) break;
			else v++;
		/* Log.d("qqq", "is ok v:"+v); */
		for(int i = y-1; i >= 0; i--)
			if(!value(x,i).eq(checked)) break;
			else v++;

		/* Log.d("qqq", "is ok v:"+v); */
		if(v >= MININLINE)
		{
			for(int i = y+1; i < WIDTH; i++)
				if(!value(x,i).eq(checked)) break;
				else value(x,i).clean();
			for(int i = y-1; i >= 0; i--)
				if(!value(x,i).eq(checked)) break;
				else value(x,i).clean();
			value(x,y).clean();
		}
		else v = 0;

		int h = 1;
		for(int i = x+1; i < HEIGHT; i++)
			if(!value(i,y).eq(checked)) break;
			else h++;
		/* Log.d("qqq", "is ok h:"+h); */
		for(int i = x-1; i >= 0; i--)
			if(!value(i,y).eq(checked)) break;
			else h++;

		/* Log.d("qqq", "is ok h:"+h); */
		if(h >= MININLINE)
		{
			for(int i = x+1; i < HEIGHT; i++)
				if(!value(i,y).eq(checked)) break;
				else value(i,y).clean();
			for(int i = x-1; i >= 0; i--)
				if(!value(i,y).eq(checked)) break;
				else value(i,y).clean();
			value(x,y).clean();
		}
		else h = 0;

		int r = 1;
		for(int i=x+1, j=y+1; i<WIDTH && j<HEIGHT; i++, j++)
			if(!value(i,j).eq(checked)) break;
			else r++;
		/* Log.d("qqq", "is ok r:"+r); */
		for(int i=x-1, j=y-1; i>=0 && j>=0; i--, j--)
			if(!value(i,j).eq(checked)) break;
			else r++;

		/* Log.d("qqq", "is ok r:"+r); */
		if(r >= MININLINE)
		{
			for(int i=x+1, j=y+1; i<WIDTH && j<HEIGHT; i++, j++)
				if(!value(i,j).eq(checked)) break;
				else value(i,j).clean();
			for(int i=x-1, j=y-1; i>=0 && j>=0; i--, j--)
				if(!value(i,j).eq(checked)) break;
				else value(i,j).clean();
			value(x,y).clean();
		}
		else r = 0;

		int l = 1;
		for(int i=x-1, j=y+1; i>=0 && j<HEIGHT; i--, j++)
			if(!value(i,j).eq(checked)) break;
			else l++;
		/* Log.d("qqq", "is ok l:"+l); */
		for(int i=x+1, j=y-1; i<WIDTH && j>=0; i++, j--)
			if(!value(i,j).eq(checked)) break;
			else l++;

		/* Log.d("qqq", "is ok l:"+l); */
		if(l >= MININLINE)
		{
			for(int i=x-1, j=y+1; i>=0 && j<HEIGHT; i--, j++)
				if(!value(i,j).eq(checked)) break;
				else value(i,j).clean();
			for(int i=x+1, j=y-1; i<WIDTH && j>=0; i++, j--)
				if(!value(i,j).eq(checked)) break;
				else value(i,j).clean();
			value(x,y).clean();
		}
		else l = 0;

		_amount -= h+v+r+l;
		_points += h+v+r+l;
		return h+v+r+l;
	}

	public Ball value(int x, int y)
	{
		assert(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT);
		return _board[x][y];
	}

	public void click(int x, int y)
	{
		if(!value(x,y).empty())
		{
			/* if(x == _selected.x && y == _selected.y) _selected.set(-1, -1); */
			if(_selected == new Point(x,y)) _selected.set(-1, -1);
			else                            _selected.set(x,y);
			return;
		}

		if(_selected.x == -1) return; // Nothing selected

		ArrayList<Direction> path = canMove(_selected, new Point(x,y));
		if(path == null) return;
		
		move(_selected, new Point(x,y), path);

		if(calculatePoints(x,y) == 0)
			shuffleNew();

		_selected.set(-1, -1);
	}

	public Point selected()
	{
		return _selected;
	}

	public int points()
	{
		return _points;
	}
}

