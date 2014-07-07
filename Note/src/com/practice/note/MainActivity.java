package com.practice.note;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
		setContentView(R.layout.activity_main);
		Log.d(TAG, "onCreate Called");

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("My Notes");
		isPhone = findViewById(R.id.note_frag) == null;
		if (isPhone) {
			if (savedInstanceState != null) {
				return;
			}

			noteTitleFragment.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, noteTitleFragment).commit();
		}

	}

	@Override
	public void onListItemSelected(int position) {
		if (!isPhone) {
			noteContentFragment = (NoteContentFragment) getSupportFragmentManager()
					.findFragmentById(R.id.note_content_frag);
			if (noteContentFragment != null) {
				noteContentFragment.updateItemContentView(position);
			} else {
				createNewNoteFragment();
			}
		} else {
			Log.d(TAG, "List Item Selected");
			// Intent intent = new Intent(this, NewNoteActivity.class);
			// startActivity(intent);
		}
	}

	private void createNewNoteFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		noteContentFragment = new NoteContentFragment();
		ft.replace(R.id.note_content_frag, noteContentFragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

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
				startActivity(intent);
			} else {
				if (noteContentFragment == null) {
					createNewNoteFragment();
				} else {
					noteContentFragment.clear();
				}
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

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// Note note;
	// switch (requestCode) {
	// case ACTIVITY_CREATE:
	// Log.d(TAG, "Request Code: ");
	// noteTitleFragment.createNote(data);
	// break;
	// case ACTIVITY_EDIT:
	// Log.d(TAG, "activity edit");
	// // noteTitleFragment.updateNote(data);
	// break;
	// }
	//
	// }

	@Override
	public void onResume() {
		Log.d(TAG, "Main Activity onResume()");
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
