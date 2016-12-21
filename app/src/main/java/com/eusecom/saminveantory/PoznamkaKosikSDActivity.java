package com.eusecom.saminveantory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
 
public class PoznamkaKosikSDActivity extends Activity {
 
    EditText txtDesc;
    Button btnSave;
    Button btnZfaktur;
    Button btnZcispoz;
    TextView inputEdiServer;
    TextView inputEdiUser;
    EditText inputDesc;
    
    String pid;
    BufferedReader in;
 
    // Progress Dialog
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    String mno;
    String hod;
    String mnozs;
    int mnozi;
    String adresarxx="";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poznamka);       
 
        inputEdiServer = (TextView) findViewById(R.id.inputEdiServer);
        inputEdiServer.setText(SettingsActivity.getServerName(this));
        inputEdiUser = (TextView) findViewById(R.id.inputEdiUser);
        inputEdiUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + "99");
        
        String serverx = inputEdiServer.getText().toString();
    	String delims3 = "[/]+";
    	String[] serverxxx = serverx.split(delims3);
    	if (serverxxx.length < 2 ) {
    		adresarxx="androideshop";
    		}else{
    			adresarxx=serverxxx[1];
    		}

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        btnZfaktur = (Button) findViewById(R.id.btnZfaktur);
        btnZfaktur.setVisibility(View.GONE);
        btnZcispoz = (Button) findViewById(R.id.btnZcispoz);
        btnZcispoz.setVisibility(View.GONE);
        
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        
        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new SavePoznamku().execute();
            }
        });
 

        new GetPoznamku().execute();
        
    }
    //koniec oncreate
 
    /**
     * Background Async Task to Get complete product details
     * */
    class GetPoznamku extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2 = new ProgressDialog(PoznamkaKosikSDActivity.this);
            pDialog2.setMessage(getString(R.string.progallproducts));
            pDialog2.setIndeterminate(false);
            pDialog2.setCancelable(true);
            pDialog2.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

            // write on SD card file data in the text box
            try {
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarxx + "/kosik_memo.csv";
            	
            	File myFile = new File(baseDir + File.separator + fileName);

                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
            	
                while ((aDataRow = myReader.readLine()) != null) {

                	aBuffer += aDataRow + "\n";

                }

                inputDesc = (EditText) findViewById(R.id.inputDesc);                
                inputDesc.setText(aBuffer);
                myReader.close();
                

        
                
                } catch (Exception e) {
                    
                }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog2.dismiss();
        }
    }
    //koniec GetPoznamku
 
    /**
     * Background Async Task to  Save product Details
     * */
    class SavePoznamku extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PoznamkaKosikSDActivity.this);
            pDialog.setMessage(getString(R.string.progsavproduct));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
 
            // write on SD card file data in the text box
            try {

            	inputDesc = (EditText) findViewById(R.id.inputDesc);
            	
            	String baseDir2 = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName2 = "/eusecom/" + adresarxx + "/kosik_memo.csv";

            	File myFile2 = new File(baseDir2 + File.separator + fileName2);
            	
                myFile2.createNewFile();
                FileOutputStream fOut2 = new FileOutputStream(myFile2);
                OutputStreamWriter myOutWriter2 = 
                                        new OutputStreamWriter(fOut2);
                myOutWriter2.append(inputDesc.getText());
                myOutWriter2.close();
                fOut2.close();

        
                
                } catch (Exception e) {
                    
                }
            
            finish();
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
    //koniec SavePoznamku
 

}