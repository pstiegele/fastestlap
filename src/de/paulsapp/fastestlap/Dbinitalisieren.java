package de.paulsapp.fastestlap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Dbinitalisieren {
	
public static boolean checkvorhanden(Context context) throws UnsupportedEncodingException, IOException{
	Cursor cr=MainActivity.connection.rawQuery("SELECT version FROM allgemein", null);
	cr.moveToFirst();
	int version;
	if (cr.getCount()!=0) {
		version=cr.getInt(cr.getColumnIndex("version"));
	} else {
		version=0;
	}
	
	
	
	int txtversion=0;
	
	BufferedReader readerversion = new BufferedReader(
            new InputStreamReader(context.getAssets().open("version.txt"), "Cp1252")); //Cp1252 steht für den verwendeten Zeichensatz, da Umlaute sonst nicht dargestellt werden
	txtversion=Integer.valueOf(readerversion.readLine());
	
	if (version<txtversion) {
		return false;
	} else {
		return true;
	}
	
	
}

public static void einlesen(Context context) throws IOException{
		//rennstrecken.txt einlesen
		//zeilen zählen
	
    BufferedReader readerz = new BufferedReader(
            new InputStreamReader(context.getAssets().open("rennstrecken.txt"), "Cp1252")); //Cp1252 steht für den verwendeten Zeichensatz, da Umlaute sonst nicht dargestellt werden
    String nextLine;
    int counter=-2;
    do {
		nextLine=readerz.readLine();
		counter++;
	} while (nextLine!=null);
		//ende Zeilen zählen
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(context.getAssets().open("rennstrecken.txt"), "Cp1252")); //Cp1252 steht für den verwendeten Zeichensatz, da Umlaute sonst nicht dargestellt werden
        reader.readLine();
        nextLine = reader.readLine();
        int i=0;
        	String rennstreckencache[] = nextLine.split(","); // –> splitten an den Zeichen..
        	String[][]rennstrecken=new String[counter][];
        	for (int j = 0; j < rennstrecken.length; j++) {
        		rennstrecken[j]=new String[rennstreckencache.length];
        		for (i = 0; i < rennstreckencache.length; i++) {
    				rennstrecken[j][i]=rennstreckencache[i];
    			}
        		nextLine=reader.readLine();
        		if (nextLine!=null) {
        			rennstreckencache = nextLine.split(",");
				}
			}
        	
        	//rennstrecken.txt einlesen fertig
        	i=0;
        	SQLiteOpenHelper database = new DatabaseHelper(context.getApplicationContext());
    		SQLiteDatabase connection = database.getWritableDatabase();
    		do {
    			
    			connection.execSQL("INSERT INTO rennstrecke (name) values ('"+rennstrecken[i][0]+"')");
    			for (int j = 0; j < rennstrecken[i].length-1; j++) {
    				connection.execSQL("INSERT INTO rennstreckenvariation (name,rennstrecke_id) values ('"+rennstrecken[(i)][(j+1)]+"',"+(i+1)+")");	
				}
    			i++;
			} while (i<rennstrecken.length);
    		
    		
    	//autos.txt einlesen	
    		
    	    	//autos zählen
    	    	BufferedReader reader2count = new BufferedReader(
        	            new InputStreamReader(context.getAssets().open("autos.txt"), "Cp1252")); //Cp1252 steht für den verwendeten Zeichensatz, da Umlaute sonst nicht dargestellt werden
    	    	counter=-1;
    	    	reader2count.readLine();
    	    	do {
					nextLine=reader2count.readLine();
					counter++;
				} while (nextLine!=null);
    	    	//autos zählen fertig
    	        BufferedReader reader2 = new BufferedReader(
    	            new InputStreamReader(context.getAssets().open("autos.txt"), "Cp1252")); //Cp1252 steht für den verwendeten Zeichensatz, da Umlaute sonst nicht dargestellt werden
    	        reader2.readLine();
    	        nextLine = reader2.readLine();
    	        i=0;
    	        String autoscache[] = nextLine.split(","); // –> splitten an den Zeichen..
            	String[][]autos=new String[counter][4];
            	for (int z = 0; z < autos.length; z++) {
            		for (i = 0; i < autoscache.length; i++) {
        				autos[z][i]=autoscache[i];
        			}
            		nextLine=reader2.readLine();
            		if (nextLine!=null) {
            			autoscache = nextLine.split(",");
    				}
    			}
    	        	//autos.txt einlesen fertig
    	        	i=0;
    	    		do {
    	    			connection.execSQL("INSERT INTO auto (hersteller,name,leistung,klasse) values ('"+autos[i][0]+"','"+autos[i][1]+"',"+autos[i][3]+",'"+autos[i][2]+"')");
    	    			i++;
    				} while (i<autos.length);
    	    		
    	    		
    	    		
    	    		//version
    	    		int txtversion=0;
    	    		BufferedReader readerversion = new BufferedReader(
    	    	            new InputStreamReader(context.getAssets().open("version.txt"), "Cp1252")); //Cp1252 steht für den verwendeten Zeichensatz, da Umlaute sonst nicht dargestellt werden
    	    		txtversion=Integer.valueOf(readerversion.readLine());
    	    	connection.execSQL("INSERT INTO allgemein (version) values ("+txtversion+")");
    	    		
    	    		
    		
    		connection.close();
    		database.close();
	}	
		
}

