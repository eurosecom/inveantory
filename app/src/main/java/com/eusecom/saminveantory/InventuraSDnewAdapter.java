package com.eusecom.saminveantory;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Wasabeef on 2015/01/03.
 */
@SuppressWarnings("deprecation")
public class InventuraSDnewAdapter extends RecyclerView.Adapter<InventuraSDnewAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mText;
    private List<String> mMnovalue;
    private List<String> mPricevalue;
    private List<String> mIdx;

    interface DoSomething2 {

        void doChangeItem(String itemx);
    }

    DoSomething2 myDoSomething2CallBack;

    public InventuraSDnewAdapter(DoSomething2 callback, Context context, List<String> Text, List<String> Mnovalue,
    		List<String> Pricevalue, List<String> Idx) {
    	
        mContext = context;
        mText = Text;
        mMnovalue = Mnovalue;
        mPricevalue = Pricevalue;
        mIdx = Idx;
        myDoSomething2CallBack = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {   	
        
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_list_invsd, parent, false);
        return new ViewHolder(v);
        
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext).load(R.drawable.add2new).into(holder.image);
        holder.text.setText(mText.get(position));
        holder.mnovalue.setText(mMnovalue.get(position));
        holder.pricevalue.setText(mPricevalue.get(position));
        holder.idx.setText(mIdx.get(position));
        
        holder.setClickListener(new InventuraSDnewAdapter.ViewHolder.ClickListener() {
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {

                    String poslx = pos + "";
                    String cplx = mIdx.get(pos);
                    //Toast.makeText(mContext, "longclick pos. " + poslx + " idx " + cplx, Toast.LENGTH_SHORT).show();
                    //toggleSelection(pos);

                    //Intent i = new Intent(mContext, ZmazInventuraSDActivity.class);
                    //Bundle extras = new Bundle();
                    //extras.putString("cat", cplx);
                    //extras.putString("odk", "0");
                    //i.putExtras(extras);
                    //v.getContext().startActivity(i);
                    myDoSomething2CallBack.doChangeItem(cplx);
                    v.showContextMenu();


                	
                } else {

                    String poslx = pos + "";
                    String cplx = mIdx.get(pos);
                    //Toast.makeText(mContext, "shortclick pos. " + poslx + " idx " + cplx, Toast.LENGTH_SHORT).show();
                    myDoSomething2CallBack.doChangeItem(cplx);
                    v.showContextMenu();

                }
            }
        });
        
    }


    @Override
    public int getItemCount() {
        return mText.size();
    }

    public void remove(int position) {
        mText.remove(position);
        notifyItemRemoved(position);
    }

    public void add(String text, int position) {
        mText.add(position, text);
        notifyItemInserted(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView image;
        public TextView text;
        public TextView mnovalue;
        public TextView pricevalue;
        public TextView idx;
        private ClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            pricevalue = (TextView) itemView.findViewById(R.id.pricevalue);
            mnovalue = (TextView) itemView.findViewById(R.id.mnovalue);
            idx = (TextView) itemView.findViewById(R.id.idx);
            
            // We set listeners to the whole item view, but we could also
            // specify listeners for the title or the icon.
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            
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
}
