

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

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;


//toto slidovanie pouzivam len ked slidujem nie zoznamy


/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because {@link ViewPager}
 * automatically plays such an animation when calling {@link ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 *
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 */
public class ZostavyActivity extends FragmentActivity {
	
	
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    
    private static final String TAG_PAGEX = "page";
    private static final String TAG_POKLX = "pokl";
    String pagex;
    String poklx;
    
 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });
        
     // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        pagex = extras.getString(TAG_PAGEX);
        poklx = extras.getString(TAG_POKLX);
        
        int pageint = Integer.parseInt(pagex);
        
        mPager.setCurrentItem(pageint);
        
        
    }
    //koniec oncreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);
        menu.findItem(R.id.action_next).setEnabled(mPager.getCurrentItem() < 2);
        if(mPager.getCurrentItem() == 0){ this.setTitle(getResources().getString(R.string.zostavyean)); }
        if(mPager.getCurrentItem() == 1){ this.setTitle(getResources().getString(R.string.rozdielyean)); }
        if(mPager.getCurrentItem() == 2){ this.setTitle(getResources().getString(R.string.exportcsv1)); }
        if(mPager.getCurrentItem() == 3){ this.setTitle("zrusene"); }
        if(mPager.getCurrentItem() == 4){ this.setTitle("zrusene"); }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
    	switch (item.getItemId()) {
        
            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;
        }
    
        return super.onOptionsItemSelected(item);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ZostavyPageFragment.create(position); 
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
    //koniec screenslidepageradapter
    
//koniec screenslideactivity
} 
