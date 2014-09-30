package com.aPlayer.musicplayer;

import java.util.ArrayList;
import java.util.HashMap;

import com.androidhive.musicplayer.R;
import com.androidhive.musicplayer.R.layout;
import com.androidhive.musicplayer.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CustomArtist extends ListActivity {

	int artistID;
	public MediaStoreHandler mediaStoreHandler = MediaStoreHandler.getInstance(this);

	ArrayList<Integer> ids = new ArrayList<Integer>();
	ArrayList<String> names = new ArrayList<String>();

	public void onCreate(Bundle savedInstanceState) {
		Log.w("Custom", "Made");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
		artistID = getIntent().getExtras().getInt("artistID");
		displayList(artistID);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
			Intent in = new Intent(getApplicationContext(), PlayListActivity.class);
//			Intent in = getIntent();
			in.putExtra("type",  1);
			in.putExtra("artistId",  artistID);
			in.putExtra("songId", ids.get(position));
//			startActivity(in);
			setResult(101,in);
			finish();
	}


	public void displayList(int playlistID) {
		mediaStoreHandler.getArtistSongs(playlistID, names, ids);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.playlist_item_textview, names);
		setListAdapter(adapter);
	}

}
