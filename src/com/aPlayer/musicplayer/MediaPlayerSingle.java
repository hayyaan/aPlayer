package com.aPlayer.musicplayer;

import android.media.MediaPlayer;

public class MediaPlayerSingle extends MediaPlayer {
	static MediaPlayerSingle mySelf;
	public static MediaPlayerSingle getInstance(){
		if(mySelf == null){
			mySelf = new MediaPlayerSingle();
		}
		return mySelf;
	}
}
