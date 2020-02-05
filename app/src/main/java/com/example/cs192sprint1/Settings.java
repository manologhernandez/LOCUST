/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Manolo Hernandez

Code History
     2/5/20 - File created by Manolo Hernandez.
             - Added getter and setter functions for Settings
*/

package com.example.cs192sprint1;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings extends AppCompatActivity {
     String userName;
     String userSex;
     String locationFreq;
     String userDefmsg;
     int userAge;
     String filename;
     String filepath;
     String baseDir;
     Context c;

     public Settings(Context c){
//          this.userName = name;
//          this.userSex = sex;
//          this.userAge = age;
//          this.locationFreq = freq;
//          this.userDefmsg = defMsg;
          this.c = c;
          this.baseDir = c.getApplicationInfo().dataDir;
          this.filename = "userSettings.json";
          this.filepath = baseDir + File.separator + filename;
     }

     public void saveSettings(String name, int age, String sex, String freq, String defMsg) throws JSONException {
          JSONObject newSettings = new JSONObject();
          newSettings.put("Name", name);
          newSettings.put("Age", age);
          newSettings.put("Sex", sex);
          newSettings.put("Location Frequency",freq);
          newSettings.put("Default Message", defMsg);
          File f = new File(filepath);
          //Write JSON file
          try  {
               Log.i("json:", newSettings.toString());
               FileWriter file = new FileWriter(f);
               file.write(newSettings.toString());
               file.flush();
               file.close();
          } catch (IOException e) {
               e.printStackTrace();
          }
     }

     public void readSettings(){
          try{
               FileReader fr = new FileReader(filepath);
               JSONParser jsonParser = new JSONParser();
               JSONObject obj = (JSONObject) jsonParser.parse(fr);
               System.out.println(obj);
          }catch (FileNotFoundException e) {
               e.printStackTrace();
          } catch (ParseException e) {
               e.printStackTrace();
          } catch (IOException e) {
               e.printStackTrace();
          }
     }
}
