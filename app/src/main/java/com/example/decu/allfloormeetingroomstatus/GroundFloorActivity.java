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
import android.widget.ImageButton;
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

public class GroundFloorActivity extends AppCompatActivity {

    //const layout
    private ImageView btnScopiusStatus;
    private ImageView btnPuppisStatus;
    private ImageView btnSagittariusStatus;
    private ImageView btnScutumStatus;
    private ImageView btnVolansStatus;
//    private Button refreshGFRoomStatus;
    ProgressDialog progressDialog;
    private String resDisplay;
    private ArrayList<Model> GroundFloorRoomList;
    private TextView tvOutlookStatusScutum;//, tvTimeScutum;//,//tvDeviceStatusScutum,;
    private TextView tvOutlookStatusPuppis;//, tvTimePuppis;//,//tvDeviceStatusPuppis;
    private TextView tvOutlookStatusScorpius;//, tvTimeScorpius;////tvDeviceStatusScorpius
    private TextView tvOutlookStatusSagittarius;//, tvTimeSagittarius;////tvDeviceStatusSagittarius
    private TextView tvOutlookStatusVolans;//, tvTimeVolans;////tvDeviceStatusVolans,
    private LinearLayout linearLayout_Scutum,linearLayout_Puppis,linearLayout_Scorpious,linearLayout_Sagittarius,linearLayout_Volans;

    public static final String strSMRSharedPref = "SMRSharedPref" ;
    public static SharedPreferences sharedPreferences;

    private final String _TAG = "GroundFloorActivity_Logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ground_floor);

        /*App Action Bar*/
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.room_groundfloor);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ////tvDeviceStatusScorpius =(TextView)findViewById(R.id.tv_dstatus_scorpius);
        tvOutlookStatusScorpius =(TextView)findViewById(R.id.tv_ostatus_scorpius);
        ////tvDeviceStatusScutum =(TextView)findViewById(R.id.tv_dstatus_scutum);
        tvOutlookStatusScutum =(TextView)findViewById(R.id.tv_ostatus_scutum);
        ////tvDeviceStatusSagittarius =(TextView)findViewById(R.id.tv_dstatus_sagittarius);
        tvOutlookStatusSagittarius =(TextView)findViewById(R.id.tv_ostatus_sagittarius);
        ////tvDeviceStatusVolans =(TextView)findViewById(R.id.tv_dstatus_volans);
        tvOutlookStatusVolans =(TextView)findViewById(R.id.tv_ostatus_volans);
        ////tvDeviceStatusPuppis =(TextView)findViewById(R.id.tv_dstatus_puppis);
        tvOutlookStatusPuppis =(TextView)findViewById(R.id.tv_ostatus_puppis);

        //tvTimeScorpius=(TextView)findViewById(R.id.tv_time_scorpius);
        //tvTimeSagittarius=(TextView)findViewById(R.id.tv_time_sagittarius);
        //tvTimeScutum=(TextView)findViewById(R.id.tv_time_scutum);
        //tvTimeVolans=(TextView)findViewById(R.id.tv_time_volans);
        //tvTimePuppis=(TextView)findViewById(R.id.tv_time_puppis);

        btnScopiusStatus=(ImageView)findViewById(R.id.btn_scorpius);
        btnScutumStatus=(ImageView)findViewById(R.id.btn_scutum);
        btnSagittariusStatus=(ImageView)findViewById(R.id.btn_sagittarius);
        btnVolansStatus=(ImageView)findViewById(R.id.btn_volans);
        btnPuppisStatus=(ImageView)findViewById(R.id.btn_pupis);

        linearLayout_Puppis =(LinearLayout)findViewById(R.id.ll_gf_puppis);
        linearLayout_Sagittarius =(LinearLayout)findViewById(R.id.ll_gf_sagittarius);
        linearLayout_Scorpious =(LinearLayout)findViewById(R.id.ll_gf_scorpius);
        linearLayout_Scutum =(LinearLayout)findViewById(R.id.ll_gf_scutum);
        linearLayout_Volans =(LinearLayout)findViewById(R.id.ll_gf_volans);






        //showing dialog
        progressDialog = new ProgressDialog(GroundFloorActivity.this, 0);
        progressDialog.setCancelable(false);
        progressDialog.show();

        sharedPreferences = getSharedPreferences("SMRSharedPref", Context.MODE_PRIVATE);

//        trustAllCertificates();//trusting certificates /*Secured Connection Source Code*/

        //getting data from server
//        new getDataFromServerForGroundFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/display0", "display"});
//        new getDataFromServerForGroundFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=1", "display"});
//          new getDataFromServerForGroundFloor().execute(new String[]{"http://213.63.135.67:8080/RegisterServer/DisplayRoomStatus?floorid=1", "display"});/*Non Secured Connection Source Code*/
//        new getDataFromServerForGroundFloor().execute(new String[]{"https://213.63.135.67/RegisterServer/DisplayRoomStatus?floorid=1", "display"});/*Secured Connection Source Code*/
//        new getDataFromServerForGroundFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=1", "display"});/*Non Secured Connection Source Code, new server*/
        new getDataFromServerForGroundFloor().execute(new String[]{"http://smartmeetingroom.nokia.com:8080/RegisterServer/DisplayRoomStatus?floorid=1", "display"});/*Non Secured Connection Source Code, new server*/
    //booking room activity
        final Intent intent = new Intent(this,BookRoomActivity.class);

        linearLayout_Puppis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomIndex","Puppis");
                startActivity(intent);

            }
        });

        linearLayout_Volans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomIndex","Volans");
                startActivity(intent);

            }
        });

        linearLayout_Sagittarius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomIndex","Sagittarius");
                startActivity(intent);

            }
        });

        linearLayout_Scutum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomIndex","Scutum");
                startActivity(intent);

            }
        });


        linearLayout_Scorpious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("roomIndex","Scorpius");
                startActivity(intent);

            }
        });
    }

    public class getDataFromServerForGroundFloor extends AsyncTask<String, Void, String>
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

            tvOutlookStatusScutum.setTextColor(Color.WHITE);
            tvOutlookStatusVolans.setTextColor(Color.WHITE);
            tvOutlookStatusScorpius.setTextColor(Color.WHITE);
            tvOutlookStatusSagittarius.setTextColor(Color.WHITE);
            tvOutlookStatusVolans.setTextColor(Color.WHITE);
            if(isNetworkAvailable()==false)
            {
                //tvTimeScutum.setText("Room NA: Error");
                btnScutumStatus.setImageResource(R.drawable.icn_grey);
                ////tvDeviceStatusScutum.setText("Room:Error");
                tvOutlookStatusScutum.setText("Outlook:Error");

                //tvTimeScorpius.setText("Room NA: Error");
                btnScopiusStatus.setImageResource(R.drawable.icn_grey);
                ////tvDeviceStatusScorpius.setText("Room: Error");
                tvOutlookStatusScorpius.setText("Outlook: Error");

                //tvTimeSagittarius.setText("Room NA: Error");
                btnSagittariusStatus.setImageResource(R.drawable.icn_grey);
                ////tvDeviceStatusSagittarius.setText("Room: Error");
                tvOutlookStatusSagittarius.setText("Outlook: Error");

                //tvTimeVolans.setText("Room NA: Error");
                btnVolansStatus.setImageResource(R.drawable.icn_grey);
                ////tvDeviceStatusVolans.setText("Room: Error");
                tvOutlookStatusVolans.setText("Outlook: Error");

                //tvTimePuppis.setText("Room NA: Error");
                btnPuppisStatus.setImageResource(R.drawable.icn_grey);
                ////tvDeviceStatusPuppis.setText("Room: Error");
                tvOutlookStatusPuppis.setText("Outlook: Error");

                Toast.makeText(getApplicationContext(),R.string.room_no_internet_connection,Toast.LENGTH_LONG).show();

            }
            else if(GroundFloorRoomList==null || GroundFloorRoomList.size()==0)
                {
                    //tvTimeScutum.setText("Room NA: Error");
                    btnScutumStatus.setImageResource(R.drawable.icn_grey);
                    ////tvDeviceStatusScutum.setText("Room: Error");
                    tvOutlookStatusScutum.setText("Outlook: Error");

                    //tvTimeScorpius.setText("Room NA: Error");
                    btnScopiusStatus.setImageResource(R.drawable.icn_grey);
                    ////tvDeviceStatusScorpius.setText("Room: Error");
                    tvOutlookStatusScorpius.setText("Outlook: Error");

                    //tvTimeSagittarius.setText("Room NA: Error");
                    btnSagittariusStatus.setImageResource(R.drawable.icn_grey);
                    ////tvDeviceStatusSagittarius.setText("Room: Error");
                    tvOutlookStatusSagittarius.setText("Outlook: Error");

                    //tvTimeVolans.setText("Room NA: Error");
                    btnVolansStatus.setImageResource(R.drawable.icn_grey);
                    ////tvDeviceStatusVolans.setText("Room: Error");
                    tvOutlookStatusVolans.setText("Outlook: Error");

                    //tvTimePuppis.setText("Room NA: Error");
                    btnPuppisStatus.setImageResource(R.drawable.icn_grey);
                    ////tvDeviceStatusPuppis.setText("Room: Error");
                    tvOutlookStatusPuppis.setText("Outlook: Error");

                    Toast.makeText(getApplicationContext(),R.string.room_network_down,Toast.LENGTH_LONG).show();
                }
                else {

////                // Scutum
////                tvTimeScutum.setText("Since:"+GroundFloorRoomList.get(0).timeStamp);
////
////                /*Outlook Status*/
////                if(GroundFloorRoomList.get(0).outlookStatus==0)
////                {
////                    tvOutlookStatusScutum.setText("Outlook:Unreserved");
////                }
////                else
////                    if(GroundFloorRoomList.get(0).outlookStatus==1)
////                    {
////                             tvOutlookStatusScutum.setText("Outlook:Reserved");
////                    }
////                    else
////                        tvOutlookStatusScutum.setText("Outlook:N/A");
////
////
////                if(GroundFloorRoomList.get(0).physicalStatus==0)
////                {
////                    //btnScutumStatus.setBackgroundColor(Color.GREEN);
////                    btnScutumStatus.setImageResource(R.drawable.icn_green);
////                    tvDeviceStatusScutum.setText("Room:Free");
////
////                }
////                if(GroundFloorRoomList.get(0).physicalStatus==-1)
////                {
////                   // btnScutumStatus.setBackgroundColor(Color.GRAY);
////                    btnScutumStatus.setImageResource(R.drawable.icn_grey);
////                    tvDeviceStatusScutum.setText("Room:N/A");
////                }
////                if(GroundFloorRoomList.get(0).physicalStatus==1)
////                {
////                    //btnScutumStatus.setBackgroundColor(Color.RED);
////                    btnScutumStatus.setImageResource(R.drawable.icn_red);
////                    tvDeviceStatusScutum.setText("Room:Busy");
////
////                }
//
//                //Puppis
////                tvTimePuppis.setText("Since:"+GroundFloorRoomList.get(1).timeStamp);
////
////                         /*Outlook Status*/
////                         if(GroundFloorRoomList.get(0).outlookStatus==0)
////                         {
////                             tvOutlookStatusPuppis.setText(R.string.outlook_status_free);
////                         }
////                         else
////                         if(GroundFloorRoomList.get(0).outlookStatus==1)
////                         {
////                             tvOutlookStatusPuppis.setText(R.string.outlook_status_busy);
////                         }
////                         else
////                             tvOutlookStatusPuppis.setText(R.string.outlook_status_na);
////
////                if(GroundFloorRoomList.get(1).physicalStatus==0)
////                {
////                    //btnPuppisStatus.setBackgroundColor(Color.GREEN);
////                    btnPuppisStatus.setImageResource(R.drawable.icn_green);
////                    tvDeviceStatusPuppis.setText(R.string.device_status_free);
////
////                }
////                if(GroundFloorRoomList.get(1).physicalStatus==-1)
////                {
////                    //btnPuppisStatus.setBackgroundColor(Color.GRAY);
////                    btnPuppisStatus.setImageResource(R.drawable.icn_grey);
////                    tvDeviceStatusPuppis.setText(R.string.device_status_na);
////
////                }
////                if(GroundFloorRoomList.get(1).physicalStatus==1)
////                {
////                    //btnPuppisStatus.setBackgroundColor(Color.RED);
////                    btnPuppisStatus.setImageResource(R.drawable.icn_red);
////                        tvDeviceStatusPuppis.setText("Room:Busy");
////
////                }
//
//                //Scorpius
////                tvTimeScorpius.setText("Since:"+GroundFloorRoomList.get(2).timeStamp);
////
////                         /*Outlook Status*/
////                         if(GroundFloorRoomList.get(2).outlookStatus==0)
////                         {
////                             tvOutlookStatusScorpius.setText("Outlook:Unreserved");
////                         }
////                         else
////                         if(GroundFloorRoomList.get(2).outlookStatus==1)
////                         {
////                             tvOutlookStatusScorpius.setText("Outlook:Reserved");
////                         }
////                         else
////                             tvOutlookStatusScorpius.setText("Outlook:N/A");
////
////
////                         if(GroundFloorRoomList.get(2).physicalStatus==0)
////                {
////                    //btnScopiusStatus.setBackgroundColor(Color.GREEN);
////                    btnScopiusStatus.setImageResource(R.drawable.icn_green);
////                    tvDeviceStatusScorpius.setText(R.string.device_status_free);
////
////                }
////                if(GroundFloorRoomList.get(2).physicalStatus==-1)
////                {
////                    //btnScopiusStatus.setBackgroundColor(Color.GRAY);
////                    btnScopiusStatus.setImageResource(R.drawable.icn_grey);
////                    tvDeviceStatusScorpius.setText(R.string.device_status_na);
////
////                }
////                if(GroundFloorRoomList.get(2).physicalStatus==1)
////                {
////                    //btnScopiusStatus.setBackgroundColor(Color.RED);
////                    btnScopiusStatus.setImageResource(R.drawable.icn_red);
////                    tvDeviceStatusScorpius.setText("Room:Busy");
////
////                }
////
////                //Sagittarius
////                tvTimeSagittarius.setText("Since:"+GroundFloorRoomList.get(3).timeStamp);
////
////                /*Outlook Status*/
////                if(GroundFloorRoomList.get(0).outlookStatus==0)
////                {
////                   tvOutlookStatusScorpius.setText(R.string.outlook_status_free);
////                }
////                else
////                  if(GroundFloorRoomList.get(0).outlookStatus==1)
////                    {
////                     tvOutlookStatusScorpius.setText(R.string.outlook_status_busy);
////                    }
////                    else
////                        tvOutlookStatusScorpius.setText(R.string.outlook_status_na);
////
////                if(GroundFloorRoomList.get(3).physicalStatus==0)
////                {
////                    //btnSagittariusStatus.setBackgroundColor(Color.GREEN);
////                    btnSagittariusStatus.setImageResource(R.drawable.icn_green);
////                    tvDeviceStatusSagittarius.setText(R.string.device_status_free);
////
////                }
////                if(GroundFloorRoomList.get(3).physicalStatus==-1)
////                {
////                    //btnSagittariusStatus.setBackgroundColor(Color.GRAY);
////                    btnSagittariusStatus.setImageResource(R.drawable.icn_grey);
////                    tvDeviceStatusSagittarius.setText(R.string.device_status_na);
////
////                }
////                if(GroundFloorRoomList.get(3).physicalStatus==1)
////                {
////                    //btnSagittariusStatus.setBackgroundColor(Color.RED);
////                    btnSagittariusStatus.setImageResource(R.drawable.icn_red);
////                    tvDeviceStatusSagittarius.setText(R.string.device_status_busy);
////                }
////
////                //Volans
////                tvTimeVolans.setText("Since:"+GroundFloorRoomList.get(4).timeStamp);
////
////                /*Outlook Status*/
////                if(GroundFloorRoomList.get(0).outlookStatus==0)
////                  {
////                     tvOutlookStatusScorpius.setText(R.string.outlook_status_free);
////                  }
////                else
////                   if(GroundFloorRoomList.get(0).outlookStatus==1)
////                     {
////                      tvOutlookStatusScorpius.setText(R.string.outlook_status_busy);
////                     }
////                     else
////                        tvOutlookStatusScorpius.setText(R.string.outlook_status_na);
////
////                if(GroundFloorRoomList.get(4).physicalStatus==0)
////                {
////                    //btnVolansStatus.setBackgroundColor(Color.GREEN);
////                    btnVolansStatus.setImageResource(R.drawable.icn_green);
////                    tvDeviceStatusVolans.setText(R.string.device_status_free);;
////
////                }
////                if(GroundFloorRoomList.get(4).physicalStatus==-1)
////                {
////                    //btnVolansStatus.setBackgroundColor(Color.GRAY);
////                    btnVolansStatus.setImageResource(R.drawable.icn_grey);
////                    tvDeviceStatusVolans.setText(R.string.device_status_na);
////
////                }
////                if(GroundFloorRoomList.get(4).physicalStatus==1)
////                {
////                    //btnVolansStatus.setBackgroundColor(Color.RED);
////                    btnVolansStatus.setImageResource(R.drawable.icn_red);
////                    tvDeviceStatusVolans.setText(R.string.device_status_busy);
////                }
//
//
            for(int i=0;i<GroundFloorRoomList.size();i++)
            {

/**************************Volans*********************************/
                if(GroundFloorRoomList.get(i).roomName.equalsIgnoreCase("Volans"))
                {


//                if(GroundFloorRoomList.get(i).physicalStatus==0)
//                    tvTimeVolans.setText("Room Free:"+convertTime(GroundFloorRoomList.get(i).timeStamp));
//                else if(GroundFloorRoomList.get(i).physicalStatus==1)
//                    tvTimeVolans.setText("Room Busy:"+convertTime(GroundFloorRoomList.get(i).timeStamp));
//                else
//                    tvTimeVolans.setText("Room NA:"+convertTime(GroundFloorRoomList.get(i).timeStamp));

                /*Outlook Status*/
                if(GroundFloorRoomList.get(i).outlookStatus==0)
                  {
                     tvOutlookStatusVolans.setText("Outlook:Unreserved");
                     tvOutlookStatusVolans.setTextColor(Color.GREEN);
                  }
                else
                   if(GroundFloorRoomList.get(i).outlookStatus==1)
                     {
                         tvOutlookStatusVolans.setText("Outlook:Reserved");
                         tvOutlookStatusVolans.setTextColor(Color.RED);
                     }
                     else
                   {
                       tvOutlookStatusVolans.setText("Outlook:N/A");
                       tvOutlookStatusVolans.setTextColor(Color.GRAY);
                   }

                if(GroundFloorRoomList.get(i).physicalStatus==0)
                {
                    //btnVolansStatus.setBackgroundColor(Color.GREEN);
                    btnVolansStatus.setImageResource(R.drawable.icn_green);
                    ////tvDeviceStatusVolans.setText("Room:Free");;

                }
                if(GroundFloorRoomList.get(i).physicalStatus==-1)
                {
                    //btnVolansStatus.setBackgroundColor(Color.GRAY);
                    btnVolansStatus.setImageResource(R.drawable.icn_grey);
                    ////tvDeviceStatusVolans.setText("Room:N/A");

                }
                if(GroundFloorRoomList.get(i).physicalStatus==1)
                {
                    //btnVolansStatus.setBackgroundColor(Color.RED);
                    btnVolansStatus.setImageResource(R.drawable.icn_red);
                    ////tvDeviceStatusVolans.setText("Room:Busy");
                }
                }
/**************************Scutum*********************************/
                if(GroundFloorRoomList.get(i).roomName.equalsIgnoreCase("Scutum"))
                {
//                    if(GroundFloorRoomList.get(i).physicalStatus==0)
//                        tvTimeScutum.setText("Room Free:"+convertTime(GroundFloorRoomList.get(i).timeStamp));
//                    else if(GroundFloorRoomList.get(i).physicalStatus==1)
//                        tvTimeScutum.setText("Room Busy:"+convertTime(GroundFloorRoomList.get(i).timeStamp));
//                    else
//                        tvTimeScutum.setText("Room NA:"+convertTime(GroundFloorRoomList.get(i).timeStamp));

                /*Outlook Status*/
                if(GroundFloorRoomList.get(i).outlookStatus==0)
                {
                    tvOutlookStatusScutum.setText("Outlook:Unreserved");
                    tvOutlookStatusScutum.setTextColor(Color.GREEN);
                }
                else
                    if(GroundFloorRoomList.get(i).outlookStatus==1)
                    {
                             tvOutlookStatusScutum.setText("Outlook:Reserved");
                             tvOutlookStatusScutum.setTextColor(Color.RED);
                    }
                    else
                    {
                        tvOutlookStatusScutum.setText("Outlook:N/A");
                        tvOutlookStatusScutum.setTextColor(Color.GRAY);
                    }


                if(GroundFloorRoomList.get(i).physicalStatus==0)
                {
                    //btnScutumStatus.setBackgroundColor(Color.GREEN);
                    btnScutumStatus.setImageResource(R.drawable.icn_green);
                    //tvDeviceStatusScutum.setText("Room:Free");

                }
                if(GroundFloorRoomList.get(i).physicalStatus==-1)
                {
                   // btnScutumStatus.setBackgroundColor(Color.GRAY);
                    btnScutumStatus.setImageResource(R.drawable.icn_grey);
                    //tvDeviceStatusScutum.setText("Room:N/A");
                }
                if(GroundFloorRoomList.get(i).physicalStatus==1)
                {
                    //btnScutumStatus.setBackgroundColor(Color.RED);
                    btnScutumStatus.setImageResource(R.drawable.icn_red);
                    //tvDeviceStatusScutum.setText("Room:Busy");

                }
                }
/**************************Saggitaruis*********************************/
                if(GroundFloorRoomList.get(i).roomName.equalsIgnoreCase("Sagittarius"))
                {
//                    if(GroundFloorRoomList.get(i).physicalStatus==0)
//                        tvTimeSagittarius.setText("Room Free:"+convertTime(GroundFloorRoomList.get(i).timeStamp));
//                    else if(GroundFloorRoomList.get(i).physicalStatus==1)
//                        tvTimeSagittarius.setText("Room Busy:"+convertTime(GroundFloorRoomList.get(i).timeStamp));
//                    else
//                        tvTimeSagittarius.setText("Room NA:"+convertTime(GroundFloorRoomList.get(i).timeStamp));


                /*Outlook Status*/
                if(GroundFloorRoomList.get(i).outlookStatus==0)
                {
                   tvOutlookStatusSagittarius.setText("Outlook:Unreserved");
                   tvOutlookStatusSagittarius.setTextColor(Color.GREEN);
                }
                else
                  if(GroundFloorRoomList.get(i).outlookStatus==1)
                    {
                        tvOutlookStatusSagittarius.setText("Outlook:Reserved");
                        tvOutlookStatusSagittarius.setTextColor(Color.RED);
                    }
                    else
                  {
                      tvOutlookStatusSagittarius.setText("Outlook:N/A");
                      tvOutlookStatusSagittarius.setTextColor(Color.GRAY);
                  }

                if(GroundFloorRoomList.get(i).physicalStatus==0)
                {
                    btnSagittariusStatus.setImageResource(R.drawable.icn_green);
                    //tvDeviceStatusSagittarius.setText("Room:Free");

                }
                if(GroundFloorRoomList.get(i).physicalStatus==-1)
                {
                    //btnSagittariusStatus.setBackgroundColor(Color.GRAY);
                    btnSagittariusStatus.setImageResource(R.drawable.icn_grey);
                    //tvDeviceStatusSagittarius.setText("Room:N/A");

                }
                if(GroundFloorRoomList.get(i).physicalStatus==1)
                {
                    //btnSagittariusStatus.setBackgroundColor(Color.RED);
                    btnSagittariusStatus.setImageResource(R.drawable.icn_red);
                    //tvDeviceStatusSagittarius.setText("Room:Busy");
                }
                }
/**************************Scorpius*********************************/
                if(GroundFloorRoomList.get(i).roomName.equalsIgnoreCase("Scorpius"))
                {

//                    if(GroundFloorRoomList.get(i).physicalStatus==0)
//                        tvTimeScorpius.setText("Room Free:"+convertTime(GroundFloorRoomList.get(i).timeStamp));
//                    else if(GroundFloorRoomList.get(i).physicalStatus==1)
//                        tvTimeScorpius.setText("Room Busy:"+convertTime(GroundFloorRoomList.get(i).timeStamp));
//                    else
//                        tvTimeScorpius.setText("Room NA:"+convertTime(GroundFloorRoomList.get(i).timeStamp));


                    /*Outlook Status*/
                    if(GroundFloorRoomList.get(i).outlookStatus==0)
                    {
                        tvOutlookStatusScorpius.setText("Outlook:Unreserved");
                        tvOutlookStatusScorpius.setTextColor(Color.GREEN);
                    }
                    else
                    if(GroundFloorRoomList.get(i).outlookStatus==1)
                    {
                        tvOutlookStatusScorpius.setText("Outlook:Reserved");
                        tvOutlookStatusScorpius.setTextColor(Color.RED);
                    }
                    else
                    {
                        tvOutlookStatusScorpius.setText("Outlook:N/A");
                        tvOutlookStatusScorpius.setTextColor(Color.GRAY);
                    }


                    if(GroundFloorRoomList.get(i).physicalStatus==0)
                    {
                        //btnScopiusStatus.setBackgroundColor(Color.GREEN);
                        btnScopiusStatus.setImageResource(R.drawable.icn_green);
                        //tvDeviceStatusScorpius.setText("Room:Free");

                    }
                    if(GroundFloorRoomList.get(i).physicalStatus==-1)
                    {
                        //btnScopiusStatus.setBackgroundColor(Color.GRAY);
                        btnScopiusStatus.setImageResource(R.drawable.icn_grey);
                        //tvDeviceStatusScorpius.setText("Room:N/A");

                    }
                    if(GroundFloorRoomList.get(i).physicalStatus==1)
                    {
                        //btnScopiusStatus.setBackgroundColor(Color.RED);
                        btnScopiusStatus.setImageResource(R.drawable.icn_red);
                        //tvDeviceStatusScorpius.setText("Room:Busy");

                    }
                }
/**************************Puppis*********************************/
                if(GroundFloorRoomList.get(i).roomName.equalsIgnoreCase("Puppis"))
                {
                    //Puppis

//                    if(GroundFloorRoomList.get(i).physicalStatus==0)
//                        tvTimePuppis.setText("Room Free:"+convertTime(GroundFloorRoomList.get(i).timeStamp));
//                    else if(GroundFloorRoomList.get(i).physicalStatus==1)
//                        tvTimePuppis.setText("Room Busy:"+convertTime(GroundFloorRoomList.get(i).timeStamp));
//                    else
//                        tvTimePuppis.setText("Room NA:"+convertTime(GroundFloorRoomList.get(i).timeStamp));


                    /*Outlook Status*/
                    if(GroundFloorRoomList.get(i).outlookStatus==0)
                    {
                        tvOutlookStatusPuppis.setText("Outlook:Unreserved");
                        tvOutlookStatusPuppis.setTextColor(Color.GREEN);
                    }
                    else
                    if(GroundFloorRoomList.get(i).outlookStatus==1)
                    {
                        tvOutlookStatusPuppis.setText("Outlook:Reserved");
                        tvOutlookStatusPuppis.setTextColor(Color.RED);
                    }
                    else
                    {
                        tvOutlookStatusPuppis.setText("Outlook:N/A");
                        tvOutlookStatusPuppis.setTextColor(Color.GRAY);
                    }

                    if(GroundFloorRoomList.get(i).physicalStatus==0)
                    {
                        //btnPuppisStatus.setBackgroundColor(Color.GREEN);
                        btnPuppisStatus.setImageResource(R.drawable.icn_green);
                        //tvDeviceStatusPuppis.setText("Room:Free");

                    }
                    if(GroundFloorRoomList.get(i).physicalStatus==-1)
                    {
                        //btnPuppisStatus.setBackgroundColor(Color.GRAY);
                        btnPuppisStatus.setImageResource(R.drawable.icn_grey);
                        //tvDeviceStatusPuppis.setText("Room:N/A");

                    }
                    if(GroundFloorRoomList.get(i).physicalStatus==1)
                    {
                        //btnPuppisStatus.setBackgroundColor(Color.RED);
                        btnPuppisStatus.setImageResource(R.drawable.icn_red);
                        //tvDeviceStatusPuppis.setText("Room:Busy");

                    }
                }




            }//for loop
            }//else clause

        }//onPostExecute Clause

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
                    GroundFloorRoomList = new ArrayList<>();
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
                        GroundFloorRoomList.add(i,room);

                    }

//                    String[] strArr = resDisplay.split(",");
//                    int i = 0;
//
//                    int arrysize = strArr.length / 3;
//
//                    GroundFloorRoomList = new ArrayList<>();
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
//                        GroundFloorRoomList.add(j,room);
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
//                urlConnection.setRequestMethod("GET");
//
//                Log.i(_TAG, "KeyShibsessionCookie : " + sharedPreferences.getString("KeyShibsessionCookie",null));
//                urlConnection.setRequestProperty("Cookie", sharedPreferences.getString("KeyShibsessionCookie",null));
//                int responseCode = urlConnection.getResponseCode();
//
//                if(responseCode==HttpURLConnection.HTTP_OK)
//                {
//                    resDisplay=readStream(urlConnection.getInputStream());//
//                    String roomname;
//                    int physta,outlooksta;
//                    long time;
//                    JSONArray jsonArray = new JSONArray(resDisplay);
//                    int parselength = jsonArray.length();
//                    JSONObject jsonObject;
//                    GroundFloorRoomList = new ArrayList<>();
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
//                        GroundFloorRoomList.add(i,room);
//
//                    }
//
////                    String[] strArr = resDisplay.split(",");
////                    int i = 0;
////
////                    int arrysize = strArr.length / 3;
////
////                    GroundFloorRoomList = new ArrayList<>();
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
////                        GroundFloorRoomList.add(j,room);
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
                    progressDialog = new ProgressDialog(GroundFloorActivity.this, 0);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

//                    new getDataFromServerForGroundFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/display0", "display"});
//                    new getDataFromServerForGroundFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=1", "display"});
//                    new getDataFromServerForGroundFloor().execute(new String[]{"http://213.63.135.67:8080/RegisterServer/DisplayRoomStatus?floorid=1", "display"});/*Non Secured Connection Source Code*/
//                    new getDataFromServerForGroundFloor().execute(new String[]{"https://213.63.135.67/RegisterServer/DisplayRoomStatus?floorid=1", "display"});/*Secured Connection Source Code*/
//                    new getDataFromServerForGroundFloor().execute(new String[]{"http://87.254.206.105:8080/RegisterServer/DisplayRoomStatus?floorid=1", "display"});/*Non Secured Connection Source Code, new server code*/
                    new getDataFromServerForGroundFloor().execute(new String[]{"http://smartmeetingroom.nokia.com:8080/RegisterServer/DisplayRoomStatus?floorid=1", "display"});/*Non Secured Connection Source Code, new server code*/
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
