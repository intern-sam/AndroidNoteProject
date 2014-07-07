package com.practice.note;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NotesDataSource {
	public static final String TAG = NotesDataSource.class.getName();

	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_CONTENT };
	private String[] allTitles = { MySQLiteHelper.COLUMN_TITLE };

	public NotesDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		Log.d(TAG, "DB open");
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		Log.d(TAG, "DB closed");
		dbHelper.close();
	}

	public Note createNote(String title, String content) {
		Log.d(TAG, "in create note");
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_TITLE, title);
		Log.d(TAG, "Put title");
		values.put(MySQLiteHelper.COLUMN_CONTENT, content);
		Log.d(TAG, "put content");
		long insertId = database.insert(MySQLiteHelper.TABLE_NOTES, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_NOTES, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		Note newNote = cursorToNote(cursor);
		cursor.close();
		Log.d(TAG, "Note created");
		return newNote;
	}

	public void deleteNote(Note note) {
		Log.d(TAG, "Note delete with id: " + note.getId());
		long id = note.getId();
		database.delete(MySQLiteHelper.TABLE_NOTES, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);

	}

	public void deleteNote(Long mRowId) {
		Log.d(TAG, "Note delete with id: " + mRowId);
		Cursor mCursor = database.query(MySQLiteHelper.TABLE_NOTES, allColumns,
				null, null, null, null, null);
		if (mRowId < Integer.MIN_VALUE || mRowId > Integer.MAX_VALUE) {
			// toast an error, can't convert without value change
			return;
		}
		int pos = (int) (long) mRowId;
		mCursor.moveToPosition(pos);
		Note note = cursorToNote(mCursor);
		database.delete(MySQLiteHelper.TABLE_NOTES, MySQLiteHelper.COLUMN_ID
				+ " = " + note.getId(), null);

	}

	public Note getNote(long rowID) throws SQLException {
		Note note;

		Cursor mCursor = database.query(true, MySQLiteHelper.TABLE_NOTES,
				new String[] { MySQLiteHelper.COLUMN_ID,
						MySQLiteHelper.COLUMN_TITLE,
						MySQLiteHelper.COLUMN_CONTENT },
				MySQLiteHelper.COLUMN_ID + "=" + rowID, null, null, null, null,
				null);
		Log.d(TAG, "Cursor: " + mCursor);

		if (mCursor != null) {
			mCursor.moveToFirst();
			Log.d(TAG, "Cursor moved to first");
		}
		note = cursorToNote(mCursor);
		Log.d(TAG, "Cursor converted");
		return note;
	}

	public Note getNote(int pos) {
		Note note;
		Cursor mCursor = database.query(MySQLiteHelper.TABLE_NOTES, allColumns,
				null, null, null, null, null);
		mCursor.moveToPosition(pos);
		note = cursorToNote(mCursor);
		return note;
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

	public Cursor allNotes() {
		return database.query(MySQLiteHelper.TABLE_NOTES, new String[] {
				MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_TITLE,
				MySQLiteHelper.COLUMN_CONTENT }, null, null, null, null, null);
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
		Log.d(TAG, "In cursorToNote()");
		Log.d(TAG, "ID from cursor: " + cursor.getLong(0));
		note.setId(cursor.getLong(0));
		note.setTitle(cursor.getString(1));
		Log.d(TAG, "Title from cursor: " + cursor.getString(1));
		note.setNoteContent(cursor.getString(2));
		return note;
	}

	private String cursorToTitle(Cursor cursor) {
		String title = null;
		title = cursor.getString(1);
		return title;
	}

	public boolean updateNote(long rowID, String title, String content) {
		Cursor mCursor = database.query(MySQLiteHelper.TABLE_NOTES, allColumns,
				null, null, null, null, null);
		if (rowID < Integer.MIN_VALUE || rowID > Integer.MAX_VALUE) {
			// toast an error, can't convert without value change
			return false;
		}
		int pos = (int) rowID;
		mCursor.moveToPosition(pos);
		ContentValues args = new ContentValues();
		args.put(MySQLiteHelper.COLUMN_TITLE, title);
		args.put(MySQLiteHelper.COLUMN_CONTENT, content);

		// mCursor.getLong(0)
		return database.update(MySQLiteHelper.TABLE_NOTES, args,
				MySQLiteHelper.COLUMN_ID + "=" + rowID, null) > 0;
	}
}
