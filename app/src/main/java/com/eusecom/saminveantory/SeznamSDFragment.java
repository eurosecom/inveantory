package com.eusecom.saminveantory;

import android.app.Activity;
		import android.app.ProgressDialog;
		import android.database.Cursor;
		import android.database.sqlite.SQLiteDatabase;
		import android.graphics.Color;
		import android.os.AsyncTask;
		import android.os.Bundle;
		import android.os.Environment;
		import android.support.v4.app.ListFragment;
		import android.view.View;
		import android.widget.Button;
		import android.widget.ListAdapter;
		import android.widget.ListView;
		import android.widget.SimpleAdapter;

		import org.json.JSONArray;

		import java.io.File;
		import java.util.ArrayList;
		import java.util.Arrays;
		import java.util.Collections;
		import java.util.HashMap;
		import java.util.List;


public class SeznamSDFragment extends ListFragment {

	View ColoredView;
	private ProgressDialog pDialog;
	Button btnRefresh;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	ArrayList<HashMap<String, String>> productsList;

	// JSON Node names
	private static final String TAG_BANK = "bankxy";
	private static final String TAG_ORDER = "orderxy";
	private static final String TAG_SEND = "sendxy";

	public static final String SERVER_NAME = "servername";
	public static final String USER_ID = "userid";
	public static final String DRUH_ID = "druhid";

	// products JSONArray
	JSONArray products = null;


	protected static String[] names = new String[] { "Samo1", "Svatopluk I." };

	private OnRulerSelectedListener mOnRulerSelectedListener;

	private SQLiteDatabase db2=null;
	private Cursor constantsCursor2=null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		productsList = new ArrayList<HashMap<String, String>>();

		new LoadObj().execute();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Donutíme kontejnerovou Activitu implementovat naše rozhraní
		try {
			mOnRulerSelectedListener = (OnRulerSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnRulerSelectedListener");
		}


	}
//koniec onattach

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		if (ColoredView != null)
			ColoredView.setBackgroundColor(Color.BLACK); //original color

		v.setBackgroundColor(Color.GRAY); //selected color
		ColoredView = v;


		mOnRulerSelectedListener.onRulerSelected(position);
	}

	public interface OnRulerSelectedListener {
		public void onRulerSelected(int index);
	}


	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadObj extends AsyncTask<String, String, String> {


		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage(getString(R.string.progallproducts));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All obj from url
		 * */
		protected String doInBackground(String... args) {

			String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
			String fileName = "/eusecom/androidcaffe";
			//ArrayList<String> FilesInFolder = GetFiles("/sdcard/eusecom/androidcaffe");

			//ArrayList<String> FilesInFolder = GetFiles(baseDir + File.separator + fileName);

			List<String> listFile = null;
			File fileDir = new File(baseDir + File.separator + fileName);
			if(fileDir.isDirectory()){
				listFile = Arrays.asList(fileDir.list());

			}
			Collections.sort(listFile,Collections.reverseOrder());

			//Log.d("FilesInFolder", FilesInFolder.toString());
			//Log.d("listFile", listFile.toString());

			db2=(new DatabaseOrders(getActivity())).getWritableDatabase();
			constantsCursor2=db2.rawQuery("SELECT _ID, bankx, orderx, sendx, datm FROM orders WHERE _id > 0 ORDER BY _id DESC ",
					null);

			constantsCursor2.moveToFirst();
			while(!constantsCursor2.isAfterLast()) {

				//myfavpairs[ic] = constantsCursor2.getString(constantsCursor2.getColumnIndex("pair2"));

				String bankxy = constantsCursor2.getString(constantsCursor2.getColumnIndex("bankx"));
				String orderxy = "inv" + constantsCursor2.getString(constantsCursor2.getColumnIndex("orderx")) + "_nostore.csv";
				String sendxy = constantsCursor2.getString(constantsCursor2.getColumnIndex("sendx"));
				//String datmxy = constantsCursor2.getString(constantsCursor2.getColumnIndex("datm"));


				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();

				// adding each child node to HashMap key => value
				map.put(TAG_BANK, bankxy);
				map.put(TAG_ORDER, orderxy);
				map.put(TAG_SEND, sendxy);

				// adding HashList to ArrayList
				productsList.add(map);

				constantsCursor2.moveToNext();
			}
			constantsCursor2.close();
			db2.close();


			return null;
		}

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
							R.layout.list_item_mojeobj, new String[] { TAG_BANK, TAG_ORDER, TAG_SEND },
							new int[] { R.id.bank, R.id.order, R.id.send });
					// updating listview
					setListAdapter(adapter);

				}
			});

		}

	}
	//koniec LoadObj


	public ArrayList<String> GetFiles(String DirectoryPath) {
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
				if( subnaz.equals("order")) { MyFiles.add(files[i].getName()); }
			}
		}

		return MyFiles;
	}



}
//celkom koniec seznamfragment