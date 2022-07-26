package com.example.zacusca;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActivitySettings extends AppCompatActivity {
    private Button SaveInfoBtn;
    private EditText ip;
    private EditText port;
    private EditText username;
    private EditText parola;
    public static final String url = "jdbc:mysql://";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        SaveInfoBtn = findViewById(R.id.save_button);
        ip = findViewById(R.id.ip);
        port = findViewById(R.id.port);
        username = findViewById(R.id.username);
        parola = findViewById(R.id.parola);


        SaveInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_func();
            }
        });


    }
    private void save_func(){
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("ip", ip.getText().toString());
        myEdit.putString("port", port.getText().toString());
        myEdit.putString("username", username.getText().toString());
        myEdit.putString("parola", parola.getText().toString());


        myEdit.apply();

        //Toast.makeText(this,"Salvare setari", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        String ip2 =sh.getString("ip","");
        String port2 =sh.getString("port","");
        String username2 =sh.getString("username","");
        String parola2 =sh.getString("parola","");

        ip.setText(ip2);
        port.setText(port2);
        username.setText(username2);
        parola.setText(parola2);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}