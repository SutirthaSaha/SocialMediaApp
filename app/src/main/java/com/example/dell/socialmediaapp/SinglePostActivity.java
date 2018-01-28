package com.example.dell.socialmediaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SinglePostActivity extends AppCompatActivity {

    private String post_key=null;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private ImageView singleImageView;
    private TextView singleTitle,singleDesc;
    private Button deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        post_key=getIntent().getExtras().getString("Postid");
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("InstaApp").child(post_key);
        firebaseAuth=FirebaseAuth.getInstance();

        deleteBtn=findViewById(R.id.deleteBtn);
        singleImageView=findViewById(R.id.singleImageView);
        singleTitle=findViewById(R.id.singleTitle);
        singleDesc=findViewById(R.id.singleDesc);

        deleteBtn.setVisibility(View.INVISIBLE);
        deleteBtn.setEnabled(false);
        if(!deleteBtn.isEnabled()){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    singleTitle.setText((String)dataSnapshot.child("title").getValue());
                    singleDesc.setText((String)dataSnapshot.child("desc").getValue());

                    String post_image=(String)dataSnapshot.child("image").getValue();
                    Picasso.with(SinglePostActivity.this).load(post_image).into(singleImageView);
                    if(firebaseAuth.getCurrentUser().getUid().equals((String)dataSnapshot.child("uid").getValue())){
                        deleteBtn.setVisibility(View.VISIBLE);
                        deleteBtn.setEnabled(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void onDeleteBtnClick(View view) {
        databaseReference.removeValue();
        Intent mainintent=new Intent(SinglePostActivity.this,MainActivity.class);
        startActivity(mainintent);
    }
}
