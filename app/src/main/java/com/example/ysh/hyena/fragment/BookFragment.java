package com.example.ysh.hyena.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ysh.hyena.context.ContextAdapter;
import com.example.ysh.hyena.context.DataForm;
import com.example.ysh.hyena.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookFragment extends Fragment {

    private RecyclerView rcv_book;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<DataForm> dataContext;
    private FirebaseDatabase database;
    private String email;


    public BookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_book, container, false);

        // 리사이클러뷰
        database = FirebaseDatabase.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        rcv_book = (RecyclerView) v.findViewById(R.id.rcv_book);


        final LinearLayoutManager layoutManager_book = new LinearLayoutManager(getActivity());
        layoutManager_book.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager_book.setReverseLayout(true);
        layoutManager_book.setStackFromEnd(true);
        rcv_book.setLayoutManager(layoutManager_book);

        dataContext = new ArrayList<>();
        mAdapter = new ContextAdapter(dataContext,email,getActivity());
        rcv_book.setAdapter(mAdapter);

        DatabaseReference myRef = database.getReference("book");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataContext.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataForm dataForm = snapshot.getValue(DataForm.class);
                    dataContext.add(dataForm);
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"실패",Toast.LENGTH_SHORT).show();
            }
        });

        return  v;
    }
}
