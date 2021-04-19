package com.unik.firebaseweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.unik.firebaseweather.Model.WModel;
import com.unik.firebaseweather.Model.WeatherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetailActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rec_weather)
    RecyclerView rec_weather;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    String city_name;
    ArrayList<WeatherModel> arrVoucher = new ArrayList<>();
    ArrayList<WModel> arrw = new ArrayList<>();
    WeatherAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        ButterKnife.bind(this);

        context = WeatherDetailActivity.this;
        toolbar.setNavigationIcon(R.drawable.back_white);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rec_weather.setLayoutManager(layoutManager);
        rec_weather.setHasFixedSize(true);
        rec_weather.setNestedScrollingEnabled(false);
        Intent intent=getIntent();

        city_name=intent.getStringExtra("city_name");

        attemptType(city_name);
    }

    public void attemptType(String names) {
        mRequestQueue = Volley.newRequestQueue(WeatherDetailActivity.this);


        String urlmm = "http://api.openweathermap.org/data/2.5/forecast?q="+names+"&appid=cfcacd5a0fdfaab06f5f96140dafdb05&units=metric";

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, urlmm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("voucer_response", response);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject arrayElement_0 = jsonArray.getJSONObject(i);
                                arrayElement_0.optJSONObject("main").optString("temp");
                                //arrVoucher.add(gson.fromJson(String.valueOf(arrayElement_0), WeatherModel.class));
                                arrw.add(new WModel(arrayElement_0.optString("dt_txt"),arrayElement_0.optJSONObject("main").optString("temp")));
                            }

                            weatherAdapter=new WeatherAdapter(context,arrw);
                            rec_weather.setAdapter(weatherAdapter);






                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(WeatherDetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Accept", "application/json");
                return params;
            }
        };


        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(mStringRequest);


    }

    public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
        private Context context;
        private List<WModel> rowbrand;

        public WeatherAdapter(Context context, List<WModel> rowbrand) {
            this.context = context;
            this.rowbrand = rowbrand;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_title)
            TextView tx_title;
            @BindView(R.id.tx_temp)
            TextView tx_temp;


            public ViewHolder(final View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_weather, parent, false);
            return new WeatherAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.tx_title.setText(rowbrand.get(position).getDate());
            holder.tx_temp.setText(rowbrand.get(position).getTemperature());

        }


        @Override
        public long getItemId(int position) {
            return rowbrand.size();
        }

        @Override
        public int getItemCount() {
            return rowbrand.size();
        }
    }
}
