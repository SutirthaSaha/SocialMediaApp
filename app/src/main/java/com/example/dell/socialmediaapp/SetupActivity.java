package com.example.dell.socialmediaapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupActivity extends AppCompatActivity {

    //private EditText displayName;
    private ImageButton displayImage;
    private static  final int GALLERY_REQ=1;
    private Uri imageUri;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //displayName=findViewById(R.id.displayName);
        displayImage=findViewById(R.id.displayImage);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        firebaseAuth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference().child("ProfileImages");

    }

    public void onDoneBtnClick(View view) {
        //final String userName=displayName.getText().toString();
        final String user_id=firebaseAuth.getCurrentUser().getUid();
        //if(!TextUtils.isEmpty(userName) && imageUri!=null){
         if(imageUri!=null){
            StorageReference filepath=storageReference.child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUrl=taskSnapshot.getDownloadUrl().toString();
                    //databaseReference.child(user_id).child("Name").setValue(userName);
                    databaseReference.child(user_id).child("Image").setValue(downloadUrl);

                    Intent intent=new Intent(SetupActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void onProfileImgBtnClick(View view) {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQ && resultCode==RESULT_OK){

            Uri imageUri=data.getData();
            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);

        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                imageUri=result.getUri();
                displayImage.setImageURI(imageUri);

            }else if(resultCode==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error=result.getError();
            }
        }
    }
}
