package com.eusecom.saminveantory;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadFileActivity extends Activity implements Callback<UploadFileResponse> {

    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_NAI = "nai";
    private static final String TAG_PIDX = "pidx";
    String encrypted;
    String adresarxx="", akyserver="";
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

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

    }
    public void onClick(View view) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UploadFileAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //NEW ADD file
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        //String fileName = "/eusecom/androideshop/inventura.csv";
        //String fileName = "/eusecom/androideshop/products.xml";
        String fileName = "/eusecom/androideshop/odberfak.xml";
        File file = new File(baseDir + File.separator + fileName);

        //String filenamex = "inventura.csv";
        String filenamex = "odberfak.xml";


        //jpg to bytearray
        //Bitmap bMap = BitmapFactory.decodeFile(baseDir + File.separator + fileName);
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        //byte[] byteimg = stream.toByteArray();
        // get the base 64 string
        //String imgString = Base64.encodeToString(byteimg,Base64.NO_WRAP);

        //text file to stream
        byte[] byteArray = convertFileToByteArray(file);
        String imgString = Base64.encodeToString(byteArray,Base64.NO_WRAP);



        //RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgString);

        // prepare call in Retrofit 2.0
        UploadFileAPI uploadfileAPI = retrofit.create(UploadFileAPI.class);

        Call<UploadFileResponse> call = uploadfileAPI.upload(filenamex, requestFile);
        //asynchronous call
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<UploadFileResponse> call, Response<UploadFileResponse> response) {
        int code = response.code();
        if (code == 200) {
            UploadFileResponse user = response.body();
            //Log.d("response.body().toStr()", response.body().toString());
            //Log.d("GithubUser user", user.toString());

            Toast.makeText(this, "Got the message: " + user.name, Toast.LENGTH_LONG).show();
            new AccountQR().execute();
        } else {
            Toast.makeText(this, "Did not work: " + String.valueOf(code), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<UploadFileResponse> call, Throwable t) {
        Toast.makeText(this, "Nope", Toast.LENGTH_LONG).show();

    }

    public static byte[] convertFileToByteArray(File f)
    {
        byte[] byteArray = null;
        try
        {
            InputStream inputStream = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead =0;

            while ((bytesRead = inputStream.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return byteArray;
    }//end convertfileto...


        class AccountQR extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UploadFileActivity.this);
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
                        String userx = "Nick/" + SettingsActivity.getNickName(UploadFileActivity.this)
                                + "/Id/" + SettingsActivity.getUserId(UploadFileActivity.this)
                                + "/Psw/" + SettingsActivity.getUserPsw(UploadFileActivity.this)
                                + "/druhID/99/Doklad/1";

                        String ftpqrencodex=SettingsActivity.getFtpqr(UploadFileActivity.this);
                        String ftpqrencode="";
                        try {
                            ftpqrencode = URLEncoder.encode(ftpqrencodex, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String prmall = "fir/" + SettingsActivity.getFir(UploadFileActivity.this)
                                + "/rok/" + SettingsActivity.getFirrok(UploadFileActivity.this)
                                + "/qrfolder/" + ftpqrencode;
                        String fakx = "";

                        String qrx = "";
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
                            //inputQr.setText(product.getString(TAG_NAI));
                            String newfak = product.getString(TAG_NAI).toString();
                            Toast.makeText(UploadFileActivity.this, "Accounted new invoice: " + newfak, Toast.LENGTH_LONG).show();
                            finish();


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

}