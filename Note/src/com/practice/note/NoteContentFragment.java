package com.practice.note;

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
	private Note note;
	private NotesDataSource dataSource;
	private static final String TAG = NoteContentFragment.class.getName();
	private boolean isEdit;
	private boolean isPhone;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// here you are not checking the activty view, just using the activity context to check your
		// current view.
		if (this.getActivity() instanceof NewNoteActivity) {
			isPhone = true;
		}
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

			if (mRowId != null && mRowId > 0) {
				isEdit = true;
			}
			if (isPhone) {
				note = dataSource.getNote(mRowId);
			}
		}
		Log.d(TAG, "made it through ifs");

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

				// CHANGES BY ANNA: check for the existance of mRowId if you want to check for
				// new/edit
				if (isEdit) {
					dataSource.updateNote(mRowId, mTitleText.getText()
							.toString(), mContentText.getText().toString());

				} else {
					dataSource.createNote(mTitleText.getText().toString(),
							mContentText.getText().toString());
				}

				if (isPhone) {
					getActivity().finish();
				} else {
					NoteTitleFragment noteTitleFragment = (NoteTitleFragment) getFragmentManager()
							.findFragmentById(R.id.note_title_frag);
					noteTitleFragment.updateNote();
				}
			}
		});

		deleteBtn = (Button) view.findViewById(R.id.delete_btn);
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View argO) {
				dataSource.deleteNote(note);
				Log.d(TAG, "Does note get deleted");

				if (isPhone) {
					getActivity().finish();
				} else {
					NoteTitleFragment noteTitleFragment = (NoteTitleFragment) getFragmentManager()
							.findFragmentById(R.id.note_title_frag);
					noteTitleFragment.updateNote();
					clear();
				}

			}
		});
		if (isEdit) {
			deleteBtn.setVisibility(View.VISIBLE);
		} else {
			// Dont even display the delete button if in new mode
			deleteBtn.setVisibility(View.GONE);
		}

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

	public void clear() {
		// reset all values
		mTitleText.setText("");
		mContentText.setText("");
		deleteBtn.setVisibility(View.GONE);
		isEdit = false;

	}

	public void updateItemContentView(int position) {
		note = dataSource.getNote(position);
		mRowId = note.getId();

		if (mRowId != null && mRowId > 0) {
			mTitleText.setText(note.getTitle());
			mContentText.setText(note.getNoteContent());
			deleteBtn.setVisibility(View.VISIBLE);
			isEdit = true;
		} else {
			Log.d(TAG, "Clear called");
			clear();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(ARG_POSITION, mCurrentPosition);
	}
}
