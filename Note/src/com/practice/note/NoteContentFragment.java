package com.practice.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NoteContentFragment extends Fragment {
	private final static String ARG_POSITION = "position";
	private int mCurrentPosition = -1;
	private View view;
	private Button doneBtn;
	private Button deleteBtn;
	private EditText mTitleText;
	private EditText mContentText;
	private Long mRowId;
	private NotesDataSource dataSource;
	private Note note = null;
	private static final String TAG = NoteContentFragment.class.getName();
	private boolean isEdit;
	private boolean isPhone;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isPhone = getActivity().findViewById(R.id.note_frag) == null;
		dataSource = new NotesDataSource(getActivity());
		dataSource.open();
		Log.d(TAG, "onCreateView called. tablet");
		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
		}
		view = inflater.inflate(R.layout.fragment_note_content, container,
				false);
		mTitleText = (EditText) view.findViewById(R.id.edit_title);
		mContentText = (EditText) view.findViewById(R.id.edit_content);

		mRowId = null;
		// if (mTitleText == null && mContentText == null) {
		// isEdit = false;
		// } else {
		// isEdit = true;
		// }

		final Bundle extras = getActivity().getIntent().getExtras();
		Log.d(TAG, "Got extras");
		if (extras != null) {
			String title = extras.getString(MySQLiteHelper.COLUMN_TITLE);
			String content = extras.getString(MySQLiteHelper.COLUMN_CONTENT);
			mRowId = extras.getLong(MySQLiteHelper.COLUMN_ID);
			Log.d(TAG, "title: " + title);
			Log.d(TAG, "content: " + content);

			if (title != null) {
				mTitleText.setText(title);
				Log.d(TAG, "title set");
			}

			if (content != null) {
				mContentText.setText(content);
				Log.d(TAG, "Content set");
			}
		}
		Log.d(TAG, "made it through ifs");

		deleteBtn = (Button) view.findViewById(R.id.delete_btn);
		doneBtn = (Button) view.findViewById(R.id.done_btn);

		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View argO) {
				Bundle bundle = new Bundle();
				Log.d(TAG, "Made it to click");

				if (mTitleText.getText().toString().equals("")) {
					Log.d(TAG, "Title is empty");
					// make a toast
				}
				if (mContentText.getText().toString().equals("")) {
					// sure you want to save an empty note dialog
				}

				bundle.putString(MySQLiteHelper.COLUMN_TITLE, mTitleText
						.getText().toString());
				bundle.putString(MySQLiteHelper.COLUMN_CONTENT, mContentText
						.getText().toString());

				if (mRowId != null) {
					bundle.putLong(MySQLiteHelper.COLUMN_ID, mRowId);
					isEdit = true;
				} else {
					isEdit = false;
				}
				// Intent intent = new Intent();
				// intent.putExtras(bundle);
				// getActivity().finish();
				Log.d(TAG, "wtf is happening");
				if (!isEdit) {
					dataSource.createNote(mTitleText.getText().toString(),
							mContentText.getText().toString());
					Log.d(TAG, "Lets return to the main activity");
				} else {
					dataSource.updateNote(mRowId, mTitleText.getText()
							.toString(), mContentText.getText().toString());
				}
				if (!isPhone) {
					Log.d(TAG, "Should not eget here on tablet");
					getActivity().finish();
				} else {
					// refresh list
				}
			}
		});

		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View argO) {
				Bundle bundle = new Bundle();
				bundle.putString(MySQLiteHelper.COLUMN_TITLE, mTitleText
						.getText().toString());
				bundle.putString(MySQLiteHelper.COLUMN_CONTENT, mContentText
						.getText().toString());
				if (mRowId != null) {
					bundle.putLong(MySQLiteHelper.COLUMN_ID, mRowId);
				}

				bundle.putBoolean("done", false);

				Intent intent = new Intent();
				intent.putExtras(bundle);
				Log.d(TAG, "***Delete pressed");
				// Log.d(TAG, "ID: " + extras.getLong(MySQLiteHelper.COLUMN_ID));
				// note = dataSource.getNote(mRowId);
				// Log.d(TAG, note.getTitle());
				Log.d(TAG, "********");
				dataSource.deleteNote(mRowId);
				Log.d(TAG, "********");
				// adapter.remove(note);
				Log.d(TAG, "********");
				getActivity().finish();
			}
		});

		// if (savedInstanceState == null) {
		// getSupportFragmentManager().beginTransaction()
		// .add(R.id.container, new PlaceholderFragment()).commit();
		// }

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
		mTitleText.setText("");
		mContentText.setText("");
	}

}
