package com.example.decu.allfloormeetingroomstatus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SecondFloorActivity extends AppCompatActivity {


    private ImageView btnLynxStatus;
    ProgressDialog progressDialog;
    private String resDisplay;
    private ArrayList<Model> SecondFloorRoomList;
    private TextView tvOutlookStatusLynx;//, tvTimeLynx;//,tvDeviceStatusLynx;
    private Button refreshSFRoomStatus;
    private LinearLayout linearLayout_lynx;

    private final String _TAG="FifthFloorActivity_logs";

    public static final String strSMRSharedPref = "SMRSharedPref" ;
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_floor);

        /*App Action Bar*/
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.room_secondfloor);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        btnLynxStatus=(ImageView)findViewById(R.id.btn_lynx);

//        tvTimeLynx=(TextView)findViewById(R.id.tv_time_lynx);
//        tvDeviceStatusLynx=(TextView)findViewById(R.id.tv_dstatus_lynx);
        tvOutlookStatusLynx=(TextView)findViewById(R.id.tv_ostatus_lynx);
        linearLayout_lynx =(LinearLayout)findViewById(R.id.ll_sf_lynx);
        //showing dialog
        progressDialog = new ProgressDialog(SecondFloorActivity.this, 0);
        progressDialog.setCancelable(false);
        progressDialog.show();


        sharedPreferences = getSharedPreferences("SMRSharedPref", Context.MODE_PRIVATE);

//        trustAllCertificates();//trusting certificates /*Secured Connection Source Code*/

        //getting data from server
//        new getDataFromServerForSecondFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/display2", "display"});
//        new getDataFromServerForSecondFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=3", "display"});
//        new getDataFromServerForSecondFloor().execute(new String[]{"http://213.63.135.67:8080/RegisterServer/DisplayRoomStatus?floorid=3", "display"});/*Non Secured Connection Source Code*/
//        new getDataFromServerForSecondFloor().execute(new String[]{"https://213.63.135.67/RegisterServer/DisplayRoomStatus?floorid=3", "display"});/*Secured Connection Source Code*/
        //new getDataFromServerForSecondFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=3", "display"});/*Non Secured Connection Source Code*/
        new getDataFromServerForSecondFloor().execute(new String[]{"http://smartmeetingroom.nokia.com:8080/RegisterServer/DisplayRoomStatus?floorid=3", "display"});/*Non Secured Connection Source Code*/

        final Intent intent = new Intent(this,BookRoomActivity.class);
        linearLayout_lynx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomIndex","Lynx");
                startActivity(intent);

            }
        });
    }

    public class getDataFromServerForSecondFloor extends AsyncTask<String, Void, String>
    {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            tvOutlookStatusLynx.setTextColor(Color.WHITE);
        /*Web Link is down*/
            if(isNetworkAvailable()==false)
            {
                //tvTimeLynx.setText("Room NA:Error");
                btnLynxStatus.setImageResource(R.drawable.icn_grey);
                //tvDeviceStatusLynx.setText("Room:Error");
                tvOutlookStatusLynx.setText("Outlook:Error");
                Toast.makeText(getApplicationContext(),R.string.room_no_internet_connection,Toast.LENGTH_SHORT).show();
            }
            else if(SecondFloorRoomList==null || SecondFloorRoomList.size()==0)
                {
                    //tvTimeLynx.setText("Room NA:Error");
                    btnLynxStatus.setImageResource(R.drawable.icn_grey);
                    //tvDeviceStatusLynx.setText("Room:Error");
                    tvOutlookStatusLynx.setText("Outlook:Error");
                    Toast.makeText(getApplicationContext(),R.string.room_network_down,Toast.LENGTH_SHORT).show();
                }
                else
                {
                /*Weblink is working and runnning*/

//                    if(SecondFloorRoomList.get(0).physicalStatus==0)
//                        tvTimeLynx.setText("Room Free:"+convertTime(SecondFloorRoomList.get(0).timeStamp));
//                    else if(SecondFloorRoomList.get(0).physicalStatus==1)
//                        tvTimeLynx.setText("Room Busy:"+convertTime(SecondFloorRoomList.get(0).timeStamp));
//                    else
//                        tvTimeLynx.setText("Room NA:"+convertTime(SecondFloorRoomList.get(0).timeStamp));

                    if(SecondFloorRoomList.get(0).outlookStatus==1)
                    {
                        tvOutlookStatusLynx.setText("Outlook:Reserved");
                        tvOutlookStatusLynx.setTextColor(Color.RED);
                    }
                    else
                        if(SecondFloorRoomList.get(0).outlookStatus==0)
                        {
                            tvOutlookStatusLynx.setText("Outlook:Unreserved");
                            tvOutlookStatusLynx.setTextColor(Color.GREEN);

                        }
                        else
                        {
                            tvOutlookStatusLynx.setText("Outlook:N/A");
                            tvOutlookStatusLynx.setTextColor(Color.GRAY);
                        }


                    if(SecondFloorRoomList.get(0).physicalStatus==0)
                    {

                        btnLynxStatus.setImageResource(R.drawable.icn_green);
                        //tvDeviceStatusLynx.setText("Room:Free");
                    }
                    if(SecondFloorRoomList.get(0).physicalStatus==-1)
                    {
                        btnLynxStatus.setImageResource(R.drawable.icn_grey);
                        //tvDeviceStatusLynx.setText("Room:N/A");

                    }
                    if(SecondFloorRoomList.get(0).physicalStatus==1)
                    {

                        btnLynxStatus.setImageResource(R.drawable.icn_red);
                        //tvDeviceStatusLynx.setText("Room:Busy");

                    }
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            /*Non Secured Connection Source Code*/
            URL url;
            HttpURLConnection urlConnection = null;


            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode==HttpURLConnection.HTTP_OK)
                {
                    resDisplay=readStream(urlConnection.getInputStream());//

                    String roomname;
                    int physta,outlooksta,floorid,roomid;
                    long time;
                    JSONArray jsonArray = new JSONArray(resDisplay);
                    int parselength = jsonArray.length();
                    JSONObject jsonObject;
                    SecondFloorRoomList = new ArrayList<>();
                    for(int i=0;i<parselength;i++)
                    {
                        jsonObject=jsonArray.getJSONObject(i);
                        Model room = new Model();
                        roomname = (String) jsonObject.get("roomName");
                        physta = (int) jsonObject.get("physicalStatus");
                        outlooksta = (int) jsonObject.get("outlookStatus");
                        time = (long )jsonObject.get("updateTime");
                        floorid = (int) jsonObject.get("floorId");
                        roomid = (int) jsonObject.get("roomId");
                        room.setRoomName(roomname);
                        room.setOutlookStatus(outlooksta);
                        room.setPhysicalStatus(physta);
                        room.setTimeStamp(time);
                        room.setFloorId(floorid);
                        room.setRoomId(roomid);
                        SecondFloorRoomList.add(i,room);

                    }
//                    String[] strArr = resDisplay.split(",");
//                    int i = 0;
//
//                    int arrysize = strArr.length / 3;
//
//                    SecondFloorRoomList = new ArrayList<>();
//
//                    for (int j = 0; j < arrysize ; j++) {
//
//                        Model room = new Model();
//
//                        room.setRoomName(strArr[i++]);
//
//                        room.setStatus(strArr[i++]);
//
//                        room.setTimeStamp(strArr[i++]);
//
//                        SecondFloorRoomList.add(j,room);
//
//                    }

                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*Secured Connection Source Code*/
//            URL url;
//            HttpsURLConnection urlConnection = null;
//
//
//            try {
//                url = new URL(strings[0]);
//                urlConnection = (HttpsURLConnection) url.openConnection();
//
//                urlConnection.setRequestMethod("GET");
//
//                Log.i(_TAG, "KeyShibsessionCookie : " + sharedPreferences.getString("KeyShibsessionCookie",null));
//                urlConnection.setRequestProperty("Cookie", sharedPreferences.getString("KeyShibsessionCookie",null));
//
//                int responseCode = urlConnection.getResponseCode();
//
//                if(responseCode==HttpURLConnection.HTTP_OK)
//                {
//                    resDisplay=readStream(urlConnection.getInputStream());//
//
//                    String roomname;
//                    int physta,outlooksta,floorid,roomid;
//                    long time;
//                    JSONArray jsonArray = new JSONArray(resDisplay);
//                    int parselength = jsonArray.length();
//                    JSONObject jsonObject;
//                    SecondFloorRoomList = new ArrayList<>();
//                    for(int i=0;i<parselength;i++)
//                    {
//                        jsonObject=jsonArray.getJSONObject(i);
//                        Model room = new Model();
//                        roomname = (String) jsonObject.get("roomName");
//                        physta = (int) jsonObject.get("physicalStatus");
//                        outlooksta = (int) jsonObject.get("outlookStatus");
//                        time = (long )jsonObject.get("updateTime");
//                        floorid = (int) jsonObject.get("floorId");
//                        roomid = (int) jsonObject.get("roomId");
//                        room.setRoomName(roomname);
//                        room.setOutlookStatus(outlooksta);
//                        room.setPhysicalStatus(physta);
//                        room.setTimeStamp(time);
//                        room.setFloorId(floorid);
//                        room.setRoomId(roomid);
//                        SecondFloorRoomList.add(i,room);
//
//                    }
////                    String[] strArr = resDisplay.split(",");
////                    int i = 0;
////
////                    int arrysize = strArr.length / 3;
////
////                    SecondFloorRoomList = new ArrayList<>();
////
////                    for (int j = 0; j < arrysize ; j++) {
////
////                        Model room = new Model();
////
////                        room.setRoomName(strArr[i++]);
////
////                        room.setStatus(strArr[i++]);
////
////                        room.setTimeStamp(strArr[i++]);
////
////                        SecondFloorRoomList.add(j,room);
////
////                    }
//
//                }
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

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




    //Code for Application Action Bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_actions,menu);
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

            case R.id.action_home:
            {
                Intent homeintent = new Intent(this,MainActivity.class);
                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeintent);
                return true;
            }

            case R.id.action_refresh :
            {
                if(!progressDialog.isShowing())/*multiple item click progress dialog showing always*/
                {
                    progressDialog = new ProgressDialog(SecondFloorActivity.this, 0);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

//                    new getDataFromServerForSecondFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=3", "display"});
//                    new getDataFromServerForSecondFloor().execute(new String[]{"http://213.63.135.67:8080/RegisterServer/DisplayRoomStatus?floorid=3", "display"});
//                    new getDataFromServerForSecondFloor().execute(new String[]{"https://213.63.135.67/RegisterServer/DisplayRoomStatus?floorid=3", "display"});/*Secured Connection Source Code*/
                    //new getDataFromServerForSecondFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/display2", "display"});
//                    new getDataFromServerForSecondFloor().execute(new String[]{"http://213.63.135.67:8080/RegisterServer/DisplayRoomStatus?floorid=3", "display"});/*Non Secured Connection Source Code*/
                    //new getDataFromServerForSecondFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=3", "display"});/*Non Secured Connection Source Code*/
                    new getDataFromServerForSecondFloor().execute(new String[]{"http://smartmeetingroom.nokia.com:8080/RegisterServer/DisplayRoomStatus?floorid=3", "display"});/*Non Secured Connection Source Code*/
                }

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

    private static String convertTime(long time)
    {
        Date date = new Date(time);
        DateFormat format = new SimpleDateFormat("HH:mm aa");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String formatted = format.format(date);
        return formatted;
    }

    public static void trustAllCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }
}
