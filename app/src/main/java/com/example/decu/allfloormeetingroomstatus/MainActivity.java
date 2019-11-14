package com.example.decu.allfloormeetingroomstatus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    private final String _TAG="MainActivity_logs";
    private Button groundFloor;
    private Button firstFloor;
    private Button secondFloor;
    private Button fifthFloor;
    private TextView tvListFloorDateTime;



    private String resDisplay;
    //shared prefs
    public static final String strSMRSharedPref = "SMRSharedPref" ;
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*App Action Bar*/
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

//        //adding date time
        tvListFloorDateTime =(TextView)findViewById(R.id.tv_mf_date_time);
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy HH:mm");
        String formattedDate = df.format(c.getTime());
        // Now we display formattedDate value in TextView
        tvListFloorDateTime.setText(formattedDate);

        //initialize the Buttons


        firstFloor=(Button)findViewById(R.id.btn_firstflr);
        fifthFloor=(Button)findViewById(R.id.btn_fifthflr);
        groundFloor=(Button)findViewById(R.id.btn_groundflr);
        secondFloor=(Button)findViewById(R.id.btn_secondflr);

        sharedPreferences = getSharedPreferences("SMRSharedPref", Context.MODE_PRIVATE);

        sharedPreferences.edit().putLong("KeyLastUsedTime", System.currentTimeMillis()).apply();//update last used time

        Log.i(_TAG, "<-------------------MainActivity---------------->");
        Log.i(_TAG, "isAuthenticated : " + sharedPreferences.getBoolean("isAuthenticated",false));
        Log.i(_TAG, "KeyLoginTime : " + sharedPreferences.getLong("KeyLoginTime",0));
        Log.i(_TAG, "KeyShibsession : " + sharedPreferences.getString("KeyShibsession",null));
        Log.i(_TAG, "KeyLastUsedTime : " + sharedPreferences.getLong("KeyLastUsedTime",0));
        Log.i(_TAG, "KeyShibsessionValue : " + sharedPreferences.getString("KeyShibsessionValue",null));
        Log.i(_TAG, "KeyUserEmaildId : " + sharedPreferences.getString("KeyUserEmaildId",null));
        Log.i(_TAG, "KeyShibsessionCookie : " + sharedPreferences.getString("KeyShibsessionCookie",null));

        //new getuserData().execute(new String[]{"https://213.63.135.67/RegisterServer/loggeduser", "userdata"});


//        resDisplay="{\"name\":\"Ritu Raj\",\"email\":\"ritu.raj@nokia.com\",\"mobile\":\"+919811067815\"}";
//        String[] strArr = resDisplay.split(",");
//        String stremailID = strArr[1];
//        String[] email = stremailID.split(":");
//        String emaildID = email[1];
//        Log.i(_TAG,"Emaild ID" + emaildID);
//
//
//
//
//        Log.i(_TAG,"ResDisplay");





        /*checking internet connectivity*/
        if(isNetworkAvailable()==false)
        {
            Toast.makeText(getApplicationContext(),R.string.room_no_internet_connection,Toast.LENGTH_SHORT).show();
        }

        firstFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_firstflr = new Intent(getApplicationContext(),FirstFloorActivity.class);
                startActivity(intent_firstflr);


            }
        });

        fifthFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_fifthflr=new Intent(getApplicationContext(),FifthFloorActivity.class);
                startActivity(intent_fifthflr);
            }
        });

        groundFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_grndflr=new Intent(getApplicationContext(),GroundFloorActivity.class);
                startActivity(intent_grndflr);
            }
        });

        secondFloor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent_secondflr=new Intent(getApplicationContext(),SecondFloorActivity.class);
                startActivity(intent_secondflr);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_actions,menu);

        //hide refresh and home for home screen activity
        menu.findItem(R.id.action_refresh).setVisible(false);
        menu.findItem(R.id.action_home).setVisible(false);

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();//back press kill previous activity
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

//                Intent intent = new Intent(getApplicationContext(),LoginScreenActivity.class);
//                startActivity(intent);
//                finish();//on press back button stop launching previous activity

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
    }

    //checking networking connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


//get emaild id data
public class getuserData extends AsyncTask<String,Void,String>
{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url;


        HttpsURLConnection httpsURLConnection = null;


/*trust all certificate by default*/
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }

        /*trust all certificate by default*/

        try {
            url = new URL(strings[0]);

           // urlConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection = (HttpsURLConnection) url.openConnection();

            String key = sharedPreferences.getString("KeyShibsession",null);
            String value = sharedPreferences.getString("KeyShibsessionValue",null);
            httpsURLConnection.setRequestProperty(key,value);
            //int responseCode = urlConnection.getResponseCode();
            int responseCode = httpsURLConnection.getResponseCode();

            Log.i(_TAG,"getting Email ID");

            if(responseCode==httpsURLConnection.HTTP_OK)
            {
                resDisplay=readStream(httpsURLConnection.getInputStream());//

                //resDisplay="{\"name\":\"Ritu Raj\",\"email\":\"ritu.raj@nokia.com\",\"mobile\":\"+919811067815\"}";
                String[] strArr = resDisplay.split(",");
                String stremailID = strArr[1];
                String[] email = stremailID.split(":");
                String emaildID = email[1];
                Log.i(_TAG," User Emaild ID" + emaildID);
                sharedPreferences.edit().putString("KeyEmailID",emaildID).apply();//saving emaild id to shared prefs


            }
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

    private String readStream(InputStream in) {

        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();

    }


}


