package com.aPlayer.musicplayer;

public class Song {

	int id = 0;
	String name = null;
	boolean selected = false;

	public Song(int code, String name, boolean selected) {
		super();
		this.id = code;
		this.name = name;
		this.selected = selected;
	}
	public Song(String name, boolean selected) {
		super();
		this.name = name;
		this.selected = selected;
	}
	public int getID() {
		return id;
	}

	public void setID(int code) {
		this.id = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
