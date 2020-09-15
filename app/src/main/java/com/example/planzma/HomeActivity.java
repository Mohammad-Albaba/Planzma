package com.example.planzma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
    ViewPager pagerHome;
    RecyclerView recyclerHome;
    TabLayout tabs;
    ImageView imgProfilePost;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    ImageView imageUserProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imgProfilePost=findViewById(R.id.imgProfilePost);
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Planzma");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationView);
//
//        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//                = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment;
//                switch (item.getItemId()) {
//                    case R.id.home:
//                        getSupportActionBar().setTitle("Home");
//                        fragment = new HomeFragment();
//                        loadFragment(fragment);
//                        return true;
//                    case R.id.chat:
//                        getSupportActionBar().setTitle("Chat");
//                        fragment = new ChatFragment();
//                        loadFragment(fragment);
//                        return true;
//                    case R.id.location:
//                        getSupportActionBar().setTitle("Location");
//                        fragment = new LocationFragment();
//                        loadFragment(fragment);
//                        return true;
//                    case R.id.profile:
//                        getSupportActionBar().setTitle("Profile");
//                        fragment = new ProfileFragment();
//                        loadFragment(fragment);
//                        return true;
//                }
//                return false;
//            }
//        };
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        pagerHome = findViewById(R.id.viewPagerHome);
        tabs = findViewById(R.id.tabs);
        AdapterViewPagerHome  adapterHome = new AdapterViewPagerHome(getSupportFragmentManager());
        pagerHome.setAdapter(adapterHome);
        pagerHome.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pagerHome));
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_FIXED);


    }

//    public void btn_GoogleMap(View view) {
//        Intent google_Map=new Intent(Intent.ACTION_VIEW);
//        google_Map.setData(Uri.parse("geo:31.4418573,34.421304?z=10.6"));
//        if (google_Map.resolveActivity(getPackageManager()) != null){
//            startActivity(google_Map);
//        }
//    }

//    private void loadFragment(Fragment fragment) {
//        // load fragment
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frame_container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}
