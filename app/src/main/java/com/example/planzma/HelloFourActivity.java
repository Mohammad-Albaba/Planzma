package com.example.planzma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelloFourActivity extends AppCompatActivity {
ImageView imgskip;
TextView textskip;
ImageView imghellofour;
Button btnGoToTheSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_four);
        imgskip=findViewById(R.id.imgskip);
        textskip=findViewById(R.id.textskip);
        imghellofour=findViewById(R.id.imghellofour);
        btnGoToTheSignUp=findViewById(R.id.btnGoToTheSignUp);
        btnGoToTheSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),Register.class));
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
