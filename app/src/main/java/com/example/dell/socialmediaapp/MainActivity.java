package com.example.dell.socialmediaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView instaList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        instaList=findViewById(R.id.instaList);
        instaList.setHasFixedSize(true);
        instaList.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("InstaApp");

        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent loginIntent=new Intent(MainActivity.this,RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PostActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        FirebaseRecyclerAdapter<InstaItem,InstaViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<InstaItem, InstaViewHolder>(
                InstaItem.class,
                R.layout.insta_item,
                InstaViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(InstaViewHolder viewHolder, InstaItem model, int position) {

                final String postKey=getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setUserImage(getApplicationContext(),model.getUserimage());
                viewHolder.setUserName(model.getUsername());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(MainActivity.this,SinglePostActivity.class);
                        intent.putExtra("Postid",postKey);
                        startActivity(intent);
                    }
                });
            }
        };
        instaList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.action_add){
            Intent intent=new Intent(MainActivity.this,PostActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.action_logout){
            firebaseAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class InstaViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public InstaViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setTitle(String title){
            TextView textTitle=mView.findViewById(R.id.textTitle);
            textTitle.setText(title);
        }

        public void setDesc(String desc){
            TextView textDesc= mView.findViewById(R.id.textDesc);
            textDesc.setText(desc);
        }
        public void setImage(Context ctx, String image){
            ImageView postImage=mView.findViewById(R.id.postImage);
            Picasso.with(ctx).load(image).into(postImage);
        }
        public void setUserName(String username){
            TextView postUserName=mView.findViewById(R.id.postUserName);
            postUserName.setText(username);
        }
        public void setUserImage(Context ctx, String image){
            ImageView postUserImage=mView.findViewById(R.id.postUserImage);
            Picasso.with(ctx).load(image).into(postUserImage);
        }

    }
}
