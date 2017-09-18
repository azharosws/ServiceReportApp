package com.azhar.servicereportapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class RegisterActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private EditText et_name, et_mobile, et_org, et_pass;
    private Button btn_login;
    private SharedPrefClass mSharedPrefClass = null;
    String fcm_token = null;
    public static String TAG = "SERVICE REPORT APP";

    WebserviceAPI webserviceAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mSharedPrefClass = new SharedPrefClass(getApplicationContext());
        init();
        webserviceAPI = new WebserviceAPI(getApplicationContext());
        if (CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
        {


        } else {
            Snackbar.make(coordinatorLayout, "No Internet Connection", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Please insert correct name", Toast.LENGTH_SHORT).show();
                } else if (et_org.getText().toString().length() < 3) {
                    Toast.makeText(getApplicationContext(), "Please insert the organisation name with less than 4 aphabets", Toast.LENGTH_SHORT).show();
                } else if (et_mobile.getText().toString().length() != 10) {
                    Toast.makeText(getApplicationContext(), "Please insert your mobile number with 10 digits only", Toast.LENGTH_SHORT).show();
                } else if (et_pass.getText().toString().length() != 4) {
                    Toast.makeText(getApplicationContext(), "Please insert the password with 4 characters only", Toast.LENGTH_SHORT).show();
                } else {
                    fcm_token = FirebaseInstanceId.getInstance().getToken();
                    Log.d(TAG, "Token: " + fcm_token);
                    new registratAsynctask().execute();
                }
            }
        });
    }

    public void init() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        et_name = (EditText) findViewById(R.id.et_name);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_org = (EditText) findViewById(R.id.et_org);
        et_pass = (EditText) findViewById(R.id.et_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
    }

    class registratAsynctask extends AsyncTask<String, String, String> {
        String jsnStr = null;
        String name = et_name.getText().toString();
        String org = et_org.getText().toString();
        String mobile = et_mobile.getText().toString();
        String pass = et_pass.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("name", name));
            param.add(new BasicNameValuePair("mob", mobile));
            param.add(new BasicNameValuePair("org", org));
            param.add(new BasicNameValuePair("vCpass", pass));
            param.add(new BasicNameValuePair("fcm_id", fcm_token));
            param.add(new BasicNameValuePair("imei_no", webserviceAPI.getImie()));
            jsnStr = sh.makeServiceCall(WebserviceAPI.REG_URL, ServiceHandler.GET, param);

            System.out.println("jsnStr :- " + jsnStr);
            System.out.println("Imei :- " + webserviceAPI.getImie());
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (jsnStr != null) {

                String[] difNum = jsnStr.split("\\*");
                if (difNum[0].equals("2")) {
                    System.out.println("id:- " + difNum[1]);
                    mSharedPrefClass.setUserId(difNum[1]);
                    Toast.makeText(getApplicationContext(), "You are already registered. Please do login or register with account.", Toast.LENGTH_SHORT).show();

                } else if (difNum[0].equals("1")) {
                    Toast.makeText(getApplicationContext(), "Registration done successfully but you are not active ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    System.out.println("id:- " + difNum[1]);
                    mSharedPrefClass.setUserId(difNum[1]);
                    Toast.makeText(getApplicationContext(), "Registration Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        }
    }
}
