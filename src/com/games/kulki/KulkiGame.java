package com.games.kulki;
import java.util.Random;
import android.graphics.Color;
import android.util.Log;
import android.graphics.Point;
import java.util.ArrayList;
import android.view.SurfaceHolder;

public class KulkiGame extends GamePanel
{
	KulkiBoard _board;
	KulkiActivity _act;
	int _ballSize = 0;


	KulkiGame(KulkiActivity act)
	{
		super(act);
		_act = act;
		_board = new KulkiBoard(act);
	}

	public void surfaceCreated(SurfaceHolder holder)
	{
		_ballSize = Math.min(_act.game.getWidth(), _act.game.getHeight()) /
					Math.max(_act.game.board().WIDTH, _act.game.board().HEIGHT);

		createGui();
		super.surfaceCreated(holder);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		_ballSize = Math.min(_act.game.getWidth(), _act.game.getHeight()) /
					Math.max(_act.game.board().WIDTH, _act.game.board().HEIGHT);

		super.surfaceChanged(holder, format, width, height);
	}

	void createGui()
	{
		BoardItem b = new BoardItem(_act);
		addItem(b);

		Ball.loadStatic(_act);
		for(int i =0; i < _board.WIDTH; i++)
			for(int j =0; j < _board.HEIGHT; j++)
				addItem(_board.value(i,j));
	}

	public void click(int x, int y)
	{
		Log.d("qqq", "click, x:"+x+" y:"+y);
		if(!_board.value(x,y).isEmpty())
		{
			if(_board.selected().equals(x, y)) _board.selected(-1, -1);
			else                               _board.selected(x,y);
			return;
		}

		if(_board.selected().x == -1) return; // Nothing selected

		ArrayList<KulkiBoard.Direction> path =
							_board.canMove(_board.selected(), new Point(x,y));
		if(path == null) return;
		
		_board.move(_board.selected(), new Point(x,y), path);

		if(_board.calculatePoints(x,y) == 0)
			_board.shuffleNew();

		_board.selected(-1, -1);
	}

	public KulkiBoard board()
	{
		return _board;
	}

	public int ballSize()
	{
		return _ballSize;
	}
}

