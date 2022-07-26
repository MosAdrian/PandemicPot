package com.example.zacusca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.Timer;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Reteta> retete = new ArrayList<>();
    private List<RetetaDetaliataProdus> retetaDetaliataProduses = new ArrayList<>();
    public String BASE_URL_DETAILS = "";
    public String BASE_URL_INGREDIENTS = "";
    public String final_descriere = "";
    public String final_ingrediente = "";
    public String Descriere = "";
    public String Ingrediente = "";
    long k=1,j=0;






    public RecyclerAdapter (Context context,List<Reteta> retete){
        this.mContext = context;
        this.retete = retete;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mNume, mDescriere;
        private LinearLayout mContainer;
        private ImageView mImagine;
        public MyViewHolder (View view){
            super(view);

            mNume = view.findViewById(R.id.nume);
            mDescriere = view.findViewById(R.id.descriere);
            mContainer = view.findViewById(R.id.product_container);
            mImagine = view.findViewById(R.id.imagine_reteta);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.retete_item_layout,parent,false);
        return new MyViewHolder(view);

    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SharedPreferences sh = mContext.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String ip2 =sh.getString("ip","");
        String port2 = sh.getString("port","");

        final Reteta reteta = retete.get(position);
        //final RetetaDetaliataProdus retetaDetaliataProdus = retetaDetaliataProduses.get(position);
        String link = "http://"+ip2+":"+port2+"/MainPage/Poze/"+reteta.getPoza();

        holder.mDescriere.setText(reteta.getDescriere());
        holder.mNume.setText(reteta.getNume());
        new DownloadImageFromInternetImpl((ImageView) holder.mImagine).execute(link);




        Intent intent = new Intent(mContext, ActivityRetetaDetaliata.class);


        //  Prezentarea produsului detaliat
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long when_button_clicked = System.currentTimeMillis();
                BASE_URL_DETAILS = "http://"+ip2+":"+port2+"/MainPage/Android/getProductDetails.php?id_reteta="+reteta.getId();
                BASE_URL_INGREDIENTS = "http://"+ip2+":"+port2+"/MainPage/Android/getProductIngredients.php?id_reteta="+reteta.getId();
                String link = "http://"+ip2+":"+port2+"/MainPage/Poze/"+reteta.getPoza();
                String id = reteta.getId();
                String nume = reteta.getNume();
                intent.putExtra("poza", link);
                intent.putExtra("id", id);
                intent.putExtra("nume", nume);



                StringRequest stringRequestDetali = new StringRequest(Request.Method.GET, BASE_URL_DETAILS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONArray array = new JSONArray(response);
                                    for (int i = 0; i<array.length(); i++){
                                        int j=i+1;
                                        JSONObject object = array.getJSONObject(i);
                                        String descriere = object.getString("descriere");
                                        String poza = object.getString("link");
                                        int nr_pas = object.getInt("nr_pas");
                                        final_descriere +="Pasul" + j + ":" + descriere + "\n\n";
                                    }
                                }catch (Exception e){
                                }
                                //intent.putExtra("descriereDetaliata", final_descriere);
                                Descriere = final_descriere;
                                final_descriere = "";
                                intent.putExtra("descriereDetaliata", Descriere);



                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }

                });


                StringRequest stringRequestIngrediente = new StringRequest(Request.Method.GET, BASE_URL_INGREDIENTS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONArray array = new JSONArray(response);
                                    for (int i = 0; i<array.length(); i++){
                                        JSONObject object = array.getJSONObject(i);
                                        String cantitate = object.getString("cantitate");
                                        String nume = object.getString("nume");
                                        String descriere = object.getString("descriere");
                                        final_ingrediente += cantitate + " " + nume + " " + descriere+"\n";
                                    }
                                }catch (Exception e){

                                }
                                //intent.putExtra("final_ingrediente", final_ingrediente);
                                Ingrediente = final_ingrediente;
                                final_ingrediente = "";
                                intent.putExtra("final_ingrediente", Ingrediente);

                                //mContext.startActivity(intent);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }




                });
                Volley.newRequestQueue(mContext).add(stringRequestIngrediente);
                Volley.newRequestQueue(mContext).add(stringRequestDetali);

                if(k == j){
                    mContext.startActivity(intent);
                    k=1;
                    j=0;
                }
                else
                    j=k;
                Timer timer = new Timer();

                new CountDownTimer(500, 100){
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        k=1;
                        j=0;
                    }
                }.start();



            }
        });






    }



    @Override
    public int getItemCount() {
        return retete.size();
    }
}
