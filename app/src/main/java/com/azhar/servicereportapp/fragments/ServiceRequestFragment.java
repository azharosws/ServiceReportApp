package com.azhar.servicereportapp.fragments;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.azhar.servicereportapp.R;
import com.azhar.servicereportapp.networkUtil.ServiceHandler;
import com.azhar.servicereportapp.networkUtil.SharedPrefClass;
import com.azhar.servicereportapp.networkUtil.WebserviceAPI;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ServiceRequestFragment extends android.app.Fragment {

    ImageView iv_product;
    TextView tv_product_name;
    EditText et_issue_description;
    Button btn_submit;
    View rootView = null;
    private SharedPrefClass mSharedPrefClass = null;
    String pid = null;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_c_service_request, container, false);
        init();
        progressDialog = new ProgressDialog(getActivity());
        mSharedPrefClass = new SharedPrefClass(getActivity());

        pid = getArguments().getString("pid");
        String img_url = getArguments().getString("img_url");
        String pname = getArguments().getString("pname");

        tv_product_name.setText(pname);
        tv_product_name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fax-sans.beta.otf"));
        Picasso.with(getActivity()).load(WebserviceAPI.IMGPRODUCT_URL + img_url).into(iv_product);

        et_issue_description.setTextColor(getResources().getColor(R.color.text_color));
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_issue_description.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please enter some data", Toast.LENGTH_SHORT).show();
                } else {

                    new ProductListAsynctask().execute();
                }
            }
        });
        return rootView;
    }

    class ProductListAsynctask extends AsyncTask<String, String, String> {
        String jsnStr = null;
        String des = et_issue_description.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("loading..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            ServiceHandler serviceHandler = new ServiceHandler();

            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("cid", mSharedPrefClass.getUserId()));
            param.add(new BasicNameValuePair("pid", pid));
            param.add(new BasicNameValuePair("descrp", des));
            jsnStr = serviceHandler.makeServiceCall(WebserviceAPI.MAIN_URL + "/vactech/sr/api/complaint.php", ServiceHandler.GET, param);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (jsnStr != null) {

                String[] difNum = jsnStr.split("\\*");
                if (difNum[0].equals("3")) {
                    System.out.println("id:- " + difNum[1]);
                    Toast.makeText(getActivity(), "Complaint sent successfully", Toast.LENGTH_SHORT).show();

                } else if (difNum[0].equals("6")) {
                    System.out.println("id:- " + difNum[1]);
                    Toast.makeText(getActivity(), "Complaint not registered ", Toast.LENGTH_SHORT).show();
                }
            }
            et_issue_description.setText("");
        }
    }

    public void init() {
        tv_product_name = (TextView) rootView.findViewById(R.id.tv_product_name);
        et_issue_description = (EditText) rootView.findViewById(R.id.et_issue_description);
        iv_product = (ImageView) rootView.findViewById(R.id.iv_product);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
    }
}
