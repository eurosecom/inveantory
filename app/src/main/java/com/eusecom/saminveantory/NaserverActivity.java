package com.eusecom.saminveantory;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class NaserverActivity extends Activity implements OnClickListener {
    
	
	/*********  work only for Dedicated IP ***********/
	//static final String FTP_HOST= "82.208.46.20";
	//final String FTP_HOST = SettingsActivity.getFtpserver(this).toString();
	
	/*********  FTP USERNAME ***********/
	//static final String FTP_USER = "ws453100";
	
	/*********  FTP PASSWORD ***********/
	//static final String FTP_PASS  ="genoWax";
	 
	TextView inputAllServer;
	Button btn;
	String adresarxx="";
	
	private static final String TAG_PAGEX = "pagex";
	String pagex;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naserver);
        
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        pagex = extras.getString(TAG_PAGEX);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        
        String serverx = inputAllServer.getText().toString();
    	String delims3 = "[/]+";
    	String[] serverxxx = serverx.split(delims3);
    	if (serverxxx.length < 2 ) {
    		adresarxx="androideshop";
    		}else{
    			adresarxx=serverxxx[1];
    		}
        
        //ak je pripojenie do internetu
        if (isOnline()) 
        { }else{
        	
        	new AlertDialog.Builder(this)
            .setTitle(getString(R.string.niejeinternet))
            .setMessage(getString(R.string.potrebujeteinternet))
            .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                  
                	finish();
                }
             })

             .show();
        	
        }
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    	StrictMode.setThreadPolicy(policy);
        
    }
    
    public void onClick(View v) {
    	
		
    	/********** Pick file from sdcard *******/
    	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    	String fileName = "/eusecom/" + adresarxx + "/inventura.csv";
    	if( pagex.equals("1")) { fileName = "/eusecom/" + adresarxx + "/inventura_nostore.csv"; }
    	File f = new File(baseDir + File.separator + fileName);
		//File f = new File("/sdcard/logo.png");
		
		// Upload sdcard file
		uploadFile(f);
		
	}
    
    public void uploadFile(File fileName){
    	
    	
		 FTPClient client = new FTPClient();
		 
		try {
			
			String FTP_HOST = SettingsActivity.getFtpserver(this).toString();
			String FTP_USER = SettingsActivity.getFtpuser(this).toString();
			String FTP_PASS = SettingsActivity.getFtppsw(this).toString();
			
			client.connect(FTP_HOST,21);
			client.login(FTP_USER, FTP_PASS);
			client.setType(FTPClient.TYPE_BINARY);
			client.changeDirectory("/www_root/tmp/");
			
			String from="inventura.csv";
			if( pagex.equals("1")) { from="inventura_nostore.csv"; }
			Calendar c = Calendar.getInstance();   
	        SimpleDateFormat df = new SimpleDateFormat("dd_MM_yyyy hh:mm:ss");
	        String formattedDate = df.format(c.getTime());
			String to="inv" + formattedDate + ".csv";
			if( pagex.equals("1")) { to="inv" + formattedDate + "_nostore.csv"; }
			
			client.upload(fileName, new MyTransferListener());
			client.rename(from, to);
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				client.disconnect(true);	
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
    }
    
    
    //test ci je internet pripojeny
    public boolean isOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
    //koniec test ci je internet pripojeny
    
    /*******  Used to file upload and show progress  **********/
    
    public class MyTransferListener implements FTPDataTransferListener {

    	public void started() {
    		
    		btn.setVisibility(View.GONE);
    		// Transfer started
    		Toast.makeText(getBaseContext(), getResources().getString(R.string.prenosstart), Toast.LENGTH_SHORT).show();
    		//System.out.println(" Upload Started ...");
    	}

    	public void transferred(int length) {
    		
    		// Yet other length bytes has been transferred since the last time this
    		// method was called
    		Toast.makeText(getBaseContext(), getResources().getString(R.string.prenesene) + length, Toast.LENGTH_SHORT).show();
    		//System.out.println(" transferred ..." + length);
    	}

    	public void completed() {
    		
    		btn.setVisibility(View.VISIBLE);
    		// Transfer completed
    		
    		Toast.makeText(getBaseContext(), getResources().getString(R.string.prenoskomplet), Toast.LENGTH_SHORT).show();
    		//System.out.println(" completed ..." );
    	}

    	public void aborted() {
    		
    		btn.setVisibility(View.VISIBLE);
    		// Transfer aborted
    		Toast.makeText(getBaseContext(), getResources().getString(R.string.prenosabort), Toast.LENGTH_SHORT).show();
    		//System.out.println(" aborted ..." );
    	}

    	public void failed() {
    		
    		btn.setVisibility(View.VISIBLE);
    		// Transfer failed
    		System.out.println(getResources().getString(R.string.prenosfail));
    	}

    }
}