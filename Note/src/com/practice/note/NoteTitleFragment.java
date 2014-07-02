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
	private boolean isPhone;

	public interface OnListItemSelectedListener {
		public void onListItemSelected(int position);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
				: android.R.layout.simple_list_item_1;
		isPhone = getActivity().findViewById(R.id.note_frag) == null;
		dataSource = new NotesDataSource(getActivity());
		Log.d(TAG, "On create Called");
		dataSource.open();
		values = dataSource.getAllNotes();

		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(getActivity(),
				layout, values);
		setListAdapter(adapter);
		if (!adapter.isEmpty() && !isPhone) {
			NoteContentFragment noteContentFragment = (NoteContentFragment) getFragmentManager()
					.findFragmentById(R.id.note_content_frag);
			noteContentFragment.updateItemContentView(0);
		}

	}

	@Override
	public void onStart() {
		Log.d(TAG, "On start called");
		super.onStart();
		if (getFragmentManager().findFragmentById(R.id.note_content_frag) != null) {
			Log.d(TAG, "note_content_frag not null");
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
			getActivity().startActivity(intent);
			mCallback.onListItemSelected(position);
			getListView().setItemChecked(position, true);
		} else {
			mCallback.onListItemSelected(position);
		}

	}

	public void createNote(Intent intent) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Note> adapter = (ArrayAdapter<Note>) getListAdapter();
		Note note = null;
		Bundle extras = intent.getExtras();
		String tempTitle = extras.getString(MySQLiteHelper.COLUMN_TITLE);
		String tempContent = extras.getString(MySQLiteHelper.COLUMN_CONTENT);
		note = dataSource.createNote(tempTitle, tempContent);
		adapter.add(note);

		adapter.notifyDataSetChanged();
	}

	public void updateNote(/* Intent intent */) {
		// Bundle extras = intent.getExtras();
		// String title = extras.getString(MySQLiteHelper.COLUMN_TITLE);
		// String content = extras.getString(MySQLiteHelper.COLUMN_CONTENT);
		// long rowId = extras.getLong(MySQLiteHelper.COLUMN_ID);
		Log.d(TAG, "made it to update");
		Note note = null;
		values = dataSource.getAllNotes();
		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(getActivity(),
				layout, values);

		// note = ((Note) getListItem)
		// if (extras.getBoolean("done")) {
		// Log.d(TAG, "Done Condition met");
		// dataSource.updateNote(rowId, title, content);
		// } else {
		// if (getListAdapter().getCount() > 0) {
		// Log.d(TAG, "ID: " + extras.getLong(MySQLiteHelper.COLUMN_ID));
		// note = dataSource.getNote(extras
		// .getLong(MySQLiteHelper.COLUMN_ID));
		// Log.d(TAG, note.getTitle());
		// Log.d(TAG, "********");
		// dataSource.deleteNote(note);
		// Log.d(TAG, "********");
		// adapter.remove(note);
		// Log.d(TAG, "********");
		// }
		// }
		setListAdapter(adapter);

		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "on resume called");
		super.onResume();
		dataSource.open();
		updateNote();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "on pause called");
		super.onPause();
		// dataSource.close();
	}

}
