package com.eusecom.saminveantory;
 
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


 
public class DemoinvActivity extends ListActivity {
 
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
            pDialog = new ProgressDialog(DemoinvActivity.this);
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
                    
                    // adding HashList to ArrayList
                    productsList.add(map);
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
                            DemoinvActivity.this, productsList,
                            R.layout.list_item_categ, new String[] { TAG_PID, TAG_NAME, TAG_PRICE},
                            new int[] { R.id.pid, R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
}