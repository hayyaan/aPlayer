package com.aPlayer.musicplayer;

import java.util.ArrayList;

import com.androidhive.musicplayer.R;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class CustomPlaylistTab extends ListFragment {
	ArrayList<Integer> ids;
	ArrayList<String> names;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.playlist, container, false);
		
		displayList();

		return view;

	}

	public void onListItemClick(ListView l, View view, int position, long id) {
		// getting listitem index
		if(ids.get(position) == -2){
			AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Make New Playlist");  
			alert.setMessage("Enter Name :"); 

			final EditText input = new EditText(getActivity()); 
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {  
					String name = input.getText().toString();
					Log.w("New Playlist: ", name);
					int id = ((PlayListActivity)getActivity()).mediaStoreHandler.addPlayList(name);
					ids.add(id);
					names.add(name);
					return;                  
				}  
			});  
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;   
				}
			});

			alert.show();
		}else{
			// Starting new intents
			Intent in = new Intent(getActivity().getApplicationContext(), CustomPlaylist.class);
			in.putExtra("playlistID", ids.get(position));
//			System.out.println("my position is "+ ids.get(position));
//			startActivity(in);
			getActivity().startActivityForResult(in,101);
//			getActivity().finish();
		}
	}
	
	public void displayList(){
		ids = new ArrayList<Integer>();
		names = new ArrayList<String>();
		((PlayListActivity)getActivity()).mediaStoreHandler.getPlayListNames(names,ids);

		ids.add(0, -2);
		names.add(0,  "+ Create New Playlist");
		
		ids.add(-25);
		names.add("$ Top 25 Most Played");
		

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.playlist_item_textview, names);
		setListAdapter(adapter);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		displayList();
	}


}
