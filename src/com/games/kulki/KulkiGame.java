package com.games.kulki;
import java.util.Random;
import android.graphics.Color;
import android.util.Log;
import android.graphics.Point;

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

	private Ball [][] _board = new Ball[WIDTH][HEIGHT];
	private Point _selected = new Point();
	private int _amount;

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

	private boolean canMove(Point from, Point to)
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
						if(i-1 == from.x && j == from.y) return true;
						else if(tmp[i-1][j] == 0) {tmp[i-1][j]=p+1; found=true;}
					if(i+1<WIDTH)
						if(i+1 == from.x && j == from.y) return true;
						else if(tmp[i+1][j] == 0) {tmp[i+1][j]=p+1; found=true;}
					if(j-1>=0)
						if(i == from.x && j-1 == from.y) return true;
						else if(tmp[i][j-1] == 0) {tmp[i][j-1]=p+1; found=true;}
					if(j+1<HEIGHT)
						if(i == from.x && j+1 == from.y) return true;
						else if(tmp[i][j+1] == 0) {tmp[i][j+1]=p+1; found=true;}
				}
			if(!found) break;
		}

		/* for(int i = 0; i < WIDTH; i++) */
		/* 	Log.d("qqq", tmp[0][i]+", "+tmp[1][i]+", "+tmp[2][i]+", "+tmp[3][i]+", "+ */
		/* 		tmp[4][i]+", "+tmp[5][i]+", "+tmp[6][i]+", "+tmp[7][i]+", "+ tmp[8][i]); */

		return false;
	}

	private void move(Point from, Point to)
	{
		value(to.x, to.y).set(value(from.x, from.y));
		value(from.x, from.y).clean();
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
			_selected.set(x,y);
			return;
		}

		if(_selected.x == -1) return; // Nothing selected

		if(!canMove(_selected, new Point(x,y))) return;
		
		move(_selected, new Point(x,y));

		if(calculatePoints(x,y) == 0)
			shuffleNew();

		_selected.set(-1, -1);
	}

	public Point selected()
	{
		return _selected;
	}
}

