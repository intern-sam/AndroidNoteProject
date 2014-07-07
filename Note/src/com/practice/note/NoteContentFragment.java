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
		// if (dataSource != null) {
		// updateItemContentView(0);
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

			if (mRowId != null && mRowId > 0) {
				isEdit = true;
			}
		}
		Log.d(TAG, "made it through ifs");

		doneBtn = (Button) view.findViewById(R.id.done_btn);

		doneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View argO) {
				Bundle bundle = new Bundle();
				// isPhone = getActivity().findViewById(R.id.note_frag) == null;
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
					bundle.putLong(MySQLiteHelper.COLUMN_ID, mRowId);
					dataSource.updateNote(mRowId, mTitleText.getText()
						.toString(), mContentText.getText().toString());

				} else {
					dataSource.createNote(mTitleText.getText().toString(),
						mContentText.getText().toString());
					Log.d(TAG, "Lets return to the main activity");
				}

				if (isPhone) {
					Log.d(TAG, "WHY");
					getActivity().finish();
				} else {
					NoteTitleFragment noteTitleFragment = (NoteTitleFragment) getFragmentManager()
						.findFragmentById(R.id.note_title_frag);
					Log.d(TAG, "here");
					noteTitleFragment.updateNote();
					Log.d(TAG, "here");
				}
			}
		});

		deleteBtn = (Button) view.findViewById(R.id.delete_btn);
		if (isEdit) {
			deleteBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View argO) {
					if (isEdit) {
						Bundle bundle = new Bundle();
						// TODO: You should not have to do this in multiple places. Java OO
						// principal
						// keep it DRY (do not repeeat yourself)
						// do it once at the top where you first get the view and keep it around

						// isPhone = getActivity().findViewById(R.id.note_frag) == null;
						bundle.putString(MySQLiteHelper.COLUMN_TITLE, mTitleText
							.getText().toString());
						bundle.putString(MySQLiteHelper.COLUMN_CONTENT, mContentText
							.getText().toString());
						if (mRowId != null) {
							bundle.putLong(MySQLiteHelper.COLUMN_ID, mRowId);
						}

						bundle.putBoolean("done", false);

						bundle.putLong(MySQLiteHelper.COLUMN_ID, mRowId);
						dataSource.deleteNote(mRowId);
					}

					// Intent intent = new Intent();
					// intent.putExtras(bundle);

					Log.d(TAG, "***Delete pressed");
					// Log.d(TAG, "ID: " + extras.getLong(MySQLiteHelper.COLUMN_ID));
					// note = dataSource.getNote(mRowId);
					// Log.d(TAG, note.getTitle());
					Log.d(TAG, "********");

					Log.d(TAG, "********");
					// adapter.remove(note);
					Log.d(TAG, "********");
					if (isPhone) {
						Log.d(TAG, "here");
						getActivity().finish();
					} else {
						NoteTitleFragment noteTitleFragment = (NoteTitleFragment) getFragmentManager()
							.findFragmentById(R.id.note_title_frag);
						noteTitleFragment.updateNote();
					}
				}
			});
			deleteBtn.setVisibility(View.VISIBLE);
		} else {
			// Dont even display the delete button if in new mode
			deleteBtn.setVisibility(View.GONE);
		}
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
		Note note = dataSource.getNote(position);
		EditText title = (EditText) getActivity().findViewById(R.id.edit_title);
		title.setText(note.getTitle());
		// title.setText(titles[position]);
		EditText content = (EditText) getActivity().findViewById(
			R.id.edit_content);
		content.setText(note.getNoteContent());

		// why is the rowId here the position and not the db record
		mRowId = Long.parseLong(String.valueOf(position));
		if (mRowId != null && mRowId > 0) {
			deleteBtn.setVisibility(View.VISIBLE);
			isEdit = true;
		} else {
			deleteBtn.setVisibility(View.GONE);
			isEdit = false;
		}

		// isPhone = getActivity().findViewById(R.id.note_frag) == null;
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
