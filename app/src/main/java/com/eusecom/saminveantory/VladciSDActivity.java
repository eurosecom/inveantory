package com.eusecom.saminveantory;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class VladciSDActivity extends FragmentActivity implements
		SeznamSDFragment.OnRulerSelectedListener {

	private boolean mDualPane;
    Button btnRefresh;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


    		setContentView(R.layout.vladcisd);
        	// Pokud je dostupne View s id detail, je layout dvousloupcove
    		mDualPane = findViewById(R.id.detail) != null;
    		
    		if (mDualPane) {
    		} else {
            // Buttons
            btnRefresh = (Button) findViewById(R.id.btnRefresh);

      
            // new obj click event
            btnRefresh.setOnClickListener(new View.OnClickListener() {
     
                @Override
                public void onClick(View view) {
                    // Launching All products Activity
                    Intent i = new Intent(getApplicationContext(), VladciSDActivity.class);
                    startActivity(i);
                    finish();
     
                }
            });
    						}
        	
		
	}
//koniec oncreate
		
	@Override
	public void onRulerSelected(int index) {
		if (mDualPane) { // Dvousloupcovy layout
			DetailSDFragment f = DetailSDFragment.newInstance(index);

			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.replace(R.id.detail, f);
			ft.addToBackStack(null);
			ft.commit();
		} else { // Jednosloupcovy layout
			Intent i = new Intent(this, DetailSDActivity.class);
			i.putExtra(DetailSDActivity.INDEX, index);
			startActivity(i);
		}
	}
	
	
	
		//optionsmenu
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();

			inflater.inflate(R.menu.options_menuobjsd, menu);

			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {

			switch (item.getItemId()) {
			case R.id.deleteall:
				


				return true;
				
			case R.id.endorder:
				
				finish();

				return true;

				
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		//koniec optionsmenu
	
	
	
	
}
