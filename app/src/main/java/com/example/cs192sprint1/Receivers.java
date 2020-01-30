/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Manolo Hernandez

Code History
     1/22/20 - File created by Manolo Hernandez.
             - Added getter and setter functions for Receivers.
*/

package com.example.cs192sprint1;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Receivers extends AppCompatActivity {
     Context context;         //context for where the application is at the moment the class was called.
     String baseDir;          //base directory of the application
     String fileName;         //file name of where the receiver data will be stored.
     String filePath;         //file path of where the receiver data will be stored.
     String tempFileName;     //temp file name used for modifying data.
     String tempFilePath;     //file path for the temp file.

     /*
     Method Name: Receivers
     Creation date: 1/22/20
     Purpose: To initialize the local variables of the class
     Calling Arguments: context
     Required Files: n/a
     Return Value: n/a
      */
     public Receivers(Context c){
          this.context = c;
          baseDir = context.getApplicationInfo().dataDir;
          fileName = "Receiver.csv";
          filePath = baseDir + File.separator + fileName;
          tempFileName = "temp.csv";
          tempFilePath = baseDir + File.separator + tempFileName;
     }
     /*
     Method Name: addReceiver
     Creation date: 1/22/20
     Purpose: To access the receivers.csv file and append a new receiver.
     Calling Arguments: name, num
     Required Files: receivers.csv
     Return Value: n/a
      */
     public void addReceiver(String name, String num){
          File f = new File(filePath);
          try {
               FileWriter fw = new FileWriter(f, true);
               BufferedWriter bw = new BufferedWriter(fw);
               PrintWriter out = new PrintWriter(bw);
               out.println(name+","+num);
               out.close();
               bw.close();
               fw.close();
               Toast.makeText(context, "New Receiver Added!", Toast.LENGTH_SHORT).show();
          }catch(Exception e){
               e.printStackTrace();
          }
     }
     /*
     Method Name: getAllReceivers
     Creation date: 1/22/20
     Purpose: Getter function to get all the receivers that are saved in receivers.csv
     Calling Arguments: n/a
     Required Files: receivers.csv
     Return Value: ArrayList<String> receivers
      */
     public ArrayList<String> getAllReceivers(){
          ArrayList<String> receivers = new ArrayList<>();
          try {
               BufferedReader br = new BufferedReader(new FileReader(filePath));
               String receiver = null;
               while ((receiver = br.readLine()) != null) {
                    receivers.add(receiver);
               }
               return receivers;
          } catch(Exception e) {
               e.printStackTrace();
          }
          return receivers;
     }
     /*
     Method Name: deleteReceiver
     Creation date: 1/22/20
     Purpose: Access both receiver and temp file to remove a specific receiver.
     Calling Arguments: info
     Required Files: receivers.csv, temp.csv
     Return Value: n/a
      */
     public void deleteReceiver(String info) throws IOException {
          File receiversFile = new File(filePath);
          File tempFile = new File(tempFilePath);

          BufferedReader reader = new BufferedReader(new FileReader(receiversFile));
          BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

          String lineToRemove = info;
          String currentLine;

          while((currentLine = reader.readLine()) != null) {
               // trim newline when comparing with lineToRemove
               String trimmedLine = currentLine.trim();
               if(trimmedLine.equals(lineToRemove)) continue;
               writer.write(currentLine + System.getProperty("line.separator"));
          }
          writer.close();
          reader.close();
          tempFile.renameTo(receiversFile);
     }
     /*
     Method Name: deleteAllReceivers
     Creation date: 1/22/20
     Purpose: Convenience function to delete all the receivers
     Calling Arguments: n/a
     Required Files: receivers.csv
     Return Value: n/a
      */
     public void deleteAllReceivers() {
          File receiversFile = new File(filePath);
          receiversFile.delete();
     }
}

