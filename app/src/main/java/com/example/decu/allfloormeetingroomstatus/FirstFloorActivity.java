package com.example.decu.allfloormeetingroomstatus;

import android.app.ProgressDialog;
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

public class FirstFloorActivity extends AppCompatActivity {

    private final String _TAG = "FirstFloorActivity_Logs";

    private ImageView btnLyraStatus;
    private ImageView btnMensaStatus;
    private ImageView btnMuscaStatus;
    private ImageView btnNormaStatus;
    ProgressDialog progressDialog;
    private String resDisplay;
    private ArrayList<Model> FirstFloorRoomList;
    private TextView tvOutlookStatusMusca;//, //tvTimeMusca;//tvDeviceStatusMusca;
    private TextView tvOutlookStatusMensa;//, //tvTimeMensa;//tvDeviceStatusMensa;
    private TextView tvOutlookStatusNorma;//, //tvTimeNorma;//,tvDeviceStatusNorma,;
    private TextView tvOutlookStatusLyra;//, //tvTimeLyra;//,tvDeviceStatusLyra;
    private LinearLayout linearLayout_Musca,linearLayout_Mensa,linearLayout_Norma,linearLayout_Lyra;

    public static final String strSMRSharedPref = "SMRSharedPref" ;
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_floor);

        /*App Action Bar*/
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.room_firstfloor);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

//        //tvDeviceStatusMusca =(TextView)findViewById(R.id.tv_dstatus_musca);
//        //tvDeviceStatusMensa =(TextView)findViewById(R.id.tv_dstatus_mensa);
//        //tvDeviceStatusNorma =(TextView)findViewById(R.id.tv_dstatus_norma);
//        //tvDeviceStatusLyra =(TextView)findViewById(R.id.tv_dstatus_lyra);
        tvOutlookStatusMusca =(TextView)findViewById(R.id.tv_ostatus_musca);
        tvOutlookStatusMensa =(TextView)findViewById(R.id.tv_ostatus_mensa);
        tvOutlookStatusNorma =(TextView)findViewById(R.id.tv_ostatus_norma);
        tvOutlookStatusLyra =(TextView)findViewById(R.id.tv_ostatus_lyra);

//        //tvTimeMusca=(TextView)findViewById(R.id.tv_time_musca);
//        //tvTimeMensa=(TextView)findViewById(R.id.tv_time_mensa);
//        //tvTimeNorma=(TextView)findViewById(R.id.tv_time_norma);
//        //tvTimeLyra=(TextView)findViewById(R.id.tv_time_lyra);

        btnLyraStatus=(ImageView)findViewById(R.id.btn_lyra);
        btnMuscaStatus=(ImageView)findViewById(R.id.btn_musca);
        btnMensaStatus=(ImageView)findViewById(R.id.btn_mensa);
        btnNormaStatus=(ImageView)findViewById(R.id.btn_norma);
//        refreshFFRoomStatus=(Button)findViewById(R.id.btn_firstflr_refresh) ;

        linearLayout_Musca=(LinearLayout)findViewById(R.id.ll_ff_musca);
        linearLayout_Mensa=(LinearLayout)findViewById(R.id.ll_ff_mensa);
        linearLayout_Lyra=(LinearLayout)findViewById(R.id.ll_ff_lyra);
        linearLayout_Norma=(LinearLayout)findViewById(R.id.ll_ff_norma);

    /*Show Progress Dialog*/
        progressDialog = new ProgressDialog(FirstFloorActivity.this, 0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        sharedPreferences = getSharedPreferences("SMRSharedPref", Context.MODE_PRIVATE);

//        trustAllCertificates();//trusting certificates /*Secured Connection Source Code*/
    /*getting data from server*/
//        new getDataFromServerForFirstFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/display1", "display"});
//        new getDataFromServerForFirstFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=2", "display"});
//        new getDataFromServerForFirstFloor().execute(new String[]{"http://213.63.135.67:8080/RegisterServer/DisplayRoomStatus?floorid=2", "display"});///*Non Secured Connection Source Code*/
        //new getDataFromServerForFirstFloor().execute(new String[]{"https://213.63.135.67/RegisterServer/DisplayRoomStatus?floorid=2", "display"});///*Secured Connection Source Code*/
//        new getDataFromServerForFirstFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=2", "display"});///*Non Secured Connection Source Code*/
        new getDataFromServerForFirstFloor().execute(new String[]{"http://smartmeetingroom.nokia.com:8080/RegisterServer/DisplayRoomStatus?floorid=2", "display"});///*Non Secured Connection Source Code*/

        final Intent intent = new Intent(this,BookRoomActivity.class);

        linearLayout_Musca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("roomIndex","Musca");
                startActivity(intent);

            }
        });


        linearLayout_Mensa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomIndex","Mensa");
                startActivity(intent);

            }
        });

        linearLayout_Lyra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomIndex","Lyra");
                startActivity(intent);

            }
        });

        linearLayout_Norma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomIndex","Norma");
                startActivity(intent);

            }
        });
    }

    public class getDataFromServerForFirstFloor extends AsyncTask<String, Void, String> {


        @Override
        protected void onPostExecute(String s) {
            if(progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            //set text colour default
            tvOutlookStatusMensa.setTextColor(Color.WHITE);
            tvOutlookStatusMusca.setTextColor(Color.WHITE);
            tvOutlookStatusNorma.setTextColor(Color.WHITE);
            tvOutlookStatusLyra.setTextColor(Color.WHITE);
            //checking networking connectivity
            if(isNetworkAvailable()==false)
            {

                //tvTimeMensa.setText("Room NA:Error");
                btnMensaStatus.setImageResource(R.drawable.icn_grey);
                //tvDeviceStatusMensa.setText("Room:Error");
                tvOutlookStatusMensa.setText("Outlook:Error");

                //tvTimeMusca.setText("Room NA:Error");
                btnMuscaStatus.setImageResource(R.drawable.icn_grey);
                //tvDeviceStatusMusca.setText("Room:Error");
                tvOutlookStatusMusca.setText("Outlook:Error");

                //tvTimeNorma.setText("Room NA:Error");
                btnNormaStatus.setImageResource(R.drawable.icn_grey);
                //tvDeviceStatusNorma.setText("Room:Error");
                tvOutlookStatusNorma.setText("Outlook:Error");

                //tvTimeLyra.setText("Room NA:Error");
                btnLyraStatus.setImageResource(R.drawable.icn_grey);
                //tvDeviceStatusLyra.setText("Room:Error");
                tvOutlookStatusLyra.setText("Outlook:Error");

                Toast.makeText(getApplicationContext(),R.string.room_no_internet_connection,Toast.LENGTH_LONG).show();
            }
            else if(FirstFloorRoomList==null || FirstFloorRoomList.size()==0)
             {
                //tvTimeMensa.setText("Room NA:Error");
                btnMensaStatus.setImageResource(R.drawable.icn_grey);
                 //tvDeviceStatusMensa.setText("Room:Error");
                 tvOutlookStatusMensa.setText("Outlook:Error");

                //tvTimeMusca.setText("Room NA:Error");
                btnMuscaStatus.setImageResource(R.drawable.icn_grey);
                 //tvDeviceStatusMusca.setText("Room:Error");
                 tvOutlookStatusMusca.setText("Outlook:Error");

                //tvTimeNorma.setText("Room NA:Error");
                btnNormaStatus.setImageResource(R.drawable.icn_grey);
                 //tvDeviceStatusNorma.setText("Room:Error");
                 tvOutlookStatusNorma.setText("Outlook:Error");

                //tvTimeLyra.setText("Room NA:Error");
                btnLyraStatus.setImageResource(R.drawable.icn_grey);
                 //tvDeviceStatusLyra.setText("Room:Error");
                 tvOutlookStatusLyra.setText("Outlook:Error");


                Toast.makeText(getApplicationContext(),R.string.room_network_down,Toast.LENGTH_LONG).show();
             }
            else
            {
//                // Mensa
//                //tvTimeMensa.setText("Since:"+FirstFloorRoomList.get(0).timeStamp);
//                if(FirstFloorRoomList.get(0).physicalStatus==0)
//                {
//
//                    btnMensaStatus.setImageResource(R.drawable.icn_green);
//                    //tvDeviceStatusMensa.setText("Status:Available");
//                    tvOutlookStatusMensa.setText("Status:Available");
//
//                }
//                if(FirstFloorRoomList.get(0).physicalStatus==-1)
//                {
//
//                    btnMensaStatus.setImageResource(R.drawable.icn_grey);
//                    //tvDeviceStatusMensa.setText("Status:Unavailable");
//                    tvOutlookStatusMensa.setText("Status:Unavailable");
//
//                }
//                if(FirstFloorRoomList.get(0).physicalStatus==1)
//                {
//
//                    btnMensaStatus.setImageResource(R.drawable.icn_red);
//                    //tvDeviceStatusMensa.setText("Status:Busy");
//                    tvOutlookStatusMensa.setText("Status:Busy");
//
//                }
//
////Musca
//                //tvTimeMusca.setText("Since:"+FirstFloorRoomList.get(1).timeStamp);
//
//                if(FirstFloorRoomList.get(1).physicalStatus==0)
//                {
//
//                    btnMuscaStatus.setImageResource(R.drawable.icn_green);
//                    //tvDeviceStatusMusca.setText("Status:Available");
//                    tvOutlookStatusMusca.setText("Status:Available");
//
//                }
//                if(FirstFloorRoomList.get(1).physicalStatus==-1)
//                {
//
//                    btnMuscaStatus.setImageResource(R.drawable.icn_grey);
//                    //tvDeviceStatusMusca.setText("Status:Unavailable");
//                    tvOutlookStatusMusca.setText("Status:Unavailable");
//
//                }
//                if(FirstFloorRoomList.get(1).physicalStatus==1)
//                {
//
//                    btnMuscaStatus.setImageResource(R.drawable.icn_red);
//                    //tvDeviceStatusMusca.setText("Status:Busy");
//                    tvOutlookStatusMusca.setText("Status:Busy");
//
//                }
//
//                //Norma
//                //tvTimeNorma.setText("Since:"+FirstFloorRoomList.get(2).timeStamp);
//                if(FirstFloorRoomList.get(2).physicalStatus==0)
//                {
//
//                    btnNormaStatus.setImageResource(R.drawable.icn_green);
//                    //tvDeviceStatusNorma.setText("Status:Available");
//                    tvOutlookStatusNorma.setText("Status:Available");
//
//                }
//                if(FirstFloorRoomList.get(2).physicalStatus==-1)
//                {
//
//                    btnNormaStatus.setImageResource(R.drawable.icn_grey);
//                    //tvDeviceStatusNorma.setText("Status:Unavailable");
//                    tvOutlookStatusNorma.setText("Status:Unavailable");
//
//                }
//                if(FirstFloorRoomList.get(2).physicalStatus==1)
//                {
//                    btnNormaStatus.setImageResource(R.drawable.icn_red);
//                    //tvDeviceStatusNorma.setText("Status:Busy");
//                    tvOutlookStatusNorma.setText("Status:Busy");
//
//                }
//
//                //Lyra
//                //tvTimeLyra.setText("Since:"+FirstFloorRoomList.get(3).timeStamp);
//
//                if(FirstFloorRoomList.get(3).physicalStatus==0)
//                {
//                    btnLyraStatus.setImageResource(R.drawable.icn_green);
//                    //tvDeviceStatusLyra.setText("Status:Available");
//                    tvOutlookStatusLyra.setText("Status:Available");
//
//                }
//                if(FirstFloorRoomList.get(3).physicalStatus==-1)
//                {
//
//                    btnLyraStatus.setImageResource(R.drawable.icn_grey);
//                    //tvDeviceStatusLyra.setText("Status:Unavailable");
//                    tvOutlookStatusLyra.setText("Status:Unavailable");
//
//                }
//                if(FirstFloorRoomList.get(3).physicalStatus==1)
//                {
//
//                    btnLyraStatus.setImageResource(R.drawable.icn_red);
//                    //tvDeviceStatusLyra.setText("Status:Busy");
//                    tvOutlookStatusLyra.setText("Status:Busy");
//                }

                for(int i=0;i<FirstFloorRoomList.size();i++)
                {

/*****************Mensa********************************/
                    if(FirstFloorRoomList.get(i).roomName.equalsIgnoreCase("Mensa"))
                    {
//Physical Room Status
//                        if(FirstFloorRoomList.get(i).physicalStatus==0)
//                            tvTimeMensa.setText("Room Free:"+convertTime(FirstFloorRoomList.get(i).timeStamp));
//                        else if(FirstFloorRoomList.get(i).physicalStatus==1)
//                            tvTimeMensa.setText("Room Busy:"+convertTime(FirstFloorRoomList.get(i).timeStamp));
//                        else
//                            tvTimeMensa.setText("Room NA:"+convertTime(FirstFloorRoomList.get(i).timeStamp));

                        /*Outlook Status*/
                        if(FirstFloorRoomList.get(i).outlookStatus==0)
                        {
                            tvOutlookStatusMensa.setText("Outlook:Unreserved");
                            tvOutlookStatusMensa.setTextColor(Color.GREEN);
                        }
                        else
                             if(FirstFloorRoomList.get(i).outlookStatus==1)
                                {
                                    tvOutlookStatusMensa.setText("Outlook:Reserved");
                                    tvOutlookStatusMensa.setTextColor(Color.RED);
                                 }
                                else
                                 {
                                 tvOutlookStatusMensa.setText("Outlook:N/A");
                                     tvOutlookStatusMensa.setTextColor(Color.GRAY);
                                 }

                            if(FirstFloorRoomList.get(i).physicalStatus==0)
                            {

                                btnMensaStatus.setImageResource(R.drawable.icn_green);
                                //tvDeviceStatusMensa.setText("Status:Available");
                            }
                            if(FirstFloorRoomList.get(i).physicalStatus==-1)
                            {

                                btnMensaStatus.setImageResource(R.drawable.icn_grey);
                                //tvDeviceStatusMensa.setText("Status:Unavailable");

                            }
                            if(FirstFloorRoomList.get(i).physicalStatus==1)
                            {

                                btnMensaStatus.setImageResource(R.drawable.icn_red);
                                //tvDeviceStatusMensa.setText("Status:Busy");

                            }
                    }
                    //Musca
                    if(FirstFloorRoomList.get(i).roomName.equalsIgnoreCase("Musca"))
                    {
//                        tvTimeMusca.setText("Since:"+convertTime(FirstFloorRoomList.get(i).timeStamp));

//                        //Physical Room Status
//                        if(FirstFloorRoomList.get(i).physicalStatus==0)
//                            tvTimeMusca.setText("Room Free:"+convertTime(FirstFloorRoomList.get(i).timeStamp));
//                        else if(FirstFloorRoomList.get(i).physicalStatus==1)
//                            tvTimeMusca.setText("Room Busy:"+convertTime(FirstFloorRoomList.get(i).timeStamp));
//                        else
//                            tvTimeMusca.setText("Room NA:"+convertTime(FirstFloorRoomList.get(i).timeStamp));

                        /*Outlook Status*/
                        if(FirstFloorRoomList.get(i).outlookStatus==0)
                        {
                            tvOutlookStatusMusca.setText("Outlook:Unreserved");
                            tvOutlookStatusMusca.setTextColor(Color.GREEN);
                        }
                        else
                        if(FirstFloorRoomList.get(i).outlookStatus==1)
                        {
                            tvOutlookStatusMusca.setText("Outlook:Reserved");
                            tvOutlookStatusMusca.setTextColor(Color.RED);
                        }
                        else
                        {
                            tvOutlookStatusMusca.setText("Outlook:N/A");
                            tvOutlookStatusMusca.setTextColor(Color.GRAY);
                        }


                            if(FirstFloorRoomList.get(i).physicalStatus==0)
                            {

                                btnMuscaStatus.setImageResource(R.drawable.icn_green);
                                //tvDeviceStatusMusca.setText("Status:Available");

                            }
                            if(FirstFloorRoomList.get(i).physicalStatus==-1)
                            {

                                btnMuscaStatus.setImageResource(R.drawable.icn_grey);
                                //tvDeviceStatusMusca.setText("Status:Unavailable");

                            }
                            if(FirstFloorRoomList.get(i).physicalStatus==1)
                            {

                                btnMuscaStatus.setImageResource(R.drawable.icn_red);
                                //tvDeviceStatusMusca.setText("Status:Busy");

                            }
    //
                    }
//Norma
                    if(FirstFloorRoomList.get(i).roomName.equalsIgnoreCase("Norma"))
                    {
//                        tvTimeNorma.setText("Since:"+convertTime(FirstFloorRoomList.get(i).timeStamp));

//                        //Physical Room Status
//                        if(FirstFloorRoomList.get(i).physicalStatus==0)
//                            tvTimeNorma.setText("Room Free:"+convertTime(FirstFloorRoomList.get(i).timeStamp));
//                        else if(FirstFloorRoomList.get(i).physicalStatus==1)
//                            tvTimeNorma.setText("Room Busy:"+convertTime(FirstFloorRoomList.get(i).timeStamp));
//                        else
//                            tvTimeNorma.setText("Room :"+convertTime(FirstFloorRoomList.get(i).timeStamp));

                        /*Outlook Status*/
                        if(FirstFloorRoomList.get(i).outlookStatus==0)
                        {
                            tvOutlookStatusNorma.setText("Outlook:Unreserved");
                            tvOutlookStatusNorma.setTextColor(Color.GREEN);
                        }
                        else
                        if(FirstFloorRoomList.get(i).outlookStatus==1)
                        {
                            tvOutlookStatusNorma.setText("Outlook:Reserved");
                            tvOutlookStatusNorma.setTextColor(Color.RED);
                        }
                        else
                        {
                            tvOutlookStatusNorma.setText("Outlook:N/A");
                            tvOutlookStatusNorma.setTextColor(Color.GRAY);
                        }


                        if(FirstFloorRoomList.get(i).physicalStatus==0)
                            {

                                btnNormaStatus.setImageResource(R.drawable.icn_green);
                                //tvDeviceStatusNorma.setText("Status:Available");

                            }
                            if(FirstFloorRoomList.get(i).physicalStatus==-1)
                            {

                                btnNormaStatus.setImageResource(R.drawable.icn_grey);
                                //tvDeviceStatusNorma.setText("Status:Unavailable");

                            }
                            if(FirstFloorRoomList.get(i).physicalStatus==1)
                            {
                                btnNormaStatus.setImageResource(R.drawable.icn_red);
                                //tvDeviceStatusNorma.setText("Status:Busy");

                            }
                    }
/************************lyra*********************************/
                    if(FirstFloorRoomList.get(i).roomName.equalsIgnoreCase("Lyra"))
                    {
//                        tvTimeLyra.setText("Since:"+convertTime(FirstFloorRoomList.get(i).timeStamp));

                        //Physical Room Status
//                        if(FirstFloorRoomList.get(i).physicalStatus==0)
//                            tvTimeLyra.setText("Room Free:"+convertTime(FirstFloorRoomList.get(i).timeStamp));
//                        else if(FirstFloorRoomList.get(i).physicalStatus==1)
//                            tvTimeLyra.setText("Room Busy:"+convertTime(FirstFloorRoomList.get(i).timeStamp));
//                        else
//                            tvTimeLyra.setText("Room NA:"+convertTime(FirstFloorRoomList.get(i).timeStamp));

                        /*Outlook Status*/
                        if(FirstFloorRoomList.get(i).outlookStatus==0)
                        {
                            tvOutlookStatusLyra.setText("Outlook:Unreserved");
                            tvOutlookStatusLyra.setTextColor(Color.GREEN);
                        }
                        else
                        if(FirstFloorRoomList.get(i).outlookStatus==1)
                        {
                            tvOutlookStatusLyra.setText("Outlook:Reserved");
                            tvOutlookStatusLyra.setTextColor(Color.RED);
                        }
                        else
                        {
                            tvOutlookStatusLyra.setText("Outlook:N/A");
                            tvOutlookStatusLyra.setTextColor(Color.GRAY);
                        }


                            if(FirstFloorRoomList.get(i).physicalStatus==0)
                            {
                                btnLyraStatus.setImageResource(R.drawable.icn_green);
                                //tvDeviceStatusLyra.setText("Room:Available");

                            }
                            if(FirstFloorRoomList.get(3).physicalStatus==-1)
                            {

                                btnLyraStatus.setImageResource(R.drawable.icn_grey);
                                //tvDeviceStatusLyra.setText("Room:Unavailable");

                            }
                            if(FirstFloorRoomList.get(3).physicalStatus==1)
                            {

                                btnLyraStatus.setImageResource(R.drawable.icn_red);
                                //tvDeviceStatusLyra.setText("Room:Busy");
                            }

                    }


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
                    FirstFloorRoomList = new ArrayList<>();
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
                        FirstFloorRoomList.add(i,room);

                    }
//                    String[] strArr = resDisplay.split(",");
//                    int i = 0;
//
//                    int arrysize = strArr.length / 3;
//
//                    FirstFloorRoomList = new ArrayList<>();
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
//                        FirstFloorRoomList.add(j,room);
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
//                    int physta,outlooksta;
//                    long time;
//                    JSONArray jsonArray = new JSONArray(resDisplay);
//                    int parselength = jsonArray.length();
//                    JSONObject jsonObject;
//                    FirstFloorRoomList = new ArrayList<>();
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
//                        FirstFloorRoomList.add(i,room);
//
//                    }
////                    String[] strArr = resDisplay.split(",");
////                    int i = 0;
////
////                    int arrysize = strArr.length / 3;
////
////                    FirstFloorRoomList = new ArrayList<>();
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
////                        FirstFloorRoomList.add(j,room);
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
                    progressDialog = new ProgressDialog(FirstFloorActivity.this, 0);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

//                    new getDataFromServerForFirstFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/display1", "display"});
                    //new getDataFromServerForFirstFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=2", "display"});
//                    new getDataFromServerForFirstFloor().execute(new String[]{"http://213.63.135.67:8080/RegisterServer/DisplayRoomStatus?floorid=2", "display"});/*Non Secured Connection Source Code*/
                    //new getDataFromServerForFirstFloor().execute(new String[]{"https://213.63.135.67/RegisterServer/DisplayRoomStatus?floorid=2", "display"});/*Secured Connection Source Code*/
//                    new getDataFromServerForFirstFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=2", "display"});///*Non Secured Connection Source Code*/
                    new getDataFromServerForFirstFloor().execute(new String[]{"http://smartmeetingroom.nokia.com:8080/RegisterServer/DisplayRoomStatus?floorid=2", "display"});///*Non Secured Connection Source Code*/
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