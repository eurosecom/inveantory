package com.eusecom.saminveantory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventuraSDnewActivity extends AppCompatActivity {

    EditText inputMno, inputEan, inputAll;
    Button btnObjednaj, btnScan, btnFakturuj;
    TextView inputAllServer, popisEan;

    private ProgressDialog pDialog;

    private static final String TAG_PIDX = "pidx";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_MNOX = "mnox";
    private static final String TAG_CPL = "cpl";


    String adresarxx="", cplx;
    String ipx="0";

    public ArrayList<HashMap<String, String>> productsList;
    public Comparator<Map<String, String>> mapComparator;
    RecyclerView recyclerView;
    InventuraSDnewAdapter adapter;

    private List<String> mDataSet = new ArrayList<String>();
    private List<String> myAskList = new ArrayList<String>();
    private List<String> myBidList = new ArrayList<String>();
    private List<String> myProfitList = new ArrayList<String>();

    private static String[] datas = null;
    private static String[] asks = null;
    private static String[] bids = null;
    private static String[] profs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invsd_activity_scrolling);

        View bf = findViewById(R.id.btnFakturuj);
        bf.setVisibility(View.GONE);
        View bs  = findViewById(R.id.btnScan);
        bs.setVisibility(View.INVISIBLE);

        inputEan = (EditText) findViewById(R.id.inputEan);

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                IntentIntegrator integrator = new IntentIntegrator(InventuraSDnewActivity.this);
                integrator.initiateScan();
            }
        });

        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllSDProducts().execute();

        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }//end oncreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


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

    }//end onactivityresult


    class LoadAllSDProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InventuraSDnewActivity.this);
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

                datas = new String[ip];
                asks = new String[ip];
                bids = new String[ip];
                profs = new String[ip];

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

                    datas[i] = idx;
                    asks[i] = idx;
                    bids[i] = idx;
                    profs[i] = idx;

                }

                mapComparator = new Comparator<Map<String, String>>() {
                    public int compare(Map<String, String> m1, Map<String, String> m2) {
                        return m1.get("cpl").compareTo(m2.get("cpl"));
                    }
                };

                Collections.sort(productsList, mapComparator);
                //Log.d("productsList", productsList.toString());


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

                    myAskList = new ArrayList<>(Arrays.asList(asks));
                    myBidList = new ArrayList<>(Arrays.asList(bids));
                    myProfitList = new ArrayList<>(Arrays.asList(profs));
                    mDataSet = new ArrayList<>(Arrays.asList(datas));
                    //Log.d("mDataSet", mDataSet.toString());
                    //Log.d("myBidList", myBidList.toString());

                    adapter = new InventuraSDnewAdapter(InventuraSDnewActivity.this, mDataSet, myAskList, myBidList, myProfitList);
                    recyclerView.setAdapter(adapter);



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
    //end loadallproducts


}//end of activity
