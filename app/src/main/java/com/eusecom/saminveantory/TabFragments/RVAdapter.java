package com.eusecom.saminveantory.TabFragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eusecom.saminveantory.R;

import java.util.ArrayList;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private List<CountryModel> mCountryModel;
//    private List<CountryModel> mOriginalCountryModel;

    public RVAdapter(List<CountryModel> countryModel) {
        mCountryModel = new ArrayList<>(countryModel);
//        mOriginalCountryModel = new ArrayList<>(countryModel);
        //  this.mCountryModel = mCountryModel;
        //   this.mOriginalCountryModel = mCountryModel;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
//        final ExampleModel model = mModels.get(position);
//        holder.bind(model);
        final CountryModel model = mCountryModel.get(i);
        itemViewHolder.bind(model);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
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

//    public void flushFilter(List<CountryModel> models) {
//        animateTo(models);
//    }
}
