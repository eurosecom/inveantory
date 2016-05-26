/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eusecom.saminveantory;
/* The Projekt began 1.5.2016 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    private String[] navMenuTitles;
    int ICONS[] = {R.drawable.ic_search_black_24dp,R.drawable.ic_search_black_24dp,R.drawable.ic_search_black_24dp,
            R.drawable.ic_search_black_24dp,R.drawable.ic_search_black_24dp,R.drawable.ic_settings_black_24dp};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "EuroSecom";
    String EMAIL = "edcom@edcom.sk";
    int PROFILE = R.drawable.add2new;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    Button btnEan, btnEanno;
    String incomplet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_inveantory);

    /* Assinging the toolbar object ot the view
    and setting the the Action bar to our toolbar
     */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        incomplet = "0";
        String serverx = SettingsActivity.getServerName(this);
        if (serverx != null && !serverx.isEmpty()) {
            // doSomething
        }else {
            serverx="www.ala.sk/androideshop";
        }

        String delims = "[/]+";
        String[] serverxxx = serverx.split(delims);

        if( serverxxx.length < 2 ) {
            serverx="www.ala.sk/androideshop";
            serverxxx = serverx.split(delims);
        }

        //Create Folder
        File folder = new File(Environment.getExternalStorageDirectory().toString()+"/eusecom/" + serverxxx[1] + "/inventura");
        if(!folder.exists()) {
            folder.mkdirs();
        }

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        String TITLES[] = {navMenuTitles[0],navMenuTitles[1],navMenuTitles[2],navMenuTitles[3]
                ,navMenuTitles[4],navMenuTitles[5]};

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MyAdapter(this, TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        // btn ean control stock
        btnEan = (Button) findViewById(R.id.btnEan);
        btnEan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), InventuraSDActivity.class);
                startActivity(i);

            }
        });

        // btn ean no stock
        btnEanno = (Button) findViewById(R.id.btnEanno);
        btnEanno.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), InventuraNOActivity.class);
                startActivity(i);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
            return true;
        }

        if (id == R.id.delinventory) {

            Intent idm = new Intent(getApplicationContext(), InvsetActivity.class);
            Bundle extrasdm = new Bundle();
            extrasdm.putString("page", "0");
            idm.putExtras(extrasdm);
            startActivity(idm);
            return true;
        }

        if (id == R.id.delinventory_nostore) {

            Intent idm = new Intent(getApplicationContext(), InvsetActivity.class);
            Bundle extrasdm = new Bundle();
            extrasdm.putString("page", "2");
            idm.putExtras(extrasdm);
            startActivity(idm);
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
}