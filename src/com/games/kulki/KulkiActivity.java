package com.games.kulki;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.Window;
import com.games.kulki.*;


public class KulkiActivity extends Activity
{
	KulkiGame game = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		game = new KulkiGame(this);
		setContentView(game);
	}
}

