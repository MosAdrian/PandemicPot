package com.example.zacusca;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;


public class ActivityRetetaDetaliata extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private ImageView mImage;
    private TextView mNume, mDescriere;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reteta_detaliata);
        mToolbar = findViewById(R.id.toolbar);
        mImage = findViewById(R.id.imagine_reteta);
        mNume = findViewById(R.id.nume);
        mDescriere = findViewById(R.id.descriere);

        Intent intent = getIntent();
        String nume = intent.getStringExtra("nume");
        String descriere = intent.getStringExtra("descriere");
        String imagine = intent.getStringExtra("imagine");

        if (intent != null) {

            mActionBar.setTitle(nume);
            mNume.setText(nume);
            mDescriere.setText(descriere);

        }
    }
}
