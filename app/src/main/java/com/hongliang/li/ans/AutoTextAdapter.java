package com.hongliang.li.ans;


import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.mozilla.materialfennec.R;

public class AutoTextAdapter extends RecyclerView.Adapter<AutoTextAdapter.ViewHolder>{

    SparseArray<String> mDatas;

    public AutoTextAdapter( ){
    }

    public void setData(SparseArray<String> datas){
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.autotext_item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mCenter.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView mLeft;
        public TextView mCenter;
//        public TextView mRight;
        public ViewHolder(View view){
            super(view);
            mCenter = (TextView) view.findViewById(R.id.autotext_msg_textview);
        }
    }
}
