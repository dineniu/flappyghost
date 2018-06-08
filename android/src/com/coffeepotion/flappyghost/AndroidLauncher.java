package com.coffeepotion.flappyghost;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication {
	private AdView adView;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		View gameView=initializeForView(new FlappyGhost(this.getContext()), config);

		RelativeLayout.LayoutParams gameViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		gameViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		gameViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

		gameView.setLayoutParams(gameViewParams);
		layout.addView(gameView);

		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(getString(R.string.banner_ad_unit_id));

		AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
		adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
		adView.loadAd(adRequestBuilder.build());

		RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		topParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
		topParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		layout.addView(adView, topParams);
		adView.setBackgroundColor(android.graphics.Color.TRANSPARENT);

		setContentView(layout);
	}

	@Override
	protected void onResume() {
		super.onResume();
		adView.resume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		adView.pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		adView.destroy();
	}
}
