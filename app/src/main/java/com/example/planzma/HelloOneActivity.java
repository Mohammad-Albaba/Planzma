package com.example.planzma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelloOneActivity extends AppCompatActivity {
ImageView imgskip;
TextView textskip;
ImageView imghelloone;
ImageView imgadd;
TextView textadd;
TextView texthello;
Button btnnexthelloone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_one);
        imgskip=findViewById(R.id.imgskip);
        textskip=findViewById(R.id.textskip);
        imghelloone=findViewById(R.id.imghelloone);
        imgadd=findViewById(R.id.imgadd);
        textadd=findViewById(R.id.textadd);
        texthello=findViewById(R.id.texthello);
        btnnexthelloone=findViewById(R.id.btntexthelloone);
        btnnexthelloone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),HelloTwoActivity.class));
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
