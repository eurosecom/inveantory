package com.eusecom.saminveantory;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QrcodeActivity extends AppCompatActivity {
    ImageView qrCodeImageview;
    String QRcode, adresarxx="";
    public final static int WIDTH=500;

    EditText inputQr;
    Button btnQrcreate, btnSaveqr, btnUploadqr;
    private ProgressDialog pDialog;
    Bitmap bitmap = null;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.qrcode_activity_scrolling);

     String serverx = SettingsActivity.getServerName(this);
     String delims3 = "[/]+";
     String[] serverxxx = serverx.split(delims3);
     if (serverxxx.length < 2 ) {
         adresarxx="androideshop";
     }else{
         adresarxx=serverxxx[1];
     }

        // this is the msg which will be encode in QRcode
        QRcode="This is My first QR code";

     btnSaveqr = (Button) findViewById(R.id.btnSaveqr);
     btnSaveqr.setVisibility(View.GONE);
     btnUploadqr  = (Button) findViewById(R.id.btnUploadqr);
     btnUploadqr.setVisibility(View.GONE);

        inputQr = (EditText) findViewById(R.id.inputQr);
        inputQr.setText(QRcode);
        btnQrcreate = (Button) findViewById(R.id.btnQrcreate);
        btnQrcreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                new CreateQR().execute();


            }
         });

     btnSaveqr.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View arg0) {

             new SaveQR().execute();


         }
     });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
     if (fab != null) {
         fab.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 new CreateQR().execute();
             }
         });
     }

 }//end oncreate

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

            new CreateQR().execute();

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

      String qrtext = inputQr.getText().toString();
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


}//end Activity