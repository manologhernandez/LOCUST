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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {

         /*
     Method Name: onCreateView
     Creation date: 1/21/20
     Purpose: loads the view.
     Calling Arguments: n/a
     Required Files: n/a
     Return Value: n/a
      */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Settings", getActivity().MODE_PRIVATE);
        if( sharedPreferences.getString("Settings_Name",null) == null ){
            Toast toast = Toast.makeText(getActivity(),"Empty Settings! Redirecting...", Toast.LENGTH_SHORT);
            toast.show();

            new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {}

                public void onFinish() {
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    SettingsFragment toSettings = new SettingsFragment();
                    fragmentTransaction.replace(R.id.fragment_container, toSettings);
                    fragmentTransaction.commit();

                }
            }.start();

        }
    }
}
