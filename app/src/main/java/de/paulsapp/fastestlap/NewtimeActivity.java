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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewtimeActivity extends Activity {

	
	public int aktrennstrecke=0;
	public int aktrennstreckenvariation=0;
	public int aktauto=0;
	public String akthersteller="";
	public Context context;
	public SQLiteDatabase connection;
	public EditText min;
	public EditText sek;
	public EditText milli;
	public EditText notiz;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newtime);
		// Show the Up button in the action bar.
		setupActionBar();
		
		context=getApplicationContext();
		spinnerinit();
		
		
		
		min = (EditText)findViewById(R.id.minutennt);
		sek = (EditText)findViewById(R.id.sekundennt);
		milli = (EditText)findViewById(R.id.millisekundennt);
		notiz = (EditText)findViewById(R.id.notiznt);
		
		
		min.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (min.getText().length()>=1) {
					sek.requestFocus();
				}
				return false;
			}
		});
		
		sek.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (sek.getText().length()==2) {
					milli.requestFocus();
				}
				return false;
			}
		});
		
		
		milli.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (milli.getText().length()==3) {
					notiz.requestFocus();
				}
				return false;
			}
		});
		
		
		
		
		Button submit = (Button)findViewById(R.id.submitrundenzeitnt);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean alleskorrekt=true;
				if (sek.getText().length()>=3||sek.getText().length()==0) {
					Toast.makeText(context, "Die angegebene Sekundenangabe ist fehlerhaft.", Toast.LENGTH_LONG).show();
					alleskorrekt=false;
				}
				if (milli.getText().length()>=4||milli.getText().length()==0) {
					Toast.makeText(context, "Die angegebene Millisekundenangabe ist fehlerhaft.", Toast.LENGTH_LONG).show();
					alleskorrekt=false;
				}
				
				if (alleskorrekt) {
				long rundenzeit=Integer.valueOf(String.valueOf(min.getText()))*60000+Integer.valueOf(String.valueOf(sek.getText()))*1000+Integer.valueOf(String.valueOf(milli.getText()));
				String notizstr=String.valueOf(notiz.getText());
				Toast.makeText(context, "Rundenzeit wurde erfolgreich eingetragen", Toast.LENGTH_LONG).show();				
				SQLiteOpenHelper database = new DatabaseHelper(context.getApplicationContext());
				connection = database.getWritableDatabase();
				connection.execSQL("INSERT INTO rundenzeit (auto_id,rennstreckenvariation_id,zeit,notiz) values ("+aktauto+","+aktrennstreckenvariation+","+rundenzeit+",'"+notizstr+"')");
				connection.close();
				database.close();
				finish();
			}
				}
		});
		
		
		min.requestFocus();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
	//	getMenuInflater().inflate(R.menu.newtime, menu);
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

public void spinnerinit() {
		
		//rennstrecke
		Spinner spinner1 = (Spinner)findViewById(R.id.rennstreckent);
		spinner1.setAdapter(getspinnerdata("name", "rennstrecke", "", 0));
		//rennstrecke ende
		
		//rennstrecken variation
		Spinner spinner2 = (Spinner)findViewById(R.id.rennstreckenvariationnt);
		spinner2.setAdapter(getspinnerdata("name", "rennstreckenvariation", "", 0));
		//rennstrecken variation ende
		
		//hersteller 
		Spinner spinner3 = (Spinner)findViewById(R.id.herstellernt);
		spinner3.setAdapter(getspinnerdata("hersteller", "auto", "", 0));
		
		//hersteller ende
		
		//auto modell
		Spinner spinner4 = (Spinner)findViewById(R.id.modellnt);
		spinner4.setAdapter(getspinnerdata("name", "auto", "", 0));
		
		//auto modell ende
		
		
		//addListener
		
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				setspinnerdata(getspinnerdata("name", "rennstreckenvariation", "rennstrecke_id", arg2), 2);
				aktrennstrecke=arg2+1;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
			
		
		});
		
		
		
		
		spinner3.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				setspinnerdata(getspinnerdatatext("name", "auto", "hersteller",String.valueOf(arg0.getItemAtPosition(arg2)) ), 4);
				akthersteller=String.valueOf(arg0.getItemAtPosition(arg2));
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
			
		
		});
		
		
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				
				getid(String.valueOf(arg0.getItemAtPosition(arg2)),0);   //0 für rennstreckenvariation, 1 für auto
				
				
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
			
		
		});
		
		spinner4.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				getid(String.valueOf(arg0.getItemAtPosition(arg2)),1);	//0 für rennstreckenvariation, 1 für auto
				
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
			
		
		});
	}

public void getid(String value, int was){	//0 für Rennstreckenvariation, 1 für Autos
			
			Cursor getidcursor;
			if (was==0) {
				getidcursor = connection.rawQuery("SELECT id FROM rennstreckenvariation WHERE name='"+value+"' AND rennstrecke_id='"+aktrennstrecke+"'", null);
			} else {
				getidcursor = connection.rawQuery("SELECT id FROM auto WHERE name='"+value+"' AND hersteller='"+akthersteller+"'", null);
			}
			
			getidcursor.moveToFirst();
			
			if (getidcursor!=null) {
				if (was==0) {
					aktrennstreckenvariation=getidcursor.getInt(getidcursor.getColumnIndex("id"));
				} else {
					aktauto=getidcursor.getInt(getidcursor.getColumnIndex("id"));
				}
			}
			

		}
	
	public void setspinnerdata(ArrayAdapter<String> getspinnerdata,int spinnerzahl) {
		Spinner spinner=null;
		if (spinnerzahl==2) {
			spinner = (Spinner)findViewById(R.id.rennstreckenvariationnt);
		}else if (spinnerzahl==4) {
			spinner = (Spinner)findViewById(R.id.modellnt);
		}
		
		spinner.setAdapter(getspinnerdata);
		
	}

	public ArrayAdapter<String> getspinnerdata(String was, String woraus, String ausserfeld,int ausserauf){
		
		SQLiteOpenHelper database = new DatabaseHelper(context.getApplicationContext());
		connection = database.getWritableDatabase();
		Cursor cursor;
		
		if (ausserfeld.equals("")) {
			if (was.equals("hersteller")) {
				cursor = connection.rawQuery("SELECT DISTINCT "+was+" FROM "+woraus+" ORDER BY hersteller", null);
			} else {
				cursor = connection.rawQuery("SELECT "+was+" FROM "+woraus+"", null);
			}
			
		}else {
			String str="SELECT "+was+" FROM "+woraus+" WHERE "+ausserfeld+" = "+String.valueOf(ausserauf+1)+";";
			cursor = connection.rawQuery(str, null);
		}
		
		cursor.moveToFirst();
		
		List<String> list=new ArrayList<String>();
		while (cursor.isLast()==false) {
			list.add(cursor.getString(cursor.getColumnIndex(was)));
			cursor.moveToNext();
		}
		list.add(cursor.getString(cursor.getColumnIndex(was)));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return dataAdapter;
	}
public ArrayAdapter<String> getspinnerdatatext(String was, String woraus, String ausserfeld,String ausserauf){
		
		SQLiteOpenHelper database = new DatabaseHelper(context.getApplicationContext());
		connection = database.getWritableDatabase();
		Cursor cursor;
		
		if (ausserfeld.equals("")) {
			cursor = connection.rawQuery("SELECT "+was+" FROM "+woraus+"", null);
		}else {
			String str="SELECT "+was+" FROM "+woraus+" WHERE "+ausserfeld+" like '"+ausserauf+"';";
			cursor = connection.rawQuery(str, null);
		}
		
		cursor.moveToFirst();
		
		List<String> list=new ArrayList<String>();
		while (cursor.isLast()==false) {
			list.add(cursor.getString(cursor.getColumnIndex(was)));
			cursor.moveToNext();
		}
		list.add(cursor.getString(cursor.getColumnIndex(was)));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return dataAdapter;
	}

	
	
	
}
