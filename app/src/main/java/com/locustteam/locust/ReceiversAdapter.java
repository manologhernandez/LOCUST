package com.locustteam.locust;

/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Ricardo Puato III

Code History
     1/22/20 - File Created by Ricardo Puato III
*/

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReceiversAdapter extends RecyclerView.Adapter<ReceiversAdapter.ReceiversViewHolder> {
    private ArrayList<Receivers> mReceiversList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /*
    Method Name: ReceiversViewHolder
    Creation date: 1/21/20
    Purpose: connects the UI to the recycler
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    public static class ReceiversViewHolder extends RecyclerView.ViewHolder{
        public TextView mName;
        public TextView mPhone;
        public ImageView mDelete;

        public ReceiversViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mName = itemView.findViewById(R.id.recycler_name);
            mPhone = itemView.findViewById(R.id.recycler_phone);
            mDelete = itemView.findViewById(R.id.recycler_delete);

            mDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ReceiversAdapter(ArrayList<Receivers> receiversList){
        mReceiversList = receiversList;
    }

    /*
    Method Name: onCreateViewHolder
    Creation date: 1/21/20
    Purpose: loads the view
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    @NonNull
    @Override
    public ReceiversViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiver_item, parent, false);
        ReceiversViewHolder rvh = new ReceiversViewHolder(v, mListener);
        return rvh;
    }

    /*
    Method Name: onBindViewHolder
    Creation date: 1/21/20
    Purpose: populates the recyclerview
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    @Override
    public void onBindViewHolder(@NonNull ReceiversViewHolder holder, int position) {
        Receivers currentItem = mReceiversList.get(position);

        holder.mName.setText(currentItem.getName());
        holder.mPhone.setText(currentItem.getPhone());
    }

    @Override
    public int getItemCount() {
        return mReceiversList.size();
    }
}
