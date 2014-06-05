package com.games.kulki;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.Window;


public class KulkiActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		/* setContentView(R.layout.main); */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		GamePanel panel = new GamePanel(this);
		setContentView(panel);
	}
}

