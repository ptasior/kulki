package com.games.kulki;
import java.util.Random;
import android.graphics.Color;
import android.util.Log;
import android.graphics.Point;
import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.*;

class Ball extends SceneItem
{
	private static final int MAXCOLORS = 7;
	private static final int EMPTY = 0;

	private int _value;
	KulkiActivity _act;
	int _x, _y;
	boolean _selected;
	private static Bitmap []_staticPic = new Bitmap[MAXCOLORS];
	private static boolean _staticInitialized = false;


	Ball(KulkiActivity act, int x, int y)
	{
		super();
		_act = act;
		_value = EMPTY;
		_x = x;
		_y = y;
		_selected = false;
	}

	public static void loadStatic(KulkiActivity act)
	{
		if(_staticInitialized) return;
		Log.d("qqq", "Loading images");
		

		/* _staticPic[0] = act.game.loadPicture(R.drawable.k1); */
		/* _staticPic[1] = act.game.loadPicture(R.drawable.k2);; */
		/* _staticPic[2] = act.game.loadPicture(R.drawable.k3); */
		/* _staticPic[3] = act.game.loadPicture(R.drawable.k4); */
		/* _staticPic[4] = act.game.loadPicture(R.drawable.k5); */
		/* _staticPic[5] = act.game.loadPicture(R.drawable.k6); */
		/* _staticPic[6] = act.game.loadPicture(R.drawable.k7); */


		_staticPic[0] = act.game.loadBitmap(R.drawable.k1);
		_staticPic[1] = act.game.loadBitmap(R.drawable.k2);;
		_staticPic[2] = act.game.loadBitmap(R.drawable.k3);
		_staticPic[3] = act.game.loadBitmap(R.drawable.k4);
		_staticPic[4] = act.game.loadBitmap(R.drawable.k5);
		_staticPic[5] = act.game.loadBitmap(R.drawable.k6);
		_staticPic[6] = act.game.loadBitmap(R.drawable.k7);

		_staticInitialized = true;
	}

	public boolean isEmpty()
	{
		return _value == EMPTY;
	}

	public void clean()
	{
		_value = EMPTY;
	}

	/* public int color() */
	/* { */
	/* 	switch(_value) */
	/* 	{ */
	/* 		case 1: return Color.RED; */
	/* 		case 2: return Color.GREEN; */
	/* 		case 3: return Color.BLUE; */
	/* 		case 4: return Color.YELLOW; */
	/* 		case 5: return Color.CYAN; */
	/* 		case 6: return Color.MAGENTA; */
	/* 		default: return Color.BLACK; */
	/* 	} */
	/* } */

	public void randColor()
	{
		Random generator = new Random();
		_value = 1+generator.nextInt(MAXCOLORS-1);
	}

	public boolean eq(Ball r)
	{
		return _value == r._value;
	}

	// Animations
	public void set(Ball r)
	{
		_value = r._value;
	}

	public void move(ArrayList<KulkiBoard.Direction> path)
	{
	}

	public void select()
	{
		_selected = true;
	}

	public void unselect()
	{
		_selected = false;
	}
	
	public void draw(Canvas canvas, int t)
	{
		Paint bg = new Paint();
		bg.setStyle(Paint.Style.STROKE);
		bg.setColor(Color.BLACK);

		Rect r = new Rect(  (int)(_pos.x),
							(int)(_pos.y),
							(int)(_pos.x+_size.x),
							(int)(_pos.y+_size.y));
		canvas.drawRect(r, bg);

		if(isEmpty()) return;

		Paint paint = new Paint();
		paint.setAntiAlias(false);
		paint.setFilterBitmap(true);
		paint.setDither(true);

		int off = _size.x/10;
		r = new Rect(   (int)(_pos.x+off),
						(int)(_pos.y+off),
						(int)(_pos.x+_size.x-off),
						(int)(_pos.y+_size.y-off));

		Rect rb = new Rect(0, 0,
				_staticPic[_value].getWidth(), _staticPic[_value].getHeight());
		canvas.drawBitmap(_staticPic[_value], rb, r, paint);

		if(!_selected) return;
		bg.setColor(Color.YELLOW);
		r = new Rect(   (int)(_pos.x+10),
						(int)(_pos.y+10),
						(int)(_pos.x+_size.x/2),
						(int)(_pos.y+_size.y/2));
		canvas.drawRect(r, bg);
	}

	public void changed()
	{
		_size = new Point(_act.game.ballSize(),   _act.game.ballSize());
		_pos = new Point(_act.game.ballSize()*_x, _act.game.ballSize()*_y);
	}
}

