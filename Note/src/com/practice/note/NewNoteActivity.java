package com.practice.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NewNoteActivity extends FragmentActivity {
	private EditText titleText = (EditText) findViewById(R.id.edit_title);
	private EditText contentText = (EditText) findViewById(R.id.edit_content);

	public final static String EXTRA_MESSAGE = "com.practice.note.NewNoteActivity";
	public final static String EXTRA_MESSAGE2 = "com.practice.note.NewNoteActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_note);

		// if (savedInstanceState == null) {
		// getSupportFragmentManager().beginTransaction()
		// .add(R.id.container, new PlaceholderFragment()).commit();
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
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
			View rootView = inflater.inflate(R.layout.fragment_new_note,
					container, false);
			return rootView;
		}
	}

	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		// titleText = (EditText) layout.findViewById(R.id.titleText);
		Intent intent = new Intent();
		String titleStr = titleText.getText().toString();
		intent.putExtra("title", titleStr);
		String contentStr = contentText.getText().toString();
		intent.putExtra("content", contentStr);
		setResult(RESULT_OK, intent);
	}
}
