package com.example.lenovo.lake;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Blog> MainImageUploadInfoList;
    List<String> keyList;
    DatabaseReference myref ;
    DatabaseReference mychild,mlike,mcheck;
    FirebaseAuth mAuth;
    boolean mprocesslike = false;

    public RecyclerViewAdapter(Context context, List<Blog> TempList, List<String> TempKeyList) {

        this.MainImageUploadInfoList = TempList;
        this.keyList = TempKeyList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_activity, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mAuth = FirebaseAuth.getInstance();
        myref = FirebaseDatabase.getInstance().getReference();
        final Blog blog = MainImageUploadInfoList.get(position);
        final String key = keyList.get(position);
        mychild = myref.child("root").child("Blog");
        holder.postTitle.setText(blog.getPostTitle());
        holder.uplike.findViewById(R.id.imgUp);
        holder.downlike.findViewById(R.id.imgdown);
        holder.postDesc.setText(blog.getPostDesc());

        Glide.with(context.getApplicationContext()).load(blog.getPostImage()).into(holder.postImage);

        holder.userName.setText(blog.getUserName());
        holder.upcounter.setText(blog.getupCounter());
        holder.downcounter.setText(blog.getdownCounter());
        Glide.with(context.getApplicationContext()).load(blog.getProfileImage()).into(holder.profileImage);

        mcheck = mychild.child(key);
        mcheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("upVotes").hasChild(mAuth.getCurrentUser().getUid()))
                {
                    holder.uplike.setImageResource(R.mipmap.bluelike);
                }
                else if (dataSnapshot.child("downVotes").hasChild(mAuth.getCurrentUser().getUid()))
                {
                    holder.downlike.setImageResource(R.mipmap.redlike);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.uplike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mlike= mychild.child(key);
            mlike.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("upVotes").hasChild(mAuth.getCurrentUser().getUid()) || dataSnapshot.child("downVotes").hasChild(mAuth.getCurrentUser().getUid()) )
                    {

                    }
//                    else if(dataSnapshot.child("downVotes").hasChild(mAuth.getCurrentUser().getUid()))
//                    {
//                        blog.decdownCounter();
//                        holder.downcounter.setText(blog.getdownCounter());
//                        mlike.child("downVotes").child(mAuth.getCurrentUser().getUid()).removeValue();
//                        mychild.child(key).child("downCounter").setValue(blog.getdownCounter());
//                        holder.downlike.setImageResource(R.mipmap.unlikethumb);
//
//                        blog.setupCounter();
//                        holder.upcounter.setText(blog.getupCounter());
//                        mlike.child("upVotes").child(mAuth.getCurrentUser().getUid()).setValue("0");
//                        mychild.child(key).child("upCounter").setValue(blog.getupCounter());
//                        holder.uplike.setImageResource(R.mipmap.bluelike);
//
//                    }
                    else
                    {
                        blog.setupCounter();
                        holder.upcounter.setText(blog.getupCounter());
                        mlike.child("upVotes").child(mAuth.getCurrentUser().getUid()).setValue("0");
                        mychild.child(key).child("upCounter").setValue(blog.getupCounter());
                        holder.uplike.setImageResource(R.mipmap.bluelike);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            }
        });

        holder.downlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlike= mychild.child(key);
                mlike.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("downVotes").hasChild(mAuth.getCurrentUser().getUid()) || dataSnapshot.child("upVotes").hasChild(mAuth.getCurrentUser().getUid()) )
                        {

                        }
//                        else if(dataSnapshot.child("upVotes").hasChild(mAuth.getCurrentUser().getUid()))
//                        {
//                            blog.decreaseup();
//                            holder.upcounter.setText(blog.getupCounter());
//                            mlike.child("upVotes").child(mAuth.getCurrentUser().getUid()).removeValue();
//                            mychild.child(key).child("upCounter").setValue(blog.getupCounter());
//                            holder.uplike.setImageResource(R.mipmap.likethumb);
//
//                            blog.setdownCounter();
//                            holder.downcounter.setText(blog.getdownCounter());
//                            mlike.child("downVotes").child(mAuth.getCurrentUser().getUid()).setValue("0");
//                            mychild.child(key).child("downCounter").setValue(blog.getdownCounter());
//                            holder.downlike.setImageResource(R.mipmap.redlike);
//
//                        }
                        else
                        {
                            blog.setdownCounter();
                            holder.downcounter.setText(blog.getdownCounter());
                            mlike.child("downVotes").child(mAuth.getCurrentUser().getUid()).setValue("0");
                            mychild.child(key).child("downCounter").setValue(blog.getdownCounter());
                            holder.downlike.setImageResource(R.mipmap.redlike);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView postTitle;
        public TextView postDesc;
        public ImageView postImage;
        public ImageView profileImage;
        public TextView userName;
        public TextView upcounter,downcounter;
        public ImageButton uplike,downlike;
        public ViewHolder(View itemView) {

            super(itemView);

            postTitle = (TextView) itemView.findViewById(R.id.postTitle);

            postDesc = (TextView) itemView.findViewById(R.id.postDescription);

            postImage = (ImageView) itemView.findViewById(R.id.postImage);

            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);

            userName = (TextView) itemView.findViewById(R.id.userName);

            upcounter = (TextView) itemView.findViewById(R.id.uptext);
            downcounter = (TextView)itemView.findViewById(R.id.downtext);
            uplike = (ImageButton)itemView.findViewById(R.id.imgUp);
            downlike = (ImageButton)itemView.findViewById(R.id.imgdown);
        }
    }

    public void clear()
    {
        int size = this.MainImageUploadInfoList.size();
        if (size > 0)
        {
            for (int i = 0;i<size;i++)
                delete(i);
            this.notifyItemRangeRemoved(0,size);
        }
    }

    private void delete(int i) {
        MainImageUploadInfoList.remove(i);
        notifyItemRemoved(i);
    }
}