package com.locustteam.locust;

/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Ricardo Puato III and Manolo Hernandez

Code History
     2/18/20 - File Created by Ricardo Puato III
     3/04/20 - Added location handling by Manolo Hernandez
*/

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
     private ImageButton sosButton;
     private FusedLocationProviderClient fusedLocationClient;
     private TextView currentUserCoordinatesTextView;
     private TextView currentUserStreetAddressTextView;
     private Geocoder geocoder;
     private LocationCallback locationCallback;
     private LocationRequest locationRequest;
     private String userAddress;
     private double currentLatitude;
     private double currentLongitude;
     private SharedPreferences settingsPreferences;
     private SharedPreferences locationPreferences;
     private final static int MAX_RETRY_COUNT = 5;
     /*
 Method Name: onCreateView
 Creation date: 2/18/20
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
     Creation date: 2/18/20
     Purpose: connects the elements to the code.
     Calling Arguments: n/a
     Required Files: n/a
     Return Value: n/a
      */
     @Override
     public void onActivityCreated(@Nullable Bundle savedInstanceState) {
          super.onActivityCreated(savedInstanceState);

          //initialize shared preferences variables
          settingsPreferences = getActivity().getSharedPreferences(Constants.SETTINGS_SHARED_PREFS, getActivity().MODE_PRIVATE);
          locationPreferences = getActivity().getSharedPreferences(Constants.LOCATION_SHARED_PREFS, getActivity().MODE_PRIVATE);

          //Check if settings were already set, if not, redirect user to settings screen.
          if(settingsPreferences.getString("Settings_Name",null) == null ){
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

          //initialize location services variables
          fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
          geocoder = new Geocoder(getContext());
          locationCallback = new LocationCallback() {
               @Override
               public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                         return;
                    }
                    for (Location location : locationResult.getLocations()) {
                         Log.e("Location", "New location updated!");

                         currentLatitude = (location.getLatitude());
                         currentLongitude = (location.getLongitude());
                         currentUserCoordinatesTextView.setText("Lat: "+currentLatitude+" Long: " +currentLongitude);
                         currentUserStreetAddressTextView.setText("Locating street address...");
//                         try {
//                              userAddress = reverseGeocode(currentLatitude, currentLongitude);
//                              currentUserStreetAddressTextView.setText(userAddress);
//                         } catch (IOException e) {
//                              e.printStackTrace();
//                              currentUserStreetAddressTextView.setText("Unable to locate Street Address.");
//                         }
//                         currentUserCoordinatesTextView.setText("Lat: "+currentLatitude+" Long: " +currentLongitude);
//                         saveLocationData(currentLatitude, currentLongitude, userAddress);

                         new AsyncTask<Void, Void, Void>(){
                              @Override
                              protected Void doInBackground(Void... params) {

                                   int retryCount = 0;

                                   while(true){
                                        try {
                                             userAddress = reverseGeocode(currentLatitude, currentLongitude);
                                             break;
                                        } catch (IOException e) {
                                             if(retryCount > MAX_RETRY_COUNT){
                                                  userAddress = "Unable to locate street address. Please check your internet connection.";
                                                  e.printStackTrace();
                                                  break;
                                             }
                                             retryCount++;
                                             continue;
                                        }
                                   }

                                   return null;
                              }

                              @Override
                              protected void onPostExecute(Void result) {
                                   super.onPostExecute(result);
                                   currentUserStreetAddressTextView.setText(userAddress);
                                   saveLocationData(currentLatitude, currentLongitude, userAddress);
                              }
                         }.execute();
                    }
               };
          };

          //initialize ui elements
          currentUserCoordinatesTextView = getView().findViewById(R.id.user_current_coordinates);
          currentUserStreetAddressTextView = getView().findViewById(R.id.user_street_address_text);
          updateLocationUIElements();
          sosButton = getView().findViewById(R.id.button_home_send);
          sosButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.LOCATION_SHARED_PREFS, getActivity().MODE_PRIVATE);
                    if(!sharedPreferences.getBoolean("Location_isSharing", false)) { //not yet sharing...
                         buildStartLocationTrackingDialog();
                    }else{
                         buildStopLocationTrackingDialog();
                    }
               }
          });
     }

     private void updateLocationUIElements() {
          if(locationPreferences.getBoolean("Location_isSharing",false)) { //if you are sharing ...
               userAddress = locationPreferences.getString("Location_Address", null);
               String userLatitude = locationPreferences.getString("Location_Latitude", null);
               String userLongitude = locationPreferences.getString("Location_Longitude", null);
               currentUserStreetAddressTextView.setText(userAddress);
               currentUserCoordinatesTextView.setText("Lat: "+userLatitude+" Long: " +userLongitude);
          }else{
               currentUserStreetAddressTextView.setText("");
               currentUserCoordinatesTextView.setText("Press SOS to begin Location Tracking");
          }
     }

     private void saveLocationData(double currentLatitude, double currentLongitude, String userAddress) {
          SharedPreferences.Editor editor = locationPreferences.edit();
          editor.putString("Location_Latitude", String.valueOf(currentLatitude));
          editor.putString("Location_Longitude", String.valueOf(currentLongitude));
          editor.putString("Location_Address", userAddress );
          editor.apply();
     }


     public void startLocationForegroundService() {
          Intent serviceIntent = new Intent(getActivity(), LocationForegroundService.class);
          ContextCompat.startForegroundService(getContext(), serviceIntent);
     }

     public void stopLocationForegroundService() {
          Intent serviceIntent = new Intent(getActivity(), LocationForegroundService.class);
          getActivity().stopService(serviceIntent);
     }

     public void buildStartLocationTrackingDialog(){
          DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                         case DialogInterface.BUTTON_POSITIVE:
                              //Yes button clicked
                              requestPermissions();
                              break;

                         case DialogInterface.BUTTON_NEGATIVE:
                              //No button clicked
                              break;
                    }
               }
          };

          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setMessage("Start Sending Location?")
                  .setPositiveButton("Yes", dialogClickListener)
                  .setNegativeButton("No", dialogClickListener)
                  .show();
     }

     public void buildStopLocationTrackingDialog(){
          DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                         case DialogInterface.BUTTON_POSITIVE:
                              //Yes button clicked
                              stopLocationUpdates();
                              break;

                         case DialogInterface.BUTTON_NEGATIVE:
                              //No button clicked
                              break;
                    }
               }
          };

          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setMessage("Stop Sending Location?")
                  .setPositiveButton("Yes", dialogClickListener)
                  .setNegativeButton("No", dialogClickListener)
                  .show();
     }

     public void buildGoToSettingsPermissionsDialog(){
          DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                         case DialogInterface.BUTTON_POSITIVE:
                              //Settings button clicked
                              Intent intent = new Intent();
                              intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                              Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                              intent.setData(uri);
                              startActivity(intent);
                              break;

                         case DialogInterface.BUTTON_NEGATIVE:
                              //Cancel button clicked
                              break;
                    }
               }
          };

          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setMessage("Locust requires your Location permissions to function. Please go to your Settings and allow Location permissions.")
                  .setTitle("Permissions Required")
                  .setPositiveButton("Settings", dialogClickListener)
                  .setNegativeButton("Cancel", dialogClickListener)
                  .show();
     }

     public void requestPermissions(){
          if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) +
                  ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) +
                  ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) +
                  ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_NETWORK_STATE)
                  != PackageManager.PERMISSION_GRANTED){

               if (ActivityCompat.shouldShowRequestPermissionRationale (getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ||
                       ActivityCompat.shouldShowRequestPermissionRationale (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ||
                       ActivityCompat.shouldShowRequestPermissionRationale (getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) ||
                       ActivityCompat.shouldShowRequestPermissionRationale (getActivity(), Manifest.permission.ACCESS_NETWORK_STATE)){
                    //Show a short explanation to the user
                    Snackbar.make(getActivity().findViewById(android.R.id.content),
                            "Please Grant Location Permissions to start sending location.",
                            Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                            new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                      getActivity().requestPermissions(
                                              new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                      Manifest.permission.ACCESS_COARSE_LOCATION,
                                                      Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                                      Manifest.permission.ACCESS_NETWORK_STATE},
                                              Constants.MY_MULTIPLE_PERMISSIONS_REQUEST_LOCATION);
                                      buildStartLocationTrackingDialog();
                                 }
                            }).show();
               } else {
                    buildGoToSettingsPermissionsDialog();
               }
          } else {
               // write your logic code if permission already granted
               createLocationRequest();
          }
     }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          //I dont think this function even gets called lol
          switch (requestCode) {
               case Constants.MY_MULTIPLE_PERMISSIONS_REQUEST_LOCATION:
                    if (grantResults.length > 0) {
                         boolean backgroundPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                         boolean coarsePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                         boolean finePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                         if(finePermission && coarsePermission && backgroundPermission) {
                              Log.e("Test", "Hello there");
                              requestPermissions();
                         } else {
                              Log.e("Test", "noice");
                              Snackbar.make(getActivity().findViewById(android.R.id.content),
                                      "Please Grant ALL Permissions to start sending location.",
                                      Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                                      new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                                requestPermissions(
                                                        new String[]{
                                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                                        },
                                                        Constants.MY_MULTIPLE_PERMISSIONS_REQUEST_LOCATION);
                                           }
                                      }).show();
                         }
                    }
                    break;
          }
     }

     protected void createLocationRequest() {
          locationRequest = LocationRequest.create();
          locationRequest.setInterval(60000); //1 minute
          locationRequest.setFastestInterval(5000); // 5 seconds
          locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //use GPS

          LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                  .addLocationRequest(locationRequest);
          SettingsClient client = LocationServices.getSettingsClient(getActivity());
          Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
          task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
               @Override
               public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    // All location settings are satisfied. The client can initialize location requests here.
                    currentUserCoordinatesTextView.setText("Locating...");
                    startLocationUpdates();
               }
          });
          task.addOnFailureListener(getActivity(), new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                         // Location settings are not satisfied, but this can be fixed
                         // by showing the user a dialog.
                         try {
                              // Show the dialog by calling startResolutionForResult(),
                              // and check the result in onActivityResult().
                              ResolvableApiException resolvable = (ResolvableApiException) e;
                              resolvable.startResolutionForResult(getActivity(),
                                      Constants.REQUEST_CHECK_SETTINGS);
                         } catch (IntentSender.SendIntentException sendEx) {
                              // Ignore the error.
                         }
                    }
               }
          });
     }

     private void startLocationUpdates(){
          //Start Foreground Location Service
          startLocationForegroundService();
          //Save application state in shared prefs
          SharedPreferences.Editor editor = locationPreferences.edit();
          editor.putBoolean("Location_isSharing", true);
          editor.apply();
          //start requesting for location updates
          fusedLocationClient.requestLocationUpdates(locationRequest,
                  locationCallback,
                  Looper.getMainLooper());
     }

     private void stopLocationUpdates(){
          //stop fusedlocationclient from getting location updates
          fusedLocationClient.removeLocationUpdates(locationCallback);

          //update shared preferences
          SharedPreferences.Editor editor = locationPreferences.edit();
          editor.putString("Location_Latitude", "");
          editor.putString("Location_Longitude", "");
          editor.putString("Location_Address", "");
          editor.putBoolean("Location_isSharing", false);
          editor.apply();

          //stop location foreground service
          stopLocationForegroundService();

          //update UI elements
          updateLocationUIElements();
     }

     public String reverseGeocode(double lat, double lon) throws IOException {
          if(isConnectedToInternet()){
               List<Address> userAddressList;
               userAddressList = geocoder.getFromLocation(lat, lon, 1);
               return userAddressList.get(0).getAddressLine(0);
          }else{
               return "Please turn on WIFI or mobile data to view approximate address";
          }
          
     }

     private boolean isConnectedToInternet() {
          ConnectivityManager cm =
                  (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

          NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
          boolean isConnected = activeNetwork != null &&
                  activeNetwork.isConnectedOrConnecting();
          return isConnected;
     }
}
