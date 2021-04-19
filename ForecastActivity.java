package com.unik.firebaseweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tx_city_name)
    TextView tx_city_name;
    @BindView(R.id.tx_temp)
    TextView tx_temp;
    @BindView(R.id.tx_feel)
    TextView tx_feel;
    @BindView(R.id.tx_min_temp)
    TextView tx_min_temp;
    @BindView(R.id.tx_max_temp)
    TextView tx_max_temp;
    @BindView(R.id.tx_pressure)
    TextView tx_pressure;
    @BindView(R.id.tx_humidity)
    TextView tx_humidity;
    @BindView(R.id.btn_detail)
    Button btn_detail;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    String city_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        ButterKnife.bind(this);

        context = ForecastActivity.this;
        toolbar.setNavigationIcon(R.drawable.back_white);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("");
        Intent intent=getIntent();

        city_name=intent.getStringExtra("city_name");

        attemptType(city_name);
    }
    public void attemptType(String names) {
        mRequestQueue = Volley.newRequestQueue(ForecastActivity.this);


        String urlmm = "http://api.openweathermap.org/data/2.5/weather?q="+names+"&appid=a2f9eb804f5bc707848aa6dfd35ce88a&units=metric";

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, urlmm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("voucer_response", response);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
String name=jsonObject.getString("name");
JSONObject jsonObject1=jsonObject.getJSONObject("main");
                            String temp=String.valueOf(jsonObject1.getDouble("temp"));
                            String feels_like=String.valueOf(jsonObject1.getDouble("feels_like"));
                            String temp_min=String.valueOf(jsonObject1.getDouble("temp_min"));
                            String temp_max=String.valueOf(jsonObject1.getDouble("temp_max"));
                            String pressure=String.valueOf(jsonObject1.getDouble("pressure"));
                            String humidity=String.valueOf(jsonObject1.getDouble("humidity"));



                            tx_city_name.setText(name);
                            tx_temp.setText("Temp:" +temp);
                            tx_feel.setText("Feel Like:" +feels_like);
                            tx_min_temp.setText("Min Temp:" +temp_min);
                            tx_max_temp.setText("Max Temp:" +temp_max);
                            tx_pressure.setText("Pressure:" +pressure);
                            tx_humidity.setText("Humidity:" +humidity);

                            btn_detail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, WeatherDetailActivity.class);
                                    intent.putExtra("city_name", city_name);
                                    startActivity(intent);
                                }
                            });



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ForecastActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
}
