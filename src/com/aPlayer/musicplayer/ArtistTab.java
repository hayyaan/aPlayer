package com.aPlayer.musicplayer;

import java.util.ArrayList;

import com.androidhive.musicplayer.R;

import android.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.TextView;

public class ArtistTab extends ListFragment {
	ArrayList<Integer> ids;
	ArrayList<String> names;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.playlist, container, false);
		ids = new ArrayList<Integer>();
		names = new ArrayList<String>();
		((PlayListActivity)getActivity()).mediaStoreHandler.getArtistNames(names,ids);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.playlist_item_textview, names);
		setListAdapter(adapter);

		return view;

	}

	public void onListItemClick(ListView l, View view, int position, long id) {

			Intent in = new Intent(getActivity().getApplicationContext(), CustomArtist.class);
			in.putExtra("artistID", ids.get(position));
//			startActivity(in);
			getActivity().startActivityForResult(in,101);
			System.out.println("started customartist");
//			getActivity().setResult(100,in);
//			getActivity().finish();

	}
	

}