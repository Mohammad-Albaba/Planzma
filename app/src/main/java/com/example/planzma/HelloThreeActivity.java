package com.example.planzma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelloThreeActivity extends AppCompatActivity {
ImageView imgskip;
TextView  textskip;
ImageView imghellothree;
Button btn;
TextView textinvitation;
TextView Ask;
Button btngoithellothree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_three);
        imgskip=findViewById(R.id.imgskip);
        textskip=findViewById(R.id.textskip);
        imghellothree=findViewById(R.id.imghellothree);
        btn=findViewById(R.id.btn);
        textinvitation=findViewById(R.id.textinvitations);
        Ask=findViewById(R.id.Ask);
        btngoithellothree=findViewById(R.id.btngoithellothree);
        btngoithellothree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),HelloFourActivity.class));
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
