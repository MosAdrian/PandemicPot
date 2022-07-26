package com.example.zacusca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class ActivityRetetaDetaliata extends AppCompatActivity {

    private TextView mNume, mDescriere, mIngrediente;
    private ImageView mImageView;
    private Button mStartbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reteta_detaliata);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(false);
            }
        });
        mNume = findViewById(R.id.nume);
        mDescriere = findViewById(R.id.descriereDetaliata);
        mIngrediente = findViewById(R.id.ingredinte_reteta);
        mImageView = findViewById(R.id.imagine_reteta);
        mStartbtn = findViewById(R.id.startbtn);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String ip2 =sh.getString("ip","");
        String port2 = sh.getString("port","");
            String url = "http://"+ip2+":"+port2+"/MainPage/retete/button2.php";

        Intent intent = getIntent();
        String descriere = intent.getStringExtra("descriereDetaliata");
        String final_ingrediente = intent.getStringExtra("final_ingrediente");
        String nume = intent.getStringExtra("nume");
        mNume.setText(nume);
        mDescriere.setText(descriere);
        mIngrediente.setText(final_ingrediente);
        String poza = intent.getStringExtra("poza");
        String id = intent.getStringExtra("id");


        new DownloadImageFromInternetImpl((ImageView) mImageView).execute(poza);
        mStartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(ActivityRetetaDetaliata.this, "Reteta a fost aleasa cu succes", Toast.LENGTH_LONG).show();

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ActivityRetetaDetaliata.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<String,String>();
                        params.put("id", id);

                        return params;

                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(ActivityRetetaDetaliata.this);
                requestQueue.add(request);


            }
        });




    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
