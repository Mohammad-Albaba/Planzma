package com.example.planzma;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Signup extends AppCompatActivity {
    ImageView imageProfileAddPostsignup;
    TextView ChosePostsignup;
    EditText emailSignUp;
    EditText password;
    EditText name;
    EditText phoneNumber;
    Button btnSignUp;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressDialog dialog;
    Bitmap bitmap;
    Uri imageUri;
    private StorageReference mStorageRef;
    private StorageTask uploadTask;
    private FirebaseStorage storage;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbarSignUp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SignUp");
        toolbar.setSubtitleTextColor(R.color.colorwhite1);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageProfileAddPostsignup = findViewById(R.id.imageProfileAddPostsignup);
        ChosePostsignup = findViewById(R.id.ChosePostsignup);
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        emailSignUp = findViewById(R.id.emailSignUp);
        password = findViewById(R.id.passwordSignUp);
        phoneNumber = findViewById(R.id.phoneNumber);
        name = findViewById(R.id.name);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidAll() & imageProfileAddPostsignup != null) {

                } else {
                    SignUser();
                }
            }
        });
        ChosePostsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, 1);
            }
        });


    }

    @Override
    public void onActivityResult(int res, int resultCode, Intent data) {
        super.onActivityResult(res, resultCode, data);
        if (data != null && res == 1) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageProfileAddPostsignup = findViewById(R.id.imageProfileAddPostsignup);
                imageProfileAddPostsignup.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getFileExtintios(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri));

    }

    private void SignUser() {
        dialog.setMessage("SignUp...");
        dialog.show();
        mAuth.createUserWithEmailAndPassword(emailSignUp.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(Signup.this, "Signed Successful", Toast.LENGTH_SHORT).show();
                    addUser();
                    startActivity(new Intent(getApplicationContext(), Login.class));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



    public boolean ValidAll() {
        return IsEmpty(emailSignUp, "Required")
                & IsEmpty(password, "Required")
                & IsEmpty(name, "Required")
                & IsEmpty(phoneNumber, "Required");
    }

    public boolean IsEmpty(EditText editText, String msg) {
        boolean isDone = true;
        if (editText != null) {
            if (editText.getText().toString().isEmpty()) {
                editText.setError(msg);
                isDone = false;
            }
        }
        return isDone;


    }
    private void addUser() {
        dialog.setMessage("UpLoad Post...");
        dialog.show();
        if (imageUri != null){
            final StorageReference fileRefernace = mStorageRef.child("ImageProfile/"+ UUID.randomUUID().toString()+getFileExtintios(imageUri));
            uploadTask = fileRefernace.putFile(imageUri);
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
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY HH:MM aa");
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
                        Map<String, Object> map = new HashMap<>();
                        map.put("email", emailSignUp.getText().toString());
                        map.put("password", password.getText().toString());
                        map.put("name", name.getText().toString());
                        map.put("ImageProfileUri", uri);
                        map.put("phone", phoneNumber.getText().toString());
                        map.put("Uid", mAuth.getUid());
                        reference.child(mAuth.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }else {
                        Toast.makeText(Signup.this, "Field", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }


                }
            });

        }else {
            Toast.makeText(this, "Field", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }




}