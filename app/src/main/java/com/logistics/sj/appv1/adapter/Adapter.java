package com.logistics.sj.appv1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.logistics.sj.appv1.R;
import com.logistics.sj.appv1.model.TruckBean;
import com.logistics.sj.appv1.ui.Primary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AJ on 03-05-2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.TruckViewHolder> {

    private List<TruckBean> truckArray;//list of truckObject
    private LayoutInflater inflater;
    private ItemClickCallBack itemClickCallBack;

    public Adapter(List<TruckBean> truckArray, Context context){
        this.truckArray=truckArray;
        this.inflater=LayoutInflater.from(context);
    }

    //load the view from record xml
    @Override
    public TruckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recordView=inflater.inflate(R.layout.record_primary,parent,false);
        TruckViewHolder truckViewHolder=new TruckViewHolder(recordView);
        return truckViewHolder;
    }

    /////populate the recordview with values.hopefully gets acalled multiple times
    @Override
    public void onBindViewHolder(TruckViewHolder holder, int position) {
        TruckBean truckBean=truckArray.get(position);
        holder.truckNo.setText(truckBean.getTruckNumber());
        //TODO image to be paassed to the viewholder
        //holder.icon.setImageResource(truckBean.getIcon());
    }

    @Override
    public int getItemCount() {
        return truckArray.size();
    }

    public void setFilter(ArrayList<TruckBean> searchList) {
        truckArray=new ArrayList<TruckBean>();
        truckArray.addAll(searchList);
        notifyDataSetChanged();
    }

    ////ViewHolder truckrecord xml
    public class TruckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView truckNo;
        private ImageView icon;
        private View container;
        public TruckViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            truckNo=(TextView)itemView.findViewById(R.id.item_text);
            //TODO iconImage to be discussed
           // icon=(ImageView)itemView.findViewById(R.id.item_icon);
            container = (View) itemView.findViewById(R.id.record_root);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           itemClickCallBack.onItemClick(getAdapterPosition());
               //to handle multiple objects from record
//               if (v.getId()==R.id.record_root)
//               {itemClickCallBack.onItemClick(getAdapterPosition());
//               }else {
//                   Log.e("tagg","onclick "+v.getId()+"     ");
//               }

           //}
        }

    }

    //to be cleared
    public interface ItemClickCallBack{
        void onItemClick(int p);
    }

    public void setItemClickCallBack(final ItemClickCallBack itemClickCallBack) {
        this.itemClickCallBack=itemClickCallBack;
    }

}
