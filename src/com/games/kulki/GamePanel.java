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
	private KulkiGame game;
	private float size = 0;
	private GameThread thread = null;

	public GamePanel(Context context)
	{
		super(context);
		game = new KulkiGame();

		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	public void startGame()
	{
		if(thread != null) return;
	
		thread = new GameThread(this);
		thread.startThread();
	}

	public void stopGame()
	{
		if(thread == null) return;

		thread.stopThread();

		// Waiting for the thread to die by calling thread.join,
		// repeatedly if necessary
		boolean retry = true;
		while (retry)
		{
			try
			{
				thread.join();
				retry = false;
			} 
			catch (InterruptedException e)
			{
			}
		}
		thread = null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		size = Math.min(getWidth(), getHeight())/ Math.max(game.WIDTH, game.HEIGHT);

		/* setWillNotDraw(false); */
		startGame();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		stopGame();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		/* if (event.getAction() == MotionEvent.ACTION_DOWN) */
		/* { */
		/* 	Paint paint = new Paint(); */
		/* 	paint.setStyle(Paint.Style.FILL); */
		/* 	paint.setColor(Color.GREEN); */
		/* 	paint.setAntiAlias(true); */
		/* 	Canvas c = getHolder().lockCanvas(); */
		/* 	onDraw(c); */
		/* 	c.drawCircle(event.getX(),event.getY(),5,paint); */
		/* 	getHolder().unlockCanvasAndPost(c); */
			/* c.drawCircle(event.getX(),event.getY(),5,paint); */
			/* ((Activity)getContext()).finish(); */
		/* } */

		if(event.getX()<size*9 &&  event.getY()<size*9)
			game.click((int)(event.getX()/size), (int)(event.getY()/size));
		else
			Log.d("qqq", "clililick x="+event.getX()+" y="+event.getY());

		// Em, refresh
		/* Canvas c = getHolder().lockCanvas(); */
		/* onDraw(c); */
		/* getHolder().unlockCanvasAndPost(c); */

		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		Paint bg = new Paint();
		bg.setStyle(Paint.Style.FILL);
		bg.setColor(Color.DKGRAY);
		canvas.drawRect(0,0,getWidth(), getHeight(), bg);

		Paint brd = new Paint();
		brd.setStyle(Paint.Style.FILL);
		brd.setColor(Color.GRAY);
		canvas.drawRect(0,0,size*9, size*9, brd);

		/* canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 10, 10, null); */
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);

		Paint border = new Paint();
		border.setStyle(Paint.Style.STROKE);
		border.setColor(Color.BLACK);

		int cnt = 0;
		for(int i = 0; i < game.WIDTH; i++)
			for(int j = 0; j < game.HEIGHT; j++)
			{
				canvas.drawRect(i*size, j*size, (i+1)*size, (j+1)*size, border);
				if(game.value(i,j).empty()) continue;

				cnt++;
				paint.setColor(game.value(i,j).color());
				canvas.drawCircle((float)(size*(i+0.5)), (float)(size*(j+0.5)),
													(float)(size*0.4), paint);
			}

		Point s = game.selected();

		Paint selected = new Paint();
		selected.setStyle(Paint.Style.STROKE);
		selected.setColor(Color.WHITE);

		Point txtPoint;
		if(getResources().getConfiguration().orientation ==
												Configuration.ORIENTATION_LANDSCAPE)
			txtPoint = new Point((int)size*9+5, 15);
		else
			txtPoint = new Point(5, (int)size*9+5);

		canvas.drawText("Points:"+game.points(), txtPoint.x, txtPoint.y, selected);

		if(s.x == -1) return;
		canvas.drawRect(s.x*size, s.y*size,
									(s.x+1)*size, (s.y+1)*size, selected);
	}
}

