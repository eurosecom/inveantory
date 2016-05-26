package com.eusecom.saminveantory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class NaserverActivity extends Activity {
    

	 
	TextView inputAllServer;
	Button btn;
	String adresarxx="";
	
	private static final String TAG_PAGEX = "pagex";
	String pagex;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naserver);

    }
}