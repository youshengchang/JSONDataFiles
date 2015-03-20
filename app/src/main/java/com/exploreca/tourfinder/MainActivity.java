package com.exploreca.tourfinder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.exploreca.tourfinder.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
	
	public static final String LOGTAG="EXPLORECA";
	public static final String USERNAME="pref_username";
	public static final String VIEWIMAGE="pref_viewimages";
	
	private SharedPreferences settings;
	
	private OnSharedPreferenceChangeListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		settings = PreferenceManager.getDefaultSharedPreferences(this);
		
		listener = new OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				MainActivity.this.refreshDisplay(null);
			}
		};
		settings.registerOnSharedPreferenceChangeListener(listener);
		
		File f = getFilesDir();
		String path = f.getAbsolutePath();
		UIHelper.displayText(this, R.id.textView1, path);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void setPreference(View v) {
		Log.i(LOGTAG, "Clicked set");
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void refreshDisplay(View v) {
		Log.i(LOGTAG, "Clicked show");
		
		String prefValue = settings.getString(USERNAME, "Not found");
		UIHelper.displayText(this, R.id.textView1, prefValue);
	
	}
	
	public void createFile(View v) throws IOException, JSONException {

        JSONArray data = new JSONArray();

        JSONObject tour = new JSONObject();
        tour.put("tour", "Salton Sea");
        tour.put("price", 900);
        data.put(tour);

        tour = new JSONObject();
        tour.put("tour", "Death Vally");
        tour.put("price", 800);
        data.put(tour);

        tour = new JSONObject();
        tour.put("tour", "San Francisco");
        tour.put("price", 1200);
        data.put(tour);

		String text = data.toString();
		
		FileOutputStream fos = openFileOutput("tours", MODE_PRIVATE);
		fos.write(text.getBytes());
		fos.close();
		
		UIHelper.displayText(this, R.id.textView1, "File written to disk\n" + data.toString());
		
	}
	
	public void readFile(View v) throws IOException, JSONException {
		
		FileInputStream fis = openFileInput("tours");
		BufferedInputStream bis = new BufferedInputStream(fis);
		StringBuffer b = new StringBuffer();
		while (bis.available() != 0) {
			char c = (char) bis.read();
			b.append(c);
		}
		bis.close();
		fis.close();

        JSONArray data = new JSONArray(b.toString());
        StringBuffer tourBuffer = new StringBuffer();
        for(int i = 0; i < data.length(); i++){
            String tour = data.getJSONObject(i).getString("tour");
            tourBuffer.append(tour+"\n");
        }

        UIHelper.displayText(this, R.id.textView1, tourBuffer.toString());
	}
	
}
