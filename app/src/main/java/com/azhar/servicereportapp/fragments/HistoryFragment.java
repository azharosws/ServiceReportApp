package com.azhar.servicereportapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.azhar.servicereportapp.R;
import com.azhar.servicereportapp.networkUtil.ServiceHandler;
import com.azhar.servicereportapp.networkUtil.SharedPrefClass;
import com.azhar.servicereportapp.networkUtil.WebserviceAPI;
import com.azhar.servicereportapp.pojo.HistoryItem;
import com.azhar.servicereportapp.pojo.ProductsItem;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;

public class HistoryFragment extends android.app.Fragment {

    RecyclerView rv_product_list;
    View rootView = null;
    Call<ProductsItem> call;
    String id = null;
    private SharedPrefClass mSharedPrefClass = null;
    private HistoryListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    List<HistoryItem> list;
    HistoryItem historyItem;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_c_product_list, container, false);
        init();
        progressDialog = new ProgressDialog(getActivity());
        mSharedPrefClass = new SharedPrefClass(getActivity());
        id = mSharedPrefClass.getUserId();
        list = new ArrayList<>();
        new ProductListAsynctask().execute();
        return rootView;
    }

    public void init() {
        rv_product_list = (RecyclerView) rootView.findViewById(R.id.rv_product_list);
    }

    class ProductListAsynctask extends AsyncTask<String, String, String> {


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
            param.add(new BasicNameValuePair("cid", id));
            String jsnStr = serviceHandler.makeServiceCall(WebserviceAPI.HISTORY_URL, ServiceHandler.GET, param);
            System.out.println("jsnStr :- " + jsnStr);
            try {
                JSONArray jsonArray = new JSONArray(jsnStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("nPCompId");
                    String name = jsonObject.getString("vPName");
                    String descp = jsonObject.getString("tCDescrp");
                    String image = jsonObject.getString("tCImage");
                    String time = jsonObject.getString("ttCompTimeStamp");
                    historyItem = new HistoryItem(id, name, descp, image, time);
                    list.add(historyItem);
                }

                adapter = new HistoryListAdapter(getActivity(), list);
                layoutManager = new LinearLayoutManager(getActivity());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            rv_product_list.setLayoutManager(layoutManager);
            rv_product_list.setAdapter(adapter);
        }
    }

    class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
        Context context;
        View itemView;
        List<HistoryItem> list;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_product_name, tv_c_time, tv_c_desp, tv_pr_id;
            public ImageView iv_product;

            public ViewHolder(View itemView) {
                super(itemView);
                iv_product = (ImageView) itemView.findViewById(R.id.iv_product);
                tv_c_time = (TextView) itemView.findViewById(R.id.tv_c_time);
                tv_c_desp = (TextView) itemView.findViewById(R.id.tv_c_desp);
                tv_pr_id = (TextView) itemView.findViewById(R.id.tv_pr_id);
                tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            }
        }

        public HistoryListAdapter(Context context, List<HistoryItem> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public HistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_item, parent, false);
            ViewHolder vh = new ViewHolder(itemView);
            return vh;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.tv_product_name.setText(list.get(position).getvPName());
            holder.tv_c_desp.setText(list.get(position).gettCDescrp());

            holder.tv_pr_id.setText(list.get(position).getnPCompId());

            String str = list.get(position).getTtCompTimeStamp();
            String[] splited = str.split("\\s+");

            System.out.println("" + splited[0]);
            System.out.println("" + splited[1]);

            holder.tv_product_name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fax-sans.beta.otf"));
            holder.tv_c_desp.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fax-sans.beta.otf"));
            holder.tv_c_time.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fax-sans.beta.otf"));

            StringTokenizer tk = new StringTokenizer(str);
            String date = tk.nextToken();
            String time = tk.nextToken();

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
            Date dt;
            try {
                dt = sdf.parse(time);
                holder.tv_c_time.setText(splited[0] + " " + sdfs.format(dt));
                System.out.println("Time Display: " + sdfs.format(dt)); // <-- I got result here
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Picasso.with(context).load(WebserviceAPI.IMGCPRODUCT_URL + list.get(position).gettCImage()).error(R.drawable.loading).into(holder.iv_product);


        }

        @Override
        public int getItemCount() {

            int size = 0;
            try {
                size = list.size();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return size;
        }
    }
}

