package com.practice.note;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity implements
		NoteTitleFragment.OnListItemSelectedListener {
	public static final String TAG = MainActivity.class.getName();

	private NoteContentFragment noteContentFragment;
	private NoteTitleFragment noteTitleFragment = new NoteTitleFragment();
	public static final int ACTIVITY_CREATE = 0;
	public static final int ACTIVITY_EDIT = 1;

	private boolean isPhone;

	// @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("My Notes");
		isPhone = findViewById(R.id.note_frag) == null;
		if (isPhone) {
			// isPhone = false;
			if (savedInstanceState != null) {
				return;
			}
			// NoteTitleFragment noteTitleFragment = new NoteTitleFragment();

			noteTitleFragment.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, noteTitleFragment).commit();
		}

	}

	@Override
	public void onListItemSelected(int position) {
		// super.onListItemSelected(position);
		// noteContentFragment = (NoteContentFragment) getSupportFragmentManager()
		// .findFragmentById(R.id.note_content_frag);
		// if (isPhone) {
		// NoteContentFragment newContentFragment = new NoteContentFragment();
		// Bundle args = new Bundle();
		// args.putInt(NoteContentFragment.ARG_POSITION, position);
		// newContentFragment.setArguments(args);
		// FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();

		// transaction.replace(R.id.container, newContentFragment);
		// transaction.addToBackStack(null);
		// transaction.commit();
		// return;
		// }
		// // particular note selected
		// if (noteContentFragment != null) {
		// noteContentFragment.updateItemContentView(position);
		// } else {
		// // noteContentFragment = new NoteContentFragment();
		// // NoteContentFragment newContentFragment = new NoteContentFragment();
		// Bundle args = new Bundle();
		// args.putInt(NoteContentFragment.ARG_POSITION, position);
		// noteContentFragment.setArguments(args);
		// FragmentTransaction transaction = getSupportFragmentManager()
		// .beginTransaction();

		// transaction.replace(R.id.container, noteContentFragment);
		// transaction.addToBackStack(null);
		// transaction.commit();

		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		// return true;
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, menu);

		menu.add("Add New Note");

		MenuItem actionItem = menu.add("Action Button");
		actionItem.setTitle("Add New Note");

		// Items that show as actions should favor the "if room" setting, which will
		// prevent too many buttons from crowding the bar. Extra items will show in the
		// overflow area.
		actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		// Items that show as actions are strongly encouraged to use an icon.
		// These icons are shown without a text description, and therefore should
		// be sufficiently descriptive on their own.
		actionItem.setIcon(R.drawable.ic_action_new);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Intent intent = new Intent(this, NewNoteActivity.class);
		if (item.getTitle().equals("Add New Note")) {
			if (isPhone) {
				Log.d(TAG, "is phone, start clicked");
				startActivityForResult(intent, ACTIVITY_CREATE);
			} else {
				noteContentFragment.clear();
			}

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Note note;
		Log.d(TAG, "Returned to main");
		Log.d(TAG, "Request Code: ");
		switch (requestCode) {
		case ACTIVITY_CREATE:
			Log.d(TAG, "Request Code: ");
			noteTitleFragment.createNote(data);
			break;
		case ACTIVITY_EDIT:
			Log.d(TAG, "activity edit");
			noteTitleFragment.updateNote(data);
			break;
		}
		// isPhone = findViewById(R.id.note_frag) == null;
		// if (requestCode == 1 && data != null) {
		// if (resultCode == RESULT_OK) {
		// Log.d(TAG, "requestCode = 1, calling on click");
		// note = noteTitleFragment.updateTitleFragment(data);
		// Log.d(TAG, "back to main from on click");
		// Log.d(TAG, "set layout");
		// if (!isPhone) {
		//
		// }
		// }
		// }
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
