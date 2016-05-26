package com.eusecom.saminveantory;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eusecom.saminveantory.SimpleGestureFilter.SimpleGestureListener;
 
public class RozdielyActivity extends ListActivity implements SimpleGestureListener{
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    EditText inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    TextView obsahKosika;
    TextView mnozstvox;
    Button btnPrevt;
    Button btnNextt;
    int iean;
    
    TextView hladaj1;
    TextView hladaj2;
    TextView hladaj3;
    String hladajx="0";
    
    ArrayList<HashMap<String, String>> productsList;
    public Comparator<Map<String, String>> mapComparator;


    static final String NODE_CAT = "ckat";
    static final String NODE_PIDM = "pidm";
    
    // JSON Node names
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";   
    private static final String TAG_MNOX = "mnox";
    private static final String TAG_CPL = "cpl";
    private static final String TAG_EAN = "ean";
    
    static final String NODE_PID = "pid";
    static final String NODE_NAME = "name";
    static final String NODE_PRICE = "price";
    static final String NODE_EAN = "ean";
    static final String NODE_MER = "mer";
    static final String NODE_CPL = "cpl";
    static final String NODE_ZASOBA = "zasoba";

  
    private static final String TAG_CATY = "caty";
    private static final String TAG_PID = "pid";
    private static final String TAG_PAGEY = "pagey";

    
    // products JSONArray
    JSONArray products = null;
    String caty;
    String pidx;
    String pid;
    String ean;
    String nazean;
    String pagey;
    String spoluxi;
    String adresarxx="";
    
    private SimpleGestureFilter detector;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.pokladnica);
        
        detector = new SimpleGestureFilter(this,this);
        
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        caty = extras.getString(TAG_CATY);
        pagey = extras.getString(TAG_PAGEY);
        

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
 
        if( caty.equals("1")) {
            this.setTitle(getResources().getString(R.string.popisrozdean) + " " + getResources().getString(R.string.page) + " " + pagey);
        }
        if( caty.equals("2")) {
            this.setTitle(getResources().getString(R.string.popisrozdcis) + " " + getResources().getString(R.string.page) + " " + pagey);
        }
        if( caty.equals("3")) {
            this.setTitle(getResources().getString(R.string.popisrozdnat) + " " + getResources().getString(R.string.page) + " " + pagey);
        }
        if( caty.equals("4")) {
            this.setTitle(getResources().getString(R.string.popisrozdhod) + " " + getResources().getString(R.string.page) + " " + pagey);
        }
        
        obsahKosika = (TextView) findViewById(R.id.obsahKosika);
        
        btnNextt = (Button) findViewById(R.id.btnNextt);
        
        if( caty.equals("1")) { btnNextt.setText(getResources().getString(R.string.popiscis) + " >");}
        if( caty.equals("2")) { btnNextt.setText(getResources().getString(R.string.popiseditname) + " >");}
        if( caty.equals("3")) { btnNextt.setText(getResources().getString(R.string.popishod) + " >");}
        if( caty.equals("4")) { btnNextt.setText(getResources().getString(R.string.popisean) + " >");}
             
        // info button click event
        btnNextt.setOnClickListener(new View.OnClickListener() {
  
            @Override
            public void onClick(View arg0) {

         	   Intent slideactivity = new Intent(RozdielyActivity.this, RozdielyActivity.class);
         	   
         	   Bundle extras = new Bundle();
         	   if( caty.equals("1")) { extras.putString(TAG_CATY, "2"); }
         	   if( caty.equals("2")) { extras.putString(TAG_CATY, "3"); }
         	   if( caty.equals("3")) { extras.putString(TAG_CATY, "4"); }
         	   if( caty.equals("4")) { extras.putString(TAG_CATY, "1"); }
                extras.putString(TAG_PAGEY, "1");
                slideactivity.putExtras(extras);
         	   
 				Bundle bndlanimation =
 						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toleft,R.anim.animation_toleft2).toBundle();
 				startActivity(slideactivity, bndlanimation);
 				finish();
            }
        });
        
        btnPrevt = (Button) findViewById(R.id.btnPrevt);
        
        if( caty.equals("1")) { btnPrevt.setText("< " + getResources().getString(R.string.popishod));}
        if( caty.equals("2")) { btnPrevt.setText("< " + getResources().getString(R.string.popisean));}
        if( caty.equals("3")) { btnPrevt.setText("< " + getResources().getString(R.string.popiscis));}
        if( caty.equals("4")) { btnPrevt.setText("< " + getResources().getString(R.string.popiseditname));}
             
        // info button click event
        btnPrevt.setOnClickListener(new View.OnClickListener() {
  
            @Override
            public void onClick(View arg0) {

            	Intent slideactivity = new Intent(RozdielyActivity.this, RozdielyActivity.class);
          	   
          	   Bundle extras = new Bundle();
          	   if( caty.equals("1")) { extras.putString(TAG_CATY, "4"); }
          	   if( caty.equals("2")) { extras.putString(TAG_CATY, "1"); }
          	   if( caty.equals("3")) { extras.putString(TAG_CATY, "2"); }
          	   if( caty.equals("4")) { extras.putString(TAG_CATY, "3"); }
                 extras.putString(TAG_PAGEY, "1");
                 slideactivity.putExtras(extras);
          	   
  				Bundle bndlanimation =
  						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toright,R.anim.animation_toright2).toBundle();
  				startActivity(slideactivity, bndlanimation);
  				finish();
            }
        });
        

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    	String fileName = "/eusecom/" + adresarxx + "/invdifference.csv";
    	File myFile = new File(baseDir + File.separator + fileName);
    	
    	if(!myFile.exists()){
    		new VytvorRozdiely().execute();
    		Intent ir = new Intent(getApplicationContext(), RozdielyActivity.class);
            Bundle extrasr = new Bundle();
            extrasr.putString(TAG_CATY, caty);
            extrasr.putString(TAG_PAGEY, pagey);
            ir.putExtras(extras);
            startActivity(ir);
    		finish();
    
		}
        
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
        
        // Loading products in Background Thread
        new LoadRozdiely().execute();
        
        
       
    }
    //koniec oncreate
    
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class VytvorRozdiely extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RozdielyActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
        	
        	XMLDOMParser parser = new XMLDOMParser();
        	
            // rename file inventura.csv
            try {
            	
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileNameFrom = "/eusecom/" + adresarxx + "/inventura/productsean1.xml";
            	File from = new File(baseDir + File.separator + fileNameFrom);
 
            	String fileNameTo = "/eusecom/" + adresarxx + "/invdifference.csv";
            	File to = new File(baseDir + File.separator + fileNameTo);
            	
            	String fileName = "/eusecom/" + adresarxx + "/inventura.csv";
            	File invFile = new File(baseDir + File.separator + fileName);
            	
            	if(!to.exists()){
        			to.createNewFile();
        		}
            	FileOutputStream fOut = new FileOutputStream(to, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);                
                

            	Document doc = parser.getDocument(new FileInputStream(from));
                
                // Get elements by name employee
                NodeList nodeList = doc.getElementsByTagName(TAG_PRODUCT);
                
                DecimalFormat df = new DecimalFormat("0.00");
                Float rozdielfl=0.00f;
                Float zasobafl=0.00f;
                String rozdiels2;
                Float mnozefl=0.00f;
                
                for (int i = 0; i < nodeList.getLength(); i++) {
                	
                    Element e = (Element) nodeList.item(i);
                    String pidx = parser.getValue(e, NODE_PID);
                    String eanx = parser.getValue(e, NODE_EAN);
                    String namex = parser.getValue(e, NODE_NAME);
                    String pricex = parser.getValue(e, NODE_PRICE);
                    String zasobax = parser.getValue(e, NODE_ZASOBA);
                    
                    FileInputStream fIn = new FileInputStream(invFile);
                    InputStreamReader inpin = new InputStreamReader(fIn);
                    BufferedReader myReader = new BufferedReader(inpin);
                    String aDataRow = "";
                    String delims2 = "[;]+";

                    mnozefl=0.00f;
                    zasobafl = Float.parseFloat(zasobax);
                    rozdielfl=-zasobafl;
                    while ((aDataRow = myReader.readLine()) != null) {
                    	
                    	String[] polozkyx = aDataRow.split(delims2);
                    	//String cple =  polozkyx[0];
                    	String pide =  polozkyx[1];
                    	//String namee =  polozkyx[2];
                    	//String pricee =  polozkyx[3];
                    	String mnoze =  polozkyx[4];
                    	//String eane =  polozkyx[5];
                    	
                    	if( pidx.equals(pide)) {
                    	mnozefl = Float.parseFloat(mnoze);
                        rozdielfl = rozdielfl + mnozefl;
                    	}

                    }
                    myReader.close();

                    rozdiels2 = df.format(rozdielfl);
                    rozdiels2 = rozdiels2.replace(',','.');

                    String datatxt = "1;" + pidx + ";" + namex + ";" + pricex
                    		+ ";" + rozdiels2 + ";" + eanx + "\n";
                    myOutWriter.append(datatxt);
                    
                    

                }   
                
                myOutWriter.close();
                fOut.close();
                
                

 
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

                    
                }
            });
 
        }
 
    }
    //koniec VytvorRozdiely

 

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    @SuppressLint("DefaultLocale")
	class LoadRozdiely extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RozdielyActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        @SuppressLint("DefaultLocale")
		protected String doInBackground(String... args) {
        	

        		String akehl="1";
        		String hladaj1x = ""; String hladaj2x = ""; String hladaj3x = ""; 
        		if( hladajx.equals("1")) {
        			hladaj1 = (TextView) findViewById(R.id.hladaj1);
            		hladaj1x = hladaj1.getText().toString();
            		hladaj2 = (TextView) findViewById(R.id.hladaj2);
            		hladaj2x = hladaj2.getText().toString();
            		hladaj3 = (TextView) findViewById(R.id.hladaj3);
            		hladaj3x = hladaj3.getText().toString();
            		
            		if( hladaj2x.equals("")) { }else{ akehl="2";}
            		if( hladaj3x.equals("")) { }else{ akehl="3";}
        		}

            
            // write on SD card file data in the text box
            try {
            	
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarxx + "/invdifference.csv";
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
                

                String kosikx = aBuffer;
                myReader.close();

            	String delims = "[\n]+";
            	String delims2 = "[;]+";
            	
            	String[] riadokxxx = kosikx.split(delims);
            	
            	Float spolufl=0.00f;
                for (int i = 0; i < riadokxxx.length; i++) {
            	String riadok1 =  riadokxxx[i];
            	
            	String[] polozkyx = riadok1.split(delims2);
            	String cplx =  polozkyx[0];
            	String idx =  polozkyx[1];
            	String namex =  polozkyx[2];
            	String pricex =  polozkyx[3];
            	String mnozx =  polozkyx[4];
            	String eanx =  polozkyx[5];
            	
            	Float pricefl = Float.parseFloat(pricex);
            	Float mnozfl = Float.parseFloat(mnozx);
            	Float hodnotafl = pricefl * mnozfl;
            	spolufl=spolufl+hodnotafl;
            	
            	DecimalFormat df = new DecimalFormat("0.00");
            	
            	String hodnotas2 = df.format(hodnotafl);
            	hodnotas2 = hodnotas2.replace(',','.');
            	String spolufl2 = df.format(spolufl);
            	spolufl2 = spolufl2.replace(',','.');
            	
            	String mnozxx =  mnozx + " x";
            	String ipx = ip + "";
            	String spolux = spolufl2 + "";
            	
            	//String infx = "  i" + ipx + "/ " + spolux;
            	spoluxi = "  i" + ipx + "/ " + spolux;
  
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_PID, idx);
                map.put(TAG_EAN, eanx);
                map.put(TAG_NAME, namex + " /" + pricex + " " + eanx + "/" + idx);
                map.put(TAG_PRICE, hodnotas2);
                map.put(TAG_MNOX, mnozxx);
                map.put(TAG_CPL, cplx);


                // adding HashList to ArrayList dam tam len poslednych 10 i >= ip
                if( hladajx.equals("0")) { productsList.add(map); }
                if( hladajx.equals("2")) { productsList.add(map); }
                if( hladajx.equals("1")) {
                	if( akehl.equals("1")) {
                		if( idx.equals(hladaj1x)) { productsList.add(map); }
                		}
                	if( akehl.equals("2")) {
                    	if( eanx.equals(hladaj2x)) { productsList.add(map); }
                    	}
                	if( akehl.equals("3")) {
                		if(namex.toLowerCase().contains(hladaj3x.toLowerCase())) {
                			productsList.add(map);
                			}
                    	}
                }

                //koniec for
                }
                
                if( caty.equals("1")) {
                mapComparator = new Comparator<Map<String, String>>() {
                    public int compare(Map<String, String> m1, Map<String, String> m2) {
                        return m1.get("ean").compareTo(m2.get("ean"));
                    }
                };
                
                Collections.sort(productsList, mapComparator);
                }
                if( caty.equals("2")) {
                    mapComparator = new Comparator<Map<String, String>>() {
                        public int compare(Map<String, String> m1, Map<String, String> m2) {
                            return m1.get("pid").compareTo(m2.get("pid"));
                        }
                    };
                    
                 Collections.sort(productsList, mapComparator);
                 }
                if( caty.equals("3")) {
                    mapComparator = new Comparator<Map<String, String>>() {
                        public int compare(Map<String, String> m1, Map<String, String> m2) {
                            return m1.get("name").compareTo(m2.get("name"));
                        }
                    };
                    
                 Collections.sort(productsList, mapComparator);
                 }
                if( caty.equals("4")) {
                    mapComparator = new Comparator<Map<String, String>>() {
                        public int compare(Map<String, String> m1, Map<String, String> m2) {
                            return m1.get("price").compareTo(m2.get("price"));
                        }
                    };
                    
                 Collections.sort(productsList, mapComparator);
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
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                	
                	
                    ListAdapter adapter = new CustomAdapter(
                            RozdielyActivity.this, productsList,
                            R.layout.list_item_pokl, new String[] { TAG_PID, TAG_NAME, TAG_NAME, TAG_PRICE, TAG_MNOX, TAG_CPL},
                            new int[] { R.id.pid, R.id.ean, R.id.name, R.id.price, R.id.mnozstvox, R.id.cplx });
                    
                    // updating listview
                    setListAdapter(adapter);
                    
                    obsahKosika = (TextView) findViewById(R.id.obsahKosika);
                    obsahKosika.setText(spoluxi);
                    
                }
            });
 
        }
 
    }
    //koniec loadallproducts
    
    
    public class CustomAdapter extends SimpleAdapter {
        public CustomAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            // toto mi da poziciu polozky tuto to nepotrebujem
            //Object item = getItem(position);
            //TextView name = (TextView) v.findViewById(R.id.name);
            TextView mno = (TextView) v.findViewById(R.id.mnozstvox);
            
            String mnox = mno.getText().toString();
            String mnox1 = mnox.replace(" x","");
            Float mnofl = Float.parseFloat(mnox1);
            
            mno.setTextColor(Color.WHITE);
            if( mnofl < 0 ){mno.setTextColor(Color.RED);}
            if( mnofl > 0 ){mno.setTextColor(Color.GREEN);}

            //text.setText("xxxxxx");
            return v;
        }
    }
    
    
//simplegesture ovladanie
    
    @Override 
	  public boolean dispatchTouchEvent(MotionEvent me){ 
	    this.detector.onTouchEvent(me);
	   return super.dispatchTouchEvent(me); 
	  }

	  @Override
	  public void onSwipe(int direction) {
	   String str = "";
	   
	   switch (direction) {
	   
	   case SimpleGestureFilter.SWIPE_RIGHT : 
	
		   if( caty.equals("1")) { str = getString(R.string.popisrozdean); }
		   if( caty.equals("2")) { str = getString(R.string.popisrozdcis); }
		   if( caty.equals("3")) { str = getString(R.string.popisrozdnat); }
		   if( caty.equals("4")) { str = getString(R.string.popisrozdhod); }
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	   break;
	   case SimpleGestureFilter.SWIPE_LEFT :  

		   if( caty.equals("1")) { str = getString(R.string.popisrozdean); }
		   if( caty.equals("2")) { str = getString(R.string.popisrozdcis); }
		   if( caty.equals("3")) { str = getString(R.string.popisrozdnat); }
		   if( caty.equals("4")) { str = getString(R.string.popisrozdhod); }
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	   break;
	   case SimpleGestureFilter.SWIPE_DOWN :

	   break;
	   case SimpleGestureFilter.SWIPE_UP :

	   break;
	                                            
	   } 
	    
	    
	    switch (direction) {
		   
		   case SimpleGestureFilter.SWIPE_RIGHT :
			   
			   Intent slideactivityr = new Intent(RozdielyActivity.this, RozdielyActivity.class);
        	   
        	   Bundle extrasr = new Bundle();
        	   if( caty.equals("1")) { extrasr.putString(TAG_CATY, "4"); }
        	   if( caty.equals("4")) { extrasr.putString(TAG_CATY, "3"); }
        	   if( caty.equals("3")) { extrasr.putString(TAG_CATY, "2"); }
        	   if( caty.equals("2")) { extrasr.putString(TAG_CATY, "1"); }
               extrasr.putString(TAG_PAGEY, "1");
               slideactivityr.putExtras(extrasr);
        	   
               Bundle bndlanimation =
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toright,R.anim.animation_toright2).toBundle();
               startActivity(slideactivityr, bndlanimation);
               finish();


		   break;
		   case SimpleGestureFilter.SWIPE_LEFT :
              
			   Intent slideactivityl = new Intent(RozdielyActivity.this, RozdielyActivity.class);
        	   
        	   Bundle extras = new Bundle();
        	   if( caty.equals("1")) { extras.putString(TAG_CATY, "2"); }
        	   if( caty.equals("2")) { extras.putString(TAG_CATY, "3"); }
        	   if( caty.equals("3")) { extras.putString(TAG_CATY, "4"); }
        	   if( caty.equals("4")) { extras.putString(TAG_CATY, "1"); }
               extras.putString(TAG_PAGEY, "1");
               slideactivityl.putExtras(extras);
        	   
               Bundle bndlanimationl =
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toleft,R.anim.animation_toleft2).toBundle();
               startActivity(slideactivityl, bndlanimationl);
               finish();

		   break;
		  
		                                            
		   }
	  }

	  @Override
	  public void onDoubleTap() {
	     Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show(); 
	  }
	  
	  //koniec simplegesture ovladanie 

		//optionsmenu
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.options_pokl, menu);


			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			switch (item.getItemId()) {

			case R.id.kontextvsetky:
				hladaj1 = (TextView) findViewById(R.id.hladaj1);
				hladaj1.setText("");
				hladaj2 = (TextView) findViewById(R.id.hladaj2);
				hladaj2.setText("");
				hladaj3 = (TextView) findViewById(R.id.hladaj3);
				hladaj3.setText("");
				
				// Hashmap for ListView
				hladajx="2";
		        productsList = new ArrayList<HashMap<String, String>>();
				new LoadRozdiely().execute();
				
				return true;
			
			case R.id.kontexthladaj:

				hladajx="1";
	    		hladaj();
	    		return true;

			
			
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		//koniec optionsmenu
		
		@SuppressLint("InflateParams")
		private void hladaj() {
			LayoutInflater inflater=LayoutInflater.from(this);
			View addView=inflater.inflate(R.layout.add_edit, null);
			final DialogWrapper wrapper=new DialogWrapper(addView);
			
			new AlertDialog.Builder(this)
				.setTitle(R.string.konthladaj)
				.setView(addView)
				.setPositiveButton(R.string.textyes,
														new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
																int whichButton) {
						processHladaj(wrapper);
					}
				})
				.setNegativeButton(R.string.textno,
														new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
																int whichButton) {
						// ignore, just dismiss
						
					}
				})
				.show();
		}
		//koniec hladaj
		
		private void processHladaj(DialogWrapper wrapper) {
			
			hladaj1 = (TextView) findViewById(R.id.hladaj1);
			hladaj1.setText(wrapper.getTitle());
			String valuex = wrapper.getValue() + "";
			hladaj2 = (TextView) findViewById(R.id.hladaj2);
			hladaj2.setText(valuex);
			hladaj3 = (TextView) findViewById(R.id.hladaj3);
			hladaj3.setText(wrapper.getText());
		
			// Hashmap for ListView
	        productsList = new ArrayList<HashMap<String, String>>();
			new LoadRozdiely().execute();
		}
		//koniec processhladaj
		
		class DialogWrapper {
			EditText titleField=null;
			EditText valueField=null;
			EditText textField=null;
			View base=null;
			
			DialogWrapper(View base) {
				this.base=base;
				valueField=(EditText)base.findViewById(R.id.value);
			}
			
			String getTitle() {
				return(getTitleField().getText().toString());
			}
			
			float getValuefloat() {
				//return(new Float(getValueField().getText().toString()).floatValue());
				return(Float.valueOf(getValueField().getText().toString()));
			}
			
			String getValue() {
				return(getValueField().getText().toString());
			}
			
			String getText() {
				return(getTextField().getText().toString());
			}
			
			private EditText getTitleField() {
				if (titleField==null) {
					titleField=(EditText)base.findViewById(R.id.title);
				}
				
				return(titleField);
			}
			
			private EditText getValueField() {
				if (valueField==null) {
					valueField=(EditText)base.findViewById(R.id.value);
				}
				
				return(valueField);
			}
			
			private EditText getTextField() {
				if (textField==null) {
					textField=(EditText)base.findViewById(R.id.retazec);
				}
				
				return(textField);
			}
		}
		//koniec wrapper
		
		
		 /**
	     * Background Async Task to Load all product by making HTTP Request
	     * */
	    class VytvorRozdielyCopyInv extends AsyncTask<String, String, String> {
	    	
	        /**
	         * Before starting background thread Show Progress Dialog
	         * */
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(RozdielyActivity.this);
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
	            	String fileName = "/eusecom/" + adresarxx + "/inventura.csv";
	            	File from = new File(baseDir + File.separator + fileName);
	            	
	    			String tonaz="invdifference.csv";

	            	String fileNameTo = "/eusecom/" + adresarxx + "/" + tonaz;
	            	File to = new File(baseDir + File.separator + fileNameTo);

	            	//from.renameTo(to);
	                
	            	to.createNewFile();
	                FileInputStream fileInputStream = new FileInputStream(from);
					FileChannel src = fileInputStream.getChannel();
	                FileOutputStream fileOutputStream = new FileOutputStream(to);
					FileChannel dst = fileOutputStream.getChannel();
	                dst.transferFrom(src, 0, src.size());
	                src.close();
	                dst.close();
	                fileInputStream.close();
	                fileOutputStream.close();

	                
	                
	            } catch (IOException e) {
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

	                    
	                }
	            });
	 
	        }
	 
	    }
	    //koniec VytvorRozdielyCopyInv
		
    
    
}
//koniec activity