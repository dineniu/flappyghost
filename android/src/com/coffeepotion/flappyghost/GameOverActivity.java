package com.coffeepotion.flappyghost;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

public class GameOverActivity extends Activity {

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        GameOverFragment gameOverFragment =
                new GameOverFragment().newInstance(getBaseContext());
        gameOverFragment.show(getFragmentManager(),".");
    }
}
