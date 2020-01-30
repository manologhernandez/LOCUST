/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author/s: Manolo Hernandez and Ricardo Puato III

Code History
     1/22/20 - Set up RecyclerView view. Made by Manolo Hernandez
     1/28/20 - Set up buttons and layout. Made by Ricardo Puato III
*/

package com.example.cs192sprint1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManageReceiversActivity extends AppCompatActivity  {

     Button addReceiverBtn;             //button to go to add receivers page
     Button removeReceiverBtn;          //button to go to remove receivers page
     ImageButton backToMainMenuBtn;     //button to go back to the main menu
     MyAdapter adapter;                 //adapter variable to populate the recycler view.

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_manage_receivers);

          final Receivers receivers = new Receivers(getApplicationContext());
          final ArrayList<String> receiversList = receivers.getAllReceivers();

          // set up the RecyclerView
          RecyclerView recyclerView = findViewById(R.id.manageReceiversList);
          recyclerView.setLayoutManager(new LinearLayoutManager(this));
          adapter = new MyAdapter(this, receiversList);
          recyclerView.setAdapter(adapter);
          DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
          recyclerView.addItemDecoration(dividerItemDecoration);

          addReceiverBtn = (Button) findViewById(R.id.addReceiverBtn);
          removeReceiverBtn = (Button) findViewById(R.id.removeReceiverBtn);
          backToMainMenuBtn = (ImageButton) findViewById(R.id.manageReceiversBackBtn);

          addReceiverBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    Intent addReceiverView = new Intent(getApplicationContext(), AddReceiverActivity.class);
                    startActivity(addReceiverView);
               }
          });

          removeReceiverBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    Intent removeReceiverView = new Intent(getApplicationContext(), RemoveReceiverActivity.class);
                    startActivity(removeReceiverView);
               }
          });

          backToMainMenuBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    Intent mainMenuView = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(mainMenuView);
               }
          });
     }
}
