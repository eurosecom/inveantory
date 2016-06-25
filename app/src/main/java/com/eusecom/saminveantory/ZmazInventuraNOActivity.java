package com.eusecom.saminveantory;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
 
public class ZmazInventuraNOActivity extends Activity {
 
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    TextView inputAllKosik;
    TextView obsahKosika;
    TextView inputAllnazserver;
    TextView inputHodbez;
    
    String pid;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
 
    // JSON Node names
    private static final String TAG_CAT = "cat";
    private static final String TAG_ODK = "odk";
    private static final String TAG_CATY = "caty";
    private static final String TAG_PAGEY = "pagey";
    
    String mno;
    String hod;
    String mnozs;
    int mnozi;
    String zmazid;
    String odkx;
    String adresarxx="";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zmaz_tov);
        
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        String zmazid = extras.getString(TAG_CAT);
        odkx = extras.getString(TAG_ODK);
 


        inputAllKosik = (TextView) findViewById(R.id.inputAllKosik);
        inputAllnazserver = (TextView) findViewById(R.id.inputAllnazserver);
        inputAllnazserver.setText(SettingsActivity.getServerName(this));
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText(zmazid);
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputHodbez = (TextView) findViewById(R.id.inputHodbez);
        
        String serverx = inputAllnazserver.getText().toString();
    	String delims3 = "[/]+";
    	String[] serverxxx = serverx.split(delims3);
    	if (serverxxx.length < 2 ) {
    		adresarxx="androideshop";
    		}else{
    			adresarxx=serverxxx[1];
    		}


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
        .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        

        new DeleteProduct().execute();
 

        
    }
 //koniec oncreate

 
    /*****************************************************************
     * Background Async Task to Delete Product
     * */
    class DeleteProduct extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ZmazInventuraNOActivity.this);
            pDialog.setMessage(getString(R.string.progdelproduct));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Deleting product
         * */
        protected String doInBackground(String... args) {
        	
            // write on SD card file data in the text box
            try {
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarxx + "/inventura_nostore.csv";
            	File myFile = new File(baseDir + File.separator + fileName);
            	
            	String fileDel = "/eusecom/" + adresarxx + "/invdifference.csv";
            	File delFile = new File(baseDir + File.separator + fileDel);            	
            	if(delFile.exists()){ delFile.delete(); }

                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                String testBuffer = "";
 
            	inputAll = (TextView) findViewById(R.id.inputAll);                
            	String pidzmaz = inputAll.getText().toString();
            	
                while ((aDataRow = myReader.readLine()) != null) {

                	testBuffer = aDataRow + "\n";
                	
                	String indexx = testBuffer;
                	String delims2 = "[;]+";
                	String[] riadokxxx = indexx.split(delims2);
                	String cplzmaz =  riadokxxx[0]; 
                	
                	if( cplzmaz.equals(pidzmaz)) { 
                	
                	}else{
                		aBuffer += aDataRow + "\n";
                	}
               

                }

            	inputAllKosik = (TextView) findViewById(R.id.inputAllKosik);                
            	inputAllKosik.setText(aBuffer);
                myReader.close();
                

                try {
                	String serverx2 = inputAllnazserver.getText().toString();
                	String delims2 = "[/]+";
                	String[] serverxxx2 = serverx2.split(delims2);
                	
                	String baseDir2 = Environment.getExternalStorageDirectory().getAbsolutePath();
                	String fileName2 = "/eusecom/" + serverxxx2[1] + "/inventura_nostore.csv";

                	File myFile2 = new File(baseDir2 + File.separator + fileName2);
                	
                    myFile2.createNewFile();
                    FileOutputStream fOut2 = new FileOutputStream(myFile2);
                    OutputStreamWriter myOutWriter2 = 
                                            new OutputStreamWriter(fOut2);
                    myOutWriter2.append(inputAllKosik.getText());
                    myOutWriter2.close();
                    fOut2.close();
                
                } catch (Exception e) {
                    
                }
                


                if( odkx.equals("0")) {
                Intent i = new Intent(getApplicationContext(), InventuraNOnewActivity.class);
                startActivity(i);
                }
                if( odkx.equals("1")) {
                	Intent i = new Intent(getApplicationContext(), PokladnicaActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(TAG_CATY, "1");
                    extras.putString(TAG_PAGEY, "1");
                    i.putExtras(extras);
                    startActivity(i);	
                }
                
                finish();
                
           
            
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
 
        }
 
    }
}