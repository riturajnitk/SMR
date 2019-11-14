package com.example.decu.allfloormeetingroomstatus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SamlLoginActivity extends AppCompatActivity {


    public static String TAG = "SamlLoginActivity_Logs";

    public static final String strSMRSharedPref = "SMRSharedPref" ;

    public static String cookiesSuccessLogin;
    public static String cookiesshibsession;

    public static String mshibsessionvalue;
    public static String mshibsessionkey;


    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saml_login);

        final WebView wv_saml = (WebView) findViewById(R.id.webview_saml);

        //clearing cache and format data
        CookieManager cookieManager=CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.removeSessionCookie();
        wv_saml.clearSslPreferences();;
        wv_saml.clearHistory();
        wv_saml.clearFormData();

        wv_saml.getSettings().setJavaScriptEnabled(true);
        wv_saml.getSettings().setAppCacheEnabled(true);
        wv_saml.getSettings().setDatabaseEnabled(true);
        wv_saml.getSettings().setDomStorageEnabled(true);
        wv_saml.getSettings().setSupportZoom(true);
        wv_saml.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_saml.getSettings().setBuiltInZoomControls(true);
        wv_saml.getSettings().setGeolocationEnabled(true);

        wv_saml.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        sharedPreferences = getSharedPreferences("SMRSharedPref", Context.MODE_PRIVATE);
//        sharedPreferences.edit().putBoolean("isAuthenticated", true).apply();
        trustAllCertificates();

//disb
//        CookieManager cookieManager=CookieManager.getInstance();
//        cookieManager.removeAllCookie();
//        cookieManager.removeSessionCookie();
//        cookieSyncMngr.stopSync();
//        cookieSyncMngr.sync();



       // wv_saml.loadUrl("https://213.63.135.67/RegisterServer/DisplayRooms.html");//load URL for SAML authentication

       //wv_saml.loadUrl("http://87.254.206.105:8080/RegisterServer/DisplayRooms.html");//load URL for SAML authentication

        //wv_saml.loadUrl("https://213.63.135.67/Shibboleth.sso/Login");//load URL for SAML authentication
//        wv_saml.loadUrl("https://213.63.135.67/RegisterServer/loggeduser");//old server

        //wv_saml.loadUrl("https://87.254.206.105:8443/RegisterServer/loggeduser");// new server
        wv_saml.loadUrl("https://smartmeetingroom.nokia.com:8443/RegisterServer/loggeduser");// new server



//        mLoginTime = System.currentTimeMillis();
//
//        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        final SharedPreferences.Editor editor = mPreferences.edit();
//        //editor.putLong("LoginTime",mLoginTime);
//        //editor.commit();




        wv_saml.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler
                    handler, SslError error)
            {
               //proceed in case of error
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "<----URL Before entering any override method--->> " + url);

                if(url.contains("_eventId_proceed"))// check loggin success
                {

//                    cookiesSuccessLogin = CookieManager.getInstance().getCookie(url);
//                    Log.i(TAG, " cookiesSuccessLogin: " + cookiesSuccessLogin);
//
//                    Toast.makeText(getApplicationContext(),"Logged In Successfully",Toast.LENGTH_SHORT).show();
//
//
                    finish();//close webview and main activity
//
////                    long current_logintime = System.currentTimeMillis();
////                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
////
////                    intent.putExtra("logintime",current_logintime);//sending data via intent
////
////                    startActivity(intent);
                }
                else if(url.contains("Authentication-error"))
                {
                    Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                }
                else if(url.contains("REALMOID"))// check for logging screen
                {
                    Log.i(TAG, "logging screen to enter username/email and password " + url);
                }
                else if(url.contains("DisplayRooms"))
                {
                    //finish();
//                    cookiesshibsession = CookieManager.getInstance().getCookie(url);
//                    loginSessionID = cookiesshibsession.substring(cookiesshibsession.indexOf("=_") + 1);//, cookiesDisplayRoomAccess.indexOf(Coo))
//                    Log.i(TAG, "cookiesshibsession = " + cookiesshibsession);
//
//                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//
//
//                    long current_logintime = System.currentTimeMillis();
//
//                    intent.putExtra("shibsession",loginSessionID);//sending data via intent
//                    intent.putExtra("logintime",current_logintime);//sending data via intent
//
//                    startActivity(intent);

                    long current_logintime = System.currentTimeMillis();

                    sharedPreferences.edit().putBoolean("isAuthenticated", true).apply();
                    sharedPreferences.edit().putLong("KeyLoginTime", current_logintime).apply();
                    sharedPreferences.edit().putString("KeyShibsession", "shibvalue").apply();
                    sharedPreferences.edit().putString("KeyShibsessionValue", "shibvalue").apply();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else if(url.contains("Shibboleth.sso/Login"))
                {


                    //finish();
                    cookiesshibsession = CookieManager.getInstance().getCookie(url);
                    mshibsessionkey = cookiesshibsession.substring(cookiesshibsession.indexOf("_sh"),cookiesshibsession.indexOf("="));
                    mshibsessionvalue = cookiesshibsession.substring(cookiesshibsession.indexOf("=_") + 1);//, cookiesDisplayRoomAccess.indexOf(Coo))

                    sharedPreferences.edit().putBoolean("isAuthenticated", true).apply();
                    sharedPreferences.edit().putLong("KeyLoginTime", System.currentTimeMillis()).apply();
                    sharedPreferences.edit().putString("KeyShibsessionValue", mshibsessionvalue).apply();
                    sharedPreferences.edit().putString("KeyShibsession", mshibsessionkey).apply();
                    //sharedPreferences.edit().putLong("KeyLastUsedTime", System.currentTimeMillis()).apply();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                else if(url.contains("loggeduser"))
                {


                    wv_saml.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

                    Log.i(TAG,",<------------------loggeduser URL if statement---------------->");
                    cookiesshibsession = CookieManager.getInstance().getCookie(url);
                    mshibsessionkey = cookiesshibsession.substring(cookiesshibsession.indexOf("_sh"),cookiesshibsession.indexOf("="));
                    mshibsessionvalue = cookiesshibsession.substring(cookiesshibsession.indexOf("=_") + 1);//, cookiesDisplayRoomAccess.indexOf(Coo))

                    sharedPreferences.edit().putBoolean("isAuthenticated", true).apply();
                    sharedPreferences.edit().putLong("KeyLoginTime", System.currentTimeMillis()).apply();
                    sharedPreferences.edit().putString("KeyShibsessionValue", mshibsessionvalue).apply();
                    sharedPreferences.edit().putString("KeyShibsession", mshibsessionkey).apply();
                    sharedPreferences.edit().putString("KeyShibsessionCookie", cookiesshibsession).apply();
                    //sharedPreferences.edit().putLong("KeyLastUsedTime", System.currentTimeMillis()).apply();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    //finish();//kill webview on back press


    }

    class MyJavaScriptInterface
    {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html)
        {
            // process the html as needed by the app

            Log.i("Loaded Image", "Loaded HTML" + html);

            Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(html);
            while (m.find()) {
                //System.out.println(m.group());

                Log.i("Loaded Image", "Loaded HTML emaild" + m.group());
                sharedPreferences.edit().putString("KeyUserEmaildId",m.group()).apply();
            }

            //sharedPreferences.edit().putString("emailid",html).apply();//

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
