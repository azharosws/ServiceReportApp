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
import com.azhar.servicereportapp.pojo.ProductsItem;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ProductListFragment extends android.app.Fragment {

    RecyclerView rv_product_list;
    View rootView = null;
    Call<ProductsItem> call;
    String id = null;
    private SharedPrefClass mSharedPrefClass = null;
    private ProductListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    List<ProductsItem> list;
    ProductsItem productsItem;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_c_product_list, container, false);
        init();
        progressDialog=new ProgressDialog(getActivity());
        mSharedPrefClass = new SharedPrefClass(getActivity());
        id = mSharedPrefClass.getUserId();
        list = new ArrayList<>();
//        product_list("1");
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
            String jsnStr = serviceHandler.makeServiceCall(WebserviceAPI.MAIN_URL + "/vactech/sr/api/products.php", ServiceHandler.GET, param);
            System.out.println("jsnStr :- " + jsnStr);
            try {
                JSONArray jsonArray = new JSONArray(jsnStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("nPPid");
                    String name = jsonObject.getString("vPName");
                    String catid = jsonObject.getString("nFCatId");
                    String descp = jsonObject.getString("tPDescrp");
                    String image = jsonObject.getString("tPImage");
                    productsItem = new ProductsItem(id, name, catid, descp, image, null);
                    list.add(productsItem);
                }

                adapter = new ProductListAdapter(getActivity(), list);
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

    class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
        Context context;
        View itemView;
        List<ProductsItem> list;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_product_name, tv_model_no, tv_pr_id;
            public ImageView iv_product;

            public ViewHolder(View itemView) {
                super(itemView);
                iv_product = (ImageView) itemView.findViewById(R.id.iv_product);
                tv_pr_id = (TextView) itemView.findViewById(R.id.tv_pr_id);
                tv_product_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            }
        }

        public ProductListAdapter(Context context, List<ProductsItem> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public ProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_item, parent, false);
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

            holder.tv_product_name.setText(list.get(position).getNPName());
            holder.tv_pr_id.setText(list.get(position).getNPPid());

            holder.tv_product_name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fax-sans.beta.otf"));

            Picasso.with(context).load(WebserviceAPI.IMGPRODUCT_URL + list.get(position).getTPImage()).error(R.drawable.loading).into(holder.iv_product);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pname", list.get(position).getNPName());
                    bundle.putString("pid", list.get(position).getNPPid());
                    bundle.putString("img_url", list.get(position).getTPImage());
                    ServiceRequestFragment fragment = new ServiceRequestFragment();
                    fragment.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.frame_container, fragment)
                            .commit();
                }
            });

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

