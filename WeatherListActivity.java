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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unik.firebaseweather.Model.CityListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherListAcitivy extends AppCompatActivity {
    Context context;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rec_city)
    RecyclerView rec_city;
    @BindView(R.id.tx_add)
    TextView tx_add;
    ArrayList<CityListModel> arrProject = new ArrayList<>();
    CityListAdapter projectListAdapter;
    FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;


    @Override
    protected void onStart() {
        super.onStart();
        db = FirebaseFirestore.getInstance();

        db.collection("City")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    arrProject.clear();
                    for (QueryDocumentSnapshot documents : task.getResult()) {

//                        Log.d("hello", String.valueOf(task.getResult()));
                        Log.d("hello", documents.getId() + " => " + documents.getData());
                        String p_name = documents.getData().get("city_name").toString();

                        Log.d("names", p_name);

                        arrProject.add(new CityListModel(p_name));
                    }

                    projectListAdapter = new CityListAdapter(context, arrProject);
                    rec_city.setAdapter(projectListAdapter);

                } else {
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_list_acitivy);
        ButterKnife.bind(this);

        context = WeatherListAcitivy.this;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("");
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rec_city.setLayoutManager(layoutManager);
        rec_city.setHasFixedSize(true);
        rec_city.setNestedScrollingEnabled(false);


        rec_city.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(context, ForecastActivity.class);
                intent.putExtra("city_name", arrProject.get(position).getCity_name());
                startActivity(intent);
            }
        }));

        tx_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddCityActivity.class));
            }
        });



    }

    public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
        private Context context;
        private List<CityListModel> rowbrand;

        public CityListAdapter(Context context, List<CityListModel> rowbrand) {
            this.context = context;
            this.rowbrand = rowbrand;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_title)
            TextView tx_title;


            public ViewHolder(final View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_city_list, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.tx_title.setText(rowbrand.get(position).getCity_name());

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
