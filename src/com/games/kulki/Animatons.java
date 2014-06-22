package com.games.kulki;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.graphics.*;
import android.util.Log;
import java.util.Random;
import java.util.ArrayList;
import android.os.SystemClock;

abstract class SceneItem
{
	public Point _pos;
	public Point _size;

	public abstract void draw(Canvas canvas, int t);

	SceneItem()
	{
		_pos=new Point(0,0);
		_size=new Point(10,10);
	}

	SceneItem(Point pos, Point size)
	{
		_pos = pos;
		_size = size;
	}

	public boolean clicked(int x, int y)
	{
		return false;
	}

	public void changed()
	{
	}

	public void pos(Point p)
	{
		_pos = p;
	}

	public void size(Point p)
	{
		_size = p;
	}
}

class StaticPicture extends SceneItem
{
	public Picture _picture;

	public StaticPicture(Picture p)
	{
		_picture = p;
	}

	public void draw(Canvas canvas, int t)
	{
		Rect r = new Rect(  (int)(_pos.x),
							(int)(_pos.y),
							(int)(_pos.x+_size.x),
							(int)(_pos.y+_size.y));

		canvas.drawPicture(_picture, r);
	}
}
abstract class Anim extends SceneItem
{
	public Picture _picture;
	public int _ms;

	public Anim(Picture p)
	{
		_picture = p;
		/* Random generator = new Random(); */
		/* _pos = new Point(generator.nextInt(300), generator.nextInt(300)); */
		/* int v = 10+generator.nextInt(10); */
		/* _size = new Point(v,v); */
	}

	public abstract boolean isDone();
}

class MoveAnim extends Anim
{
	int _start = 0;
	Point _dst = new Point();

	public MoveAnim(Picture p, Point dst)
	{
		super(p);
		_ms = 1000;
		_dst = dst;
	}

	public void draw(Canvas canvas, int t)
	{
		if(_start == 0) _start = t;
		int time = t - _start;
		if(time >= _ms) return; // Finished
		float step = ((float)time/_ms)*100;

		Rect r;
		if(step < 20)
			r = new Rect((int)(_pos.y),
						(int)(_pos.x+_size.x*(step/50)),
						(int)(_pos.y+_size.y),
						(int)(_pos.x+_size.x));
		else if(step < 70)
		{
			float jumpph = ((step-20)*10/5);
			Point p = new Point(_pos.x, _pos.y);
			p.x += (int)((float)(_dst.x-_pos.x)*(jumpph/100));
			p.y += (int)((float)(_dst.y-_pos.y)*(jumpph/100));

			double off = Math.sin(Math.PI*(jumpph/100))*_size.y;

			r = new Rect((int)(p.x),
						(int)(p.y-off),
						(int)(p.x+_size.x),
						(int)(p.y+_size.y-off));
		}
		else
			r = new Rect((int)(_dst.x),
						(int)(_dst.y-Math.min(0,_size.y*((step-70)*(step-100)/500))),
						(int)(_dst.x+_size.x),
						(int)(_dst.y+_size.y));

		canvas.drawPicture(_picture, r);
	}

	public boolean isDone()
	{
		return  _start != 0 &&
				_start+_ms < SystemClock.uptimeMillis();
	}

	public boolean clicked(int x, int y)
	{
		return false;
	}
}

class PulseAnim extends Anim
{
	public PulseAnim(Picture p)
	{
		super(p);
		_ms = 200;
	}

	public void draw(Canvas canvas, int t)
	{
		double offh = Math.sin(Math.PI*2*(t%_ms)/_ms)*1.5;
		double offv = -offh;

		Rect r = new Rect(  (int)(_pos.x-offh),
							(int)(_pos.y-offv),
							(int)(_pos.x+_size.x+offh),
							(int)(_pos.y+_size.y+offv));

		canvas.drawPicture(_picture, r);
	}

	public boolean isDone()
	{
		return false;
	}
}

