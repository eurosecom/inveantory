package com.eusecom.saminveantory;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class PrenosCSVActivity extends Activity implements OnClickListener {
    

	Button btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docsv);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
        
    }
    
    public void onClick(View v) {
		
    	/********** Pick file from sdcard *******/
    	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    	String fileName = "/eusecom/androideshop/inventura.csv";
    	//File f = new File(baseDir + File.separator + fileName);
    	
    	//Uri uri = Uri.parse(fileName);
        //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //startActivity(intent);
        
        File musicFile2Play = new File(baseDir + File.separator + fileName);
        Intent i2 = new Intent();
        i2.setAction(android.content.Intent.ACTION_VIEW);
        i2.setDataAndType(Uri.fromFile(musicFile2Play), "text/plain");
        startActivity(i2);
    	

		
	}
    
   
}