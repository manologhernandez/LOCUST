/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Manolo Hernandez

Code History
     1/22/20 - File Created by Manolo Hernandez
             - Added functionality for populating RecyclerView with row data of Receivers.
             - Added helper functions
*/

package com.example.cs192sprint1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

     private List<String> mData; //List of data that will be shown in the recycler view
     private LayoutInflater mInflater; //Used for the view of items
     private ItemClickListener mClickListener; //Used to see which row was clicked

     /*
     Method Name: MyAdapter
     Creation date: 1/22/20
     Purpose: Sets the layout inflater and the data list.
     Calling Arguments: context, data
     Required Files: n/a
     Return Value: n/a
      */
     MyAdapter(Context context, List<String> data) {
          this.mInflater = LayoutInflater.from(context);
          this.mData = data;
     }
     /*
     Method Name: onCreateViewHolder
     Creation date: 1/22/20
     Purpose: inflates the row layout from xml when needed
     Calling Arguments: parent, viewType
     Required Files: n/a
     Return Value: ViewHolder
      */
     @Override
     public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
          return new ViewHolder(view);
     }
      /*
     Method Name: onBindViewHolder
     Creation date: 1/22/20
     Purpose: binds the data to the TextView in each row
     Calling Arguments: holder, position
     Required Files: n/a
     Return Value: ViewHolder
      */
     @Override
     public void onBindViewHolder(ViewHolder holder, int position) {
          String receiver = mData.get(position);
          holder.myTextView.setText(receiver);
     }

     /*
     Method Name: getItemCount
     Creation date: 1/22/20
     Purpose: getter function to get the size of the receivers list.
     Calling Arguments:
     Required Files: n/a
     Return Value: mData.size
     */
     @Override
     public int getItemCount() {
          return mData.size();
     }


     public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
          TextView myTextView;

          ViewHolder(View itemView) {
               super(itemView);
               myTextView = itemView.findViewById(R.id.tvAnimalName);
               itemView.setOnClickListener(this);
          }

          @Override
          public void onClick(View view) {
               if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
          }

     }

     /*
     Method Name: getItem
     Creation date: 1/22/20
     Purpose: getter function to get the data in the receivers list.
     Calling Arguments: id
     Required Files: n/a
     Return Value: mData.get(id)
      */
     public String getItem(int id) {
          return mData.get(id);
     }
     /*
     Method Name: setClickListener
     Creation date: 1/22/20
     Purpose: used so that we can track on click events.
     Calling Arguments: itemClickListener
     Required Files: n/a
     Return Value: n/a
      */
     // allows clicks events to be caught
     void setClickListener(ItemClickListener itemClickListener) {
          this.mClickListener = itemClickListener;
     }

     public interface ItemClickListener {
          void onItemClick(View view, int position);
     }
}