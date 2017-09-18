package com.azhar.servicereportapp.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.azhar.servicereportapp.R;
import com.azhar.servicereportapp.networkUtil.ServiceHandler;
import com.azhar.servicereportapp.networkUtil.SharedPrefClass;
import com.azhar.servicereportapp.networkUtil.WebserviceAPI;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends android.app.Fragment {

    private EditText et_name, et_mobile, et_org, et_pass;
    private Button btn_login;
    View rootView = null;
    String id = null;
    private SharedPrefClass mSharedPrefClass = null;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile_update, container, false);
        init();
        progressDialog = new ProgressDialog(getActivity());
        mSharedPrefClass = new SharedPrefClass(getActivity());
        id = mSharedPrefClass.getUserId();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_name.getText().toString().length() < 3) {
                    Toast.makeText(getActivity(), "Please insert correct name", Toast.LENGTH_SHORT).show();
                } else if (et_org.getText().toString().length() < 3) {
                    Toast.makeText(getActivity(), "Please insert the organisation name with less than 4 aphabets", Toast.LENGTH_SHORT).show();
                } else if (et_mobile.getText().toString().length() != 10) {
                    Toast.makeText(getActivity(), "Please insert your mobile number with 10 digits only", Toast.LENGTH_SHORT).show();
                } else if (et_pass.getText().toString().length() != 4) {
                    Toast.makeText(getActivity(), "Please insert the password with 4 characters only", Toast.LENGTH_SHORT).show();
                } else {
                    new ProfileAsynctask().execute();

                }
            }
        });

        return rootView;
    }

    public void init() {
        et_name = (EditText) rootView.findViewById(R.id.et_name);
        et_mobile = (EditText) rootView.findViewById(R.id.et_mobile);
        et_org = (EditText) rootView.findViewById(R.id.et_org);
        et_pass = (EditText) rootView.findViewById(R.id.et_pass);
        btn_login = (Button) rootView.findViewById(R.id.btn_login);
    }

    class ProfileAsynctask extends AsyncTask<String, String, String> {
        String jsnStr = null;
        String name = et_name.getText().toString();
        String org = et_org.getText().toString();
        String mobile = et_mobile.getText().toString();
        String pass = et_pass.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("loading..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("cid", id));
            param.add(new BasicNameValuePair("vCname", name));
            param.add(new BasicNameValuePair("tCmob", mobile));
            param.add(new BasicNameValuePair("vCorg", org));
            param.add(new BasicNameValuePair("vCpass", pass));
            jsnStr = sh.makeServiceCall(WebserviceAPI.PROFILEUPDATE_URL, ServiceHandler.GET, param);

            System.out.println("jsnStr :- " + jsnStr);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (jsnStr != null) {

                if (jsnStr.equals("1")) {
                    Toast.makeText(getActivity(), "Profile update successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error. Please try again later", Toast.LENGTH_SHORT).show();
                }
            }
            et_name.setText("");
            et_org.setText("");
            et_mobile.setText("");
            et_pass.setText("");
        }
    }
}


