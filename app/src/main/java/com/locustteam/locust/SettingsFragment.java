package com.locustteam.locust;

/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Ricardo Puato III

Code History
     2/18/20 - File Created by Ricardo Puato III
*/

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
                //Store Data then Display.
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Settings", getActivity().MODE_PRIVATE);
                if( sharedPreferences.getString("Settings_Name",null) == null ){
                    saveData();
                    Toast.makeText(getActivity(),"Settings Saved!", Toast.LENGTH_SHORT).show();

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    HomeFragment toMain = new HomeFragment();
                    fragmentTransaction.replace(R.id.fragment_container, toMain);
                    fragmentTransaction.commit();
                }else{
                    saveData();
                    loadData();
                    Toast.makeText(getActivity(),"Settings Saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadData();
    }

    /*
    Method Name: saveData
    Creation date: 1/21/20
    Purpose: stores the data to sharedPreferences
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    private void saveData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Settings", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Settings_Name", inputName.getText().toString() );
        editor.putString("Settings_Def", inputDef.getText().toString() );
        editor.putString("Settings_Freq", freqSpinner.getSelectedItem().toString() );
        editor.apply();
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Settings", getActivity().MODE_PRIVATE);
        try {
            inputName.setText( sharedPreferences.getString("Settings_Name", null) );
            inputDef.setText( sharedPreferences.getString("Settings_Def", null) );
            switch(sharedPreferences.getString("Settings_Freq", null)){
                case "Every 10 Minutes":
                    freqSpinner.setSelection(0);
                    break;
                case "Every 15 Minutes":
                    freqSpinner.setSelection(1);
                    break;
                case "Every 30 Minutes":
                    freqSpinner.setSelection(2);
                    break;
            }
        }catch (Exception e){
            Toast toast = Toast.makeText(getActivity(),"Please fill up the Settings Page.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
