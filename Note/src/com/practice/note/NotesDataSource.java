package com.practice.note;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NotesDataSource {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_CONTENT };
	private String[] allTitles = { MySQLiteHelper.COLUMN_TITLE };

	public NotesDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Note createNote(String title, String content) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TITLE, title);
		values.put(MySQLiteHelper.COLUMN_CONTENT, content);
		long insertId = database.insert(MySQLiteHelper.TABLE_NOTES, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		Note newNote = cursorToNote(cursor);
		cursor.close();
		return newNote;
	}

	public void deleteNote(Note note) {
		long id = note.getId();
		System.out.println("Note delete with id: " + id);
		database.delete(MySQLiteHelper.TABLE_NOTES, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);

	}

	public List<Note> getAllNotes() {
		List<Note> notes = new ArrayList<Note>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Note note = cursorToNote(cursor);
			notes.add(note);
			cursor.moveToNext();
		}

		cursor.close();
		return notes;
	}

	public List<String> getAllTitles() {
		List<String> titles = new ArrayList<String>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES, allTitles,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String title = cursorToTitle(cursor);
			titles.add(title);
			cursor.moveToNext();
		}
		cursor.close();
		return titles;
	}

	private Note cursorToNote(Cursor cursor) {
		Note note = new Note();
		note.setId(cursor.getLong(0));
		note.setTitle(cursor.getString(1));
		note.setNoteContent(cursor.getString(2));
		return note;
	}

	private String cursorToTitle(Cursor cursor) {
		String title = null;
		title = cursor.getString(1);
		return title;
	}
}
