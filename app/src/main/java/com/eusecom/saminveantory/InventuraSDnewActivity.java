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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eusecom.saminveantory.InventuraSDnewAdapter.DoSomething2;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class InventuraSDnewActivity extends AppCompatActivity implements DoSomething2 {

    EditText inputMno, inputEan, inputAll, inputCis, inputCed, inputMer;
    Button btnObjednaj, btnPoznamka, inputNaz;
    TextView inputAllServer, popisEan;

    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    private ProgressDialog pDialog3;

    private static final String TAG_PIDX = "pidx";

    private static final String TAG_PRODUCT = "product";
    static final String NODE_PID = "pid";
    static final String NODE_NAME = "name";
    static final String NODE_PRICE = "price";
    static final String NODE_EAN = "ean";
    static final String NODE_MER = "mer";

    String adresarxx="", cplx, ean, nazean, senditem;
    String ipx="0";

    RecyclerView recyclerView;
    InventuraSDnewAdapter adapter;

    private List<String> mText = new ArrayList<String>();
    private List<String> mMno = new ArrayList<String>();
    private List<String> mPrice = new ArrayList<String>();
    private List<String> mIdx = new ArrayList<String>();

    private static String[] texts = null;
    private static String[] mnos = null;
    private static String[] prices = null;
    private static String[] idxs = null;

    ArrayList<HashMap<String, String>> productsList;
    public Comparator<Map<String, String>> mapComparator;

    int iean;

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

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "/eusecom/" + adresarxx + "/inventura.csv";
        File myFile = new File(baseDir + File.separator + fileName);

        if(!myFile.exists()) {
            try {
                myFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        inputNaz = (Button) findViewById(R.id.inputNaz);
        inputMno = (EditText) findViewById(R.id.inputMno);
        inputCis = (EditText) findViewById(R.id.inputCis);
        inputCed = (EditText) findViewById(R.id.inputCed);
        inputMer = (EditText) findViewById(R.id.inputMer);
        inputCed.setEnabled(false);
        inputCis.setEnabled(false);
        inputMer.setEnabled(false);

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

        btnObjednaj = (Button) findViewById(R.id.btnObjednaj);
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
        btnPoznamka.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(getApplicationContext(), PoznamkaKosikSDActivity.class);
                startActivity(i);

            }
        });


        inputNaz.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                inputEan.requestFocus();
                Intent ih = new Intent(getApplicationContext(), SearchRvActivity.class);
                startActivityForResult(ih, 100);

            }
        });

        inputMno = (EditText) findViewById(R.id.inputMno);
        inputMno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        inputEan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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



    }//end oncreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_invsd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.preferences) {

            Intent is = new Intent(getApplicationContext(), MyPreferencesActivity.class);
            startActivity(is);
            return true;
        }

        if (id == R.id.setnostroreinventory) {

            Intent idm = new Intent(getApplicationContext(), InvsetActivity.class);
            Bundle extrasdm = new Bundle();
            extrasdm.putString("page", "3");
            idm.putExtras(extrasdm);
            startActivity(idm);
            finish();
            return true;
        }

        if (id == R.id.delinventory) {

            Intent idm = new Intent(getApplicationContext(), InvsetActivity.class);
            Bundle extrasdm = new Bundle();
            extrasdm.putString("page", "0");
            idm.putExtras(extrasdm);
            startActivity(idm);
            finish();
            return true;
        }


        if (id == R.id.demoresources) {

            Intent idm = new Intent(getApplicationContext(), InvsetActivity.class);
            Bundle extrasdm = new Bundle();
            extrasdm.putString("page", "1");
            idm.putExtras(extrasdm);
            startActivity(idm);
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

                texts = new String[ip];
                mnos = new String[ip];
                prices = new String[ip];
                idxs = new String[ip];

                String kosikx = aBuffer;
                myReader.close();

                String delims = "[\n]+";
                String delims2 = "[;]+";

                String[] riadokxxx = kosikx.split(delims);
                ipx = ip + "";

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


                    //texts[i] = namex + " " + eanx + "/" + cplx;
                    //mnos[i] = mnozxx;
                    //prices[i] = pricex;
                    //idxs[i] = idx;

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put("texts", namex + " " + eanx + "/" + idx);
                    map.put("prices", pricex);
                    map.put("mnos", mnozxx);
                    map.put("idxs", cplx);

                    productsList.add(map);

                }

                mapComparator = new Comparator<Map<String, String>>() {
                    public int compare(Map<String, String> m1, Map<String, String> m2) {
                        return m1.get("idxs").compareTo(m2.get("idxs"));
                    }
                };

                Collections.sort(productsList, mapComparator);

                Log.d("productsList", productsList.toString());
                //Log.d("idxs 0", productsList.get(0).get("idxs"));
                //Log.d("idxs 1", productsList.get(1).get("idxs"));
                //productsList.get(0).get("idxs");

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

                    for (int i = 0; i < productsList.size(); i++) {
                        texts[i] = productsList.get(i).get("texts");
                        mnos[i] = productsList.get(i).get("mnos");
                        prices[i] = productsList.get(i).get("prices");
                        idxs[i] = productsList.get(i).get("idxs");
                    }


                    //to solve fail from 20.11.2016
                    try {
                        mText = new ArrayList<>(Arrays.asList(texts));
                        mMno = new ArrayList<>(Arrays.asList(mnos));
                        mPrice = new ArrayList<>(Arrays.asList(prices));
                        mIdx = new ArrayList<>(Arrays.asList(idxs));
                    }catch(NullPointerException e){
                        String s= "";
                        mText = new ArrayList<>(Arrays.asList(s.split(",")));
                        mMno = new ArrayList<>(Arrays.asList(s.split(",")));
                        mPrice = new ArrayList<>(Arrays.asList(s.split(",")));
                        mIdx = new ArrayList<>(Arrays.asList(s.split(",")));
                    }

                    //Log.d("mDataSet", mDataSet.toString());
                    //Log.d("myBidList", myBidList.toString());

                    adapter = new InventuraSDnewAdapter(InventuraSDnewActivity.this, InventuraSDnewActivity.this, mText, mMno, mPrice, mIdx);
                    recyclerView.setAdapter(adapter);
                    registerForContextMenu(recyclerView);

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

    class GetSDProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2 = new ProgressDialog(InventuraSDnewActivity.this);
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
    //end GetProductDetails


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
    }//end getInputEncoding

    class SaveSDProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog3 = new ProgressDialog(InventuraSDnewActivity.this);
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



                Intent i = new Intent(getApplicationContext(), InventuraSDnewActivity.class);
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
    //end saveproducts


    //oncontextmenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);


        String name2=getString(R.string.itemtxt) + " " + senditem;

        menu.setHeaderTitle( name2 );

        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.kontextinvsd_menu, menu);


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.delete:

                Intent i = new Intent(getApplicationContext(), ZmazInventuraSDActivity.class);
                Bundle extras = new Bundle();
                extras.putString("cat", senditem);
                extras.putString("odk", "0");
                i.putExtras(extras);
                startActivity(i);
                finish();


                break;



        }

        return super.onContextItemSelected(item);
    }
    //end oncontextmenu

    @Override
    public void doChangeItem(final String itemx) {

        senditem=itemx;

    }


}//end of activity
