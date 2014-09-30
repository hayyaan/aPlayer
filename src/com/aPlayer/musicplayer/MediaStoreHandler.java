package com.aPlayer.musicplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class MediaStoreHandler {

	Activity context;
	public static final String songCounterName= "SongCounter";

	static MediaStoreHandler mediaStoreHandler;

	public static MediaStoreHandler getInstance(Activity act){
		if (mediaStoreHandler == null){
			mediaStoreHandler = new MediaStoreHandler(act);
		}
		return mediaStoreHandler;
	}
	public MediaStoreHandler(Activity th){
		context = th;
	}
	public void updateStore(){
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, 
				Uri.parse("file://" + Environment.getExternalStorageDirectory() )));
	}
	public String getSongPath(int songId){		
		String path = null;
		String selection = MediaStore.Audio.Media._ID + " = '" + String.valueOf(songId) +"'";
		Cursor cursor = null;
		final String[] projection = new String[] {
				MediaStore.Audio.Media.DATA};
		try {
			// the uri of the table that we want to query
			Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			// query the db
			cursor = context.getBaseContext().getContentResolver().query(uri,
					projection, selection, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				//				while (!cursor.isAfterLast()) { 
				path = cursor.getString(0);
				//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return path;
	}
	public String getSongName(int songId){		
		String name = null;
		String selection = MediaStore.Audio.Media._ID + " = '" + String.valueOf(songId) +"'";
		Cursor cursor = null;
		final String[] projection = new String[] {
				MediaStore.Audio.Media.TITLE};
		try {
			// the uri of the table that we want to query
			Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			// query the db
			cursor = context.getBaseContext().getContentResolver().query(uri,
					projection, selection, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				//				while (!cursor.isAfterLast()) { 
				name = cursor.getString(0);
				//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return name;
	}
	public void getAllSongNames(ArrayList<String> names, ArrayList<Integer> ids){
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
//		System.out.println("lerp");
		Cursor cursor = null;
		final String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE};
		final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
		try {
			// the uri of the table that we want to query
			Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			// query the db
//			System.out.println("wat");
			cursor = context.getBaseContext().getContentResolver().query(uri, projection, selection, null, sortOrder);
			if (cursor != null) {
				cursor.moveToFirst();
//				System.out.println("whats going on");

				while (!cursor.isAfterLast()) { 
					ids.add(cursor.getInt(0));
					names.add(cursor.getString(1));
					cursor.moveToNext();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	public void getPlayListSongs(int playListId, ArrayList<String> names, ArrayList<Integer> ids){
		System.out.println("list id is " + playListId);
		if (playListId == -1){
			getAllSongNames(names, ids);
			return;
		}
		else if (playListId == -25){
			getTop25(names,ids);
			return;
		}
		Cursor cursor = null;
		final String[] projection = {MediaStore.Audio.Playlists.Members.AUDIO_ID, MediaStore.Audio.Media.TITLE};
		try{
			Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playListId);
			cursor = context.getBaseContext().getContentResolver().query(uri, projection, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();                       
				while (!cursor.isAfterLast()) { 
					ids.add(cursor.getInt(0));
					names.add(cursor.getString(1));
					cursor.moveToNext();
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	public void getPlayListNames(ArrayList<String> playListNames, ArrayList<Integer> playListIDs){
		Cursor cursor = null;
		final String[] projection = { MediaStore.Audio.Playlists._ID, MediaStore.Audio.Playlists.NAME};
		try{
			Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
			cursor = context.getBaseContext().getContentResolver().query(uri, projection, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();                       
				while (!cursor.isAfterLast()) { 
					playListNames.add(cursor.getString(1));
					playListIDs.add(cursor.getInt(0));
					cursor.moveToNext();
				}
			}

		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	public void getArtistNames(ArrayList<String> playListNames, ArrayList<Integer> playListIDs){
		Cursor cursor = null;
		final String[] projection = {MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST};
		
		try{
			Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
			cursor = context.getBaseContext().getContentResolver().query(uri, projection, null, null, MediaStore.Audio.Artists.ARTIST + " ASC");
			if (cursor != null) {
				cursor.moveToFirst();                       
				while (!cursor.isAfterLast()) { 
					playListNames.add(cursor.getString(1));
					playListIDs.add(cursor.getInt(0));
					cursor.moveToNext();
				}
			}

		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	public void getArtistSongs(int artistID, ArrayList<String> playListNames, ArrayList<Integer> playListIDs){
		Cursor cursor = null;
		final String[] projection = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE};
		String selection = MediaStore.Audio.Media.ARTIST_ID + " = " + artistID;
		System.out.println("why u no :(");
		try{
			Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			cursor = context.getBaseContext().getContentResolver().query(uri, projection, selection, null, MediaStore.Audio.Artists.ARTIST + " ASC");
			if (cursor != null) {
				System.out.println("wat?");
				cursor.moveToFirst();                       
				while (!cursor.isAfterLast()) {
					System.out.println("watwatwat?"); 
					playListNames.add(cursor.getString(1));
					playListIDs.add(cursor.getInt(0));
					cursor.moveToNext();
				}
			}

		} catch(Exception e){
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	public int addPlayList(String name) {
		ContentValues values = new ContentValues();
		Uri uri;
		values.put(MediaStore.Audio.Playlists.NAME, name);
		values.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis());
		values.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());
		uri = context.getApplicationContext().getContentResolver().insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, values);
		Cursor cur = context.getApplicationContext().getContentResolver().query(uri, new String[] { MediaStore.Audio.Playlists._ID}, null, null, null);
		cur.moveToFirst();
		int id = cur.getInt(0);
		return id;
	}
	public void addSongsToPlaylist(int playListId, ArrayList<Song> songs){
	    Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playListId);
	    ContentResolver resolver = context.getApplicationContext().getContentResolver();
	    ContentValues value;
	    int listSize = getPlayListSize(playListId);
	    int count = 0;
	    for(Song s : songs){
	        value = new ContentValues();
	        value.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, listSize + count + 1);
	        value.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, s.getID());
	        resolver.insert(uri, value);
	        count++;
	    }
	    resolver.notifyChange(Uri.parse("content://media"), null);

	}
	public void removeSongsFromPlaylist(int playListId, ArrayList<Song> songs){
        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri( "external", playListId);
        String selection = MediaStore.Audio.Playlists.Members.AUDIO_ID + "=?";
        for (Song s: songs){
            String selectionargs[] = { String.valueOf(s.getID()) };
            context.getContentResolver().delete(uri, selection, selectionargs);
        }
	}
	public void removePlaylist(int playListId){
		String where = MediaStore.Audio.Playlists._ID + "=?";
		String[] whereVal = {String.valueOf(playListId)}; 
		context.getContentResolver().delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, where, whereVal);
	}
	public int getPlayListSize(int playListId){
		int count = 0;
		String[] retCol = { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE };
		Uri membersUri = MediaStore.Audio.Playlists.Members.getContentUri("external", playListId);
		Cursor membersCursor = context.getApplicationContext().getContentResolver().query(membersUri, retCol, null, null, null);
		membersCursor.moveToFirst();
		while (!membersCursor.isAfterLast()) {
	    	count++;
	    	membersCursor.moveToNext();
	    }
        membersCursor.close();
		return count;
	}

	public void updateFrequency(int songId){
		String key = String.valueOf(songId);
		SharedPreferences songCounts = context.getSharedPreferences(songCounterName, 0);
		SharedPreferences.Editor editor = songCounts.edit();
//		if (songCounts.contains(key)==true){
//			editor.putInt(key, songCounts.getInt(key, 0)+1);
//			String st = key+":"+
		editor.putString(key, countToString(songId,stringToCount(songCounts.getString(key, key+":0") )+1 ) );
//		editor.putInt(key, (songCounts.getInt(key, 0))+1);
//		int count = songCounts.getInt(key, 0);
//		count++;
//		editor.putInt(key, count);
//		editor.putInt(key, songCounts.getInt(key, 0)+1);
//		}
//		else {
//			editor.putInt(key, 0);
//			editor.putString(key,countToString(songId,0));
//		}
		editor.commit();
	}
	public void getTop25(ArrayList<String> names,ArrayList<Integer> ids){
		SharedPreferences songCounts = context.getSharedPreferences(songCounterName, 0);
		Map map = songCounts.getAll();
		
//		map = sortByValue(map);
//		System.out.println(map.entrySet().toString());
		
		
		ArrayList<String> crap = new ArrayList<String>(map.values());
		ArrayList<Integer> counts = new ArrayList<Integer>();
		ArrayList<String> id = new ArrayList<String>();
		ArrayList<Integer> l = new ArrayList<Integer>();
		
		for (int i=0;i<crap.size();i++){
			counts.add(stringToCount(crap.get(i)));
			id.add(crap.get(i).split(":")[0]);
		}
		Collections.sort(counts);
		Collections.reverse(counts);
		for (int i=0;i<crap.size();i++){
			for (int j=0;j<crap.size();j++){
				if (counts.get(i)==stringToCount(crap.get(j))){
					l.add(Integer.parseInt(crap.get(j).split(":")[0]));
				}
			}
		}
//		ArrayList<Integer> l = new ArrayList<Integer>(map.values());
//		System.out.println(map.values().toString());
//		System.out.println(l.toString());
//		Collections.sort(l);
//		Collections.reverse(l);
//		System.out.println("this is l:"+l.toString());
//		System.out.println(l.toString());
		HashSet hs = new HashSet();
		hs.addAll(l);
		l.clear();
		l.addAll(hs);
		for(int i=0;i<((l.size()>25) ? 25:l.size());i++){
			
			ids.add(l.get(i));
			names.add(getSongName(l.get(i)));
//			System.out.println(l.get(i));
		}
		//		map = sortByValue(map);
		
//		System.out.println(map.values().toString());
//		ArrayList<Integer> allSongs = new ArrayList<Integer>(map.values());
//		Collections.sor
//		all
		
	}

	
	public int stringToCount(String st){
		return Integer.parseInt(st.split(":")[1]);
	}
	
	public String countToString(int key, int count){
		return String.valueOf(key)+":"+String.valueOf(count);
	}
	
//	static Map sortByValue(Map map) {
//	     List list = new LinkedList(map.entrySet());
//	     Collections.sort(list, new Comparator() {
//	          public int compare(Object o1, Object o2) {
//	               return ((Comparable) ((Map.Entry) (o1)).getValue())
//	              .compareTo(((Map.Entry) (o2)).getValue());
//	          }
//	     });
//
//	    Map result = new LinkedHashMap();
//	    for (Iterator it = list.iterator(); it.hasNext();) {
//	        Map.Entry entry = (Map.Entry)it.next();
//	        result.put(entry.getKey(), entry.getValue());
//	    }
//	    return result;
//	} 
	
}
