package com.example.dell.socialmediaapp;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST=2;
    private Uri uri;
    private ImageButton imageButton;
    private EditText editName,editDesc;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseUsers;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        editName=findViewById(R.id.editName);
        editDesc=findViewById(R.id.editDesc);
        imageButton=findViewById(R.id.imageButton);

        firebaseAuth=FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();

        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("InstaApp");
        databaseUsers=firebaseDatabase.getReference().child("Users").child(currentUser.getUid());
    }

    public void onSubmitClick(View view) {
        final String name=editName.getText().toString();
        final String desc=editDesc.getText().toString();

        if(!TextUtils.isEmpty(name) && (!TextUtils.isEmpty(desc))){
            StorageReference filePath=storageReference.child("PostImage").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PostActivity.this,"Upload Complete",Toast.LENGTH_SHORT).show();
                    final Uri downloadurl=taskSnapshot.getDownloadUrl();
                    final DatabaseReference newPost=databaseReference.push();

                    databaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(name);
                            newPost.child("desc").setValue(desc);
                            newPost.child("image").setValue(downloadurl.toString());
                            newPost.child("uid").setValue(currentUser.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                            newPost.child("userimage").setValue(dataSnapshot.child("Image").getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
            Intent mainActivityIntent=new Intent(PostActivity.this,MainActivity.class);
            startActivity(mainActivityIntent);
        }
    }

    public void onImageButtonClick(View view) {
        Intent galleyIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleyIntent.setType("image/*");
        startActivityForResult(galleyIntent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==GALLERY_REQUEST &&  resultCode==RESULT_OK){
           uri=data.getData();
           imageButton.setImageURI(uri);
        }
    }
}
