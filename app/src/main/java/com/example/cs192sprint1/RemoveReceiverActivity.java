/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Manolo Hernandez and Ricardo Puato III

Code History
     1/22/20 - File created by Manolo Hernandez
             - Added functionality for removing a receiver from the recycler view.
     1/28/20 - Added layout and Intent changes. Made by Ricardo Puato III.

*/

package com.example.cs192sprint1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class RemoveReceiverActivity extends AppCompatActivity implements MyAdapter.ItemClickListener {
     MyAdapter adapter;                      //adapter to populate the recycler view
     ImageButton backToManageReceiversBtn;   //button to return to the manage receivers screen.
     /*
     Method Name: onCreate
     Creation date: 1/22/20
     Purpose: to load the view and set up the recycler view.
     Calling Arguments: n/a
     Required Files: n/a
     Return Value: n/a
      */
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_remove_receiver);
          final Receivers receivers = new Receivers(getApplicationContext());
          final ArrayList<String> receiversList = receivers.getAllReceivers();
          RecyclerView recyclerView = findViewById(R.id.receiversRecyclerView);
          recyclerView.setLayoutManager(new LinearLayoutManager(this));
          adapter = new MyAdapter(this, receiversList);
          adapter.setClickListener(this);
          recyclerView.setAdapter(adapter);
          DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
          recyclerView.addItemDecoration(dividerItemDecoration);

          backToManageReceiversBtn = (ImageButton) findViewById(R.id.removeReceiversBackBtn);

          backToManageReceiversBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    Intent manageReceiversView = new Intent(getApplicationContext(), ManageReceiversActivity.class);
                    startActivity(manageReceiversView);
               }
          });
     }
     /*
     Method Name: onItemClick
     Creation date: 1/22/20
     Purpose: to delete the receiver that was clicked
     Calling Arguments: view, position
     Required Files: n/a
     Return Value: n/a
      */
     public void onItemClick(View view, int position)  {
          Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();

          final Receivers receivers = new Receivers(getApplicationContext());
          final ArrayList<String> receiversList = receivers.getAllReceivers();

          try{
               String itemtoDelete = adapter.getItem(position);
               receiversList.remove(position);
               adapter.notifyItemRemoved(position);
               Toast.makeText(getApplicationContext(), "Receiver Removed.", Toast.LENGTH_SHORT).show();
               try {
                    receivers.deleteReceiver(itemtoDelete);
               } catch (IOException e) {
                    e.printStackTrace();
               }

          }catch (IndexOutOfBoundsException e){
               Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
          }

          Intent goBackToMain = new Intent(getApplicationContext(), ManageReceiversActivity.class);
          startActivity(goBackToMain);


          //int receiverItem = Integer.parseInt(adapter.getItem(position));
     }
}
