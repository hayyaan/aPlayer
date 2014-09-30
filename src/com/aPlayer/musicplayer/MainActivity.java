package com.aPlayer.musicplayer;

import java.io.IOException;
import java.util.ArrayList;

import com.androidhive.musicplayer.R;
import com.devadvance.circularseekbar.CircularSeekBar;
import com.devadvance.circularseekbar.CircularSeekBar.OnCircularSeekBarChangeListener;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.media.MediaMetadataRetriever;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.TextView;

public class MainActivity extends Activity implements OnCompletionListener, OnCircularSeekBarChangeListener {

	private ImageButton btnPlay;
	private ImageButton btnNext;
	private ImageButton btnPrevious;
	private ImageButton btnPlaylist;
	private CircularSeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songAuthorLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;

	// Media Player
	private  MediaPlayerSingle mp;

	// Media Store Handler
	public MediaStoreHandler mediaStoreHandler = MediaStoreHandler.getInstance((Activity)this);

	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();
	private Utilities utils;
	private ArrayList<Integer> songsList;
	private int playListId;
	private int songIndex;

	private MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
	LinearLayout albumArt;
	byte[] aArt;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);

		// All player buttons
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
		songProgressBar = (CircularSeekBar) findViewById(R.id.circularSeekBar1);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		songAuthorLabel = (TextView) findViewById(R.id.songAuthor);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		albumArt = (LinearLayout) findViewById(R.id.album_art_layout);


		songsList = new ArrayList<Integer>();
		// Mediaplayer
		mp = MediaPlayerSingle.getInstance();
		mp.reset();
		utils = new Utilities();

		mediaStoreHandler.updateStore();


		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important

		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// check for already playing
				if(mp.isPlaying()){
					if(mp!=null){
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				}else{
					// Resume song
					if(mp!=null){
						mp.start();
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}

			}
		});
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// check if next song is there or not
				songIndex = (songIndex < songsList.size() -1) ? songIndex+1 : 0;
				playSong();
			}
		});
		btnPrevious.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				songIndex = (songIndex > 0) ? songIndex-1 : songsList.size()-1;
				playSong();
			}
		});
		btnPlaylist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
				startActivityForResult(i,100);			
			}
		});
		
		playListId = -1;
		songIndex = 0;
		ArrayList<String> derp = new ArrayList<String>();
//		System.out.println("Songslist size " + songsList.size());
		mediaStoreHandler.getPlayListSongs(playListId, derp, songsList);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100){
			System.out.println("I came here!!!");
			songsList = new ArrayList<Integer>();
			if(data.getExtras() != null){
				int type = data.getExtras().getInt("type");
				if(type == 0){//playlist
					playListId = data.getExtras().getInt("playListId");
					int songId = data.getExtras().getInt("songId");
					ArrayList<String> derp = new ArrayList<String>();
					mediaStoreHandler.getPlayListSongs(playListId, derp, songsList);
					songIndex = songsList.indexOf(songId);
				}else if(type == 1){//artist
					playListId = data.getExtras().getInt("artistId");
					int songId = data.getExtras().getInt("songId");
					ArrayList<String> derp = new ArrayList<String>();
					mediaStoreHandler.getArtistSongs(playListId, derp, songsList);
					songIndex = songsList.indexOf(songId);
				}else{
					System.out.println("This is not supposed to happen");
				}

			}
			playSong();
		}
	}

	/**
	 * Function to play a song
	 * @param songIndex - index of song
	 * */
	public void  playSong(){
		// Play song
		try {
			mp.reset();
			String path = mediaStoreHandler.getSongPath(songsList.get(songIndex));

			//			String path = songsList.get(songIndex).get("songPath");
			mp.setDataSource(path);

			try{
				metaRetriever.setDataSource(path);
				aArt = metaRetriever.getEmbeddedPicture();
				Bitmap songImage = BitmapFactory.decodeByteArray(aArt, 0, aArt.length);
				albumArt.setBackground( new BitmapDrawable(getResources(),songImage));

			} catch (Exception e){
				albumArt.setBackground(getResources().getDrawable(R.drawable.adele));
			}
			String title = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			String author = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
			if(title == null){
				title = path.split("/")[path.split("/").length-1];
			}
			songTitleLabel.setText(title);
			songAuthorLabel.setText(author);

			mp.prepare();
			mp.start();
			btnPlay.setImageResource(R.drawable.btn_pause);

			// set Progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);

			// Updating progress bar
			updateProgressBar();			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mediaStoreHandler.updateFrequency(songsList.get(songIndex));
//		String key = String.valueOf(songsList.get(songIndex));
//		SharedPreferences songCounts = getSharedPreferences("SongCounter", 0);
//		System.out.println(songCounts.getString(key, key+":0"));
//		System.out.println(songCounts.getInt(key, 0));
//		ArrayList<String> l = new ArrayList<String>();
//		ArrayList<Integer> l2 = new ArrayList<Integer>();
//		mediaStoreHandler.getTop25(l,l2);
		
	}


	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 10);        
	}	

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = mp.getDuration();
			long currentDuration = mp.getCurrentPosition();

			// Displaying Total Duration time
			String t = utils.milliSecondsToTimer(totalDuration - currentDuration);

			//			   if(t.substring(0, 3).equals("00:")){
			//				   t = t.substring(2, t.length()-1);
			//			   }
			songTotalDurationLabel.setText(t);
			// Displaying time completed playing
			songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

			// Updating progress bar
			int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			//Log.d("Progress", ""+progress);
			songProgressBar.setProgress(progress);

			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 10);
		}
	};

	/**
	 * 
	 * */
	public void onProgressChanged(CircularSeekBar seekBar, int progress, boolean fromTouch) {

	}

	/**
	 * When user starts moving the progress handler
	 * */
	public void onStartTrackingTouch(CircularSeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	/**
	 * When user stops moving the progress handler
	 * */
	public void onStopTrackingTouch(CircularSeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

		// forward or backward to certain seconds
		mp.seekTo(currentPosition);

		// update timer progress again
		updateProgressBar();
	}

	/**
	 * On Song Playing completed
	 * if repeat is ON play same song again
	 * if shuffle is ON play random song
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {

		songIndex = (songIndex < songsList.size() -1) ? songIndex+1 : 0;

		playSong();

	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		mp.pause();

		mp.release();
	}

}