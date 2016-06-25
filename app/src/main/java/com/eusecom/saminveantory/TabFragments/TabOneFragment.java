package com.eusecom.saminveantory.TabFragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.eusecom.saminveantory.R;
import com.eusecom.saminveantory.SettingsActivity;
import com.eusecom.saminveantory.XMLDOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
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

        //for (String countryCode : locales) {
        //    Locale obj = new Locale("", countryCode);
        //    mCountryModel.add(new CountryModel(obj.getDisplayCountry(), obj.getISO3Country()));
        //}


        String serverx = SettingsActivity.getServerName(getActivity());
        String delims3 = "[/]+";
        String[] serverxxx = serverx.split(delims3);
        if (serverxxx.length < 2 ) {
            adresarxx="androideshop";
        }else{
            adresarxx=serverxxx[1];
        }

        // Loading products in Background Thread
        new LoadAllProducts().execute();


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

            String akehl="1";
            String hladaj1x = ""; String hladaj2x = ""; String hladaj3x = "";


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


                    mCountryModel.add(new CountryModel(name, id));

                    //koniec for
                }



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
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    adapter = new RVAdapter(getActivity(), getActivity(), mCountryModel);
                    recyclerview.setAdapter(adapter);
                    int pocx = mCountryModel.size();
                    mCallback.onArticleSelected(pocx);

                }
            });

        }

    }
    //koniec loadall

}
