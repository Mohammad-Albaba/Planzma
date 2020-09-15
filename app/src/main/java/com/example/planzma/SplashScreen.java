package com.example.planzma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity{

    private static final int HANDLER = 500;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mAuth = FirebaseAuth.getInstance();

        boolean handler = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnectio()){
                     isLogin();
                }else {
                    isLogin();
                    Toast.makeText(SplashScreen.this, "يرجى الاتصال بالانترنت ", Toast.LENGTH_SHORT).show();
                }

            }
        },HANDLER);
    }

    public void isLogin(){
        FirebaseUser user = mAuth.getCurrentUser();
        if ( user != null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }else {
            startActivity(new Intent(getApplicationContext(),HelloOneActivity.class));
            finish();
        }
    }

    public boolean isNetworkConnectio(){
        ConnectivityManager cm;
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
