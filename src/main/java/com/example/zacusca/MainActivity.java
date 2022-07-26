package com.example.zacusca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {






    public String BASE_URL = "";
    public String BASE_URL_DETAILS = "";
    public List<Reteta> retete;
    public List<RetetaDetaliataProdus> reteteDetaliate;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recyclerView);
        manager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(manager);
        retete = new ArrayList<>();
        reteteDetaliate = new ArrayList<>();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String ip2 =sh.getString("ip","");
        String port2 = sh.getString("port","");
        BASE_URL = "http://"+ip2+":"+port2+"/MainPage/Android/getProduct.php";
        String final_part_ip = getIntent().getStringExtra("final_part_ip");
        BASE_URL_DETAILS = "http://"+ip2+":"+port2+"/MainPage/Android/getProductDetails.php?id_reteta="+final_part_ip;
        getProducts();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "Setari selectat", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ActivitySettings.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getProducts(){
        String listaDescriereFinala[] = {""};

        /*
        StringRequest stringRequestDetali = new StringRequest(Request.Method.GET, BASE_URL_DETAILS,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);

                                String descriere = object.getString("descriere");
                                String poza = object.getString("link");
                                int nr_pas = object.getInt("nr_pas");

                                RetetaDetaliataProdus retetaDetaliataProdus = new RetetaDetaliataProdus(descriere, poza, nr_pas);
                                reteteDetaliate.add(retetaDetaliataProdus);


                            }



                        }catch (Exception e){

                        }
                        mAdapter = new RecyclerAdapter(MainActivity.this, retete);
                        recyclerView.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }


        });*/


        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try{
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i<array.length(); i++){

                        JSONObject object = array.getJSONObject(i);

                        String nume = object.getString("Nume");
                        String descriere = object.getString("Descriere");
                        String poza = object.getString("poza");
                        String id = object.getString("id");

                        Reteta reteta = new Reteta(nume, descriere, poza, id);
                        retete.add(reteta);

                    }

                }catch (Exception e){

                }
                mAdapter = new RecyclerAdapter(MainActivity.this, retete);
                recyclerView.setAdapter(mAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }

        });








        Volley.newRequestQueue(MainActivity.this).add(stringRequest);

    }

}
