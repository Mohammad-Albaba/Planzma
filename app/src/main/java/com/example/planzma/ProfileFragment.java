package com.example.planzma;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProfileFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    ImageView imageUserProfile;
    TextView emailProfile, NameProfile, phoneNumberProfile;
    RecyclerView recyclerView;
    AdapterRecyclerHome home;
    List<User> list;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
         firebaseDatabase = FirebaseDatabase.getInstance();
         firebaseAuth = FirebaseAuth.getInstance();

         recyclerView = v.findViewById(R.id.recyclerProfile);
         list = new ArrayList<>();
         Collections.reverse(list);

         LinearLayoutManager manager = new LinearLayoutManager(getContext());
         recyclerView.setLayoutManager(manager);
         recyclerView.setHasFixedSize(true);

        imageUserProfile = v.findViewById(R.id.imageUserProfile);
        emailProfile = v.findViewById(R.id.emailProfile);
        NameProfile = v.findViewById(R.id.NameProfile);
        phoneNumberProfile = v.findViewById(R.id.phoneNumberProfile);
        loadDataProfile();
        loadPosts();
        return v;
    }

    private void loadDataProfile(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Picasso.get().load(dataSnapshot.child("ImageProfileUri").getValue()+"").placeholder(R.drawable.loadgif).into(imageUserProfile);
                    emailProfile.setText("Email : "+dataSnapshot.child("email").getValue()+"");
                    NameProfile.setText("Name : "+dataSnapshot.child("name").getValue()+"");
                    phoneNumberProfile.setText("Number : "+dataSnapshot.child("phone").getValue()+"");

                }catch (Exception E){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadPosts(){
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Post");
        Query query = reference1.orderByChild("Uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
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
                    home = new AdapterRecyclerHome(getContext(),list);
                    recyclerView.setAdapter(home);
                    home.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}


