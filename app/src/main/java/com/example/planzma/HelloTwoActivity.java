package com.example.planzma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelloTwoActivity extends AppCompatActivity {
ImageView imgskip;
TextView textskip;
ImageView  imghellotwo;
Button btnjoin;
TextView textdiscover;
TextView texthellotwo;
Button btnnexthellotwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_two);
        imgskip=findViewById(R.id.imgskip);
        textskip=findViewById(R.id.textskip);
        imghellotwo=findViewById(R.id.imghellotwo);
        btnjoin=findViewById(R.id.btnjoin);
        textdiscover=findViewById(R.id.textdiscover);
        texthellotwo=findViewById(R.id.texthellotwo);
        btnnexthellotwo=findViewById(R.id.btnnexthellotwo);

        btnnexthellotwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),HelloThreeActivity.class));
            }
        });
        textskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),Register.class));
            }
        });
    }
}
