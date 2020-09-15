package com.example.planzma;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.example.planzma.R.raw.mouseclick;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerHome;
    AdapterRecyclerHome adapterHome;
    List<User> list;
    ProgressBar bar;
    Button btnLikePosts;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerHome = v.findViewById(R.id.recyclerHome);
        bar = v.findViewById(R.id.bar);
        final MediaPlayer mediaPlayer=MediaPlayer.create(getContext(),R.raw.mouseclick);
        btnLikePosts=v.findViewById(R.id.btnLikePosts);
//        btnLikePosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mediaPlayer.start();
//            }
//        });




        list = new ArrayList<>();
        Collections.reverse(list);
        adapterHome = new AdapterRecyclerHome(getContext(),list);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerHome.setHasFixedSize(true);
        recyclerHome.setAdapter(adapterHome);
        recyclerHome.setLayoutManager(manager);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Post");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = new User();
                    user.setName(snapshot.child("nameProfile").getValue()+"");
                    user.setImageProfile(snapshot.child("imageProfile").getValue()+"");
                    user.setImagePost(snapshot.child("imageUriPost").getValue()+"");
                    user.setDateTime(snapshot.child("dateTime").getValue()+"");
                    user.setPhone(snapshot.child("imageUriPost").getValue()+"");
                    user.setPostId(snapshot.child("postId").getValue()+"");
                    user.setUid(snapshot.child("Uid").getValue()+"");
                    user.setTxtPost(snapshot.child("txtPost").getValue()+"");
                    list.add(user);
                    Collections.reverse(list);
                    adapterHome.notifyDataSetChanged();
                    bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TextView addpost=v.findViewById(R.id.textaddpost);
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),AddPostActivity.class));
            }
        });
       FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
       FirebaseUser user = firebaseAuth.getCurrentUser();

       final ImageView imageUserProfile =v.findViewById(R.id.imgProfilePost);
        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("ERERERE", "onDataChange: "+dataSnapshot.child("ImageProfileUri").getValue()+"");

                    Picasso.get().load(dataSnapshot.child("ImageProfileUri").getValue()+"").placeholder(R.drawable.loadgif).into(imageUserProfile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }

}
