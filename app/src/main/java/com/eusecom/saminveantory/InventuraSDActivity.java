package com.eusecom.saminveantory;
 
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
 
public class InventuraSDActivity extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    private ProgressDialog pDialog3;

    EditText inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    TextView obsahKosika;
    TextView popisEan;
    Button btnObjednaj, btnScan;
    //EditText inputNaz;
    EditText inputMno;
    EditText inputCis;
    EditText inputCed;
    EditText inputEan;
    EditText inputMer;
    
    TextView kosikMnoz;
    TextView kosikSdph;
    TextView kosikBdph;
    Button btnPoznamka;
    Button inputNaz;
    int iean;
    
    ArrayList<HashMap<String, String>> productsList;
    public Comparator<Map<String, String>> mapComparator;
    
    // JSON Node names
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";   
    private static final String TAG_MNOX = "mnox";
    private static final String TAG_CAT = "cat";
    private static final String TAG_CPL = "cpl";
    private static final String TAG_ODK = "odk";
    
    private static final String TAG_PIDX = "pidx";
    
    private static final String TAG_PRODUCT = "product";
    static final String NODE_PID = "pid";
    static final String NODE_NAME = "name";
    static final String NODE_PRICE = "price";
    static final String NODE_EAN = "ean";
    static final String NODE_MER = "mer";

    
    // products JSONArray
    JSONArray products = null;
    String pid;
    String ean;
    String nazean;
    String ipx="0";
    String cplx;
    String adresarxx="";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.inventura);
        
        
        inputAll = (EditText) findViewById(R.id.inputAll);
        inputAll.setText("0");
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this));
        
        String serverx = inputAllServer.getText().toString();
    	String delims3 = "[/]+";
    	String[] serverxxx = serverx.split(delims3);
    	if (serverxxx.length < 2 ) {
    		adresarxx="androideshop";
    		}else{
    			adresarxx=serverxxx[1];
    		}
 
        obsahKosika = (TextView) findViewById(R.id.obsahKosika);      
        kosikMnoz = (TextView) findViewById(R.id.kosikMnoz);
        kosikSdph = (TextView) findViewById(R.id.kosikSdph);
        kosikBdph = (TextView) findViewById(R.id.kosikBdph);

        inputNaz = (Button) findViewById(R.id.inputNaz);
        inputMno = (EditText) findViewById(R.id.inputMno);
        inputCis = (EditText) findViewById(R.id.inputCis);
        inputCed = (EditText) findViewById(R.id.inputCed);
        inputMer = (EditText) findViewById(R.id.inputMer);
        inputCed.setEnabled(false);
        inputCis.setEnabled(false);
        inputMer.setEnabled(false);
        //inputNaz.setEnabled(false);
        
        View b = findViewById(R.id.btnFakturuj);
        b.setVisibility(View.GONE);
        
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        
        // Loading products in Background Thread
        new LoadAllSDProducts().execute();
        
        // Get listview
        ListView lv = getListView();
        
        registerForContextMenu(getListView());
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

            }
        });
        
        btnScan = (Button) findViewById(R.id.btnScan);
        
        // scan button click event
        btnScan.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {

            	//Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                //intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                //startActivityForResult(intent, SCANNER_REQUEST_CODE);
            	
            	IntentIntegrator integrator = new IntentIntegrator(InventuraSDActivity.this);
            	integrator.initiateScan();
 

            }
        });
        
        btnObjednaj = (Button) findViewById(R.id.btnObjednaj);
        
        // save button click event
        btnObjednaj.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {

            	String ulozok="1";
            	String mnoz = inputMno.getText().toString();
            	String cis = inputCis.getText().toString();
            	
            	if( mnoz.equals("0")) { ulozok="0"; }
            	if( mnoz.equals("")) { ulozok="0"; }
            	if( mnoz.equals(" ")) { ulozok="0"; }
            	if( cis.equals("0")) { ulozok="0"; }
            	if( cis.equals("")) { ulozok="0"; }
            	
            	if( ulozok.equals("1")) { new SaveSDProductDetails().execute(); }
            	
            	
            }
        });
        
        btnPoznamka = (Button) findViewById(R.id.btnPoznamka);
        
        // poznamka button click event
        btnPoznamka.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {

            	Intent i = new Intent(getApplicationContext(), PoznamkaKosikSDActivity.class);
                startActivity(i);

            }
        });
        
        //nazov button
        inputNaz.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {

            	Intent ih = new Intent(getApplicationContext(), HladajActivity.class);
            	startActivityForResult(ih, 100);
            	
            }
        });
        
        inputMno = (EditText) findViewById(R.id.inputMno);
        inputMno.setOnFocusChangeListener(new OnFocusChangeListener() {          
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                	
                	iean=0;
                	String dajdetail="1";
                	inputEan = (EditText) findViewById(R.id.inputEan);
                	String ean = inputEan.getText().toString();
                	
                	if( ean.equals("0")) { dajdetail="0"; }
                	if( ean.equals("")) { dajdetail="0"; }
                	if( ean.equals(" ")) { dajdetail="0"; }
                	
                	if( dajdetail.equals("1")) { new GetSDProductDetails().execute(); }
                	
 
                	
                }
                 
            }
        });
        
        inputEan = (EditText) findViewById(R.id.inputEan);
        inputEan.setOnFocusChangeListener(new OnFocusChangeListener() {          
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                	inputCis = (EditText) findViewById(R.id.inputCis);
                	inputCis.setText("0");
                	inputMno = (EditText) findViewById(R.id.inputMno);
                	inputMno.setText("");
                	iean=0;
	
                }
                 
            }
        });
        
        //LEN NA HARDWAROVU KLAVESNICU
        inputEan = (EditText) findViewById(R.id.inputEan);
        inputEan.setOnKeyListener(new OnKeyListener() {           
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.KEYCODE_ENTER ) {
                    //do something here
                	inputMno = (EditText) findViewById(R.id.inputMno);
                	//inputMno.setText("9999");
                	inputMno.requestFocus();
                    return true;
                }

                return false;
            }
        });
        
        inputEan.addTextChangedListener(new TextWatcher() {
        	 
        	   public void afterTextChanged(Editable s) {
        		   
        		   inputMno = (EditText) findViewById(R.id.inputMno);
        		   iean=iean + 1;

        		   String ss = s.toString();
        		   
        		   iean=ss.length();

        		   	int position = iean;
       		   		String poss = position + "";
       		   		inputMno.setText(poss);
       		   		if( position == 13 ){ inputMno.requestFocus(); }
        	   }
        	 
        	   public void beforeTextChanged(CharSequence s, int start, 
        	     int count, int after) {
        	   }
        	 
        	   public void onTextChanged(CharSequence s, int start, 
        	     int before, int count) {
        	   }
        	  });
      

    }
    //koniec oncreate
    
    
    //LEN NA HARDWAROVU KLAVESNICU
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                /// call click event here by passing view
                //or u can write code which u want to be called onclick
            	inputMno = (EditText) findViewById(R.id.inputMno);
            	inputMno.requestFocus();
                return true;
                

            }
        }
        return super.onKeyDown(keyCode, event);
    }
    

    
    //oncontextmenu
    @Override 
    public void onCreateContextMenu(ContextMenu menu, View v,
    ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
    int position = info.position;
	 	
	 	String name2 = productsList.get(position).get(TAG_NAME);
 	
	 	String mnox3 = productsList.get(position).get(TAG_MNOX);

    menu.setHeaderTitle(mnox3 + " " + name2);// if your table name is name
    
    getMenuInflater().inflate(R.menu.kontext_inv, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
        case R.id.kontextzmazat:
            //String pidx = String.valueOf(info.id);
            int position = info.position;
            String cplx = productsList.get(position).get(TAG_CPL);
            
    // Starting new intent
    Intent in = new Intent(getApplicationContext(),ZmazInventuraSDActivity.class);
    Bundle extras = new Bundle();
    extras.putString(TAG_CAT, cplx);
    extras.putString(TAG_ODK, "0");
    in.putExtras(extras);

    // starting new activity and expecting some response back
    startActivity(in);
    finish();
    	break;
    	
    
        case R.id.kontextnic:
        break;

        }

        return super.onContextItemSelected(item);
    }
  
    
    //koniec oncontextmenu
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        

        	
        	IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        	if (scanResult != null) {
                String re = scanResult.getContents();
                inputEan.setText(re);
            }

        if (resultCode == 101) {

        	String pidxy = data.getStringExtra(TAG_PIDX); 
        	//String pidxy = "0000000000001";
        	
            inputEan = (EditText) findViewById(R.id.inputEan);
            inputEan.setText(pidxy);
            inputMno.requestFocus();

        }
 
    }
 

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllSDProducts extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InventuraSDActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            
            // write on SD card file data in the text box
            try {
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarxx + "/inventura.csv";
            	File myFile = new File(baseDir + File.separator + fileName);

                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                //do ip napocitam kolko riadkov mam
                int ip=0;
            	
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                    ip = ip+1;
                
                }
                
                //inputAll = (TextView) findViewById(R.id.inputAll);
                //inputAll.setText(aBuffer);

                String kosikx = aBuffer;
                myReader.close();

            	String delims = "[\n]+";
            	String delims2 = "[;]+";
            	
            	String[] riadokxxx = kosikx.split(delims);
            	ipx = ip + "";
            	ip=ip-10;
            	
                for (int i = 0; i < riadokxxx.length; i++) {
            	String riadok1 =  riadokxxx[i];

            	String[] polozkyx = riadok1.split(delims2);
            	cplx =  polozkyx[0];
            	String idx =  polozkyx[1];
            	String namex =  polozkyx[2];
            	String pricex =  polozkyx[3];
            	String mnozx =  polozkyx[4];
            	String eanx =  polozkyx[5];
            	
            	String mnozxx =  mnozx + " x";
            	
            	//pri velkom mnozstve poloziek to tu padalo musel som dat do postexecute
            	//inputAll = (EditText) findViewById(R.id.inputAll);
                //inputAll.setText(cplx);

            	//popisEan = (TextView) findViewById(R.id.popisEan);
            	//popisEan.setText(ipx + " x " + getResources().getString(R.string.popisean));
                
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_PID, idx);
                map.put(TAG_NAME, idx + " " + namex + " " + eanx + "/" + cplx);
                map.put(TAG_PRICE, pricex);
                map.put(TAG_MNOX, mnozxx);
                map.put(TAG_CPL, cplx);

                // adding HashList to ArrayList dam tam len poslednych 10 i >= ip
                if( i >= ip ) { productsList.add(map); }

                }
                
                mapComparator = new Comparator<Map<String, String>>() {
                    public int compare(Map<String, String> m1, Map<String, String> m2) {
                        return m1.get("cpl").compareTo(m2.get("cpl"));
                    }
                };
                
                Collections.sort(productsList, mapComparator);
                
                
            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            
            return null;

        }
        
        
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                	
                	
                    ListAdapter adapter = new SimpleAdapter(
                            InventuraSDActivity.this, productsList,
                            R.layout.list_item_inv, new String[] { TAG_PID, TAG_NAME, TAG_PRICE, TAG_MNOX, TAG_CPL},
                            new int[] { R.id.pid, R.id.name, R.id.price, R.id.mnozstvox, R.id.cplx });
                    


                    // updating listview
                    setListAdapter(adapter);
                    
                    //if( cplx.equals("")) { cplx="0"; }
                    
                    inputAll = (EditText) findViewById(R.id.inputAll);
                    inputAll.setText(cplx);
                    
                    if (cplx != null && !cplx.isEmpty()) {
                    	  // doSomething
                    	}else {
                        inputAll.setText("0");
                    	}
                    
                    popisEan = (TextView) findViewById(R.id.popisEan);
                	popisEan.setText(ipx + " x " + getResources().getString(R.string.popisean));
                    
                    //adapter.notifyDataSetChanged();
                    
                }
            });
 
        }
 
    }
    //koniec loadallproducts
    
    
    
    /**
     * Background Async Task to Get complete product details
     * */
    class GetSDProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2 = new ProgressDialog(InventuraSDActivity.this);
            pDialog2.setMessage(getString(R.string.progallproducts));
            pDialog2.setIndeterminate(false);
            pDialog2.setCancelable(true);
            pDialog2.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

        	
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    inputEan = (EditText) findViewById(R.id.inputEan);                 
                    ean = inputEan.getText().toString();
                    
                    inputNaz = (Button) findViewById(R.id.inputNaz);
                    inputMno = (EditText) findViewById(R.id.inputMno);
                    inputCis = (EditText) findViewById(R.id.inputCis);
                    inputCed = (EditText) findViewById(R.id.inputCed);
                    inputMer = (EditText) findViewById(R.id.inputMer);
                    
                    //inputNaz.setText("");
                    inputCis.setText("");
                    inputCed.setText("");
                    inputMer.setText("");
                    inputMno.setText("0");
                    
                    try {
                    	
                    	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                    	String fileName = "/eusecom/" + adresarxx + "/inventura/eanindex.csv";
                    	File myFile = new File(baseDir + File.separator + fileName);

                        FileInputStream fIn = new FileInputStream(myFile);
                        BufferedReader myReader = new BufferedReader(
                                new InputStreamReader(fIn));
                        String aDataRow = "";
                        String aBuffer = "";
                    	
                        while ((aDataRow = myReader.readLine()) != null) {
                            aBuffer += aDataRow + "\n";
                        
                        }

                        String indexx = aBuffer;
                        myReader.close();

                    	String delims7 = "[\n]+";
                    	String delims2 = "[;]+";
                    	
                    	String[] riadokxxx = indexx.split(delims7);
                    	
                    	long eani = Long.parseLong(ean);
                    	
                        for (int i = 0; i < riadokxxx.length; i++) {
                    	String riadok1 =  riadokxxx[i];

                    	String[] polozkyx = riadok1.split(delims2);
                    	String ean1 =  polozkyx[0]; long ean1i = Long.parseLong(ean1);
                    	String ean2 =  polozkyx[1]; long ean2i = Long.parseLong(ean2);
                    	String ean3 =  polozkyx[2]; long ean3i = Long.parseLong(ean3);
                    	String ean4 =  polozkyx[3]; long ean4i = Long.parseLong(ean4);
                    	String ean5 =  polozkyx[4]; long ean5i = Long.parseLong(ean5);
                    	String ean6 =  polozkyx[5]; long ean6i = Long.parseLong(ean6);
                    	String ean7 =  polozkyx[6]; long ean7i = Long.parseLong(ean7);
                    	String ean8 =  polozkyx[7]; long ean8i = Long.parseLong(ean8);
                    	//String ean9 =  polozkyx[8]; long ean9i = Long.parseLong(ean9);
                    	
                    	
                    	nazean = "productsean1";
                    	if( eani > ean1i ) { nazean = "productsean2"; }
                    	if( eani > ean1i && ean1i > 0 ) { nazean = "productsean2"; }
                    	if( eani > ean2i && ean2i > 0 ) { nazean = "productsean3"; }
                    	if( eani > ean3i && ean3i > 0 ) { nazean = "productsean4"; }
                    	if( eani > ean4i && ean4i > 0 ) { nazean = "productsean5"; }
                    	if( eani > ean5i && ean5i > 0 ) { nazean = "productsean6"; }
                    	if( eani > ean6i && ean6i > 0 ) { nazean = "productsean7"; }
                    	if( eani > ean7i && ean7i > 0 ) { nazean = "productsean8"; }
                    	if( eani > ean8i && ean8i > 0 ) { nazean = "productsean9"; }

                        }

                        
                    } catch (Exception e) {
                        
                    }
                    
                    XMLDOMParser parser = new XMLDOMParser();
                    try {
                    	
                    	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                    	String fileName = "";
                    	
                    	fileName = "/eusecom/" + adresarxx + "/inventura/" + nazean + ".xml";

                    	
                    	File myFile = new File(baseDir + File.separator + fileName);
                    	
                        FileInputStream finput = new FileInputStream(myFile);
                        String encoding = getInputEncoding(finput);
                        Log.d("Encoding: ", "> " + encoding);
                        
                        Document doc = parser.getDocument(new FileInputStream(myFile));
                        
                        // Get elements by name employee
                        NodeList nodeList = doc.getElementsByTagName(TAG_PRODUCT);
                        
                        /*
                         * for each <employee> element get text of name, salary and
                         * designation
                         */
                        // Here, we have only one <employee> element
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            Element e = (Element) nodeList.item(i);

                            String eanx = parser.getValue(e, NODE_EAN);
                            String idx = parser.getValue(e, NODE_PID);
                            String namex  = parser.getValue(e, NODE_NAME);
                            String merx  = parser.getValue(e, NODE_MER);
                            String pricex = parser.getValue(e, NODE_PRICE);

                            
                            			if( eanx.equals(ean)) {                             			
                                            inputNaz.setText(namex);
                                            inputCis.setText(idx);
                                            inputCed.setText(pricex);
                                            inputMer.setText(merx);
                                            inputMno.setText("");
                            			}
                            			
                            			
                        }
                       //koniec for
                        
 
                    //koniec try
                    } catch (Exception e) {
                       
                    }
                	//koniec catch
                    
                }//koniec run   
           });//koniec runable
            
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
    //koniec GetProductDetails
    
    /**
     * Background Async Task to  Save product Details
     * */
    class SaveSDProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog3 = new ProgressDialog(InventuraSDActivity.this);
            pDialog3.setMessage(getString(R.string.progsavproduct));
            pDialog3.setIndeterminate(false);
            pDialog3.setCancelable(true);
            pDialog3.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
 
            // getting updated data from EditTexts
            String name = inputNaz.getText().toString();
            String mnoz = inputMno.getText().toString();
            String pid = inputCis.getText().toString();
            String price = inputCed.getText().toString();
            String eanx = inputEan.getText().toString();
            String cplx = inputAll.getText().toString();
            String merx = inputMer.getText().toString();
    
            // write on SD card file data in the text box
            try {
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarxx + "/inventura.csv";
  
            	File myFile = new File(baseDir + File.separator + fileName);
            	
            	String fileDel = "/eusecom/" + adresarxx + "/invdifference.csv";
            	File delFile = new File(baseDir + File.separator + fileDel);            	
            	if(delFile.exists()){ delFile.delete(); }

            	//i get random number between 10 and 5000
                Random r = new Random();
                int i1=r.nextInt(15000-5000) + 5000;
                int i2 = i1;
                		
        		if(!myFile.exists()){
        			myFile.createNewFile();
        		}
        		
        		
        		if( cplx.equals("0")) {
        		}else{
        			i2 = Integer.parseInt(cplx);
                    i2=i2-1;
        		}

                //myFile.createNewFile();
                //to true znamena pridat append ked tam nie je prepise
        		FileOutputStream fOut = new FileOutputStream(myFile, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);                
                
                String datatxt = i2 + ";" + pid + ";" + name + ";" + price
                		+ ";" + mnoz + ";" + eanx + ";" + merx + "\n";
                myOutWriter.append(datatxt);
                myOutWriter.close();
                fOut.close();
                
                
            	
                Intent i = new Intent(getApplicationContext(), InventuraSDActivity.class);
                startActivity(i);
                    finish();
                
                
                //Toast.makeText(getBaseContext(),"Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }

 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog3.dismiss();
        }
    }
    //koniec saveproducts
    

    public String getInputEncoding(FileInputStream finput){
    	  String encoding = "";
    	  if(finput!=null){
    		  
    		  try{
    		  BufferedReader myReader = new BufferedReader(new InputStreamReader(finput));
              String getline = "";
              getline = myReader.readLine();
              myReader.close();
              Log.d("Line: ", "> " + getline);

          		String[] separated = getline.split("encoding=\"");
          		String encoding1 = separated[1];
          		String[] separated2 = encoding1.split("\"");
          		encoding = separated2[0];
          		
    	  	} catch (Exception e) {

    	  	}
   
    	  }
    	  return encoding;
    	}
    
    
}
//koniec activity