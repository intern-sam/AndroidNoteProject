package com.practice.note;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NoteTitleFragment extends ListFragment {
	OnListItemSelectedListener mCallback;
	private NotesDataSource dataSource;
	public static final String TAG = NoteTitleFragment.class.getName();

	public interface OnListItemSelectedListener {
		public void onListItemSelected(int position);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
				: android.R.layout.simple_list_item_1;
		dataSource = new NotesDataSource(getActivity());
		Log.d(TAG, "On create Called");
		// if (dataSource != null) {
		dataSource.open();
		List<Note> values = dataSource.getAllNotes();

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
		mCallback.onListItemSelected(position);
		getListView().setItemChecked(position, true);
	}

	public void onClick(Intent intent) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Note> adapter = (ArrayAdapter<Note>) getListAdapter();
		Note note = null;
		Log.d(TAG, "make it to onClick");
		// dataSource.open();
		if (intent.getBooleanExtra("done", true)) {
			Log.d(TAG, intent.getStringExtra("title"));
			note = dataSource.createNote(intent.getStringExtra("title"),
					intent.getStringExtra("content"));
			Log.d(TAG, "note set");
			adapter.add(note);
			Log.d(TAG, "note added");
		} else if (intent.getBooleanExtra("done", false)) {
			if (getListAdapter().getCount() > 0) {
				// note = (Note) getListAdapter().getItem(0);
				// dataSource.deleteNote(note);
			}
		}
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
		dataSource.close();
	}

}
