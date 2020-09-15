package com.example.planzma;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import static com.github.marlonlom.utilities.timeago.TimeAgo.from;

public class AdapterRecyclerHome extends RecyclerView.Adapter<AdapterRecyclerHome.ViewHolder> {
    DatabaseReference refLike;
    private Context context;
    private List<User> list;
    FirebaseAuth auth ;
    FirebaseUser myId ;
    String myUid;

    long timeInMillis;


    boolean isLike = false;

    public AdapterRecyclerHome() {
    }

    public AdapterRecyclerHome(Context context, List<User> list) {
        this.context = context;
        this.list = list;
        refLike = FirebaseDatabase.getInstance().getReference("Likes");
    }

    @NonNull
    @Override
    public AdapterRecyclerHome.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_home,parent,false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final User user = list.get(position);
        holder.nameItem.setText(user.getName());



        CharSequence timeAgo =
                DateUtils.getRelativeTimeSpanString(
                        Long.parseLong(String.valueOf(System.currentTimeMillis())),
                        System.currentTimeMillis(),
                        DateUtils.SECOND_IN_MILLIS);

      

        holder.dateItem.setText(timeAgo);
        holder.textPostItem.setText(user.getTxtPost());
        DatabaseReference reference  =FirebaseDatabase.getInstance().getReference("Likes").child(list.get(position).getPostId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    holder.pLikeCount.setText(dataSnapshot.getChildrenCount()+" Likes");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        auth = FirebaseAuth.getInstance();
        myId = auth.getCurrentUser();
        if (myId != null){
            myUid = myId.getUid();

        }
        try {
            if (user.getImagePost().equals("null")){
                holder.imagePostItem.setVisibility(View.GONE);
            }else {
                Picasso.get().load(user.getImagePost()).placeholder(R.drawable.loadgif).into(holder.imagePostItem);
            }


            Picasso.get().load(user.getImageProfile()).placeholder(R.drawable.loadgif).into(holder.imageProfileItem);


        }catch (Exception e){
            Picasso.get().load(R.mipmap.ic_launcher).placeholder(R.drawable.loadgif).into(holder.imagePostItem);
            Picasso.get().load(R.mipmap.ic_launcher).placeholder(R.drawable.loadgif).into(holder.imageProfileItem);

        }

        holder.sharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePosts(context," UriImage"+user.getImagePost()+" textPost"+user.getTxtPost());
            }
        });
        final String pId = list.get(position).getPostId();
        setLike(holder,pId);
        holder.btnLikePosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLike = true;
                final DatabaseReference referenceLike = FirebaseDatabase.getInstance().getReference().child("Likes");
                referenceLike.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (isLike){
                            if (dataSnapshot.child(pId).hasChild(myUid)){
                                referenceLike.child(pId).child(myUid).removeValue();
                                isLike = false;

                            }else {
                                referenceLike.child(pId).child(myUid).setValue("Liked");
                                isLike = false;

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    private void sharePosts(Context context, String uri) {
        Intent sharePost = new Intent(Intent.ACTION_SEND);
        sharePost.setType("text/plain");
        sharePost.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharePost.putExtra(Intent.EXTRA_SUBJECT,"Planzma");
        sharePost.putExtra(Intent.EXTRA_TEXT,uri);
        context.startActivity(Intent.createChooser(sharePost,"Planzma"));
    }

    private void setLike(final ViewHolder holder, final String postId) {
        refLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).hasChild(myUid)){
                    holder.btnLikePosts.setTextColor(context.getResources().getColor(R.color.colorwhite1));
                    holder.btnLikePosts.setBackground(context.getResources().getDrawable(R.drawable.is_liked));
                    holder.btnLikePosts.setText("Liked");


                }else {
                    holder.btnLikePosts.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                    holder.btnLikePosts.setBackground(context.getResources().getDrawable(R.drawable.is_like));
                    holder.btnLikePosts.setText("Like");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfileItem,imagePostItem;
        TextView nameItem, dateItem, textPostItem,pLikeCount;
        Button btnLikePosts,sharePost;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPostItem = itemView.findViewById(R.id.textPostItem);
            dateItem = itemView.findViewById(R.id.dateItem);
            nameItem = itemView.findViewById(R.id.nameItem);
            imageProfileItem = itemView.findViewById(R.id.imageProfileItem);
            imagePostItem = itemView.findViewById(R.id.imagePostItem);
            btnLikePosts = itemView.findViewById(R.id.btnLikePosts);
            pLikeCount = itemView.findViewById(R.id.pLikeCount);
            sharePost = itemView.findViewById(R.id.sharePost);

        }
    }
}
