package com.aPlayer.musicplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.androidhive.musicplayer.R;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

public class SelectSongs extends Activity {


	MyCustomAdapter dataAdapter = null;
	public MediaStoreHandler mediaStoreHandler = MediaStoreHandler.getInstance(this);
	public ArrayList<Song> songs = new ArrayList<Song>();
	public int playlistID;
	public int action;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_songs);


		playlistID = getIntent().getExtras().getInt("playlistID");
		action = getIntent().getExtras().getInt("Action");
		
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<String> names = new ArrayList<String>();
		if(action == 0){
			mediaStoreHandler.getPlayListSongs(-1, names, ids);
		}else if(action == 1){
			mediaStoreHandler.getPlayListSongs(playlistID, names, ids);
		}else{
			System.out.println("bro... no...");
		}
		for(int i=0;i<names.size();i++){
			songs.add(new Song(ids.get(i),names.get(i),false));
		}
		// Generate list View from ArrayList
		displayListView();

		checkButtonClick();

	}

	private void displayListView() {

		// Array list of countries
		
		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(this, R.layout.state_info, songs);
		ListView listView = (ListView) findViewById(R.id.listView1);
		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show a toast with the TextView text
				Song state = (Song) parent.getItemAtPosition(position);
				Toast.makeText(getApplicationContext(),
						"Clicked on : " + state.getName(), Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	private class MyCustomAdapter extends ArrayAdapter<Song> {

		private ArrayList<Song> stateList;

		public MyCustomAdapter(Context context, int textViewResourceId, ArrayList<Song> stateList) {
			super(context, textViewResourceId, stateList);
			this.stateList = new ArrayList<Song>();
			this.stateList.addAll(stateList);
		}

		private class ViewHolder {
			CheckBox name;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {

				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = vi.inflate(R.layout.state_info, null);

				holder = new ViewHolder();
//				holder.code = (TextView) convertView.findViewById(R.id.code);
				holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);

				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Song _state = (Song) cb.getTag();

//						Toast.makeText(
//								getApplicationContext(),
//								"Checkbox: " + cb.getText() + " -> "
//										+ cb.isChecked(), Toast.LENGTH_LONG)
//								.show();

						_state.setSelected(cb.isChecked());
					}
				});

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Song state = stateList.get(position);

//			holder.code.setText(" (" + state.getCode() + ")");
			holder.name.setText(state.getName());
			holder.name.setChecked(state.isSelected());

			holder.name.setTag(state);

			return convertView;
		}

	}

	private void checkButtonClick() {

		Button myButton = (Button) findViewById(R.id.findSelected);

		myButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				StringBuffer responseText = new StringBuffer();
//				responseText.append("Selected Countries are...\n");

				ArrayList<Song> stateList = dataAdapter.stateList;
				ArrayList<Song> songsToBeAdded = new ArrayList<Song>();
				for(Song s : stateList){
					if(s.isSelected()){
						songsToBeAdded.add(s);
					}
				}
				if(action == 0){
					mediaStoreHandler.addSongsToPlaylist(playlistID, songsToBeAdded);

				}else if(action == 1){
					mediaStoreHandler.removeSongsFromPlaylist(playlistID, songsToBeAdded);
					((TextView)findViewById(R.id.findSelected)).setText("Delete Selected Items");
				}else{
					System.out.println("This should not happen");
				}
//			    Intent in = new Intent(getApplicationContext(),CustomPlaylist.class);
//			    in.putExtra("playlistID", playlistID);
//				startActivity(in);
				finish();
			}
		});
	}

}
