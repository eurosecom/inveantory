package com.eusecom.saminveantory;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.os.StrictMode;

 
public class ZmazOrderSDActivity extends Activity {
 
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    TextView inputAllKosik;
    TextView obsahKosika;
    
    String pid="0", orderdaj, nostoredaj;
    int success=0;
    int allorder=0;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    String mno;
    String hod;
    String mnozs;
    int mnozi;
    String zmazid;
    String newdok;
    String podadresar;
    String stolx, dokladx, serverx, useridx, sendorderx, contentorder;
    
    private SQLiteDatabase db2=null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zmaz_tov);
        
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        orderdaj = extras.getString("orderdaj");
        allorder = extras.getInt("allorder");
        nostoredaj = extras.getString("nostoredaj");

        inputAllKosik = (TextView) findViewById(R.id.inputAllKosik);
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + "" + "/Stol/" + "1");
        
        dokladx = "";
        serverx = SettingsActivity.getServerName(this);
        stolx = "1";
        sendorderx = "1";
        useridx = "";
    	String delims3 = "[/]+";
    	String[] serverxxx = serverx.split(delims3);    	
    	podadresar=serverxxx[1];

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
        .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        String hlv1=getString(R.string.deleteorder);
        String mes1=getString(R.string.deleteordermes) + " " + orderdaj + " ?";
        if( allorder == 1 ){
        	hlv1=getString(R.string.deleteallorder);
            mes1=getString(R.string.deleteallordermes) + " ?";
        }
        
        //dialog
        new AlertDialog.Builder(this)
        .setTitle(hlv1)
        .setMessage(mes1)
        .setPositiveButton(getString(R.string.textyes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // continue with click
            	new ZmazOrderSD().execute();
            }
         })
        .setNegativeButton(getString(R.string.textno), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // do nothing
            	finish();
            }
         })
         .show();
        
 

        
    }
 //koniec oncreate

 
    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class ZmazOrderSD extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ZmazOrderSDActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {
 
        	try {
        		

        		
        		success=0;
     			if( allorder == 0 ){
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + podadresar + "/" + orderdaj;
            	Log.d("fileName", fileName);
            	File myFile = new File(baseDir + File.separator + fileName);
            	@SuppressWarnings("unused")
				boolean deleted = myFile.delete();
            	success=1;


     			} else {
     				
     				//remove all orders
     				String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        			String fileName = "/eusecom/" + podadresar;        			
					ArrayList<String> FilesInFolder = RemoveFiles(baseDir + File.separator + fileName);     			
        			Log.d("kosikx", FilesInFolder.toString());
        			success=1;
     			}
            	
        	} catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        	
        	
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            
            
 				if( success == 1 ){
            	db2=(new DatabaseOrders(ZmazOrderSDActivity.this)).getWritableDatabase();
        		String UpdateSql1 = "DELETE FROM orders WHERE orderx='" + nostoredaj + "' ";
        		if( allorder == 1 ){        			
        			UpdateSql1 = "DELETE FROM orders ";
        		}
        		Log.d("UpdateSql1", UpdateSql1);
     			db2.execSQL(UpdateSql1);
     			db2.close();
 				}


                String mes2=getString(R.string.nodeleteordermes) + " " + orderdaj + " ";
                if( allorder == 1 ){
                    mes2=getString(R.string.nodeleteallordermes) + " ";
                }
                if( success == 1 ){
                    mes2=getString(R.string.okdeleteordermes) + " " + orderdaj + " ";
                    if( allorder == 1 ){
                        mes2=getString(R.string.okdeleteallordermes) + " ";
                    }
                }

                
            new AlertDialog.Builder(ZmazOrderSDActivity.this)
            .setMessage(mes2)
            .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                    // continue with click
                	finish();
                }
             })
             .show();

 
        }
 
     }//endasynctask
    
    
    public ArrayList<String> RemoveFiles(String DirectoryPath) {
        ArrayList<String> MyFiles = new ArrayList<String>();
        File f = new File(DirectoryPath);

        String nazovx;
        String subnaz;
        
        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0)
            return null;
        else {
            for (int i=0; i<files.length; i++)
            {
            	nazovx = files[i].getName();
            	subnaz=nazovx.substring(0, 5);
            	//Log.d("subnaz", subnaz);
            	if( subnaz.equals("order")) { MyFiles.add(files[i].getName()); files[i].delete(); }
            }
        }

        return MyFiles;
    }
    //end public ArrayList<String> GetFiles
    
    
    
}