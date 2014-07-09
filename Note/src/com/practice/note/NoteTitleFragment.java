package com.practice.note;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
	View someView = getView();
	private Note note;

	// private ActionMode mActionMode;

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

		registerForContextMenu(getListView());
	}

	@Override
	public void onStart() {
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
		note = dataSource.getNote(position);

		if (getActivity().findViewById(R.id.note_frag) == null) {
			Intent intent = new Intent(getActivity(), NewNoteActivity.class);
			intent.putExtra("POS", position);
			intent.putExtra(MySQLiteHelper.COLUMN_ID, note.getId());
			intent.putExtra(
					MySQLiteHelper.COLUMN_TITLE,
					mCursor.getString(mCursor
							.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_TITLE)));
			intent.putExtra(
					MySQLiteHelper.COLUMN_CONTENT,
					mCursor.getString(mCursor
							.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_CONTENT)));
			// note.updateItemContentView(position);
			getActivity().startActivity(intent);
			mCallback.onListItemSelected(position);
			getListView().setItemChecked(position, true);
		} else {
			mCallback.onListItemSelected(position);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		note = dataSource.getNote(info.position);
		menu.setHeaderTitle(note.getTitle());
		inflater.inflate(R.menu.item_long_press_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.edit:
			mCursor = dataSource.allNotes();
			mCursor.moveToPosition(info.position);
			note = dataSource.getNote(info.position);

			if (getActivity().findViewById(R.id.note_frag) == null) {
				Intent intent = new Intent(getActivity(), NewNoteActivity.class);
				intent.putExtra("POS", info.position);
				intent.putExtra(MySQLiteHelper.COLUMN_ID, note.getId());
				intent.putExtra(
						MySQLiteHelper.COLUMN_TITLE,
						mCursor.getString(mCursor
								.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_TITLE)));
				intent.putExtra(
						MySQLiteHelper.COLUMN_CONTENT,
						mCursor.getString(mCursor
								.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_CONTENT)));
				// note.updateItemContentView(position);
				getActivity().startActivity(intent);
				mCallback.onListItemSelected(info.position);
				getListView().setItemChecked(info.position, true);
			} else {
				mCallback.onListItemSelected(info.position);
			}
			return true;
		case R.id.delete:
			dataSource.deleteNote(note);
			updateNote();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void createNote(Intent intent) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Note> adapter = (ArrayAdapter<Note>) getListAdapter();
		note = null;
		Bundle extras = intent.getExtras();
		String tempTitle = extras.getString(MySQLiteHelper.COLUMN_TITLE);
		String tempContent = extras.getString(MySQLiteHelper.COLUMN_CONTENT);
		note = dataSource.createNote(tempTitle, tempContent);
		adapter.add(note);

		adapter.notifyDataSetChanged();
	}

	public void updateNote() {
		note = null;
		values = dataSource.getAllNotes();
		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(getActivity(),
				layout, values);
		setListAdapter(adapter);

		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		dataSource.open();
		updateNote();
	}

	@Override
	public void onPause() {
		super.onPause();
		// dataSource.close();
	}

}
