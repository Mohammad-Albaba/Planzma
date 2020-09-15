package com.example.planzma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddPostActivity extends AppCompatActivity {
    ProgressDialog dialog;
    TextView ChosePost,nameAddPost;
    EditText txtAddPost;
    ImageView imageProfileAddPost,ImageAddPost;
    Button btnAddPost;
    Bitmap bitmap;
    File imageUri;
    private File compressedImage;

    private StorageReference mStorageRef;
    private StorageTask uploadTask;
    private FirebaseStorage storage;
    FirebaseAuth auth;
    String imageProfile, nameProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Toolbar toolbar = findViewById(R.id.toolbarAddPost);
        storage = FirebaseStorage.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        ChosePost =  findViewById(R.id.ChosePost);
        ImageAddPost = findViewById(R.id.ImageAddPost);
        nameAddPost = findViewById(R.id.nameAddPost);
        txtAddPost = findViewById(R.id.txtAddPost);
        imageProfileAddPost = findViewById(R.id.imageProfileAddPost);
        dialog = new ProgressDialog(this);
        btnAddPost = findViewById(R.id.btnAddPost);
        ChosePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,1);
            }
        });
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidAll()) {

                } else {
                    loadPosts();
                }

            }
        });

        upLoadUser();

    }
    @Override
    public void onActivityResult(int res, int resultCode,  Intent data) {
        super.onActivityResult(res,resultCode, data);
        if (data != null && res == 1){

            try {

                imageUri = FileUtil.from(this, data.getData());
                ImageAddPost.setImageBitmap(BitmapFactory.decodeFile(imageUri.getAbsolutePath()));
                compressImage(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void compressImage(File file) {
        if (imageUri == null) {

        } else {
            Compressor.getDefault(this)
                    .compressToFileAsObservable(file)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            compressedImage = file;
                            setCompressedImage();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
                    }
        }

    public void customCompressImage(View view) {
        if (imageUri == null) {

        } else {
            // Compress image in main thread using custom Compressor
            compressedImage = new Compressor.Builder(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .build()
                    .compressToFile(imageUri);
            setCompressedImage();

        }
        }

    private void setCompressedImage() {
        ImageAddPost.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));
    }

    public String getFileExtintios(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));

    }

    private void loadPosts() {
        dialog.setMessage("UpLoad Post...");
        dialog.show();
        if (imageUri != null){
            final StorageReference fileRefernace = mStorageRef.child("ImagePosts/"+ UUID.randomUUID().toString()+getFileExtintios(Uri.fromFile(compressedImage)));
            uploadTask = fileRefernace.putFile(Uri.fromFile(compressedImage));
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRefernace.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri imageuri = task.getResult();
                        String uri = imageuri.toString();
                        long timeInMillis = System.currentTimeMillis();
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                        String dateNow = dateFormat.format(new Date());
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");
                        Map<String,Object> map = new HashMap<>();
                        map.put("imageUriPost",uri);
                        map.put("txtPost",txtAddPost.getText().toString());
                        map.put("postId",timestamp);
                        map.put("dateTime",timeInMillis);
                        map.put("Uid",auth.getUid());
                        map.put("imageProfile",imageProfile);
                        map.put("nameProfile",nameProfile);
                        reference.child(timestamp).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(AddPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else {
                        Toast.makeText(AddPostActivity.this, "Field", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }


                }
            });

        }else {
            dialog.setMessage("UpLoad Post...");
            dialog.show();
            String timestamp = String.valueOf(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY HH:mm aa");
            String dateNow = dateFormat.format(new Date());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Post");
            Map<String,Object> map = new HashMap<>();
            map.put("txtPost",txtAddPost.getText().toString());
            map.put("postId",timestamp);
            map.put("dateTime",dateNow);
            map.put("Uid",auth.getUid());
            map.put("imageUriPost","null");
            map.put("imageProfile",imageProfile);
            map.put("nameProfile",nameProfile);
            reference.child(timestamp).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(AddPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public boolean ValidAll(){
        return IsEmpty(txtAddPost,"Required Text Post");
    }

    public boolean IsEmpty(EditText editText,String msg){
        boolean isDone = true;
        if (editText!=null){
            if (editText.getText().toString().isEmpty()){
                editText.setError(msg);
                isDone = false;
            }
        }
        return isDone;

    }

    public void upLoadUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("ImageProfileUri").getValue()+"").placeholder(R.drawable.loadgif).into(imageProfileAddPost);
                nameAddPost.setText(dataSnapshot.child("name").getValue()+"");
                nameProfile = dataSnapshot.child("name").getValue()+"";
                imageProfile = dataSnapshot.child("ImageProfileUri").getValue()+"";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
