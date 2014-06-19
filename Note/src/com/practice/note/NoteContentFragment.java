package com.practice.note;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NoteContentFragment extends Fragment {
	final static String ARG_POSITION = "position";
	private int mCurrentPosition = -1;
	private EditText editTitle;
	private EditText editContent;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
		}
		view = inflater.inflate(R.layout.activity_new_note, container, false);
		editTitle = (EditText) view.findViewById(R.id.edit_title);
		editContent = (EditText) view.findViewById(R.id.edit_content);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		Bundle args = getArguments();
		if (args != null) {
			updateItemContentView(args.getInt(ARG_POSITION));
		} else if (mCurrentPosition != -1) {
			updateItemContentView(mCurrentPosition);
		}
	}

	public void updateItemContentView(int position) {
		// String titles[] = { "Title 1", "Title 2" };
		// String contentA[] = { "Test Content Message 1",
		// "Test Content Message 2" };
		// EditText title = (EditText) getActivity().findViewById(R.id.edit_title);
		// title.setText(titles[position]);
		EditText content = (EditText) getActivity().findViewById(
				R.id.edit_content);
		// content.setText(contentA[position]);
		// mCurrentPosition = position;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(ARG_POSITION, mCurrentPosition);
	}

	public void clear() {
		editTitle.setText("");
		editContent.setText("");
	}

}
