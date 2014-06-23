package com.practice.note;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NoteTitleFragment extends ListFragment {
	public static final String TAG = NoteTitleFragment.class.getName();

	private static final int ACTIVITY_EDIT = 1;

	private OnListItemSelectedListener mCallback;
	private NotesDataSource dataSource;
	private Cursor mCursor;
	private int layout;
	private List<Note> values;

	public interface OnListItemSelectedListener {
		public void onListItemSelected(int position);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
				: android.R.layout.simple_list_item_1;
		dataSource = new NotesDataSource(getActivity());
		Log.d(TAG, "On create Called");
		// if (dataSource != null) {
		dataSource.open();
		values = dataSource.getAllNotes();

		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(getActivity(),
				layout, values);
		// String tempItems[] = { "one", "two" };
		setListAdapter(adapter);
		// }
	}

	@Override
	public void onStart() {
		Log.d(TAG, "On start called");
		super.onStart();
		if (getFragmentManager().findFragmentById(R.id.note_content_frag) != null) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnListItemSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnListItemSelectedListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		mCursor = dataSource.allNotes();
		mCursor.moveToPosition(position);
		if (getActivity().findViewById(R.id.note_frag) == null) {
			Intent intent = new Intent(getActivity(), NewNoteActivity.class);
			intent.putExtra(MySQLiteHelper.COLUMN_ID, id);
			intent.putExtra(
					MySQLiteHelper.COLUMN_TITLE,
					mCursor.getString(mCursor
							.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_TITLE)));
			intent.putExtra(
					MySQLiteHelper.COLUMN_CONTENT,
					mCursor.getString(mCursor
							.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_CONTENT)));
			getActivity().startActivityForResult(intent, ACTIVITY_EDIT);
			mCallback.onListItemSelected(position);
			getListView().setItemChecked(position, true);

		}

	}

	public void createNote(Intent intent) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Note> adapter = (ArrayAdapter<Note>) getListAdapter();
		Note note = null;
		Bundle extras = intent.getExtras();
		// dataSource.open();

		// String tempTitle = intent.getStringExtra("title");
		String tempTitle = extras.getString(MySQLiteHelper.COLUMN_TITLE);
		// String tempContent = intent.getStringExtra("content");
		String tempContent = extras.getString(MySQLiteHelper.COLUMN_CONTENT);
		note = dataSource.createNote(tempTitle, tempContent);
		adapter.add(note);

		adapter.notifyDataSetChanged();
		// return note;
	}

	public void updateNote(Intent intent) {
		Bundle extras = intent.getExtras();
		String title = extras.getString(MySQLiteHelper.COLUMN_TITLE);
		String content = extras.getString(MySQLiteHelper.COLUMN_CONTENT);
		long rowId = extras.getLong(MySQLiteHelper.COLUMN_ID);
		Log.d(TAG, "made it to update");
		Note note = null;
		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(getActivity(),
				layout, values);

		if (extras.getBoolean("done")) {
			Log.d(TAG, "Done Condition met");
			dataSource.updateNote(rowId, title, content);
		} else {
			if (getListAdapter().getCount() > 0) {
				Log.d(TAG, "****made it ot update");
				note = dataSource.getNote(extras
						.getLong(MySQLiteHelper.COLUMN_ID));
				dataSource.deleteNote(note);
				adapter.remove(note);
			}
		}
		setListAdapter(adapter);

		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "on resume called");
		super.onResume();
		dataSource.open();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "on pause called");
		super.onPause();
		// dataSource.close();
	}

}
