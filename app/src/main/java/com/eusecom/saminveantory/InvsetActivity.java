package com.eusecom.saminveantory;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;




public class InvsetActivity extends Activity {
  
	private static final String TAG_PAGEX = "page";
	String pagex;
	Button btn;
	TextView inputAllServer;
	String adresarxx="";
	
	private ProgressDialog pDialog;
	private SQLiteDatabase db6=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.naserver);
        
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        pagex = extras.getString(TAG_PAGEX);
        
        btn = (Button) findViewById(R.id.btn);
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
        
    	//del store inv
        if( pagex.equals("0")) {
        	btn.setVisibility(View.GONE);
            this.setTitle(getResources().getString(R.string.delinventory));
            
            new DelInventory().execute();
            
            new AlertDialog.Builder(this)
            .setTitle(getString(R.string.delinventory))
            .setMessage(getString(R.string.delinventoryok))
            .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

					Intent i = new Intent(getApplicationContext(), InventuraSDnewActivity.class);
					startActivity(i);
                	finish();
                }
             })

             .show();
        }
        
        //set demo inv
        if( pagex.equals("1")) {
        	btn.setVisibility(View.GONE);
            this.setTitle(getResources().getString(R.string.demoresources));
            
            new LoadDemo().execute();
        }
        
        //del no store inv
        if( pagex.equals("2")) {
        	btn.setVisibility(View.GONE);
            this.setTitle(getResources().getString(R.string.delinventory_nostore));

            new DelInventoryNo().execute();
            
            new AlertDialog.Builder(this)
            .setTitle(getString(R.string.delinventory_nostore))
            .setMessage(getString(R.string.delinventoryok))
            .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                  
                	finish();
                }
             })

             .show();
        }
        
        //set no store inv to store
        if( pagex.equals("3")) {
        	btn.setVisibility(View.GONE);
            this.setTitle(getResources().getString(R.string.setnostoreinventory));
            
            new NostoreToStore().execute();
            
            new AlertDialog.Builder(this)
            .setTitle(getString(R.string.setnostore))
            .setMessage(getString(R.string.setnostoreinventory))
            .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                  
                	finish();
                }
             })

             .show();
        }
 
        

        
    }
    //koniec oncreate
    
    class NostoreToStore extends AsyncTask<String, String, String> {
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InvsetActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
        	

            try {

            	//Create Folder
            	File folder = new File(Environment.getExternalStorageDirectory().toString()+"/eusecom/" + adresarxx + "/inventura");
            	folder.mkdirs();
            	  
            	//read inv nostore
            	String baseDirn = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileNamen = "/eusecom/" + adresarxx + "/inventura_nostore.csv";
            	File myFilen = new File(baseDirn + File.separator + fileNamen);

            	FileInputStream fInn = new FileInputStream(myFilen);
            	BufferedReader myReadern = new BufferedReader(
                          new InputStreamReader(fInn));
                  String aDataRow = "";
                  String aBuffer = "";
              	
                  while ((aDataRow = myReadern.readLine()) != null) {
                      aBuffer += aDataRow + "\n";
                  
                  }

                  String kosikx = aBuffer;
                  myReadern.close();
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarxx + "/inventura/eanindex.csv";
            	File myFile = new File(baseDir + File.separator + fileName);
            	
            	if(myFile.exists()){
        			myFile.delete();
            	}
            	
            	if(!myFile.exists()){
        			myFile.createNewFile();
        			
        			FileOutputStream fOut = new FileOutputStream(myFile, true);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);                
                    
                    String datatxt = "9999999999999;0;0;0;0;0;0;0;0";
                    myOutWriter.append(datatxt);
                    myOutWriter.close();
                    fOut.close();
        		}
            	

			

            	String fileName1 = "/eusecom/" + adresarxx + "/inventura/productsean1.xml";
            	File myFile1 = new File(baseDir + File.separator + fileName1);
            	
            	if(myFile1.exists()){
        			myFile1.delete();
            	}
            	
            	if(!myFile1.exists()){
        			myFile1.createNewFile();
        			
        			FileOutputStream fOut1 = new FileOutputStream(myFile1, true);
                    OutputStreamWriter myOutWriter1 = new OutputStreamWriter(fOut1);                
                    
                    String datatxt1 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt1 += "<products>";
                    
                    
                    String delims = "[\n]+";
                	String delims2 = "[;]+";
                	
                	String[] riadokxxx = kosikx.split(delims);

                    for (int i = 0; i < riadokxxx.length; i++) {
                	String riadok1 =  riadokxxx[i];

                	String[] polozkyx = riadok1.split(delims2);
                	//String cplx =  polozkyx[0];
                	String idx =  polozkyx[1];
                	String namex =  polozkyx[2];
                	String pricex =  polozkyx[3];
                	String mnozx =  polozkyx[4];
                	String eanx =  polozkyx[5];
                	String merx =  "ks";
                	if( polozkyx.length > 6 ){ merx = polozkyx[6]; }


                    		datatxt1 += "<product>";
                    		datatxt1 += "<pid>" + idx + "</pid>";
                    		datatxt1 += "<name>" + namex + "</name>";
                    		datatxt1 += "<mer>" + merx + "</mer>";
                    		datatxt1 += "<price>" + pricex + "</price>";
                    		datatxt1 += "<zasoba>" + mnozx + "</zasoba>";
                    		datatxt1 += "<ean>" + eanx + "</ean>";
                    		datatxt1 += "</product>";
                    		
                    		//String xxx = polozkyx.length + "";
                    		//Log.d("length", xxx);
                    		//Log.d("datatxt1", datatxt1);		
                    }//end for
                    		
                    		
                    		
                    datatxt1 += "</products>";
                    

                    myOutWriter1.append(datatxt1);
                    myOutWriter1.close();
                    fOut1.close();
        		}
            	
            	String fileName2 = "/eusecom/" + adresarxx + "/inventura/productsean2.xml";
            	File myFile2 = new File(baseDir + File.separator + fileName2);
            	
            	if(myFile2.exists()){
        			myFile2.delete();
            	}
            	
            	if(!myFile2.exists()){
        			myFile2.createNewFile();
        			
        			FileOutputStream fOut2 = new FileOutputStream(myFile2, true);
                    OutputStreamWriter myOutWriter2 = new OutputStreamWriter(fOut2);                
                    
                    String datatxt2 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt2 += "<products></products>";

                    myOutWriter2.append(datatxt2);
                    myOutWriter2.close();
                    fOut2.close();
        		}
            	
            	String fileName3 = "/eusecom/" + adresarxx + "/inventura/productsean3.xml";
            	File myFile3 = new File(baseDir + File.separator + fileName3);
            	
            	if(myFile3.exists()){
        			myFile3.delete();
            	}
            	
            	if(!myFile3.exists()){
        			myFile3.createNewFile();
        			
        			FileOutputStream fOut3 = new FileOutputStream(myFile3, true);
                    OutputStreamWriter myOutWriter3 = new OutputStreamWriter(fOut3);                
                    
                    String datatxt3 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt3 += "<products></products>";

                    myOutWriter3.append(datatxt3);
                    myOutWriter3.close();
                    fOut3.close();
        		}
            	
            	String fileName4 = "/eusecom/" + adresarxx + "/inventura/productsean4.xml";
            	File myFile4 = new File(baseDir + File.separator + fileName4);
            	
            	if(myFile4.exists()){
        			myFile4.delete();
            	}
            	
            	if(!myFile4.exists()){
        			myFile4.createNewFile();
        			
        			FileOutputStream fOut4 = new FileOutputStream(myFile4, true);
                    OutputStreamWriter myOutWriter4 = new OutputStreamWriter(fOut4);                
                    
                    String datatxt4 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt4 += "<products></products>";

                    myOutWriter4.append(datatxt4);
                    myOutWriter4.close();
                    fOut4.close();
        		}
            	
            	String fileName5 = "/eusecom/" + adresarxx + "/inventura/productsean5.xml";
            	File myFile5 = new File(baseDir + File.separator + fileName5);
            	
            	if(myFile5.exists()){
        			myFile5.delete();
            	}
            	
            	if(!myFile5.exists()){
        			myFile5.createNewFile();
        			
        			FileOutputStream fOut5 = new FileOutputStream(myFile5, true);
                    OutputStreamWriter myOutWriter5 = new OutputStreamWriter(fOut5);                
                    
                    String datatxt5 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt5 += "<products></products>";

                    myOutWriter5.append(datatxt5);
                    myOutWriter5.close();
                    fOut5.close();
        		}
            	
            	String fileName6 = "/eusecom/" + adresarxx + "/inventura/productsean6.xml";
            	File myFile6 = new File(baseDir + File.separator + fileName6);
            	
            	if(myFile6.exists()){
        			myFile6.delete();
            	}
            	
            	if(!myFile6.exists()){
        			myFile6.createNewFile();
        			
        			FileOutputStream fOut6 = new FileOutputStream(myFile6, true);
                    OutputStreamWriter myOutWriter6 = new OutputStreamWriter(fOut6);                
                    
                    String datatxt6 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt6 += "<products></products>";

                    myOutWriter6.append(datatxt6);
                    myOutWriter6.close();
                    fOut6.close();
        		}
            	
            	String fileName7 = "/eusecom/" + adresarxx + "/inventura/productsean7.xml";
            	File myFile7 = new File(baseDir + File.separator + fileName7);
            	
            	if(myFile7.exists()){
        			myFile7.delete();
            	}
            	
            	if(!myFile7.exists()){
        			myFile7.createNewFile();
        			
        			FileOutputStream fOut7 = new FileOutputStream(myFile7, true);
                    OutputStreamWriter myOutWriter7 = new OutputStreamWriter(fOut7);                
                    
                    String datatxt7 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt7 += "<products></products>";

                    myOutWriter7.append(datatxt7);
                    myOutWriter7.close();
                    fOut7.close();
        		}
            	
            	String fileName8 = "/eusecom/" + adresarxx + "/inventura/productsean8.xml";
            	File myFile8 = new File(baseDir + File.separator + fileName8);
            	
            	if(myFile8.exists()){
        			myFile8.delete();
            	}
            	
            	if(!myFile8.exists()){
        			myFile8.createNewFile();
        			
        			FileOutputStream fOut8 = new FileOutputStream(myFile8, true);
                    OutputStreamWriter myOutWriter8 = new OutputStreamWriter(fOut8);                
                    
                    String datatxt8 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt8 += "<products></products>";

                    myOutWriter8.append(datatxt8);
                    myOutWriter8.close();
                    fOut8.close();
        		}
            	
            	String fileName9 = "/eusecom/" + adresarxx + "/inventura/productsean9.xml";
            	File myFile9 = new File(baseDir + File.separator + fileName9);
            	
            	if(myFile9.exists()){
        			myFile9.delete();
            	}
            	
            	if(!myFile9.exists()){
        			myFile9.createNewFile();
        			
        			FileOutputStream fOut9 = new FileOutputStream(myFile9, true);
                    OutputStreamWriter myOutWriter9 = new OutputStreamWriter(fOut9);                
                    
                    String datatxt9 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt9 += "<products></products>";

                    myOutWriter9.append(datatxt9);
                    myOutWriter9.close();
                    fOut9.close();
        		}
            	
            	Intent i = new Intent(getApplicationContext(), DemoinvActivity.class);
                startActivity(i);
                finish();
                
            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            
            return null;

        }
        
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    
                }
            });
 
        }
 
    }
    //koniec nostoretostore
    

    class LoadDemo extends AsyncTask<String, String, String> {
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InvsetActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
        	

            try {

            	//Create Folder
				File folder = new File(Environment.getExternalStorageDirectory().toString()+"/eusecom/" + adresarxx + "/inventura");
				folder.mkdirs();
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarxx + "/inventura/eanindex.csv";
            	File myFile = new File(baseDir + File.separator + fileName);
            	
            	if(myFile.exists()){
        			myFile.delete();
            	}
            	
            	if(!myFile.exists()){
        			myFile.createNewFile();
        			
        			FileOutputStream fOut = new FileOutputStream(myFile, true);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);                
                    
                    String datatxt = "9999999999999;0;0;0;0;0;0;0;0";
                    myOutWriter.append(datatxt);
                    myOutWriter.close();
                    fOut.close();
        		}
            	

			

            	String fileName1 = "/eusecom/" + adresarxx + "/inventura/productsean1.xml";
            	File myFile1 = new File(baseDir + File.separator + fileName1);
            	
            	if(myFile1.exists()){
        			myFile1.delete();
            	}
            	
            	if(!myFile1.exists()){
        			myFile1.createNewFile();
        			
        			FileOutputStream fOut1 = new FileOutputStream(myFile1, true);
                    OutputStreamWriter myOutWriter1 = new OutputStreamWriter(fOut1);                
                    
                    String datatxt1 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt1 += "<products>";
                    
                    		datatxt1 += "<product>";
                    		datatxt1 += "<pid>1012</pid>";
                    		datatxt1 += "<name>chips slovakia 75g</name>";
                    		datatxt1 += "<mer>ks</mer>";
                    		datatxt1 += "<price>0.20</price>";
                    		datatxt1 += "<zasoba>0</zasoba>";
                    		datatxt1 += "<ean>0000000000001</ean>";
                    		datatxt1 += "</product>";
                    		
                    		datatxt1 += "<product>";
                    		datatxt1 += "<pid>1004</pid>";
                    		datatxt1 += "<name>fresh orange 1l</name>";
                    		datatxt1 += "<mer>ks</mer>";
                    		datatxt1 += "<price>1.30</price>";
                    		datatxt1 += "<zasoba>5</zasoba>";
                    		datatxt1 += "<ean>0000000000022</ean>";
                    		datatxt1 += "</product>";
                    		
                    		datatxt1 += "<product>";
                    		datatxt1 += "<pid>972</pid>";
                    		datatxt1 += "<name>book android 4.0</name>";
                    		datatxt1 += "<mer>ks</mer>";
                    		datatxt1 += "<price>4.40</price>";
                    		datatxt1 += "<zasoba>10</zasoba>";
                    		datatxt1 += "<ean>9788025137826</ean>";
                    		datatxt1 += "</product>";
                    		
                    		datatxt1 += "<product>";
                    		datatxt1 += "<pid>1042</pid>";
                    		datatxt1 += "<name>paper forbes</name>";
                    		datatxt1 += "<mer>ks</mer>";
                    		datatxt1 += "<price>1.70</price>";
                    		datatxt1 += "<zasoba>0</zasoba>";
                    		datatxt1 += "<ean>9771338252003</ean>";
                    		datatxt1 += "</product>";
                    		
                    datatxt1 += "</products>";
                    

                    myOutWriter1.append(datatxt1);
                    myOutWriter1.close();
                    fOut1.close();
        		}
            	
            	String fileName2 = "/eusecom/" + adresarxx + "/inventura/productsean2.xml";
            	File myFile2 = new File(baseDir + File.separator + fileName2);
            	
            	if(myFile2.exists()){
        			myFile2.delete();
            	}
            	
            	if(!myFile2.exists()){
        			myFile2.createNewFile();
        			
        			FileOutputStream fOut2 = new FileOutputStream(myFile2, true);
                    OutputStreamWriter myOutWriter2 = new OutputStreamWriter(fOut2);                
                    
                    String datatxt2 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt2 += "<products></products>";

                    myOutWriter2.append(datatxt2);
                    myOutWriter2.close();
                    fOut2.close();
        		}
            	
            	String fileName3 = "/eusecom/" + adresarxx + "/inventura/productsean3.xml";
            	File myFile3 = new File(baseDir + File.separator + fileName3);
            	
            	if(myFile3.exists()){
        			myFile3.delete();
            	}
            	
            	if(!myFile3.exists()){
        			myFile3.createNewFile();
        			
        			FileOutputStream fOut3 = new FileOutputStream(myFile3, true);
                    OutputStreamWriter myOutWriter3 = new OutputStreamWriter(fOut3);                
                    
                    String datatxt3 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt3 += "<products></products>";

                    myOutWriter3.append(datatxt3);
                    myOutWriter3.close();
                    fOut3.close();
        		}
            	
            	String fileName4 = "/eusecom/" + adresarxx + "/inventura/productsean4.xml";
            	File myFile4 = new File(baseDir + File.separator + fileName4);
            	
            	if(myFile4.exists()){
        			myFile4.delete();
            	}
            	
            	if(!myFile4.exists()){
        			myFile4.createNewFile();
        			
        			FileOutputStream fOut4 = new FileOutputStream(myFile4, true);
                    OutputStreamWriter myOutWriter4 = new OutputStreamWriter(fOut4);                
                    
                    String datatxt4 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt4 += "<products></products>";

                    myOutWriter4.append(datatxt4);
                    myOutWriter4.close();
                    fOut4.close();
        		}
            	
            	String fileName5 = "/eusecom/" + adresarxx + "/inventura/productsean5.xml";
            	File myFile5 = new File(baseDir + File.separator + fileName5);
            	
            	if(myFile5.exists()){
        			myFile5.delete();
            	}
            	
            	if(!myFile5.exists()){
        			myFile5.createNewFile();
        			
        			FileOutputStream fOut5 = new FileOutputStream(myFile5, true);
                    OutputStreamWriter myOutWriter5 = new OutputStreamWriter(fOut5);                
                    
                    String datatxt5 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt5 += "<products></products>";

                    myOutWriter5.append(datatxt5);
                    myOutWriter5.close();
                    fOut5.close();
        		}
            	
            	String fileName6 = "/eusecom/" + adresarxx + "/inventura/productsean6.xml";
            	File myFile6 = new File(baseDir + File.separator + fileName6);
            	
            	if(myFile6.exists()){
        			myFile6.delete();
            	}
            	
            	if(!myFile6.exists()){
        			myFile6.createNewFile();
        			
        			FileOutputStream fOut6 = new FileOutputStream(myFile6, true);
                    OutputStreamWriter myOutWriter6 = new OutputStreamWriter(fOut6);                
                    
                    String datatxt6 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt6 += "<products></products>";

                    myOutWriter6.append(datatxt6);
                    myOutWriter6.close();
                    fOut6.close();
        		}
            	
            	String fileName7 = "/eusecom/" + adresarxx + "/inventura/productsean7.xml";
            	File myFile7 = new File(baseDir + File.separator + fileName7);
            	
            	if(myFile7.exists()){
        			myFile7.delete();
            	}
            	
            	if(!myFile7.exists()){
        			myFile7.createNewFile();
        			
        			FileOutputStream fOut7 = new FileOutputStream(myFile7, true);
                    OutputStreamWriter myOutWriter7 = new OutputStreamWriter(fOut7);                
                    
                    String datatxt7 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt7 += "<products></products>";

                    myOutWriter7.append(datatxt7);
                    myOutWriter7.close();
                    fOut7.close();
        		}
            	
            	String fileName8 = "/eusecom/" + adresarxx + "/inventura/productsean8.xml";
            	File myFile8 = new File(baseDir + File.separator + fileName8);
            	
            	if(myFile8.exists()){
        			myFile8.delete();
            	}
            	
            	if(!myFile8.exists()){
        			myFile8.createNewFile();
        			
        			FileOutputStream fOut8 = new FileOutputStream(myFile8, true);
                    OutputStreamWriter myOutWriter8 = new OutputStreamWriter(fOut8);                
                    
                    String datatxt8 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt8 += "<products></products>";

                    myOutWriter8.append(datatxt8);
                    myOutWriter8.close();
                    fOut8.close();
        		}
            	
            	String fileName9 = "/eusecom/" + adresarxx + "/inventura/productsean9.xml";
            	File myFile9 = new File(baseDir + File.separator + fileName9);
            	
            	if(myFile9.exists()){
        			myFile9.delete();
            	}
            	
            	if(!myFile9.exists()){
        			myFile9.createNewFile();
        			
        			FileOutputStream fOut9 = new FileOutputStream(myFile9, true);
                    OutputStreamWriter myOutWriter9 = new OutputStreamWriter(fOut9);                
                    
                    String datatxt9 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
                    datatxt9 += "<products></products>";

                    myOutWriter9.append(datatxt9);
                    myOutWriter9.close();
                    fOut9.close();
        		}
            	

                
            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            
            return null;

        }
        
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();

			Intent i = new Intent(getApplicationContext(), DemoinvActivity.class);
			startActivity(i);
			finish();
 
        }
 
    }
    //koniec loaddemo
    

    @SuppressLint("SimpleDateFormat")
	class DelInventory extends AsyncTask<String, String, String> {
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InvsetActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        protected String doInBackground(String... args) {
        	
            
            // rename file inventura.csv
            try {
            	
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarxx + "/inventura.csv";
            	File from = new File(baseDir + File.separator + fileName);
            	
			
    			Calendar c = Calendar.getInstance();   
    	        SimpleDateFormat df = new SimpleDateFormat("dd_MM_yyyy HH:mm:ss");
    	        String formattedDate = df.format(c.getTime());
    			String tonaz="inv" + formattedDate + ".csv";

            	String fileNameTo = "/eusecom/" + adresarxx + "/" + tonaz;
            	File to = new File(baseDir + File.separator + fileNameTo);

            	from.renameTo(to);
                

                
                
            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            
            return null;

        }
        
        
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    
                }
            });
 
        }
 
    }
    //koniec loadallproducts
    

    @SuppressLint("SimpleDateFormat")
	class DelInventoryNo extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InvsetActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	
            
            // rename file inventura.csv
            try {
            	
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarxx + "/inventura_nostore.csv";
            	File from = new File(baseDir + File.separator + fileName);
            	
			
    			Calendar c = Calendar.getInstance();   
    	        SimpleDateFormat df = new SimpleDateFormat("dd_MM_yyyy HH:mm:ss");
    	        String formattedDate = df.format(c.getTime());
    			String tonaz="inv" + formattedDate + "_nostore.csv";

            	String fileNameTo = "/eusecom/" + adresarxx + "/" + tonaz;
            	File to = new File(baseDir + File.separator + fileNameTo);

            	from.renameTo(to);
            	
            	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
             	Editor editor = prefs.edit();

             	editor.putString("nostnum", "1").apply();
             	
             	editor.commit();
                
             	db6=(new DatabaseOrders(InvsetActivity.this)).getWritableDatabase();
                ContentValues cv6=new ContentValues();
        		
        		cv6.put("bankx", 1);
        		cv6.put("orderx", formattedDate);
        		cv6.put("sendx", "0");

        		db6.insert("orders", "bankx", cv6);
        		db6.close();
                
                
            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            
            return null;

        }
        
        
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    
                }
            });
 
        }
 
    }
    //koniec delinvno


}
//koniec activity