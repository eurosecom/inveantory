package com.eusecom.saminveantory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailSDFragment extends ListFragment {
	public static final String INDEX = "index";

    Button btnPrepare, btnZmazat, btnObjednaj;
    
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	ArrayList<HashMap<String, String>> productsList;
	List<String> ordersList = new ArrayList<String>();
	List<String> nostoreList = new ArrayList<String>();

	// JSON Node names
	private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";   
    private static final String TAG_MNOX = "mnox";
	
	// products JSONArray
	JSONArray products = null;
	JSONObject product;

	protected static String[] details = new String[] {
			"Nejaky text1...",
			"Nejaky text2..."
	};

	TextView txtDokindex, obsahKosika;
	String dokindex;
	String podadresar;
	int indexxy;
	String orderdaj;
	String nostoredaj;
	
	private SQLiteDatabase db2=null;
	private Cursor constantsCursor2=null;
	
	public static DetailSDFragment newInstance(int index) {
		DetailSDFragment f = new DetailSDFragment();
		Bundle args = new Bundle();
		args.putInt(INDEX, index);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.moje_objsd, container, false);
		TextView obsahKosika = (TextView) v.findViewById(R.id.obsahKosika);

		String serverx = SettingsActivity.getServerName(getActivity());
    	String delims = "[/]+";
    	String[] serverxxx = serverx.split(delims);
    	
    	podadresar=serverxxx[1];
		
		int akox = 1;
		if( akox == 1 )
		{


		int index = getArguments().getInt(INDEX, 0);
		@SuppressWarnings("unused")
		final String indexs = index + "";
		indexxy=index;
		
		db2=(new DatabaseOrders(getActivity())).getWritableDatabase();
		constantsCursor2=db2.rawQuery("SELECT _ID, bankx, orderx, sendx, datm FROM orders WHERE _id > 0 ORDER BY _id DESC ",
				null);
		constantsCursor2.moveToFirst();
        while(!constantsCursor2.isAfterLast()) {

            String orderxy = "inv" + constantsCursor2.getString(constantsCursor2.getColumnIndex("orderx")) + "_nostore.csv";
            String nostorexy = constantsCursor2.getString(constantsCursor2.getColumnIndex("orderx"));
            ordersList.add(orderxy);
            nostoreList.add(nostorexy);
            
        	constantsCursor2.moveToNext();
        	}

		constantsCursor2.close();
        db2.close();
        
        orderdaj = ordersList.get(indexxy);
        Log.d("orderdaj", orderdaj);
        obsahKosika.setText(orderdaj);
        
        nostoredaj = nostoreList.get(indexxy);
        Log.d("orderdaj", orderdaj);
		
		
		productsList = new ArrayList<HashMap<String, String>>();
		 
        // Loading products in Background Thread
        new LoadAllProducts().execute();
		
		}
		
		return v;

	}
	//konieconcerateview
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);


    	View vxx = getView();


			btnZmazat = (Button) vxx.findViewById(R.id.btnZmazat);
	 
	        // save button click event
			btnZmazat.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View view) {


	            	Intent i = new Intent(getActivity(), ZmazOrderSDActivity.class);
	            	Bundle extras = new Bundle();
	                extras.putString("orderdaj", orderdaj);
	                extras.putString("nostoredaj", nostoredaj);
	                extras.putInt("allorder", 0);
	                i.putExtras(extras);
	                startActivity(i);
	                getActivity().finish();

	            	
	 
	            }
	        });
			
			btnObjednaj = (Button) vxx.findViewById(R.id.btnObjednaj);
			 
	        // save button click event
			btnObjednaj.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View view) {


	            	Intent i = new Intent(getActivity(), ObjednajOrderSDActivity.class);
	            	Bundle extras = new Bundle();
	                extras.putString("orderdaj", orderdaj);
	                extras.putString("nostoredaj", nostoredaj);
	                i.putExtras(extras);
	                startActivity(i);
	                getActivity().finish();
	            	
	 
	            }
	        });
			

			
	}
	//koniec onactivitycreated
	
	

	class LoadAllProducts extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
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
            	String fileName = "/eusecom/" + podadresar + "/" + orderdaj;
            	File myFile = new File(baseDir + File.separator + fileName);

                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
            	
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                
                }
                
                //inputAll = (TextView) findViewById(R.id.inputAll);
                //inputAll.setText(aBuffer);

                String kosikx = aBuffer;
                myReader.close();
                
                //Log.d("kosikx", kosikx);

            	String delims = "[\n]+";
            	String delims2 = "[;]+";
            	
            	String[] riadokxxx = kosikx.split(delims);
            	
                for (int i = 0; i < riadokxxx.length; i++) {
            	String riadok1 =  riadokxxx[i];

            	String[] polozkyx = riadok1.split(delims2);
            	String idx =  polozkyx[1];
            	String namex =  polozkyx[2];
            	String pricex =  polozkyx[3];
            	String mnozx =  polozkyx[4];
            	String mnozxx =  mnozx + " x";
            	
                //inputAll.setText(namex);
                
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_PID, idx);
                map.put(TAG_NAME, namex);
                map.put(TAG_PRICE, pricex);
                map.put(TAG_MNOX, mnozxx);
                
                // adding HashList to ArrayList
                productsList.add(map);
                }
                
                Log.d("productsList", productsList.toString());
            
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
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            getActivity(), productsList,
                            R.layout.list_item_mojkosik, new String[] { TAG_PID, TAG_NAME, TAG_PRICE, TAG_MNOX},
                            new int[] { R.id.pid, R.id.name, R.id.price, R.id.mnozstvox });
                    // updating listview
                    setListAdapter(adapter);
 
                }
            });
 
        }
 
    }
    
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	
}
//koniec detailfragment