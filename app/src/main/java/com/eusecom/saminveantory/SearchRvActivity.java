package com.eusecom.saminveantory;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.eusecom.saminveantory.TabFragments.TabOneFragment;
import com.eusecom.saminveantory.TabFragments.TabThreeFragment;
import com.eusecom.saminveantory.TabFragments.TabTwoFragment;


public class SearchRvActivity extends AppCompatActivity implements TabOneFragment.OnHeadlineSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_action_person,
            R.drawable.ic_action_group,
            R.drawable.ic_action_call
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchrv_activity);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabOneFragment(), getResources().getString(R.string.popiseditname));
        adapter.addFragment(new TabTwoFragment(), getResources().getString(R.string.popisean));
        adapter.addFragment(new TabThreeFragment(), getResources().getString(R.string.popiscis));
        viewPager.setAdapter(adapter);

    }


    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article
        //Log.d("I am at  ", "onArticleSelected");
        //Toast.makeText(getApplicationContext(), "I am at onArticleSelected", Toast.LENGTH_SHORT).show();

        String poss = position + "";
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setTag(0).setText(getResources().getString(R.string.popiseditname) + " " + poss));
        tabLayout.addTab(tabLayout.newTab().setTag(1).setText(getResources().getString(R.string.popisean)));
        tabLayout.addTab(tabLayout.newTab().setTag(1).setText(getResources().getString(R.string.popiscis)));

    }

}
