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
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {CardFlipActivity} and {
 * ScreenSlideActivity} samples.</p>
 */
public class ZostavyPageFragment extends Fragment {
	
	
	private static final String TAG_PAGEY = "pagey";
    private static final String TAG_CATY = "caty";

	
	
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ZostavyPageFragment create(int pageNumber) {
        ZostavyPageFragment fragment = new ZostavyPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ZostavyPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        
    	ViewGroup rootView = null;
    	
    	switch (mPageNumber) {

        case 0:
        	rootView = (ViewGroup) inflater.inflate(R.layout.stavean, container, false);
        	
        	// btn ean
        	Button btnEan = (Button)rootView.findViewById(R.id.btnEan);
            btnEan.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Launching All products Activity
                    Intent i = new Intent(getActivity(), InventuraSDActivity.class);
                    startActivity(i);
                    getActivity().finish();

                }
            });
            

            Button btnStavean = (Button)rootView.findViewById(R.id.btnStavean);
            btnStavean.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	 Intent i = new Intent(getActivity(), PokladnicaActivity.class);
                     Bundle extras = new Bundle();
                     extras.putString(TAG_CATY, "1");
                     extras.putString(TAG_PAGEY, "1");
                     i.putExtras(extras);
                     startActivity(i);
             	   
                }
            });
            

            Button btnStavcis = (Button)rootView.findViewById(R.id.btnStavcis);
            btnStavcis.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	 Intent i = new Intent(getActivity(), PokladnicaActivity.class);
                     Bundle extras = new Bundle();
                     extras.putString(TAG_CATY, "2");
                     extras.putString(TAG_PAGEY, "1");
                     i.putExtras(extras);
                     startActivity(i);
             	   
                }
            });
            
            Button btnStavnat = (Button)rootView.findViewById(R.id.btnStavnat);
            btnStavnat.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	 Intent i = new Intent(getActivity(), PokladnicaActivity.class);
                     Bundle extras = new Bundle();
                     extras.putString(TAG_CATY, "3");
                     extras.putString(TAG_PAGEY, "1");
                     i.putExtras(extras);
                     startActivity(i);
             	   
                }
            });

            Button btnStavhod = (Button)rootView.findViewById(R.id.btnStavhod);
            btnStavhod.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	 Intent i = new Intent(getActivity(), PokladnicaActivity.class);
                     Bundle extras = new Bundle();
                     extras.putString(TAG_CATY, "4");
                     extras.putString(TAG_PAGEY, "1");
                     i.putExtras(extras);
                     startActivity(i);
             	   
                }
            });

            
            
            
         

        	break;

        case 1:
        	rootView = (ViewGroup) inflater.inflate(R.layout.rozdielyean, container, false);

        	// btn ean
        	Button btnEan2 = (Button)rootView.findViewById(R.id.btnEan2);
            btnEan2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Launching All products Activity
                    Intent i = new Intent(getActivity(), InventuraSDActivity.class);
                    startActivity(i);
                    getActivity().finish();

                }
            });
            
            Button btnRozdean = (Button)rootView.findViewById(R.id.btnRozdean);
            btnRozdean.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	 Intent i = new Intent(getActivity(), RozdielyActivity.class);
                     Bundle extras = new Bundle();
                     extras.putString(TAG_CATY, "1");
                     extras.putString(TAG_PAGEY, "1");
                     i.putExtras(extras);
                     startActivity(i);
             	   
                }
            });
            
            Button btnRozdcis = (Button)rootView.findViewById(R.id.btnRozdcis);
            btnRozdcis.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	 Intent i = new Intent(getActivity(), RozdielyActivity.class);
                     Bundle extras = new Bundle();
                     extras.putString(TAG_CATY, "2");
                     extras.putString(TAG_PAGEY, "1");
                     i.putExtras(extras);
                     startActivity(i);
             	   
                }
            });
            
            Button btnRozdnat = (Button)rootView.findViewById(R.id.btnRozdnat);
            btnRozdnat.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	 Intent i = new Intent(getActivity(), RozdielyActivity.class);
                     Bundle extras = new Bundle();
                     extras.putString(TAG_CATY, "3");
                     extras.putString(TAG_PAGEY, "1");
                     i.putExtras(extras);
                     startActivity(i);
             	   
                }
            });
            
            Button btnRozdhod = (Button)rootView.findViewById(R.id.btnRozdhod);
            btnRozdhod.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	 Intent i = new Intent(getActivity(), RozdielyActivity.class);
                     Bundle extras = new Bundle();
                     extras.putString(TAG_CATY, "4");
                     extras.putString(TAG_PAGEY, "1");
                     i.putExtras(extras);
                     startActivity(i);
             	   
                }
            });
            
        	
        	
        	break;
        	
        case 2:
        	
        	rootView = (ViewGroup) inflater.inflate(R.layout.exportcsv, container, false);
        	
        	// btn ean
        	Button btnEan3 = (Button)rootView.findViewById(R.id.btnEan3);
            btnEan3.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Launching All products Activity
                    Intent i = new Intent(getActivity(), InventuraSDActivity.class);
                    startActivity(i);
                    getActivity().finish();

                }
            });
            

        	Button btnPrenosstav = (Button)rootView.findViewById(R.id.btnPrenosstav);
        	btnPrenosstav.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Launching All products Activity
                    Intent i = new Intent(getActivity(), NaserverActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("pagex", "0");
                    extras.putString("fakx", "0");
                    i.putExtras(extras);
                    startActivity(i);
                    getActivity().finish();

                }
            });
        	
        	Button btnPrenosstavno = (Button)rootView.findViewById(R.id.btnPrenosstavno);
        	btnPrenosstavno.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Launching All products Activity
                    Intent i = new Intent(getActivity(), NaserverActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("pagex", "1");
                    extras.putString("fakx", "0");
                    i.putExtras(extras);
                    startActivity(i);
                    getActivity().finish();

                }
            });
        	
        	Button btnCsvstav = (Button)rootView.findViewById(R.id.btnCsvstav);
        	btnCsvstav.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // Launching All products Activity
                    Intent i = new Intent(getActivity(), PrenosCSVActivity.class);
                    startActivity(i);
                    getActivity().finish();

                }
            });
        	

            
        	break;
        	
        case 3:
        	rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        	// Set the title view to show the page number.
        	//((TextView) rootView.findViewById(android.R.id.text1)).setText(
            //getString(R.string.title_template_step, mPageNumber + 1));
        	break;
        	
        case 4:
        	rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        	// Set the title view to show the page number.
        	//((TextView) rootView.findViewById(android.R.id.text1)).setText(
            //getString(R.string.title_template_step, mPageNumber + 1));
        	break;

        	
    		
    		}
    	
        
        return rootView;
           
    }
    //koniec oncreateview
    
   

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }



}
