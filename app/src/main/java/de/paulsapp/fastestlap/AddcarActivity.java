package de.paulsapp.fastestlap;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddcarActivity extends Activity {

	public String akthersteller="";
	public boolean neuerhersteller=false;
	TextView neuerherstellertv;
	EditText neuerherstelleret;
	Spinner klassen;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcar);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Context context=getApplicationContext();
		
		
		neuerherstellertv=(TextView)findViewById(R.id.tvnewhersteller);
		neuerherstelleret=(EditText)findViewById(R.id.newherstellerac);
		
		
		
		Spinner hersteller=(Spinner)findViewById(R.id.spinnerherstellerac);
		
		
		SQLiteOpenHelper database = new DatabaseHelper(context.getApplicationContext());
		SQLiteDatabase connection = database.getWritableDatabase();
		Cursor cursor;
		
		cursor = connection.rawQuery("SELECT DISTINCT hersteller FROM auto", null);
		
		cursor.moveToFirst();
		
		List<String> list=new ArrayList<String>();
		while (cursor.isLast()==false) {
			list.add(cursor.getString(cursor.getColumnIndex("hersteller")));
			cursor.moveToNext();
		}
		list.add(cursor.getString(cursor.getColumnIndex("hersteller")));
		list.add("+ neuer Hersteller");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		hersteller.setAdapter(dataAdapter);
		
		
		hersteller.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				akthersteller=String.valueOf(arg0.getItemAtPosition(arg2));
				if (akthersteller.equals("+ neuer Hersteller")) {
					neuerhersteller=true;
					neuerherstellertv.setVisibility(1);
					neuerherstelleret.setVisibility(1);
				} else {
					neuerhersteller=false;
					neuerherstellertv.setVisibility(-1);
					neuerherstelleret.setVisibility(-1);
					
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
		
		
		
		
		klassen=(Spinner)findViewById(R.id.klasseac);
		
		
		List<String> list2=new ArrayList<String>();
			list2.add("E");
			list2.add("D");
			list2.add("C");
			list2.add("B");
			list2.add("A");
			list2.add("S");
			list2.add("R3");
			list2.add("R2");
			list2.add("R1");
			list2.add("X");
		
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		klassen.setAdapter(dataAdapter2);
		
		
		
		Button submit = (Button)findViewById(R.id.submitac);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			Context context2=getApplicationContext();
				SQLiteOpenHelper database = new DatabaseHelper(context2);
				SQLiteDatabase connection = database.getWritableDatabase();
				
				if (neuerhersteller) {
					akthersteller=neuerherstelleret.getText().toString();
				}
				EditText modell=(EditText)findViewById(R.id.modellnameac);
				String strmodell=modell.getText().toString();
				
				EditText leistung=(EditText)findViewById(R.id.leistungac);
				String strleistung=leistung.getText().toString();
				
				String strklasse=klassen.getSelectedItem().toString();
				
				
				Context context=getApplicationContext();
				connection.execSQL("INSERT INTO auto (hersteller,name,leistung,klasse) values ('"+akthersteller+"','"+strmodell+"',"+strleistung+",'"+strklasse+"')");
				connection.close();
				database.close();
				Toast.makeText(context, "Auto erfolgreich hinzugefügt", Toast.LENGTH_LONG).show();
				finish();
				
				
			}
		});
		
		
		
		
		
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.addcar, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
