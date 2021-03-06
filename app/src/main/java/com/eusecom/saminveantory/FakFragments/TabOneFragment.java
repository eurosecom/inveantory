package com.eusecom.saminveantory.FakFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.eusecom.saminveantory.JSONParser;
import com.eusecom.saminveantory.MCrypt;
import com.eusecom.saminveantory.R;
import com.eusecom.saminveantory.SettingsActivity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TabOneFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerview;
    private List<CountryModel> mCountryModel;
    private RVAdapter adapter;
    private ProgressDialog pDialog;
    static final String NODE_PRODUCT = "product";
    static final String NODE_EAN = "ean";
    static final String NODE_NAME = "name";
    static final String NODE_PID = "pid";
    String adresarxx="";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_ICO = "ico";
    private static final String TAG_DOK = "dok";
    private static final String TAG_HOD = "hod";
    JSONParser jsonParser = new JSONParser();
    String encrypted;
    // products JSONArray
    JSONArray products = null;

    OnHeadlineSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_one_fragment, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
        String[] locales = Locale.getISOCountries();
        mCountryModel = new ArrayList<>();


        String serverx = SettingsActivity.getServerName(getActivity());
        String delims3 = "[/]+";
        String[] serverxxx = serverx.split(delims3);
        if (serverxxx.length < 2 ) {
            adresarxx="androideshop";
        }else{
            adresarxx=serverxxx[1];
        }

        // Loading products in Background Thread
        new GetInvoices().execute();


    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter.animateTo(mCountryModel);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // adapter.setFilter(mCountryModel, newText);
        final List<CountryModel> filteredModelList = filter(mCountryModel, newText);
        adapter.animateTo(filteredModelList);
        recyclerview.scrollToPosition(0);

        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<CountryModel> filter(List<CountryModel> models, String query) {
        query = query.toLowerCase();

        final List<CountryModel> filteredModelList = new ArrayList<>();
        for (CountryModel model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        int pocx = filteredModelList.size();
        mCallback.onArticleSelected(pocx);
        return filteredModelList;
    }






    class GetInvoices extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getString(R.string.progallproducts));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        protected String doInBackground(String... params) {


                    int success;
                    try {

                        String akyserver="www.eshoptest.sk";
                        String serverxx = SettingsActivity.getServerName(getActivity());
                        String delims3 = "[/]+";
                        String[] serverxxx = serverxx.split(delims3);
                        if (serverxxx.length < 2 ) {
                            adresarxx="androideshop";
                            akyserver="www.eshoptest.sk";
                        }else{
                            adresarxx=serverxxx[1];
                            akyserver=serverxxx[0];
                        }


                        // Building Parameters
                        String serverx = akyserver + "/androiducto";
                        String userx = "Nick/" + SettingsActivity.getNickName(getActivity())
                                + "/Id/" + SettingsActivity.getUserId(getActivity())
                                + "/Psw/" + SettingsActivity.getUserPsw(getActivity())
                                + "/druhID/99/Doklad/1";

                        String ftpqrencodex=SettingsActivity.getFtpqr(getActivity());
                        String ftpqrencode="";
                        try {
                            ftpqrencode = URLEncoder.encode(ftpqrencodex, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        String prmall = "fir/" + SettingsActivity.getFir(getActivity())
                                + "/rok/" + SettingsActivity.getFirrok(getActivity())
                                + "/qrfolder/" + ftpqrencode;
                        String fakx = "0";

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

                        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                        params2.add(new BasicNameValuePair("serverx", serverx));
                        params2.add(new BasicNameValuePair("prmall", prmall));
                        params2.add(new BasicNameValuePair("userhash", encrypted));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request

                        JSONObject json = jsonParser.makeHttpRequest(
                                "http://www.ala.sk/androidfanti/get_infoallfak.php", "GET", params2);

                        // check your log for json response
                        Log.d("json", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details

                            // Getting Array of Products
                            products = json.getJSONArray(TAG_PRODUCTS);
                            Log.d("products", products.toString());

                            // looping through All Products
                            for (int i = 0; i < products.length(); i++) {
                                JSONObject c = products.getJSONObject(i);

                                // Storing each json item in variable
                                String icx = c.getString(TAG_ICO);
                                String dox = c.getString(TAG_DOK);
                                String hox = c.getString(TAG_HOD);


                                // creating new HashMap
                                HashMap<String, String> map = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                map.put(TAG_ICO, icx);
                                map.put(TAG_DOK, dox);
                                map.put(TAG_HOD, dox);

                                // adding HashList to ArrayList
                                mCountryModel.add(new CountryModel(icx, dox, hox));

                            }


                        }else{
                            // products not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    adapter = new RVAdapter(getActivity(), getActivity(), mCountryModel);
                    recyclerview.setAdapter(adapter);
                    int pocx = mCountryModel.size();
                    mCallback.onArticleSelected(pocx);

                }
            });
        }
    }//end getInvoices

}//end of fragment
