package com.example.zacusca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Reteta> retete = new ArrayList<>();



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
        String link = "http://" + ip2 + "/MainPage/Poze/"+holder.mImagine;

        final Reteta reteta = retete.get(position);

        holder.mDescriere.setText(reteta.getDescriere());
        holder.mNume.setText(reteta.getNume());
        new DownloadImageFromInternetImpl((ImageView) holder.mImagine).execute(link);





        //  Prezentarea produsului detaliat
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ActivityRetetaDetaliata.class);

                intent.putExtra("title", reteta.getNume());
                intent.putExtra("price", reteta.getDescriere());

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return retete.size();
    }
}
