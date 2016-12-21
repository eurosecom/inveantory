package com.eusecom.saminveantory.FakFragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eusecom.saminveantory.R;

/**
 * Created by iFocus on 29-10-2015.
 */
public class ItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener {

    private TextView name_TextView;
    private TextView iso_TextView;
    private TextView hod_TextView;
    private ClickListener clickListener;

    public ItemViewHolder(View itemView) {
        super(itemView);
        name_TextView = (TextView) itemView.findViewById(R.id.country_name);
        iso_TextView = (TextView) itemView.findViewById(R.id.country_iso);
        hod_TextView = (TextView) itemView.findViewById(R.id.country_hod);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }

    public void bind(CountryModel countryModel) {
        name_TextView.setText(countryModel.getName());
        iso_TextView.setText(countryModel.getisoCode());
        hod_TextView.setText(countryModel.getfakhod());

    }

    /* Interface for handling clicks - both normal and long ones. */
    public interface ClickListener {

        /**
         * Called when the view is clicked.
         *
         * @param v view that is clicked
         * @param position of the clicked item
         * @param isLongClick true if long click, false otherwise
         */
        public void onClick(View v, int position, boolean isLongClick);

    }

    /* Setter for listener. */
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {

        // If not long clicked, pass last variable as false.
        clickListener.onClick(v, getPosition(), false);
    }

    @Override
    public boolean onLongClick(View v) {

        // If long clicked, passed last variable as true.
        clickListener.onClick(v, getPosition(), true);
        return true;
    }

}
