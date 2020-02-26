package com.locustteam.locust;

/*
“This is a course requirement for CS 192 Software Engineering II
under the supervision of Asst. Prof. Ma. Rowena C. Solamo
of the Department of Computer Science, College of Engineering,
University of the Philippines, Diliman for the AY 2019-2020”.

Author: Ricardo Puato III

Code History
     1/22/20 - File Created by Ricardo Puato III
*/

import android.content.SharedPreferences;

public class Receivers {
    private String mName;
    private String mPhone;

     /*
     Method Name: Receivers
     Creation date: 1/21/20
     Purpose: Constructor for Receivers
     Calling Arguments: String name, String phone
     Required Files: n/a
     Return Value: n/a
      */
    public Receivers(String name, String phone) {
        mName = name;
        mPhone = phone;
    }

    /*
    Method Name: getName
    Creation date: 1/21/20
    Purpose: returns the receiver's name
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: String nName
     */
    public String getName() {
        return mName;
    }

    /*
    Method Name: getPhone
    Creation date: 1/21/20
    Purpose: returns the receivers's phone number
    Calling Arguments: n/a
    Required Files: n/a
    Return Value: String mPhone
     */
    public String getPhone() {
        return mPhone;
    }
}
