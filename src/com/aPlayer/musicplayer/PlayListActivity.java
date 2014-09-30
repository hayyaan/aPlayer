package com.aPlayer.musicplayer;

import com.androidhive.musicplayer.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

public class PlayListActivity extends Activity {
	ActionBar.Tab tab1, tab2, tab3;
	Fragment fragmentTab1 = new AllList();
	Fragment fragmentTab2 = new ArtistTab();
	Fragment fragmentTab3 = new CustomPlaylistTab();
	public MediaStoreHandler mediaStoreHandler = MediaStoreHandler.getInstance(this);
	public MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

    
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_test);
        
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        tab1 = actionBar.newTab().setText("All Songs");
        tab2 = actionBar.newTab().setText("Artists");
        tab3 = actionBar.newTab().setText("Custom Playlists");
        
        tab1.setTabListener(new TabListener(fragmentTab1));
        tab2.setTabListener(new TabListener(fragmentTab2));
        tab3.setTabListener(new TabListener(fragmentTab3));
        
        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("PlayListActivity result");
		if (resultCode == 101){
			Intent in = new Intent(getApplicationContext(), MainActivity.class);
			in.putExtras(data);
			setResult(100,in);
			finish();
		}
	}
}