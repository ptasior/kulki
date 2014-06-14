package com.games.kulki;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.graphics.*;
import android.util.Log;
import java.util.Random;
import java.util.ArrayList;
import android.os.SystemClock;


class Board extends SceneItem
{
	KulkiActivity _act;

	public Board(KulkiActivity act)
	{
		_act = act;

		int size = Math.min(act.panel.getWidth(), act.panel.getHeight()) /
					Math.max(act.game.board().WIDTH, act.game.board().HEIGHT);
		Point p = new Point(size*act.game.WIDTH, size*act.game.HEIGHT);

		super(new Point(0,0), p);
	}

	public void draw(Canvas canvas, int t)
	{
		Rect r = new Rect(  (int)(_pos.x),
							(int)(_pos.y),
							(int)(_pos.x+_size.x),
							(int)(_pos.y+_size.y));

		canvas.drawRect(r);
	}

	public boolean clicked(int x, int y)
	{
		if(!(x > _pos.x && x < _pos.x+_size.x && y > _pos.y && y < _pos.y+_size.y))
			return false;

		_act.game.clicked((x-_pos.x)/_act.game.WIDTH, (y-_pos.y)/_act.game.HEIGHT);
		return true;
	}
}

