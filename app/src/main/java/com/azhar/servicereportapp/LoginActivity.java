package com.azhar.servicereportapp;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.azhar.servicereportapp.networkUtil.CheckNetwork;
import com.azhar.servicereportapp.networkUtil.ServiceHandler;
import com.azhar.servicereportapp.networkUtil.SharedPrefClass;
import com.azhar.servicereportapp.networkUtil.WebserviceAPI;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by WIN 10 on 9/3/2017.
 */
public class LoginActivity extends AppCompatActivity {

//    private CoordinatorLayout coordinatorLayout;
    private EditText et_mobile, et_pass;
    private TextView tv_forgetpassword, tv_register;
    private Button btn_login;

    private SharedPrefClass mSharedPrefClass = null;
    private WebserviceAPI webserviceAPI;
    String fcm_token = null;
    public static String TAG = "SERVICE REPORT APP";
    public static final String[] PERMISSION_ALL = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
    };
    public static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(LoginActivity.this, PERMISSION_ALL, PERMISSION_REQUEST_CODE);
        }
        setContentView(R.layout.activity_login);
        init();
        webserviceAPI = new WebserviceAPI(getApplicationContext());
        mSharedPrefClass = new SharedPrefClass(getApplicationContext());

        if (CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
        {

        } else {
//            Snackbar.make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
        }

        try {
            String id=mSharedPrefClass.getUserId();
            if(id.equals("")){
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: "+e);
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_mobile.getText().toString().length() < 10) {
                    Toast.makeText(getApplicationContext(), "Please insert your mobile number", Toast.LENGTH_SHORT).show();
                } else if (et_pass.getText().toString().length() == 3) {
                    Toast.makeText(getApplicationContext(), "Please insert the correct password", Toast.LENGTH_SHORT).show();
                } else {
                    fcm_token = FirebaseInstanceId.getInstance().getToken();
                    Log.d(TAG, "Token: " + fcm_token);
                    new loginAsynctask().execute();
                }
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

    }

    public void init() {
//        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_forgetpassword = (TextView) findViewById(R.id.tv_forgetpassword);
        tv_register = (TextView) findViewById(R.id.tv_register);
    }

    class loginAsynctask extends AsyncTask<String, String, String> {
        String jsnStr = null;
        String mob = et_mobile.getText().toString();
        String pass = et_pass.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("mob", mob));
            param.add(new BasicNameValuePair("vCpass", pass));
            param.add(new BasicNameValuePair("fcm_id", fcm_token));
            param.add(new BasicNameValuePair("imeino", webserviceAPI.getImie()));
            jsnStr = sh.makeServiceCall(WebserviceAPI.LOGIN_URL, ServiceHandler.GET, param);

            System.out.println("jsnStr :- " + jsnStr);
            System.out.println("Imei :- " + webserviceAPI.getImie());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (jsnStr != null) {

                if (jsnStr.equals("3")) {
                    Toast.makeText(getApplicationContext(), "Please login with correct credentials", Toast.LENGTH_SHORT).show();
                } else {
                    String[] difNum = jsnStr.split("\\*");

                    if (difNum[0].equals("2")) {
                        System.out.println("id:- " + difNum[1]);
                        mSharedPrefClass.setUserId(difNum[1]);
                        Toast.makeText(getApplicationContext(), "Login done successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else if (difNum[0].equals("1")) {

                        Toast.makeText(getApplicationContext(), "User exist but you are not active ", Toast.LENGTH_SHORT).show();
                    } else if (difNum[0].equals("4")) {
                        Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                        et_pass.setText("");
                    } else if (difNum[0].equals("0")) {
                        Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
