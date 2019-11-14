package com.example.decu.allfloormeetingroomstatus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class LoginScreenActivity extends AppCompatActivity {
    public static String _TAG = "LoginScreenActivity_Logs";

    public static final String strSMRSharedPref = "SMRSharedPref" ;
    public static final String strFirstInstallTime = "KeyfirstInstallTime";
    public static final String strLoginTime = "KeyLoginTime";
    public static final String strLastUsedTime = "KeyLastUsedTime";
    public static final String strCurrentTime = "KeyCurrentTime";
    public static final String strshibsession = "KeyShibsession";
    public static final String strshibsessionvalue = "KeyShibsessionValue";
    public static final String struserEmaildId = "KeyUserEmaildId";

    public static int mHourElapsedSinceLogin = 7;

    public static boolean isAuthAlready = false;


    public static final long LOGIN_EXPIRE_TIME_HOURS = 7;//7 hours expire time

    public static SharedPreferences sharedPreferences;
//
//    SharedPreferences sharedpreferences;
//
//
//
//    private Context mContext;
//    private Activity mActivity;
//
//
//
//    private SharedPreferences mPreferences;
//    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        sharedPreferences = getSharedPreferences(strSMRSharedPref, Context.MODE_PRIVATE);

//        mContext = getApplicationContext();
//        mActivity = LoginScreenActivity.this;
//        long lv_InstallTime=0;
//        long lv_LoginTime = 0 ;
//        long lv_LastUsedTime = 0 ;
//        String lv_shibsession=null;
//
//        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//
//
//
        //get first install time
//        try
//        {
//            lv_InstallTime = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0).firstInstallTime;
//        }catch(PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    //stroing data in sharedprefs
//        Log.i(_TAG,"First Install Time" + lv_InstallTime);//logs
//
//        editor.putLong(FirstInstallTime,lv_InstallTime);
//
//
//
//        editor.commit();//store the values in preferences
//
//        //fetching data from sharedprefs
//        long storedInstalledTime = sharedpreferences.getLong(FirstInstallTime,0);
//
//
//        Log.i(_TAG,"Install Time SharedPrefs Date" + storedInstalledTime);
        /*App Action Bar*/
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

//        Intent myintent = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(myintent);
//        finish();//on press back button stop calling login screen App

//        if(lv_lastUsedTime>lv_InstallTime)
//        {
//            Toast.makeText(getApplicationContext(),"App Already Authenticated",Toast.LENGTH_SHORT).show();
//            editor.putLong(LastUsedTime,lv_lastUsedTime);
//            editor.commit();
//            Intent myintent = new Intent(getApplicationContext(),SamlLoginActivity.class);
//            startActivity(myintent);
//            finish();//on press back button stop calling login screen App
//        }
//        else
//        {
//            Intent myintent = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(myintent);
//            finish();//on press back button stop calling login screen App
//        }


//        //shared preferences commiting the "Install Time"
//        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mEditor = mPreferences.edit();
//        mEditor.putLong(strFirstInstallTime,lv_InstallTime);
//
////        if(mPreferences.getBoolean("firstrun",true))
////        {
////            mPreferences.edit().putBoolean("firstrun",false).commit();
////        }



        sharedPreferences.edit().putLong("KeyLastUsedTime", System.currentTimeMillis()).apply();//update last used time in splash activity


        Thread navThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);//delayed to keep showing welcome/splash screen
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                navigateToHomeScreen();
            }
        };
        navThread.start();


    }
public void navigateToHomeScreen()
{

//    sharedPreferences = getSharedPreferences(strSMRSharedPref, Context.MODE_PRIVATE);
    Log.i(_TAG, "<-------------------LoginScreenActivity---------------->");
    Log.i(_TAG, "isAuthenticated : " + sharedPreferences.getBoolean("isAuthenticated",false));
    Log.i(_TAG, "KeyLoginTime : " + sharedPreferences.getLong("KeyLoginTime",0));
    Log.i(_TAG, "KeyShibsession : " + sharedPreferences.getString("KeyShibsession",null));
    Log.i(_TAG, "KeyShibsessionValue : " + sharedPreferences.getString("KeyShibsessionValue",null));
    Log.i(_TAG, "KeyLastUsedTime : " + sharedPreferences.getLong("KeyLastUsedTime",0));


//epoch time in Milliseconds

    long lv_logintime = sharedPreferences.getLong("KeyLoginTime",0);
    long lv_lasteusedtime = sharedPreferences.getLong("KeyLastUsedTime",0);


    Date date_login = new Date(lv_logintime);
    DateFormat format_login = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    //format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
    String formatted_login = format_login.format(date_login);


    Date date_lasteusedtime = new Date(lv_lasteusedtime);
    DateFormat format_lasteusedtime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    //format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
    String formatted_lasteusedtime = format_lasteusedtime.format(date_lasteusedtime);

    mHourElapsedSinceLogin = (int)TimeUnit.MILLISECONDS.toHours(date_lasteusedtime.getTime()-date_login.getTime());

    Log.i(_TAG,"<-------------Time Elapsed since Last Login------------->");
    Log.i(_TAG,"Hours:" +TimeUnit.MILLISECONDS.toHours(date_lasteusedtime.getTime()-date_login.getTime()));
    Log.i(_TAG,"Minutes:" +TimeUnit.MILLISECONDS.toMinutes(date_lasteusedtime.getTime()-date_login.getTime()));
    Log.i(_TAG,"Seconds:" +TimeUnit.MILLISECONDS.toSeconds(date_lasteusedtime.getTime()-date_login.getTime()));
    if(mHourElapsedSinceLogin>=LOGIN_EXPIRE_TIME_HOURS)
    {
        // Navigate to login Activity

        Intent myintent = new Intent(getApplicationContext(),SamlLoginActivity.class);
        startActivity(myintent);
        Log.i(_TAG,"<---------------Login Time Expired-------------------->");
    }else if(sharedPreferences.contains("isAuthenticated")){
        // Navigate to Main Activity

        Intent myintent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(myintent);
        Log.i(_TAG,"<---------------App is Authenticated-------------------->");
    }else{
        // Navigate to login Activity

        Intent myintent = new Intent(getApplicationContext(),SamlLoginActivity.class);
        startActivity(myintent);
        Log.i(_TAG,"<---------------App is NOT Authenticated-------------------->");
    }

    finish();

    Log.i(_TAG,"<---------------------Navigation to Home Screen------------------------>");
}
//    public void launchMainActivity(View view)
//    {
//        Intent myintent = new Intent(getApplicationContext(),SamlLoginActivity.class);
//        startActivity(myintent);
//        finish();//on press back button stop calling login screen App
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_actions,menu);

        //hide refresh and home for home screen activity
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.action_about:
            {
                Intent intent = new Intent(getApplicationContext(),AboutUsActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.action_contactus:
            {
//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:smartmeeting.admin@nokia.com"));
//                startActivity(emailIntent);
                Intent outlookIntent = new Intent(Intent.ACTION_SEND);
                outlookIntent.setType("text/html");

                final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(outlookIntent, 0);
                String outlookActivityClass = null;

                for (final ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.equals("com.microsoft.office.outlook")) {
                        outlookActivityClass = info.activityInfo.name;
                        if (outlookActivityClass != null && !outlookActivityClass.isEmpty()) {
                            break;
                        }
                    }
                }
                if(outlookActivityClass!=null)
                {
                    outlookIntent.setClassName("com.microsoft.office.outlook", outlookActivityClass);
                    outlookIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "smartmeeting.admin@nokia.com" });
                    outlookIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    outlookIntent.putExtra(Intent.EXTRA_CC, "cc@gmail.com"); // if necessary
                    outlookIntent.putExtra(Intent.EXTRA_TEXT, "Please write your feedback here..");
                    outlookIntent.setData(Uri.parse("smartmeeting.admin@nokia.com"));
                    startActivity(outlookIntent);
                    return true;
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Outlook Not installed!!",Toast.LENGTH_SHORT).show();
                    return false;
                }


            }

            case R.id.action_logout:
            {
                Toast.makeText(getApplicationContext(),"Coming Soon...",Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.action_faq:
            {
                Intent intent = new Intent(getApplicationContext(),FaqActivity.class);
                startActivity(intent);
                return true;
            }
        }
        //return super.onOptionsItemSelected(item);
        return true;
//        return super.onOptionsItemSelected(item);
    }
}
