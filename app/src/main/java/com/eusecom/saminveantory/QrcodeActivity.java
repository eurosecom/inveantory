package com.eusecom.saminveantory;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class QrcodeActivity extends AppCompatActivity {
    ImageView qrCodeImageview;
    String QRcode, adresarxx="", akyserver="";
    public final static int WIDTH=500;

    EditText inputQr;
    TextView inputFak;
    Button btnQrcreate, btnSaveqr, btnUploadqr, btnInvnum;
    private ProgressDialog pDialog;
    Bitmap bitmap = null;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_NAI = "nai";
    private static final String TAG_PIDX = "pidx";
    String encrypted;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

 @Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.qrcode_activity_scrolling);

     String serverx = SettingsActivity.getServerName(this);
     String delims3 = "[/]+";
     String[] serverxxx = serverx.split(delims3);
     if (serverxxx.length < 2 ) {
         adresarxx="androideshop";
         akyserver="www.eshoptest.sk";
     }else{
         adresarxx=serverxxx[1];
         akyserver=serverxxx[0];
     }

        // this is the msg which will be encode in QRcode
        QRcode="Info for QR code";

     btnSaveqr = (Button) findViewById(R.id.btnSaveqr);
     btnSaveqr.setVisibility(View.GONE);
     btnUploadqr  = (Button) findViewById(R.id.btnUploadqr);
     btnUploadqr.setVisibility(View.GONE);

     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
     StrictMode.setThreadPolicy(policy);

        inputQr = (EditText) findViewById(R.id.inputQr);
        inputQr.setText(QRcode);
        inputFak = (TextView) findViewById(R.id.inputFak);
        //inputFak.setText("0");
        btnQrcreate = (Button) findViewById(R.id.btnQrcreate);
        btnQrcreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String qrtext = inputQr.getText().toString();
                new CreateQR().execute(qrtext);


            }
         });

     btnSaveqr.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View arg0) {

             new SaveQR().execute();


         }
     });

     btnUploadqr.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View arg0) {

             if( inputFak.getText().toString().equals("0"))
             {
                 new AlertDialog.Builder(QrcodeActivity.this)
                         .setTitle(getString(R.string.niejecisfak))
                         .setMessage(getString(R.string.setcisfak))
                         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {

                                 //finish();
                             }
                         })

                         .show();
             }else{

                 String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                 String fileName = "/eusecom/" + adresarxx + "/qrcode.jpg";
                 File myFile = new File(baseDir + File.separator + fileName);
                 if(!myFile.exists()){

                     new AlertDialog.Builder(QrcodeActivity.this)
                             .setTitle(getString(R.string.niejeqrjpg))
                             .setMessage(getString(R.string.setqrjpg))
                             .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                                 public void onClick(DialogInterface dialog, int which) {

                                     //finish();
                                 }
                             })

                             .show();

                 }else{


                     Intent i = new Intent(getApplicationContext(), NaserverActivity.class);
                     Bundle extras = new Bundle();
                     extras.putString("pagex", "2");
                     String fakxx = inputFak.getText().toString();
                     extras.putString("fakx", fakxx);
                     i.putExtras(extras);
                     startActivity(i);
                 }

             }


         }
     });

     btnInvnum = (Button) findViewById(R.id.btnInvnum);
     btnInvnum.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View arg0) {

             btnSaveqr.setVisibility(View.GONE);
             btnUploadqr.setVisibility(View.GONE);
             getID();
             qrCodeImageview.setImageBitmap(null);
             //qrCodeImageview.setVisibility(View.INVISIBLE);

             String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
             String fileName = "/eusecom/" + adresarxx + "/qrcode.jpg";
             File myFile = new File(baseDir + File.separator + fileName);
             if(myFile.exists()){
                 myFile.delete();
             }

             if (isOnline())
             {
                 Intent ih = new Intent(getApplicationContext(), SearchFakActivity.class);
                 startActivityForResult(ih, 100);
             }else{

                 new AlertDialog.Builder(QrcodeActivity.this)
                         .setTitle(getString(R.string.niejeinternet))
                         .setMessage(getString(R.string.potrebujeteinternet))
                         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {

                                 //finish();
                             }
                         })

                         .show();

             }


         }
     });

     final ImageView imgdown = (ImageView) findViewById(R.id.imgdown);
     imgdown.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v){

             if( inputFak.getText().toString().equals("0"))
             {
                 new AlertDialog.Builder(QrcodeActivity.this)
                         .setTitle(getString(R.string.niejecisfak))
                         .setMessage(getString(R.string.setcisfak))
                         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {

                                 //finish();
                             }
                         })

                         .show();
             }else{


             btnSaveqr.setVisibility(View.GONE);
             btnUploadqr.setVisibility(View.GONE);
             getID();
             qrCodeImageview.setImageBitmap(null);

             String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
             String fileName = "/eusecom/" + adresarxx + "/qrcode.jpg";
             File myFile = new File(baseDir + File.separator + fileName);
             if(myFile.exists()){
                 myFile.delete();
             }
             if (isOnline())
             {
                 new GetInvoice().execute();
             }else{

                 new AlertDialog.Builder(QrcodeActivity.this)
                         .setTitle(getString(R.string.niejeinternet))
                         .setMessage(getString(R.string.potrebujeteinternet))
                         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {

                                 //finish();
                             }
                         })

                         .show();

             }
             }//fak isnot 0
         }
     });

     Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
     setSupportActionBar(toolbar);

     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
     if (fab != null) {
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 IntentIntegrator integrator = new IntentIntegrator(QrcodeActivity.this);
                 integrator.initiateScan();
             }
         });
     }

     String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
     String fileName = "/eusecom/" + adresarxx + "/odberfak.xml";
     File myFile = new File(baseDir + File.separator + fileName);

     if(myFile.exists()){
         myFile.delete();
     }

     String fileName2 = "/eusecom/" + adresarxx + "/qrcode.jpg";
     File myFile2 = new File(baseDir + File.separator + fileName2);

     if(myFile2.exists()){
         myFile2.delete();
     }

 }//end oncreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {

            inputFak.setText("999999");
            String re = scanResult.getContents();
            inputQr.setText(re);

        }


        if (resultCode == 101) {

            String pidxy = data.getStringExtra(TAG_PIDX);
            inputFak.setText(pidxy);


        }

    }//end onactivityresult

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_qrcode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.btnchoosefak) {

            btnSaveqr.setVisibility(View.GONE);
            btnUploadqr.setVisibility(View.GONE);
            getID();
            qrCodeImageview.setImageBitmap(null);
            //qrCodeImageview.setVisibility(View.INVISIBLE);

            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "/eusecom/" + adresarxx + "/qrcode.jpg";
            File myFile = new File(baseDir + File.separator + fileName);
            if(myFile.exists()){
                myFile.delete();
            }

            if (isOnline())
            {
                Intent ih = new Intent(getApplicationContext(), SearchFakActivity.class);
                startActivityForResult(ih, 100);
            }else{

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.niejeinternet))
                        .setMessage(getString(R.string.potrebujeteinternet))
                        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //finish();
                            }
                        })

                        .show();

            }


            return true;
        }

        if (id == R.id.preferences) {

            Intent is = new Intent(getApplicationContext(), MyPreferencesActivity.class);
            startActivity(is);
            return true;
        }

        if (id == R.id.btnqrsave) {

            new SaveQR().execute();

            return true;
        }

        if (id == R.id.btnqrcreate) {

            //qrCodeImageview.setVisibility(View.VISIBLE);
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "/eusecom/" + adresarxx + "/qrcode.jpg";
            File myFile = new File(baseDir + File.separator + fileName);
            if(myFile.exists()){
                myFile.delete();
            }
            new CreateQR().execute();

            return true;
        }

        if (id == R.id.btnqrdownfak) {

            if( inputFak.getText().toString().equals("0"))
            {
                new AlertDialog.Builder(QrcodeActivity.this)
                        .setTitle(getString(R.string.niejecisfak))
                        .setMessage(getString(R.string.setcisfak))
                        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //finish();
                            }
                        })

                        .show();
            }else {

                btnSaveqr.setVisibility(View.GONE);
                btnUploadqr.setVisibility(View.GONE);
                getID();
                qrCodeImageview.setImageBitmap(null);

                String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName = "/eusecom/" + adresarxx + "/qrcode.jpg";
                File myFile = new File(baseDir + File.separator + fileName);
                if (myFile.exists()) {
                    myFile.delete();
                }
                if (isOnline()) {
                    new GetInvoice().execute();
                } else {

                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.niejeinternet))
                            .setMessage(getString(R.string.potrebujeteinternet))
                            .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //finish();
                                }
                            })

                            .show();

                }
                }//fak isnot 0

            return true;
        }

        if (id == R.id.btnqrxmlfak) {

            if( inputFak.getText().toString().equals("0"))
            {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.niejecisfak))
                        .setMessage(getString(R.string.setcisfak))
                        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //finish();
                            }
                        })

                        .show();
            }else{

                    String datatxt= inputQr.getText().toString();
                    new SaveXML().execute(datatxt);

            }


            return true;
        }

        if (id == R.id.btnqrupload) {


            if( inputFak.getText().toString().equals("0"))
            {
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.niejecisfak))
                        .setMessage(getString(R.string.setcisfak))
                        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //finish();
                            }
                        })

                        .show();
            }else{

                String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName = "/eusecom/" + adresarxx + "/qrcode.jpg";
                File myFile = new File(baseDir + File.separator + fileName);
                if(!myFile.exists()){

                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.niejeqrjpg))
                            .setMessage(getString(R.string.setqrjpg))
                            .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //finish();
                                }
                            })

                            .show();

                    }else{


                Intent i = new Intent(getApplicationContext(), NaserverActivity.class);
                Bundle extras = new Bundle();
                extras.putString("pagex", "2");
                String fakxx = inputFak.getText().toString();
                extras.putString("fakx", fakxx);
                i.putExtras(extras);
                startActivity(i);
                        }


                }


            return true;
        }

        if (id == R.id.btnqruploaddel) {

            btnSaveqr.setVisibility(View.GONE);
            btnUploadqr.setVisibility(View.GONE);
            getID();
            qrCodeImageview.setImageBitmap(null);
            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "/eusecom/" + adresarxx + "/qrcode.jpg";
            File myFile = new File(baseDir + File.separator + fileName);
            if(myFile.exists()){
                myFile.delete();
            }
            if(isOnline())
            {
                new DelQR().execute();
            }else{

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.niejeinternet))
                        .setMessage(getString(R.string.potrebujeteinternet))
                        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //finish();
                            }
                        })

                        .show();

            }


            return true;
        }

        if (id == R.id.btnqraccount) {

            if(isOnline())
            {

                String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName = "/eusecom/" + adresarxx + "/odberfak.xml";
                File myFile = new File(baseDir + File.separator + fileName);
                if(!myFile.exists()){

                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.niejeqrxml))
                            .setMessage(getString(R.string.setqrxml))
                            .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //finish();
                                }
                            })

                            .show();

                }else {

                    Intent i = new Intent(getApplicationContext(), UploadFileActivity.class);
                    startActivity(i);
                    //new AccountQR().execute();
                        }
            }else{

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.niejeinternet))
                        .setMessage(getString(R.string.potrebujeteinternet))
                        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //finish();
                            }
                        })

                        .show();

            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

 private void getID() {
 qrCodeImageview=(ImageView) findViewById(R.id.img_qr_code_image);
}

// this is method call from on create and return bitmap image of QRCode.
 Bitmap encodeAsBitmap(String str) throws WriterException {
 BitMatrix result;
 try {
 result = new MultiFormatWriter().encode(str,
 BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
 } catch (IllegalArgumentException iae) {
 // Unsupported format
 return null;
 }
 int w = result.getWidth();
 int h = result.getHeight();
 int[] pixels = new int[w * h];
 for (int y = 0; y < h; y++) {
 int offset = y * w;
 for (int x = 0; x < w; x++) {
 pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.black):getResources().getColor(R.color.white);
 }
 }
 Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
 bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
 return bitmap;
 } /// end of this method


    class AccountQR extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QrcodeActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... params) {


            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        String serverx = akyserver + "/androiducto";
                        String userx = "Nick/" + SettingsActivity.getNickName(QrcodeActivity.this)
                                + "/Id/" + SettingsActivity.getUserId(QrcodeActivity.this)
                                + "/Psw/" + SettingsActivity.getUserPsw(QrcodeActivity.this)
                                + "/druhID/99/Doklad/1";

                        String ftpqrencodex=SettingsActivity.getFtpqr(QrcodeActivity.this);
                        String ftpqrencode="";
                        try {
                            ftpqrencode = URLEncoder.encode(ftpqrencodex, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String prmall = "fir/" + SettingsActivity.getFir(QrcodeActivity.this)
                                + "/rok/" + SettingsActivity.getFirrok(QrcodeActivity.this)
                                + "/qrfolder/" + ftpqrencode;
                        String fakx = inputFak.getText().toString();

                        String qrx = inputQr.getText().toString();
                        String qrxencode="";
                        try {
                            qrxencode = URLEncoder.encode(qrx, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                        String userxplus = userx + "/" + fakx + "/" + qrxencode;

                        //String userhash = sha1Hash( userx );
                        MCrypt mcrypt = new MCrypt();
                    	/* Encrypt */
                        try {
                            encrypted = MCrypt.bytesToHex( mcrypt.encrypt(userxplus) );
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    	/* Decrypt */
                        //String decrypted = new String( mcrypt.decrypt( encrypted ) );

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("serverx", serverx));
                        params.add(new BasicNameValuePair("prmall", prmall));
                        params.add(new BasicNameValuePair("userhash", encrypted));
                        params.add(new BasicNameValuePair("zandroidu", "1"));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                "http://" + akyserver + "/faktury/vstf_importfakxml.php", "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // display product data in EditText
                            inputQr.setText(product.getString(TAG_NAI));


                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }//end accountQR

    class DelQR extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QrcodeActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... params) {


            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        String serverx = akyserver + "/androiducto";
                        String userx = "Nick/" + SettingsActivity.getNickName(QrcodeActivity.this)
                                + "/Id/" + SettingsActivity.getUserId(QrcodeActivity.this)
                                + "/Psw/" + SettingsActivity.getUserPsw(QrcodeActivity.this)
                                + "/druhID/99/Doklad/1";

                        String ftpqrencodex=SettingsActivity.getFtpqr(QrcodeActivity.this);
                        String ftpqrencode="";
                        try {
                            ftpqrencode = URLEncoder.encode(ftpqrencodex, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String prmall = "fir/" + SettingsActivity.getFir(QrcodeActivity.this)
                                + "/rok/" + SettingsActivity.getFirrok(QrcodeActivity.this)
                                + "/qrfolder/" + ftpqrencode;
                        String fakx = inputFak.getText().toString();


                        String userxplus = userx + "/" + fakx;

                        //String userhash = sha1Hash( userx );
                        MCrypt mcrypt = new MCrypt();
                    	/* Encrypt */
                        try {
                            encrypted = MCrypt.bytesToHex( mcrypt.encrypt(userxplus) );
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    	/* Decrypt */
                        //String decrypted = new String( mcrypt.decrypt( encrypted ) );

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("serverx", serverx));
                        params.add(new BasicNameValuePair("prmall", prmall));
                        params.add(new BasicNameValuePair("userhash", encrypted));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                "http://" + akyserver + "/androidfanti/del_infofak.php", "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // display product data in EditText
                            inputQr.setText(product.getString(TAG_NAI));


                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }//end delQR

 class CreateQR extends AsyncTask<String, String, String> {

  @Override
  protected void onPreExecute() {
   super.onPreExecute();
   pDialog = new ProgressDialog(QrcodeActivity.this);
   pDialog.setMessage(getString(R.string.progallproducts));
   pDialog.setIndeterminate(false);
   pDialog.setCancelable(false);
   pDialog.show();
  }


  protected String doInBackground(String... args) {

      getID();
      //qrCodeImageview.setVisibility(View.VISIBLE);
      String qrtext = args[0];
      try {
          bitmap = encodeAsBitmap(qrtext);
      } catch (WriterException e) {
          e.printStackTrace();
      }


   return null;

  }

  protected void onPostExecute(String file_url) {
   // dismiss the dialog after getting all products
   pDialog.dismiss();

   // updating UI from Background Thread
   runOnUiThread(new Runnable() {
    public void run() {

        qrCodeImageview.setImageBitmap(bitmap);
        btnSaveqr.setVisibility(View.VISIBLE);
        btnUploadqr.setVisibility(View.VISIBLE);

    }
   });

  }

 }
 //end createQR

    class SaveQR extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QrcodeActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "/eusecom/" + adresarxx + "/qrcode.jpg";
            File myFile = new File(baseDir + File.separator + fileName);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(myFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    //end saveQR


    class GetInvoice extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QrcodeActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... params) {


            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        String serverx = akyserver + "/androiducto";
                        String userx = "Nick/" + SettingsActivity.getNickName(QrcodeActivity.this)
                                + "/Id/" + SettingsActivity.getUserId(QrcodeActivity.this)
                                + "/Psw/" + SettingsActivity.getUserPsw(QrcodeActivity.this)
                                + "/druhID/99/Doklad/1";

                        String ftpqrencodex=SettingsActivity.getFtpqr(QrcodeActivity.this);
                        String ftpqrencode="";
                        try {
                            ftpqrencode = URLEncoder.encode(ftpqrencodex, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String prmall = "fir/" + SettingsActivity.getFir(QrcodeActivity.this)
                                + "/rok/" + SettingsActivity.getFirrok(QrcodeActivity.this)
                                + "/qrfolder/" + ftpqrencode;

                        String fakx = inputFak.getText().toString();


                        String userxplus = userx + "/" + fakx;

                        //String userhash = sha1Hash( userx );
                        MCrypt mcrypt = new MCrypt();
                    	/* Encrypt */
                        try {
                            encrypted = MCrypt.bytesToHex( mcrypt.encrypt(userxplus) );
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    	/* Decrypt */
                        //String decrypted = new String( mcrypt.decrypt( encrypted ) );

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("serverx", serverx));
                        params.add(new BasicNameValuePair("prmall", prmall));
                        params.add(new BasicNameValuePair("userhash", encrypted));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                "http://" + akyserver + "/androidfanti/get_infofak.php", "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // display product data in EditText
                            inputQr.setText(product.getString(TAG_NAI));


                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }//end getInvoice

    class SaveXML extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QrcodeActivity.this);
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {


            try {


                String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName = "/eusecom/" + adresarxx + "/odberfak.xml";
                File myFile = new File(baseDir + File.separator + fileName);

                if(myFile.exists()){
                    myFile.delete();
                }

                if(!myFile.exists()){
                    myFile.createNewFile();

                    FileOutputStream fOut = new FileOutputStream(myFile, true);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                    String datatxt= args[0];
                    myOutWriter.append(datatxt);
                    myOutWriter.close();
                    fOut.close();
                }

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
    //koniec SaveXML

    //test ci je internet pripojeny
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
    //koniec test ci je internet pripojeny

}//end Activity