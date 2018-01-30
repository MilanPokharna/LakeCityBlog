package com.example.lenovo.lake;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main2Activity extends AppCompatActivity{
    ImageView gallery;
    private Uri fileUri;
    public TextView username;
    public EditText postTitle;
    public EditText postdes;
    public ImageView profileimage;
    public ImageView postimage;
    ImageButton closebutton;
    public DatabaseReference myref;
    public DatabaseReference mchild;
    public StorageReference storeimage;
    File photoFile;
    private final static int RESULT_LOAD_IMAGE = 1;
    private final static int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CONTENT_REQUEST=1337;
    private File output=null;
    Uri photoUrl;
    FirebaseUser user;
    Uri selectedImage;
    Uri resultUri;
    public RecyclerViewAdapter rev = new RecyclerViewAdapter(this,new ArrayList<Blog>(), new  ArrayList<String>());

    StorageReference filepath;


    Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );
        gallery=(ImageView) findViewById(R.id.basic);
        username = (TextView) findViewById(R.id.userName);
        postTitle = (EditText)findViewById(R.id.postTitle);
        postdes = (EditText)findViewById(R.id.postDescription);
        postimage = (ImageView)findViewById(R.id.postImage);
        closebutton=(ImageButton)findViewById(R.id.closeButton);
        profileimage = (ImageView)findViewById(R.id.profileImage);
        myref = FirebaseDatabase.getInstance().getReference();
        mchild = myref.child("root").child("Blog").push();
        storeimage = FirebaseStorage.getInstance().getReference();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            username.setText(name);
            Glide.with(getApplicationContext()).load(photoUrl).into(profileimage);
        }


        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImage = Uri.parse("");
                gallery.setVisibility(View.GONE);

            }
        });


    }
    public void clickphoto(View v)
    {
       Intent intent=new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
        String randomString = getSaltString();
        File dir=
                new File(Environment.getExternalStorageDirectory(),randomString+".jpg");
        selectedImage = Uri.fromFile(new File("/sdcard/"+randomString+".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);


        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

        gallery.setVisibility( View.VISIBLE );
    }
    public void Gallery(View v)
    {

        Intent intent = new   Intent(Intent.ACTION_GET_CONTENT);
        intent.setType( "image/*" );
        startActivityForResult(intent, 2);
        gallery.setVisibility( View.VISIBLE );

    }
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                CropImage.activity(selectedImage)
                        .setAspectRatio(1,1)
                        .start(this);
                filepath = storeimage.child("photos").child(selectedImage.getLastPathSegment());

                closebutton.setVisibility(View.VISIBLE);
//      filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        mchild.child("postImage").setValue(taskSnapshot.getDownloadUrl().toString());
//                    }
//                });

            } else if (requestCode == 2) {
                selectedImage = data.getData();
                CropImage.activity(selectedImage)
                        .setAspectRatio(1,1)
                        .start(this);
                filepath = storeimage.child("photos").child(selectedImage.getLastPathSegment());

                closebutton.setVisibility(View.VISIBLE);

            }
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    resultUri = result.getUri();
                    gallery.setImageURI(resultUri);
                    selectedImage = resultUri;

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.postData:
                uploadData();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void uploadData() {
        EditText pd = (EditText) findViewById(R.id.postDescription);
        EditText title = (EditText) findViewById(R.id.postTitle);

        String ed_text = pd.getText().toString().trim();
        String pd_text = title.getText().toString().trim();

        if((ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null) || (pd_text.isEmpty() || pd_text.length() == 0 || pd_text.equals("") || pd_text == null))
        {
            Toast.makeText(this,"Field can't be empty",Toast.LENGTH_SHORT).show();
            //EditText is empty
        }
        else {
            //EditText is not empty


                if ((selectedImage != null)) {
                    filepath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mchild.child("postImage").setValue(taskSnapshot.getDownloadUrl().toString());
                        }
                    });
                }
            Toast.makeText(this, "data uploading", Toast.LENGTH_SHORT).show();
            String postT = postTitle.getText().toString();
            String postU = username.getText().toString();
            String postD = postdes.getText().toString();
            mchild.child("userName").setValue(postU);
            mchild.child("postTitle").setValue(postT);
            mchild.child("postDesc").setValue(postD);
            mchild.child("profileImage").setValue(photoUrl.toString());
            mchild.child("upCounter").setValue("0");
            mchild.child("downCounter").setValue("0");
            Toast.makeText(this, "data uploaded", Toast.LENGTH_SHORT).show();
            rev.clear();
            startActivity(new Intent(Main2Activity.this,MainActivity.class));

            finish();
        }
    }
}

