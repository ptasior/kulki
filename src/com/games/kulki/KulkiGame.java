package com.games.kulki;
import java.util.Random;
import android.graphics.Color;
import android.util.Log;
import android.graphics.Point;
import java.util.ArrayList;

public class KulkiGame
{
	KulkiBoard _board = new KulkiBoard();
	KulkiActivity _act;

	KulkiGame(KulkiActivity act)
	{
		_act = act;
		createGui();
	}
	
	void createGui()
	{
		Board b = new Board(_act);
		_act.panel.addItem(b);
	}

	public void click(int x, int y)
	{
		if(!_board.value(x,y).empty())
		{
			if(_board.selected().equals(x, y)) _board.selected(-1, -1);
			else                      _board.selected(x,y);
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
}

