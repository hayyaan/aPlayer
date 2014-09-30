package com.aPlayer.musicplayer;

import java.util.ArrayList;

import com.androidhive.musicplayer.R;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AllList extends ListFragment {


	ArrayList<Integer> ids = new ArrayList<Integer>();
	ArrayList<String> names = new ArrayList<String>();
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.playlist, container, false);
		((PlayListActivity)getActivity()).mediaStoreHandler.getAllSongNames(names,ids);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.playlist_item_textview, names);
		setListAdapter(adapter);
		return view;
	}

	public void onListItemClick(ListView l, View view, int position, long id) {
		Intent in = new Intent(getActivity().getApplicationContext(), MainActivity.class);
//		Intent in = getActivity().getIntent();
		in.putExtra("playListId", -1);
		in.putExtra("type", 0);

		in.putExtra("songId", ids.get(position));
//		getActivity().startActivity(in);
		getActivity().setResult(100,in);
		getActivity().finish();
	}
}
