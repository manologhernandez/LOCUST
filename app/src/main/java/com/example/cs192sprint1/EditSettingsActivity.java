/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Ricardo Puato III and Manolo Hernandez

Code History
     2/4/20 - File Created by Ricardo Puato.
             - Added functionality for UI
     2/5/20 - Manolo added functionality for buttons, and saving input.
*/
package com.example.cs192sprint1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class EditSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

     @Override
     protected void onCreate(Bundle savedInstanceState){
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_edit_settings);

          final Spinner sexArray = findViewById(R.id.editSettingsInputSex);
          ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
          adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          sexArray.setAdapter(adapter);
          sexArray.setOnItemSelectedListener(this);

          final Spinner freqArray = findViewById(R.id.editSettingsInputFreq);
          ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.freq, android.R.layout.simple_spinner_item);
          adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          freqArray.setAdapter(adapter2);
          freqArray.setOnItemSelectedListener(this);

          final EditText name = findViewById(R.id.editSettingsInputName);
          final EditText age = findViewById(R.id.editSettingsInputAge);
          final EditText defaultMsg = findViewById(R.id.editTextInputDefMes);

          Button cancelBtn = findViewById(R.id.editSettingsCancel);
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
//                    Intent goBackToMain = new Intent(getApplicationContext(), MainMenuActivity.class);
//                    startActivity(goBackToMain);
                    Settings s = new Settings(getApplicationContext());
                    s.readSettings();
               }
          });

          Button saveBtn = findViewById(R.id.editSettingsSave);
          saveBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    String userName = name.getText().toString();
                    String inputAge = age.getText().toString();
                    String userDefaultMsg = defaultMsg.getText().toString();
                    String userSex = sexArray.getSelectedItem().toString();
                    String userFreq = freqArray.getSelectedItem().toString();

                    if(userName.equals("") || inputAge.equals("") || userDefaultMsg.equals("") || userSex.equals("") || userFreq.equals("")){
                         Toast.makeText(getApplicationContext(), "Missing input fields", Toast.LENGTH_SHORT).show();
                    }else{
                         int userAge = Integer.parseInt(inputAge);
                         Settings s = new Settings(getApplicationContext());
                         try {
                              s.saveSettings(userName, userAge, userSex, userFreq, userDefaultMsg);
                         } catch (JSONException e) {
                              e.printStackTrace();
                         }

                         Intent goBackToMain = new Intent(getApplicationContext(), MainMenuActivity.class);
                         startActivity(goBackToMain);
                    }

               }
          });
     }

     @Override
     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//          String text = parent.getItemAtPosition(position).toString();
//          if(text.equals("")){
//               //nothing selected
//          }else{
//               //Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
//          }

     }

     @Override
     public void onNothingSelected(AdapterView<?> parent) {

     }
}
