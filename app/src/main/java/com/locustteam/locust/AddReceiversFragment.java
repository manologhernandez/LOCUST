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

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

public class AddReceiversFragment extends Fragment {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("((0)([0-9]{10}))$|((\\+63)([0-9]{10}))$");

    Button saveButton;
    Button cancelButton;
    EditText inputName;
    EditText inputPhone;
    ArrayList<Receivers> mReceiversList;

     /*
     Method Name: onCreateView
     Creation date:
     Purpose: loads the fragment.
     Calling Arguments: n/a
     Required Files: n/a
     Return Value: n/a
      */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_receivers, container, false);
    }

     /*
     Method Name: onCreate
     Creation date: 1/21/20
     Purpose: connects elements to the code.
     Calling Arguments: n/a
     Required Files: n/a
     Return Value: n/a
      */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        saveButton = getView().findViewById(R.id.button_add_save);
        cancelButton = getView().findViewById(R.id.button_add_cancel);
        inputName = getView().findViewById(R.id.input_add_name);
        inputPhone = getView().findViewById(R.id.input_add_number);

        loadData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addReceiver()){
                    saveData();

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ReceiversFragment toReceivers = new ReceiversFragment();
                    fragmentTransaction.replace(R.id.fragment_container, toReceivers);
                    fragmentTransaction.commit();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ReceiversFragment toReceivers = new ReceiversFragment();
                fragmentTransaction.replace(R.id.fragment_container, toReceivers);
                fragmentTransaction.commit();
            }
        });

    }

         /*
     Method Name: onCreate
     Creation date: 1/21/20
     Purpose: saves the data to sharedPreferences
     Calling Arguments: n/a
     Required Files: n/a
     Return Value: n/a
      */
    private void saveData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Receivers", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String receiversListJSON = gson.toJson(mReceiversList);
        editor.putString("Receivers_List", receiversListJSON);
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Receivers", getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
        String receiversListJSON = sharedPreferences.getString("Receivers_List", null);
        Type type = new TypeToken<ArrayList<Receivers>>() {}.getType();
        mReceiversList = gson.fromJson(receiversListJSON, type);

        if(mReceiversList == null){
            mReceiversList = new ArrayList<>();
        }
    }

        /*
    Method Name: addReceiver
    Creation date: 1/21/20
    Purpose: adds a receiver to the arrayList.
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    private boolean addReceiver(){
        Receivers receiver = new Receivers(inputName.getText().toString(),inputPhone.getText().toString());

        validateName(receiver);
        validatePhone(receiver);
        if(validateName(receiver) && validatePhone(receiver)){
            mReceiversList.add( receiver );
            return true;
        } else {
            return false;
        }
    }

    /*
    Method Name: validateName
    Creation date: 1/21/20
    Purpose: checks if there are duplicate names.
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    private boolean validateName(Receivers receiver){
        if( receiver.getName().matches("") ){
            inputName.setError("Empty Field!");
            return false;
        }

        Log.d("Running Loop", "OK!");
        for(Receivers r: mReceiversList){
            if( r.getName().trim().equals(receiver.getName().trim()) ){
                inputName.setError("Duplicate Name!");
                return false;
            }

            if( r.getPhone().equals(receiver.getPhone()) ){
                inputPhone.setError("Duplicate Phone!");
                return false;
            }
        }

        return true;
    }

    /*
    Method Name: validatePhone
    Creation date: 1/21/20
    Purpose: checks if the phone number given is valid.
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    private boolean validatePhone(Receivers receiver){
        if( receiver.getPhone().matches("") ){
            inputPhone.setError("Empty Field!");
            return false;
        }else if( !(PASSWORD_PATTERN.matcher( receiver.getPhone() ).matches()) ){
            inputPhone.setError("Invalid Phone Number!");
            return false;
        }

        return true;
    }
}
