package com.example.lenovo.lake;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileSection extends AppCompatActivity {
    CircleImageView profileimage;
    TextView Username;
    TextView Email;
    FirebaseUser user;
    TextView text,no;
    DatabaseReference mref;
    RecyclerView.Adapter recyclerViewAdapter ;
    RecyclerView recyclerView;
    String u ;
    List<Blog> list = new ArrayList<>();
    List<String> postkey = new ArrayList<>();

    ProgressDialog mprogress;

    Button Signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_section);
      profileimage= (CircleImageView)findViewById(R.id.profileImage);
      Signout=(Button)findViewById(R.id.signout);
      u = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
      text = (TextView)findViewById(R.id.delete);
    Username = (TextView)findViewById(R.id.UserName);
    Email= (TextView)findViewById(R.id.Email);
    recyclerView = (RecyclerView)findViewById(R.id.userrecyclerview);
        recyclerView.setHasFixedSize(true);
        no = (TextView)findViewById(R.id.nopost);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProfileSection.this));
    mref = FirebaseDatabase.getInstance().getReference();
    mref = mref.child("root").child("Blog");
    mref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            list.clear();
            postkey.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren())
            {
                if (u.equals(snapshot.child("userName").getValue().toString()))
                {
                    String x = snapshot.getKey().toString();
                    Blog blog = snapshot.getValue(Blog.class);
                    list.add(blog);
                    postkey.add(x);


                }
            }
            if (postkey.isEmpty())
            {
                no.setVisibility(View.VISIBLE);
            }
            else
            {
                no.setVisibility(View.GONE);
            }
            postkey.add("ProfileSection");
            recyclerViewAdapter = new RecyclerViewAdapter(ProfileSection.this,list,postkey);
            recyclerView.setAdapter(recyclerViewAdapter);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
            Username.setText(name);
            Email.setText(email);
            Glide.with(getApplicationContext()).load(photoUrl).into(profileimage);
        }
        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                    //deleteAppData();

                Intent in = new Intent(ProfileSection.this, LoginActivity.class);
                finish();
                startActivity(in);


            }

        });

    }

//    private void deleteAppData() {
//        try {
//            // clearing app data
//            String packageName = getApplicationContext().getPackageName();
//            Runtime runtime = Runtime.getRuntime();
//            runtime.exec("pm clear "+packageName);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileSection.this,MainActivity.class);
        finish();
        startActivity(intent);
    }
}
