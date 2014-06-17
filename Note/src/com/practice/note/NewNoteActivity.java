package com.practice.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NewNoteActivity extends FragmentActivity {
	
	private static final String TAG = NewNoteActivity.class.getName();
	Button doneBtn;

	//public final static String EXTRA_MESSAGE = "com.practice.note.NewNoteActivity";
	//public final static String EXTRA_MESSAGE2 = "com.practice.note.NewNoteActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Made it to start");
		setContentView(R.layout.activity_new_note);
		
		doneBtn = (Button) findViewById(R.id.done_btn);
		doneBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View argO){
				Log.d(TAG, "Made it to click");
				
				EditText titleText = (EditText) findViewById(R.id.edit_title);
				EditText contentText = (EditText) findViewById(R.id.edit_content);
				if(titleText.getText().toString().equals("")){
					Log.d(TAG, "Title is empty");
					//make a toast
				}
				if (contentText.getText().toString().equals("")){
					//sure you want to save an empty note dialog
				}
				Intent intent = new Intent();
				Log.d(TAG, "Content not null making new intent");
				//String titleStr = titleText.getText().toString();
				//intent.putExtra("title", titleStr);
				//String contentStr = contentText.getText().toString();
				//intent.putExtra("content", contentStr);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

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

//	public void onClick(View view) {
//		//@SuppressWarnings("unchecked")
//		Log.d(TAG, "Made it to click");
//			
//		EditText titleText = (EditText) findViewById(R.id.edit_title);
//		EditText contentText = (EditText) findViewById(R.id.edit_content);
//		if(titleText.getText().toString().equals("")){
//			Log.d(TAG, "Title is empty");
//			//make a toast
//		}
//		if (contentText.getText().toString().equals("")){
//			//sure you want to save an empty note dialog
//		}
//		else{
//			Intent intent = new Intent();
//			if(view.getId() == R.id.done_btn){
//				Log.d(TAG, "Content not null making new intent");
//				//String titleStr = titleText.getText().toString();
//				//intent.putExtra("title", titleStr);
//				//String contentStr = contentText.getText().toString();
//				//intent.putExtra("content", contentStr);
//				setResult(RESULT_OK, intent);
//			}
//			else{
//				setResult(RESULT_CANCELED, intent);
//			}
//		}	
//	}
}
