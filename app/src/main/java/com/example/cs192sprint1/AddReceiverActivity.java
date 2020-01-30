/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Manolo Hernandez

Code History
     1/21/20 - File Created by Manolo Hernandez.
             - Added functionality for adding a receiver given the text input.
*/

package com.example.cs192sprint1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddReceiverActivity extends AppCompatActivity {

     EditText receiverName; //Input field for the receiver name
     EditText receiverNum;  //Input field for the receiver number
     Button cancelBtn;      //button to cancel the adding of a receiver
     Button saveBtn;        //button to save the new receiver

     /*
     Method Name: onCreate
     Creation date: 1/21/20
     Purpose: To load the view and connect the elements to the code
     Calling Arguments: savedInstanceState
     Required Files: n/a
     Return Value: n/a
      */
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_add_receiver);

          receiverName = (EditText) findViewById(R.id.inputNameAddReceiver);
          receiverNum = (EditText) findViewById(R.id.inputNumberAddReceiver);
          cancelBtn = (Button) findViewById(R.id.cancelAddReceiverBtn);
          saveBtn = (Button) findViewById(R.id.saveAddReceiverBtn);
          final Receivers receivers = new Receivers(getApplicationContext());


          saveBtn.setOnClickListener(new View.OnClickListener() {
               /*
               Method Name: onClick
               Creation date: 1/21/20
               Purpose: takes the contents of the edit text and saves them as a new receiver.
               Calling Arguments:
               Required Files: n/a
               Return Value: n/a
                */
               @Override
               public void onClick(View v) {
                    String newName = receiverName.getText().toString();
                    String newNumber = receiverNum.getText().toString();
                    if (newName.equals("") || newNumber.equals("")){
                         Toast.makeText(getApplicationContext(), "Missing Requirements!", Toast.LENGTH_SHORT).show();
                    }else{
                         receivers.addReceiver(newName, newNumber);
                         Intent goBackToMain = new Intent(getApplicationContext(), ManageReceiversActivity.class);
                         startActivity(goBackToMain);
                    }
               }
          });

          cancelBtn.setOnClickListener(new View.OnClickListener() {
               /*
               Method Name: onClick
               Creation date: 1/21/20
               Purpose: Starts an Intent to go back to the main menu
               Calling Arguments:
               Required Files: n/a
               Return Value: n/a
                */
               @Override
               public void onClick(View v) {
                    Intent goBackToMain = new Intent(getApplicationContext(), ManageReceiversActivity.class);
                    startActivity(goBackToMain);
               }
          });
     }


}
