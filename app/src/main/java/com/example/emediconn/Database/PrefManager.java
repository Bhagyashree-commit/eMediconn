package com.example.emediconn.Database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.emediconn.ChooseRole;
import com.example.emediconn.MainActivity;

import java.util.HashMap;
import java.util.prefs.Preferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public static final String KEY_USERNAME= "full_name";

    public static final String KEY_MOBILENUM= "mobile_number";

    public static final String KEY_ROLE= "user_type";

    public static final String KEY_USERID= "user_id";

    public static final String KEY_PASSWORD="";




    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }



    public void createUserLoginSession( String fullname,String mobilenum,String role,String userid,String password){
        // Storing login value as TRUE
        // editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERNAME, fullname);
        editor.putString(KEY_MOBILENUM, mobilenum);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_PASSWORD, password);


        // commit changes
        editor.commit();
    }


    public void setLogin(boolean isLoggedIn,String mobilenum) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        editor.putString(KEY_MOBILENUM,mobilenum);

        // editor.putString(KEY_ACCESS,access_token);

        // commit changes
        editor.commit();

        // Log.d(TAG, "User login session modified!");
    }



    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();


        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_MOBILENUM, pref.getString(KEY_MOBILENUM, null));
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, null));
        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));


        // return user
        return user;
    }



    //=====
    public PrefManager commit() {
        editor.commit();
        return this;
    }

    //=====
    public PrefManager set(String key, String value) {

        editor.putString(key, value);
        return this;
    }

    //=====
    public String get(String key) {
        return pref.getString(key, "");
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, ChooseRole.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


}
