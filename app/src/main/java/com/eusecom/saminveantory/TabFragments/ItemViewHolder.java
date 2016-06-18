package com.eusecom.saminveantory.TabFragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eusecom.saminveantory.R;

/**
 * Created by iFocus on 29-10-2015.
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {

    private TextView name_TextView;
    private TextView iso_TextView;

    public ItemViewHolder(View itemView) {
        super(itemView);
        name_TextView = (TextView) itemView.findViewById(R.id.country_name);
        iso_TextView = (TextView) itemView.findViewById(R.id.country_iso);

    }

    public void bind(CountryModel countryModel) {
        name_TextView.setText(countryModel.getName());
        iso_TextView.setText(countryModel.getisoCode());

    }

}
