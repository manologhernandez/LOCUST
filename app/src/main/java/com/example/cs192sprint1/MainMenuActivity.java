/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author/s: Manolo Hernandez

Code History
    1/21/20 - File Created by Manolo Hernandez. Added functionality for button.
*/

package com.example.cs192sprint1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

     Button manageReceiversBtn; // button for going to the manage receivers screen

     /*
     Method Name: onCreate
     Creation date: 1/21/20
     Purpose: loads the view and connects the elements to the code.
     Calling Arguments: n/a
     Required Files: n/a
     Return Value: n/a
      */

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main_menu);

          manageReceiversBtn = (Button) findViewById(R.id.manage_receivers);

          manageReceiversBtn.setOnClickListener(new View.OnClickListener() {
               /*
               Method Name: onClick
               Creation date: 1/21/20
               Purpose: Starts a new Intent to go to the Manage Receivers page
               Calling Arguments: n/a
               Required Files: n/a
               Return Value: n/a
                */
               @Override
               public void onClick(View v) {
                    Intent manageReceiversView = new Intent(getApplicationContext(), ManageReceiversActivity.class);
                    startActivity(manageReceiversView);
               }
          });
     }
}
