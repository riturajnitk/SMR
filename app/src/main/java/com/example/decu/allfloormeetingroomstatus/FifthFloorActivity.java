package com.example.decu.allfloormeetingroomstatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FifthFloorActivity extends AppCompatActivity {

    private ImageView btnSydneyStatus;
    ProgressDialog progressDialog;
    private String resDisplay;
    private ArrayList<Model> FifthFloorRoomList;
    private TextView tvOutlookStatusSydney, tvTimeSydney;//,tvDeviceStatusSydney;
    private LinearLayout linearLayout_Sydney;
    private final String _TAG="FifthFloorActivity_logs";

    public static final String strSMRSharedPref = "SMRSharedPref" ;
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_floor);

        /*App Action Bar*/
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.room_fifthfloor);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        btnSydneyStatus=(ImageView)findViewById(R.id.btn_sydney);

//        tvTimeSydney=(TextView)findViewById(R.id.tv_time_sydney);
        //tvDeviceStatusSydney=(TextView)findViewById(R.id.tv_dstatus_sydney);
        tvOutlookStatusSydney=(TextView)findViewById(R.id.tv_ostatus_sydney);

        linearLayout_Sydney=(LinearLayout)findViewById(R.id.ll_fif_sydney);

        //showing dialog
        progressDialog = new ProgressDialog(FifthFloorActivity.this, 0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        sharedPreferences = getSharedPreferences("SMRSharedPref", Context.MODE_PRIVATE);

        //trustAllCertificates();//trusting certificates /*Secured Connection Source Code*/

        //getting data from server
//        new getDataFromServerForFifthFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/display5", "display"});
//        new getDataFromServerForFifthFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=5", "display"});
//        new getDataFromServerForFifthFloor().execute(new String[]{"http://213.63.135.67:8080/RegisterServer/DisplayRoomStatus?floorid=5", "display"});///*Secured Connection Source Code*/
//        new getDataFromServerForFifthFloor().execute(new String[]{"https://213.63.135.67/RegisterServer/DisplayRoomStatus?floorid=5", "display"});/*Secured Connection Source Code*/
//        new getDataFromServerForFifthFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=5", "display"});///*Secured Connection Source Code, new server code*/
        new getDataFromServerForFifthFloor().execute(new String[]{"http://smartmeetingroom.nokia.com:8080/RegisterServer/DisplayRoomStatus?floorid=5", "display"});///*Secured Connection Source Code, new server code*/
        final Intent intent = new Intent(this,BookRoomActivity.class);

        linearLayout_Sydney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomIndex","Sydney");
                startActivity(intent);

            }
        });
    }

    public class getDataFromServerForFifthFloor extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            if(progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            tvOutlookStatusSydney.setTextColor(Color.WHITE);
            //checking networking connectivity
            if(isNetworkAvailable()==false)
            {

//                tvTimeSydney.setText("Room NA:Error");
                btnSydneyStatus.setBackgroundResource(R.drawable.icn_grey);
                //tvDeviceStatusSydney.setText("Room:Error");
                tvOutlookStatusSydney.setText("Outlook:Error");
                Toast.makeText(getApplicationContext(),R.string.room_no_internet_connection,Toast.LENGTH_LONG).show();
            }
            else if(FifthFloorRoomList==null ||FifthFloorRoomList.size()==0)
                {
//                tvTimeSydney.setText("Room NA:Error");
                btnSydneyStatus.setBackgroundResource(R.drawable.icn_grey);
                    //tvDeviceStatusSydney.setText("Room:Error");
                    tvOutlookStatusSydney.setText("Outlook:Error");

                Toast.makeText(getApplicationContext(),R.string.room_network_down,Toast.LENGTH_LONG).show();
                }
                    else
                        {
                     if(FifthFloorRoomList.get(0).outlookStatus==0)
                       {
                         tvOutlookStatusSydney.setText("Outlook:Unreserved");
                           tvOutlookStatusSydney.setTextColor(Color.GREEN);
                       }
                     else if(FifthFloorRoomList.get(0).outlookStatus==1)
                     {
                         tvOutlookStatusSydney.setText("Outlook:Reserved");
                         tvOutlookStatusSydney.setTextColor(Color.RED);
                     }
                      else
                     {
                         tvOutlookStatusSydney.setText("Outlook:N/A");
                         tvOutlookStatusSydney.setTextColor(Color.GRAY);
                     }
//Physical Status of Room
//                    if(FifthFloorRoomList.get(0).physicalStatus==0)
//                        tvTimeSydney.setText("Room Free:"+convertTime(FifthFloorRoomList.get(0).timeStamp));
//                     else if(FifthFloorRoomList.get(0).physicalStatus==1)
//                        tvTimeSydney.setText("Room Busy:"+convertTime(FifthFloorRoomList.get(0).timeStamp));
//                     else
//                        tvTimeSydney.setText("Room NA:"+convertTime(FifthFloorRoomList.get(0).timeStamp));

                if(FifthFloorRoomList.get(0).physicalStatus==0)
                {

                    btnSydneyStatus.setImageResource(R.drawable.icn_green);
                    //tvDeviceStatusSydney.setText("Room:Free");

                }
                if(FifthFloorRoomList.get(0).physicalStatus==-1)
                {

                    btnSydneyStatus.setImageResource(R.drawable.icn_grey);
                    //tvDeviceStatusSydney.setText("Room:N/A");

                }
                if(FifthFloorRoomList.get(0).physicalStatus==1)
                {
                    btnSydneyStatus.setImageResource(R.drawable.icn_red);
                    //tvDeviceStatusSydney.setText("Room:Busy");
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
                    int physta,outlooksta;
                    long time;
                    JSONArray jsonArray = new JSONArray(resDisplay);
                    int parselength = jsonArray.length();
                    JSONObject jsonObject;
                    FifthFloorRoomList = new ArrayList<>();
                    for(int i=0;i<parselength;i++)
                    {
                        jsonObject=jsonArray.getJSONObject(i);
                        Model room = new Model();
                        roomname = (String) jsonObject.get("roomName");
                        physta = (int) jsonObject.get("physicalStatus");
                        outlooksta = (int) jsonObject.get("outlookStatus");
                        time = (long )jsonObject.get("updateTime");
                        room.setRoomName(roomname);
                        room.setOutlookStatus(outlooksta);
                        room.setPhysicalStatus(physta);
                        room.setTimeStamp(time);
                        FifthFloorRoomList.add(i,room);

                    }
//                    String[] strArr = resDisplay.split(",");
//                    int i = 0;
//
//                    int arrysize = strArr.length / 3;
//
//                    FifthFloorRoomList = new ArrayList<>();
//
//                    for (int j = 0; j < arrysize ; j++) {
//
//                        Model room = new Model();
//
//                        room.setRoomName(strArr[i++]);
//
//                        room.setStatus();
//
//                        room.setTimeStamp(strArr[i++]);
//
//                        FifthFloorRoomList.add(j,room);
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


            ///*Secured Connection Source Code*/

//            URL url;
//            HttpsURLConnection urlConnection = null;
//
//            try {
//                url = new URL(strings[0]);
//
//
//                urlConnection = (HttpsURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//
//                Log.i(_TAG, "KeyShibsessionCookie : " + sharedPreferences.getString("KeyShibsessionCookie",null));
//                urlConnection.setRequestProperty("Cookie", sharedPreferences.getString("KeyShibsessionCookie",null));
//
//
//                int responseCode = urlConnection.getResponseCode();
//
//                if(responseCode==HttpURLConnection.HTTP_OK)
//                {
//                    resDisplay=readStream(urlConnection.getInputStream());//
//
//                    String roomname;
//                    int physta,outlooksta;
//                    long time;
//                    JSONArray jsonArray = new JSONArray(resDisplay);
//                    int parselength = jsonArray.length();
//                    JSONObject jsonObject;
//                    FifthFloorRoomList = new ArrayList<>();
//                    for(int i=0;i<parselength;i++)
//                    {
//                        jsonObject=jsonArray.getJSONObject(i);
//                        Model room = new Model();
//                        roomname = (String) jsonObject.get("roomName");
//                        physta = (int) jsonObject.get("physicalStatus");
//                        outlooksta = (int) jsonObject.get("outlookStatus");
//                        time = (long )jsonObject.get("updateTime");
//                        room.setRoomName(roomname);
//                        room.setOutlookStatus(outlooksta);
//                        room.setPhysicalStatus(physta);
//                        room.setTimeStamp(time);
//                        FifthFloorRoomList.add(i,room);
//
//                    }
////                    String[] strArr = resDisplay.split(",");
////                    int i = 0;
////
////                    int arrysize = strArr.length / 3;
////
////                    FifthFloorRoomList = new ArrayList<>();
////
////                    for (int j = 0; j < arrysize ; j++) {
////
////                        Model room = new Model();
////
////                        room.setRoomName(strArr[i++]);
////
////                        room.setStatus();
////
////                        room.setTimeStamp(strArr[i++]);
////
////                        FifthFloorRoomList.add(j,room);
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
//

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
            case R.id.action_faq:
            {
                Intent intent = new Intent(getApplicationContext(),FaqActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_home:
            {
                Intent homeintent = new Intent(this,MainActivity.class);
                homeintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeintent);
                return true;
            }
            case R.id.action_refresh:
            {

                if(!progressDialog.isShowing())/*multiple item click progress dialog showing always*/
                {
                    progressDialog = new ProgressDialog(FifthFloorActivity.this, 0);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

//                    new getDataFromServerForFifthFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/display5", "display"});
                    //new getDataFromServerForFifthFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=5", "display"});
//                    new getDataFromServerForFifthFloor().execute(new String[]{"http://213.63.135.67:8080/RegisterServer/DisplayRoomStatus?floorid=5", "display"});/*Non Secured Connection Source Code*/
                    //new getDataFromServerForFifthFloor().execute(new String[]{"https://213.63.135.67/RegisterServer/DisplayRoomStatus?floorid=5", "display"});/*Secured Connection Source Code*/
//                    new getDataFromServerForFifthFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=5", "display"});///*Secured Connection Source Code, new server code*/
                    new getDataFromServerForFifthFloor().execute(new String[]{"http://smartmeetingroom.nokia.com:8080/RegisterServer/DisplayRoomStatus?floorid=5", "display"});///*Secured Connection Source Code, new server code*/
                }

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
