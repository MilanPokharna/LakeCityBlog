package com.example.lenovo.lake;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

FirebaseUser user;
    DatabaseReference databaseReference;
    public SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;

    List<Blog> list = new ArrayList<>();
    List<String> stringList = new ArrayList<>();

    RecyclerView recyclerView ;

    RecyclerView.Adapter adapter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user ==null){
            Intent in = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(in);
            finish();
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("root").child("Blog");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Blog blog = dataSnapshot.getValue(Blog.class);
                    String s = dataSnapshot.getKey();
                    stringList.add(s);
                    list.add(blog);

                }
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        //adapter = new RecyclerViewAdapter(MainActivity.this, list,stringList);
//                        Intent intent = new Intent(MainActivity.this,MainActivity.class);
//                        startActivity(intent);

                        adapter = new RecyclerViewAdapter(MainActivity.this, list,stringList);

                        recyclerView.setAdapter(adapter);


                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
               adapter = new RecyclerViewAdapter(MainActivity.this, list,stringList);

                recyclerView.setAdapter(adapter);

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressDialog.dismiss();
            }


        });


        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



    }





    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null){

                        return true;
                    }else{Intent in = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();}

                case R.id.navigation_add:
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        Intent in = new Intent(MainActivity.this, Main2Activity.class);
                        startActivity(in);

                        return true;
                    }else
                    {
                        Intent in = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                    }
                case R.id.navigation_contact:
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        Intent intent = new Intent(MainActivity.this, ProfileSection.class);
                        startActivity(intent);
                        return true;
                    }else
                    {
                        Intent in = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(in);
                        finish();
                    }
            }
            return false;
        }
    };


}
