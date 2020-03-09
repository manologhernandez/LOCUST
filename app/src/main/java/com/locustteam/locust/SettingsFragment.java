package com.locustteam.locust;

/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Ricardo Puato III

Code History
     2/18/20 - File Created by Ricardo Puato III
     3/06/20 - Settings edited by Manolo Hernandez
*/

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsFragment extends Fragment {
    Spinner freqSpinner;
    EditText inputName;
    EditText inputDef;
    Button saveButton;

    /*
    Method Name: onCreateView
    Creation date: 1/21/20
    Purpose: loads the view
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    /*
    Method Name: onActivityCreated
    Creation date: 1/21/20
    Purpose: connects the elements to the code.
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Spinner
        freqSpinner = getView().findViewById(R.id.spinner_settings_freq);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.array_freq, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freqSpinner.setAdapter(adapter);

        //EditText
        inputName = getView().findViewById(R.id.input_settings_name);
        inputDef = getView().findViewById(R.id.input_settings_def);

        //SaveButton
        saveButton = getView().findViewById(R.id.button_settings_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences locationPreference = getActivity().getSharedPreferences(Constants.LOCATION_SHARED_PREFS, getActivity().MODE_PRIVATE);
                if(locationPreference.getBoolean("Location_isSharing", false)){
                    buildWarningDialog();
                }else{
                    //Store Data then Display.
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SETTINGS_SHARED_PREFS, getActivity().MODE_PRIVATE);
                    //if first time saving data, redirect back to home after saving
                    if( sharedPreferences.getString("Settings_Name",null) == null ){
                        saveData();
                        Toast.makeText(getActivity(),"Settings Saved!", Toast.LENGTH_SHORT).show();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        HomeFragment toMain = new HomeFragment();
                        fragmentTransaction.replace(R.id.fragment_container, toMain);
                        fragmentTransaction.commit();
                    }else{
                        if(saveData()){
                            loadData();
                            Toast.makeText(getActivity(),"Settings Saved!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        loadData();
    }

    /*
     Method Name: buildWarningDialog
     Creation date: 03/06/20
     Purpose: to show the user a dialog to warn them about settings changes
     Calling Arguments: n/a
     Required Files: n/a
     Return Value: n/a
      */
    public void buildWarningDialog(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        if(saveData()){
                            loadData();
                            Toast.makeText(getActivity(),"Settings Saved!", Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Locust is currently tracking your location. Changes made in settings will only be incorporated in succeeding SOS trackings.")
                .setTitle("Warning")
                .setPositiveButton("I understand", dialogClickListener)
                .show();
    }

    /*
    Method Name: saveData
    Creation date: 1/21/20
    Purpose: stores the data to sharedPreferences
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: boolean (true or false)
     */
    private boolean saveData() {
        if(inputName.getText().toString().trim().equals("") || inputDef.getText().toString().trim().equals("") || freqSpinner.getSelectedItem().toString().trim().equals("")){
            Toast toast = Toast.makeText(getActivity(),"Please fill up all items before saving.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SETTINGS_SHARED_PREFS, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Settings_Name", inputName.getText().toString() );
        editor.putString("Settings_Def", inputDef.getText().toString() );
        editor.putInt("Settings_Freq", Integer.parseInt(freqSpinner.getSelectedItem().toString().split(" ")[1]));
        editor.apply();

        return true;

    }

    /*
    Method Name: loadData
    Creation date: 1/21/20
    Purpose: loads the data from sharedPreferences
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    private void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SETTINGS_SHARED_PREFS, getActivity().MODE_PRIVATE);
        try {
            inputName.setText( sharedPreferences.getString("Settings_Name", "") );
            inputDef.setText( sharedPreferences.getString("Settings_Def", "") );
            switch(sharedPreferences.getInt("Settings_Freq", 0)){
                case 0:
                    freqSpinner.setSelection(0);
                    break;
                case 10:
                    freqSpinner.setSelection(1);
                    break;
                case 15:
                    freqSpinner.setSelection(2);
                    break;
                case 30:
                    freqSpinner.setSelection(3);
                    break;
            }
        }catch (Exception e){
            Toast toast = Toast.makeText(getActivity(),"Please fill up the Settings Page.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
