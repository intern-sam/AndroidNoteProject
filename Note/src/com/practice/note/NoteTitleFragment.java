package com.practice.note;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NoteTitleFragment extends ListFragment {
	OnListItemSelectedListener mCallback;

	public interface OnListItemSelectedListener {
		public void onListItemSelected(int position);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
				: android.R.layout.simple_list_item_1;
		String tempItems[] = { "one", "two" };
		setListAdapter(new ArrayAdapter<String>(getActivity(), layout,
				tempItems));
	}

	@Override
	public void onStart() {
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

}
