package com.practice.note;

public class Note {
	private long id;
	private String title;
	
private String noteContent;
	
	public String getNoteContent(){
		return noteContent;
	}
	
	public void setNoteContent(String noteContent){
		this.noteContent = noteContent;
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	@Override
	public String toString(){
		return title;
	}
}
