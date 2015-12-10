package de.paulsapp.fastestlap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;



public class MainActivity extends Activity {
	 private static Context context;

	Spinner spinner1;
	Spinner spinner2;
	Spinner spinner3;
	public static List<String> list2;
	public List<String> list;
	public List<String> list3;
	public List<String> list4;
	public int aktrennstrecke=0;
	public int aktrennstreckenvariation=0;
	public int aktauto=0;
	public String akthersteller="";
	public static SQLiteDatabase connection;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		context=getApplicationContext();
		
		dbinit();
		spinnerinit();
		
		refreshlistview();
		
		
		
		
		
		
		
	}
	protected void onResume(){
		super.onResume();
		refreshlistview();
		Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
		String act=spinner3.getSelectedItem().toString();
		spinner3.setAdapter(getspinnerdata("hersteller", "auto", "", 0));
		int pos=getspinnerdata("hersteller", "auto", "", 0).getPosition(act);
		spinner3.setSelection(pos);
		
	}

	public void spinnerinit() {
		
		//rennstrecke
		Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
		spinner1.setAdapter(getspinnerdata("name", "rennstrecke", "", 0));
		//rennstrecke ende
		
		//rennstrecken variation
		Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
		spinner2.setAdapter(getspinnerdata("name", "rennstreckenvariation", "", 0));
		//rennstrecken variation ende
		
		//hersteller 
		Spinner spinner3 = (Spinner)findViewById(R.id.spinner3);
		spinner3.setAdapter(getspinnerdata("hersteller", "auto", "", 0));
		
		//hersteller ende
		
		//auto modell
		Spinner spinner4 = (Spinner)findViewById(R.id.spinner4);
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
				
				refreshlistview();
				
				
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
				
				refreshlistview();
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
			
		
		});
	}


	
	public void setspinnerdata(ArrayAdapter<String> getspinnerdata,int spinnerzahl) {
		Spinner spinner=null;
		if (spinnerzahl==2) {
			spinner = (Spinner)findViewById(R.id.spinner2);
		}else if (spinnerzahl==4) {
			spinner = (Spinner)findViewById(R.id.spinner4);
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.newtimemenu:
	    	Intent newtime = new Intent(getApplicationContext(), NewtimeActivity.class);
	    	startActivityForResult(newtime, 0);
	      break;
	    case R.id.addcarmenu:
	    	Intent addcar = new Intent(getApplicationContext(), AddcarActivity.class);
	    	startActivityForResult(addcar, 0);
	      break;
	    }
	    return true;
	}
	
	public void dbinit(){
		SQLiteOpenHelper database = new DatabaseHelper(context.getApplicationContext());
		connection = database.getWritableDatabase();
		try {
			if (Dbinitalisieren.checkvorhanden(context)==false) {
				connection.close();
				database.close();
				Toast.makeText(context, "Neue DB", Toast.LENGTH_LONG).show();
				context.deleteDatabase(context.getResources().getString(R.string.dbname));
				try {
					Dbinitalisieren.einlesen(context);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				connection.close();
				database.close();
			}
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		} catch (NotFoundException e) {
		
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

		
		
		public void refreshlistview(){

			ArrayList<String> mylist = new ArrayList<String>();
			Cursor rzcursor=connection.rawQuery("SELECT * FROM rundenzeit WHERE auto_id="+aktauto+" AND rennstreckenvariation_id="+aktrennstreckenvariation+" ORDER BY zeit",null);
			
			rzcursor.moveToFirst();
			
			for (int i = 0; i < rzcursor.getCount(); i++) {
				mylist.add(convertmilli(rzcursor.getLong((rzcursor.getColumnIndex("zeit"))))+"   |   "+rzcursor.getString(rzcursor.getColumnIndex("notiz"))+"");
				rzcursor.moveToNext();
			}
			ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mylist);
			ListView fl_lv = (ListView) findViewById(R.id.fl_result_lv);
			fl_lv.setAdapter(adapter);
			
			
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
		
		public String convertmilli(long time){
			int min=0;
			int sek=0;
			int milli=0;
			String smin="";
			String ssek="";
			String smilli="";
			if (time/60000>=1) {
				min =(int) time / 60000;
			}
			
			sek=(int) (time-min*60000)/1000;
			milli=(int) (time-(min*60000+sek*1000));
			
			
			if (min<10) {
				smin="0"+min;
			}else {
				smin=String.valueOf(min);
			}
			
			if (sek<10) {
				ssek="0"+sek;
			}else {
				ssek=String.valueOf(sek);
			}
			if (milli<10) {
				smilli="00"+milli;
			}else if (milli<100) {
				smilli="0"+milli;
			} else {
				smilli=String.valueOf(milli);
			}
			
			
			String s=smin+":"+ssek+"."+smilli;
			return s;
		}
}
