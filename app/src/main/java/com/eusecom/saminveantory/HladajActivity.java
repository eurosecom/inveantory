package com.eusecom.saminveantory;
 
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;



 
public class HladajActivity extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    
    ArrayList<HashMap<String, String>> productsList;
 
    
    // JSON Node names
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    
    private static final String TAG_PIDX = "pidx";


    // XML node names
    //static final String NODE_CAT = "category";
    //static final String NODE_CKAT = "ckat";
    //static final String NODE_NKAT = "nkat";
    //static final String NODE_PKAT = "pkat";
    
    static final String NODE_PRODUCT = "product";
    static final String NODE_EAN = "ean";
    static final String NODE_NAME = "name";
    static final String NODE_PID = "pid";
    
    // products JSONArray
    JSONArray products = null;
    
    TextView hladaj1;
    TextView hladaj2;
    TextView hladaj3;
    String hladajx="0";
    String adresarxx="";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.all_products);
       
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);

        String serverx = inputAllServer.getText().toString();
    	String delims3 = "[/]+";
    	String[] serverxxx = serverx.split(delims3);
    	if (serverxxx.length < 2 ) {
    		adresarxx="androideshop";
    		}else{
    			adresarxx=serverxxx[1];
    		}

        
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllProducts().execute();
        
        // Get listview
        ListView lv = getListView();
 
     // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
			@Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
				// TODO Auto-generated method stub
				
				String pidxy = ((TextView) view.findViewById(R.id.pid)).getText().toString();
				Intent i = new Intent();
				i.putExtra(TAG_PIDX, pidxy);
				setResult(101, i);
				finish();
				
			}
        });
 

        

    }
 //koniec oncreate
 

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HladajActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
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
  
            XMLDOMParser parser = new XMLDOMParser();
            try {
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarxx + "/inventura/productsean1.xml";
            	//File myFile = new File("/mnt/sdcard/categories.xml");
            	File myFile = new File(baseDir + File.separator + fileName);
                
                Document doc = parser.getDocument(new FileInputStream(myFile));
                
                // Get elements by name employee
                NodeList nodeList = doc.getElementsByTagName(NODE_PRODUCT);
     
                /*
                 * for each <employee> element get text of name, salary and
                 * designation
                 */
                // Here, we have only one <employee> element
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element e = (Element) nodeList.item(i);
                    String id = parser.getValue(e, NODE_EAN);
                    String name = parser.getValue(e, NODE_NAME);
                    String price = parser.getValue(e, NODE_PID);
                    
                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_PID, id);
                    map.put(TAG_NAME, name);
                    map.put(TAG_PRICE, price);
                    
                    if( hladajx.equals("0")) { productsList.add(map); }
                    if( hladajx.equals("2")) { productsList.add(map); }
                    if( hladajx.equals("1")) {
                    	if( akehl.equals("1")) {
                    		if( price.equals(hladaj1x)) { productsList.add(map); }
                    		}
                    	if( akehl.equals("2")) {
                        	if( id.equals(hladaj2x)) { productsList.add(map); }
                        	}
                    	if( akehl.equals("3")) {
                    		if(name.toLowerCase().contains(hladaj3x.toLowerCase())) {
                    			productsList.add(map);
                    			}
                        	}
                    }
                    
                //koniec for    
                }
             
                HashMap<String, String> map = new HashMap<String, String>();
                
                // adding HashList to ArrayList
                productsList.add(map);
                
               
            } catch (Exception e) {
               
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
                            HladajActivity.this, productsList,
                            R.layout.list_item_categ, new String[] { TAG_PID, TAG_NAME, TAG_PRICE},
                            new int[] { R.id.pid, R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
    //koniec loadall
    
  //optionsmenu
  		@Override
  		public boolean onCreateOptionsMenu(Menu menu) {
  			MenuInflater inflater = getMenuInflater();

  		inflater.inflate(R.menu.options_hladaj, menu);


  			return true;
  		}

  		@Override
  		public boolean onOptionsItemSelected(MenuItem item) {

  			switch (item.getItemId()) {

  			case R.id.hkontextvsetky:
  				hladaj1 = (TextView) findViewById(R.id.hladaj1);
  				hladaj1.setText("");
  				hladaj2 = (TextView) findViewById(R.id.hladaj2);
  				hladaj2.setText("");
  				hladaj3 = (TextView) findViewById(R.id.hladaj3);
  				hladaj3.setText("");
  				
  				// Hashmap for ListView
  				hladajx="2";
  		        productsList = new ArrayList<HashMap<String, String>>();
  				new LoadAllProducts().execute();
  				
  				return true;
  			
  			case R.id.hkontexthladaj:

  				hladajx="1";
  	    		hladaj();
  	    		return true;

  			
  			
  			default:
  				return super.onOptionsItemSelected(item);
  			}
  		}
  		//koniec optionsmenu
  		
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
  			new LoadAllProducts().execute();
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
    
    
}
//koniec activity