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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReceiversFragment extends Fragment {

    ImageButton addReceiverButton;
    ImageButton undoReceiverButton;
    private RecyclerView recyclerView;
    private ReceiversAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Receivers> receiversList;
    private Receivers removed;


    /*
    Method Name: onCreateView
    Creation date: 2/18/20
    Purpose: loads the view
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receivers, container, false);
    }

    /*
    Method Name: OnActivityCreated
    Creation date: 2/18/20
    Purpose: connects the elements to the code.
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadData();
        buildRecyclerView();

        addReceiverButton = getView().findViewById(R.id.button_receivers_add);
        addReceiverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddReceiversFragment toAddReceivers = new AddReceiversFragment();
                fragmentTransaction.replace(R.id.fragment_container, toAddReceivers);
                fragmentTransaction.commit();
            }
        });

        undoReceiverButton = getView().findViewById(R.id.button_receivers_undo);
        undoReceiverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoRemove();
            }
        });
    }

    /*
    Method Name: saveData
    Creation date: 2/22/20
    Purpose: saves the data to sharedPreferences
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    private void saveData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Receivers", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String receiversListJSON = gson.toJson(receiversList);
        editor.putString("Receivers_List", receiversListJSON);
        editor.apply();
    }

    /*
    Method Name: loadData
    Creation date: 2/22/20
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
        receiversList = gson.fromJson(receiversListJSON, type);

        if(receiversList == null){
            receiversList = new ArrayList<>();
        }
    }

    /*
    Method Name: undoRemove
    Creation date: 2/24/20
    Purpose: undo the removed receiver from the arrayList
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    private void undoRemove(){
        if(removed != null){
            receiversList.add(removed);
        }

        undoReceiverButton.setColorFilter(0xFF000000);
        removed = null;
        saveData();
        mAdapter.notifyDataSetChanged();
    }

    /*
    Method Name: removeReceiver
    Creation date: 2/22/20
    Purpose: removes the receiver from the list and stores it to sharedPreferences
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    public void removeReceiver(int position){
        removed = receiversList.get(position);
        receiversList.remove(position);
        undoReceiverButton.setColorFilter(0xFFCE2029);
        saveData();
        mAdapter.notifyDataSetChanged();
    }

    /*
    Method Name: buildRecyclerView
    Creation date: 2/22/20
    Purpose: build the recycler.
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: n/a
     */
    public void buildRecyclerView(){
        recyclerView = getView().findViewById(R.id.recycler_receivers_list);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ReceiversAdapter(receiversList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ReceiversAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //removeReceiver(position);
                buildDialog(position);
            }
        });
    }

    public void buildDialog(final int position){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        removeReceiver(position);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure?")
                .setTitle("Confirm Deletion")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }
}
