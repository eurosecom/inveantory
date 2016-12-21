package com.eusecom.saminveantory.FakFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eusecom.saminveantory.R;

import java.util.ArrayList;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<CountryModel> mCountryModel;
    private Context mContext;
    private Activity mActivity;
//    private List<CountryModel> mOriginalCountryModel;

    public RVAdapter(Activity activity, Context context, List<CountryModel> countryModel) {
        mCountryModel = new ArrayList<>(countryModel);
        mContext = context;
        mActivity = activity;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
//        final ExampleModel model = mModels.get(position);
//        holder.bind(model);
        final CountryModel model = mCountryModel.get(i);
        itemViewHolder.bind(model);


        itemViewHolder.setClickListener(new ItemViewHolder.ClickListener() {
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {




                } else {

                    String poslx = pos + "";
                    Log.d("poslx ", poslx);

                    TextView nameclick = (TextView) v.findViewById(R.id.country_name);
                    String poslname=nameclick.getText().toString();
                    Log.d("posl name ", poslname);

                    TextView isoclick= (TextView) v.findViewById(R.id.country_iso);
                    String poslean=isoclick.getText().toString();

                    Toast.makeText(mContext, "longclick pos. name " + poslname + " " + poslean, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent();
                    i.putExtra("pidx", poslean);
                    mActivity.setResult(101, i);
                    mActivity.finish();

                }
            }
        });

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_rowsearchfak, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mCountryModel.size();
    }

    /** Filter Logic**/
    public void animateTo(List<CountryModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);

    }

    private void applyAndAnimateRemovals(List<CountryModel> newModels) {


        for (int i = mCountryModel.size() - 1; i >= 0; i--) {
            final CountryModel model = mCountryModel.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<CountryModel> newModels) {

        for (int i = 0, count = newModels.size(); i < count; i++) {
            final CountryModel model = newModels.get(i);
            if (!mCountryModel.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<CountryModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final CountryModel model = newModels.get(toPosition);
            final int fromPosition = mCountryModel.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public CountryModel removeItem(int position) {
        final CountryModel model = mCountryModel.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, CountryModel model) {
        mCountryModel.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final CountryModel model = mCountryModel.remove(fromPosition);
        mCountryModel.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }



}
