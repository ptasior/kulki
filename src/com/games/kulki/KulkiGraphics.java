package com.games.kulki;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.graphics.*;
import android.util.Log;
import java.util.Random;
import java.util.ArrayList;
import android.os.SystemClock;


class BoardItem extends SceneItem
{
	KulkiActivity _act;
	int _ballSize = 0;

	public BoardItem(KulkiActivity act)
	{
		super(new Point(0,0), null);
		_act = act;
	}

	public void changed()
	{
		_size = new Point(_act.game.ballSize() * _act.game.board().WIDTH,
						  _act.game.ballSize() * _act.game.board().HEIGHT);
	}

	int ballSize()
	{
		return _ballSize;
	}

	public void draw(Canvas canvas, int t)
	{
		Paint bg = new Paint();
		bg.setStyle(Paint.Style.FILL);
		bg.setColor(Color.GRAY);

		Rect r = new Rect(  (int)(_pos.x),
							(int)(_pos.y),
							(int)(_pos.x+_size.x),
							(int)(_pos.y+_size.y));

		canvas.drawRect(r, bg);
	}

	public boolean clicked(int x, int y)
	{
		Log.d("qqq", "clicked, x:"+x+" y:"+y+" pos.x:"+_pos.x+" pos.y:"+_pos.y
										+" size.x:"+_size.x+" size.y:"+_size.y);

		if(!(x > _pos.x && x < _pos.x+_size.x && y > _pos.y && y < _pos.y+_size.y))
			return false;

		_act.game.click((x-_pos.x)/_act.game.ballSize(), (y-_pos.y)/_act.game.ballSize());
		return true;
	}
}

