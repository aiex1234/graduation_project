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

public class RoomFragment extends Fragment {

    private RecyclerView rcv_room;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<DataForm> dataContext;
    private FirebaseDatabase database;
    private String email;

    public RoomFragment()  {
   }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_room, container, false);
        // 리사이클러뷰
        database = FirebaseDatabase.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        rcv_room = (RecyclerView) v.findViewById(R.id.rcv_room);


        final LinearLayoutManager layoutManager_Room = new LinearLayoutManager(getActivity());
        layoutManager_Room.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager_Room.setReverseLayout(true);
        layoutManager_Room.setStackFromEnd(true);
        rcv_room.setLayoutManager(layoutManager_Room);

        dataContext = new ArrayList<>();
        mAdapter = new ContextAdapter(dataContext,email,getActivity());
        rcv_room.setAdapter(mAdapter);

        DatabaseReference myRef = database.getReference("room");
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
