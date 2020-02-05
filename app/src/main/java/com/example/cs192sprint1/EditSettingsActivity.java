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
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class EditSettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

     @Override
     protected void onCreate(Bundle savedInstanceState){
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_edit_settings);

          final Spinner sexArray = findViewById(R.id.editSettingsInputSex);
          final Spinner freqArray = findViewById(R.id.editSettingsInputFreq);
          final EditText name = findViewById(R.id.editSettingsInputName);
          final EditText age = findViewById(R.id.editSettingsInputAge);
          final EditText defaultMsg = findViewById(R.id.editTextInputDefMes);
          final Button cancelBtn = findViewById(R.id.editSettingsCancel);
          final Button saveBtn = findViewById(R.id.editSettingsSave);
          ArrayAdapter<CharSequence> sexAdapter;
          ArrayAdapter<CharSequence> freqAdapter;
          final Settings s = new Settings(getApplicationContext());
          JSONObject currentSettings;
          int spinnerPos1, spinnerPos2;

          sexAdapter = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
          sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          sexArray.setAdapter(sexAdapter);
          sexArray.setOnItemSelectedListener(this);

          freqAdapter = ArrayAdapter.createFromResource(this, R.array.freq, android.R.layout.simple_spinner_item);
          freqAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          freqArray.setAdapter(freqAdapter);
          freqArray.setOnItemSelectedListener(this);

          //Auto-populate with current settings

          if(s.exists()){
               try {
                    currentSettings = s.readSettings();
                    name.setText(currentSettings.get("Name").toString());
                    age.setText(currentSettings.get("Age").toString());
                    defaultMsg.setText(currentSettings.get("Default Message").toString());
                    spinnerPos1 = sexAdapter.getPosition(currentSettings.get("Sex").toString());
                    sexArray.setSelection(spinnerPos1);
                    spinnerPos2 = freqAdapter.getPosition(currentSettings.get("Location Frequency").toString());
                    freqArray.setSelection(spinnerPos2);
               } catch (ParseException e) {
                    e.printStackTrace();
               } catch (IOException e) {
                    e.printStackTrace();
               }

          }

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
                    Intent goBackToMain = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(goBackToMain);
               }
          });
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
