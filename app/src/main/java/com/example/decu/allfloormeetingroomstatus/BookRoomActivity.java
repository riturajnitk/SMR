package com.example.decu.allfloormeetingroomstatus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class BookRoomActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText v_et_sdate,v_et_stime,v_et_edate,v_et_etime,v_et_orgemail,v_et_attemail,v_et_sub,v_et_roomname;
    private AutoCompleteTextView v_auto_tv_roomname;
    private Button bookingButton,confirmButton;

    private String roomName,subject,orgEmail,attendEmails,resDisplay;
    private String sDate,sTime,eDate,eTime;
    private Long startTime,endTime;
    //private String baseURL="http://87.254.206.105:8080/RegisterServer/BookRoom?name=",bookingURL=null;
//    private String baseURL="http://213.63.135.67:8080/RegisterServer/BookRoom?name=",bookingURL=null; /*Non Secured Connection Source Code*/
//    private String baseURL="http://87.254.206.105:8080/RegisterServer/BookRoom?name=",bookingURL=null; /*Non Secured Connection Source Code, new server*/
    private String baseURL="http://smartmeetingroom.nokia.com:8080/RegisterServer/BookRoom?name=",bookingURL=null; /*Non Secured Connection Source Code, new server*/
    //private String baseURL="https://213.63.135.67/RegisterServer/BookRoom?name=",bookingURL=null; /*Secured Connection Source Code*/
    private int mDay,mYear,mMonth,mHour,mMinute;
    private Calendar mCalendar;
    private String sucessCode="200";
//    private String[] roomList={"Scutum","Scorpius","Sagittarius","Volans","Puppis","Musca","Mensa","Norma","Lyra","Lynx","Sydney"};
    private Spinner spinnerRoom;

    public static final String strSMRSharedPref = "SMRSharedPref" ;
    public static SharedPreferences sharedPreferences;

    private final String _TAG="BookRoomActivity_logs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);

//        v_et_roomname =(EditText)findViewById(R.id.edttxt_roomname);//Room Name
//        v_auto_tv_roomname=(AutoCompleteTextView)findViewById(R.id.autotv_roomname);
        v_et_sdate = (EditText)findViewById(R.id.edttxt_sdate);//Start Date
        v_et_stime = (EditText)findViewById(R.id.edttxt_stime);//Start Time
        v_et_edate = (EditText)findViewById(R.id.edttxt_edate);//end date
        v_et_etime = (EditText)findViewById(R.id.edttxt_etime);// end time
//        v_et_orgemail = (EditText)findViewById(R.id.edttxt_orgeml);//organizer emails
        v_et_attemail = (EditText)findViewById(R.id.edttxt_atdeml);// attendies emails
        v_et_sub = (EditText)findViewById(R.id.edttxt_subject);// subject
//        confirmButton =(Button)findViewById(R.id.btn_confirm);//confirm button
        bookingButton =(Button)findViewById(R.id.btn_booking);// booking button

        //hide input type keyboards
        v_et_sdate.setInputType(InputType.TYPE_NULL);
        v_et_edate.setInputType(InputType.TYPE_NULL);
        v_et_stime.setInputType(InputType.TYPE_NULL);
        v_et_etime.setInputType(InputType.TYPE_NULL);

        //setting current date

        SimpleDateFormat cDate = new SimpleDateFormat( "dd-MM-yyyy" );
        SimpleDateFormat cTime = new SimpleDateFormat( "HH:mm" );
        v_et_sdate.setText( cDate.format( new Date() ));
        v_et_stime.setText( cTime.format( new Date() ));


        SimpleDateFormat fDate = new SimpleDateFormat( "dd-MM-yyyy" );
        SimpleDateFormat fTime = new SimpleDateFormat( "HH:mm" );
        v_et_edate.setText( fDate.format( new Date() ));
        v_et_etime.setText( fTime.format( new Date() ));

//shared prefs

        sharedPreferences = getSharedPreferences("SMRSharedPref", Context.MODE_PRIVATE);
        trustAllCertificates();//trusting certificates
        //auto complete textview
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this,android.R.layout.select_dialog_item, roomList);
//            v_auto_tv_roomname.setThreshold(0);
//            v_auto_tv_roomname.setAdapter(adapter);

        //Spinner room
        spinnerRoom=(Spinner)findViewById(R.id.spinner_room);
        spinnerRoom.setOnItemSelectedListener(this);
        List<String> roomsList = new ArrayList<String>();
        roomsList.add("Scutum");
        roomsList.add("Scorpius");
        roomsList.add("Sagittarius");
        roomsList.add("Volans");
        roomsList.add("Puppis");
        roomsList.add("Musca");
        roomsList.add("Mensa");
        roomsList.add("Norma");
        roomsList.add("Lyra");
        roomsList.add("Lynx");
        roomsList.add("Sydney");


// Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerRoom.setAdapter(dataAdapter);
        //updating focussed spinner data
        String data = getIntent().getExtras().getString("roomIndex");
        int index;
        for(index=0;index<roomsList.size();index++)
        {

            if(roomsList.get(index).equalsIgnoreCase(data))
            {
                break;
            }
        }

        spinnerRoom.setSelection(index);

//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                roomName =v_et_roomname.getText().toString();//Room Name
//                subject =v_et_sub.getText().toString();// Subject
//                orgEmail =v_et_orgemail.getText().toString();// Org Emails
//                attendEmails=v_et_attemail.getText().toString();// Attendies Email
//                sDate=v_et_sdate.getText().toString();//start date
//                eDate=v_et_edate.getText().toString();// end date
//                sTime=v_et_stime.getText().toString();// start time
//                eTime=v_et_etime.getText().toString();// end time
//
//                //convert date and times to milliseconds
//
//                String startStringDateTime = sDate.toString()+" "+sTime.toString();
//                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//                try {
//                    Date mDate = sdf1.parse(startStringDateTime);
//                    startTime = mDate.getTime();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                String endStringDateTime = eDate.toString()+" "+eTime.toString();
//                SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//                try {
//                    Date mDate = sdf2.parse(endStringDateTime);
//                    endTime = mDate.getTime();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            //prepare URL
//
//                bookingURL=baseURL+roomName+"&email="+orgEmail+"&startTime="+startTime+"&endTime="+endTime+"&attendees="+attendEmails+"&subject="+subject;
//
//            }
//        });


        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //added value initializers
                //roomName =v_et_roomname.getText().toString();//Room Name
                subject =v_et_sub.getText().toString();// Subject
                //orgEmail =v_et_orgemail.getText().toString();// Org Emails
                attendEmails=v_et_attemail.getText().toString();// Attendies Email
                sDate=v_et_sdate.getText().toString();//start date
                eDate=v_et_edate.getText().toString();// end date
                sTime=v_et_stime.getText().toString();// start time
                eTime=v_et_etime.getText().toString();// end time


                //setting org email by sharedpreferences

                orgEmail = sharedPreferences.getString("KeyUserEmaildId",null);
                //orgEmail = "ritu.raj@nokia.com";//fixed
                //convert date and times to milliseconds
                if(sDate.matches("") || sTime.matches("") || eDate.matches("")||eTime.matches(""))
                {
                    Toast.makeText(getApplicationContext(),"Invalid Entries!",Toast.LENGTH_SHORT).show();
                }
                else if(subject.matches(""))
                {
                    Toast.makeText(getApplicationContext(),"Subject is empty!",Toast.LENGTH_SHORT).show();
                }
//                else if(orgEmail.matches(""))
//                {
//                    Toast.makeText(getApplicationContext(),"Organizer Email is empty!",Toast.LENGTH_SHORT).show();
//                }
//                else if(isInvalidEmail(orgEmail))
//                {
//                    Toast.makeText(getApplicationContext(),"email domain should be @nokia.com",Toast.LENGTH_SHORT).show();
//                }
                else
                {
                    String startStringDateTime = sDate.toString()+" "+sTime.toString();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    try {
                        Date mDate = sdf1.parse(startStringDateTime);
                        startTime = mDate.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String endStringDateTime = eDate.toString()+" "+eTime.toString();
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    try {
                        Date mDate = sdf2.parse(endStringDateTime);
                        endTime = mDate.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(endTime<=startTime)
                    {

                        Toast.makeText(getApplicationContext(),"Meeting Start Time must be less than Meeting End Time",Toast.LENGTH_SHORT).show();

                    }
//                    else if(startTime<=System.currentTimeMillis()) removed the case handling
//                    {
//                        Toast.makeText(getApplicationContext(),"Meeting Start Time must be greater than current Time",Toast.LENGTH_SHORT).show();
//                    }
                    else
                    {
//prepare URL
                        bookingURL=baseURL+roomName+"&email="+orgEmail+"&startTime="+startTime+"&endTime="+endTime+"&attendees="+attendEmails+"&subject="+subject;
                        //bookingURL=baseURL+roomName+"&email="+"&startTime="+startTime+"&endTime="+endTime+"&attendees="+attendEmails+"&subject="+subject;
                        //bookingURL=baseURL+roomName+"&email="+"&startTime="+startTime+"&endTime="+endTime+"&attendees="+attendEmails+"&subject="+subject;

                        Toast.makeText(getApplicationContext(),"Request Sent..",Toast.LENGTH_SHORT).show();
                        //executing URL
                        new setRoomBookingOnServer().execute(new String[]{bookingURL, "booking"});


                        finish();//close activity on submission request
                    }


                }//end else


            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //String item = parent.getItemAtPosition(position).toString();
        roomName=parent.getItemAtPosition(position).toString();
        // Showing selected spinner item

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    //Date Picker dialogs
    public void chooseFromDate(View view) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        v_et_sdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void chooseToDate(View view) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        v_et_edate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void chooseFromTime(View view)
    {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
//temp issue fixed 00 is not showing when minute is set
                        //v_et_stime.setText(hourOfDay + ":" + minute);
                        if(minute==0)
                            v_et_stime.setText(hourOfDay + ":" + minute+"0");
                        else
                            v_et_stime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void chooseToTime(View view)
    {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
//temp issue fixed 00 is not showing when minute is set
//                        v_et_etime.setText(hourOfDay + ":" + minute);
                        if(minute==0)
                            v_et_etime.setText(hourOfDay + ":" + minute+"0");
                        else
                            v_et_etime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public class setRoomBookingOnServer extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            String code=null,msg=null;
            code=resDisplay.substring(resDisplay.indexOf(":")+1,resDisplay.indexOf(","));//getting code
            msg=resDisplay.substring(resDisplay.indexOf(":\"")+2,resDisplay.indexOf("\"}"));//getting code
            //toast display for room status
//            if(s.equalsIgnoreCase(sucessCode))
//            {
//
//                Toast.makeText(getApplicationContext(),"Room Booked Sucessfully",Toast.LENGTH_SHORT).show();
//            }
//            else if(s.equalsIgnoreCase("406"))
//            {
//
//                Toast.makeText(getApplicationContext(),"Room is not available on requested time Slot",Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(),"Unable to book room due to Technical problem",Toast.LENGTH_SHORT).show();
//            }
            if(code.equalsIgnoreCase(sucessCode))
            {

                Toast.makeText(getApplicationContext(),"Room Booked Sucessfully",Toast.LENGTH_SHORT).show();
            }
            else if(code.equalsIgnoreCase("406"))
            {

                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Unable to book room due to Technical problem",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            /*Non Secured Connection Source Code*/
            URL url;
            HttpURLConnection urlConnection = null;
            String code=null,msg;
//            roomName =v_et_roomname.getText().toString();
//            subject =v_et_sub.getText().toString();
//            orgEmail =v_et_orgemail.getText().toString();
//            attendEmails=v_et_attemail.getText().toString();
            try {
                url = new URL(strings[0]);
                //setting property in url connection


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("email",orgEmail);//setting property

                int responseCode = urlConnection.getResponseCode();



                if(responseCode==HttpURLConnection.HTTP_OK)
                {
                    resDisplay=readStream(urlConnection.getInputStream());//


                    code=resDisplay.substring(resDisplay.indexOf(":")+1,resDisplay.indexOf(","));//getting code

//                    JSONArray jsonArray = new JSONArray(resDisplay);
//                    JSONObject jsonObject;
//
//                    jsonObject=new JSONObject();
//                    code = (String) jsonObject.get("code");
//                    msg = (String) jsonObject.get("msg");



                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }

            ///*Secured Connection Source Code*/

//            URL url;
//            HttpsURLConnection urlConnection = null;
//            String code=null,msg;
////            roomName =v_et_roomname.getText().toString();
////            subject =v_et_sub.getText().toString();
////            orgEmail =v_et_orgemail.getText().toString();
////            attendEmails=v_et_attemail.getText().toString();
//            try {
//                url = new URL(strings[0]);
//                //setting property in url connection
//
//
//                urlConnection = (HttpsURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//
//                Log.i(_TAG, "KeyShibsessionCookie : " + sharedPreferences.getString("KeyShibsessionCookie",null));
//                urlConnection.setRequestProperty("Cookie", sharedPreferences.getString("KeyShibsessionCookie",null));
//                urlConnection.setRequestProperty("email",orgEmail);//setting property
//
//                int responseCode = urlConnection.getResponseCode();
//                Log.i(_TAG," responseCode "+urlConnection.getResponseCode());
////                String URlconnectionCont = urlConnection.getContent().toString();
////                Log.i(_TAG," booking log "+urlConnection.getContent());
//
//
//
//                if(responseCode==HttpURLConnection.HTTP_OK)
//                {
//                    resDisplay=readStream(urlConnection.getInputStream());//
//
//
//                    code=resDisplay.substring(resDisplay.indexOf(":")+1,resDisplay.indexOf(","));//getting code
//
////                    JSONArray jsonArray = new JSONArray(resDisplay);
////                    JSONObject jsonObject;
////
////                    jsonObject=new JSONObject();
////                    code = (String) jsonObject.get("code");
////                    msg = (String) jsonObject.get("msg");
//
//
//
//                }
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }


//            return code;
            return resDisplay;
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

    public boolean isInvalidEmail(String str)
    {

        if(str.contains("@nokia.com"))
            return false;
        else
            return true;

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
