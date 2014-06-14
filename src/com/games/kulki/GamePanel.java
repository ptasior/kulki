package com.games.kulki;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.*;
import android.util.Log;
import android.view.WindowManager;
import android.content.res.Configuration;
import java.util.Random;
import java.util.ArrayList;
import android.os.SystemClock;

class GameThread extends Thread
{
	private final static int SLEEP_TIME = 50;

	private boolean running = false;
	private GamePanel canvas = null;
	private SurfaceHolder surfaceHolder = null;

	public GameThread(GamePanel canvas)
	{
		super();
		this.canvas = canvas;
		this.surfaceHolder = canvas.getHolder();

		Log.d("qqq", "thread constructor");
	}

	public void startThread()
	{
		Log.d("qqq", "thread start");
		running = true;
		super.start();
	}

	public void stopThread()
	{
		running = false;
	}

	public void run()
	{
		Log.d("qqq", "thread run");
		Canvas c = null;
		while(running)
		{
			c = null;
			try
			{
				c = surfaceHolder.lockCanvas();
				synchronized (surfaceHolder)
				{
					if(c == null)
					{
						Log.w("qqq", "thread cannot obtain canvas");
						continue;
					}

					canvas.onDraw(c);

					surfaceHolder.unlockCanvasAndPost(c);
				}
				sleep(SLEEP_TIME);
			}
			catch(InterruptedException ie)
			{
				surfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}
}

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
	private GameThread _thread = null;
	private ArrayList<SceneItem> _items = new ArrayList<SceneItem>();

	public GamePanel(Context context)
	{
		super(context);

		/* Picture p = SceneItem.loadPicture(R.drawable.ic_launcher); */

		/* for(int i = 0; i < 4; i++) */
		/* 	items.add(new PulseAnim(p)); */

		/* items.add(new MoveAnim(p, new Point(100, 200))); */

		getHolder().addCallback(this);
		setFocusable(true);
	}

	public void startThread()
	{
		if(_thread != null) return;
	
		_thread = new GameThread(this);
		_thread.startThread();
	}

	public void stopThread()
	{
		if(_thread == null) return;

		_thread.stopThread();

		while(true)
			try {_thread.join(); break;}
			catch(InterruptedException e) {}

		_thread = null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		/* setWillNotDraw(false); */
		startThread();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		stopThread();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		for(SceneItem i: _items)
			if(i.clicked((int)event.getX(), (int)event.getY())) break;

		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		Paint bg = new Paint();
		bg.setStyle(Paint.Style.FILL);
		bg.setColor(Color.WHITE);

		canvas.drawRect(0,0,canvas.getWidth(), canvas.getHeight(), bg);
		for(SceneItem i: _items)
			i.draw(canvas, (int)SystemClock.uptimeMillis());
	}

	public void addItem(SceneItem i)
	{
		_items.add(i);
	}

	/* public void getItem(SceneItem i) */
	/* { */
	/* 	_items.del(i); */
	/* } */

	/* public void delItem(SceneItem i) */
	/* { */
	/* 	_items.del(i); */
	/* } */

	public Picture loadPicture(int id)
	{
		Picture picture = new Picture();

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
		Canvas canvas = picture.beginRecording(bitmap.getWidth(), bitmap.getHeight());
		Rect r = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		canvas.drawBitmap(bitmap, r, r, null);
		picture.endRecording();

		return picture;
	}
}

