package com.example.ysh.hyena.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ysh.hyena.Context.ContextAdapter;
import com.example.ysh.hyena.Context.DataForm;
import com.example.ysh.hyena.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RoomFragment extends Fragment {

    //리사이클러뷰
    private RecyclerView rcv_room;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<DataForm> dataContext;
    private FirebaseDatabase database;
    private String email;
    //리사이클러뷰

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


        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_room.setLayoutManager(layoutManager2);

        dataContext = new ArrayList<>();
        mAdapter = new ContextAdapter(dataContext,email,getActivity());
        rcv_room.setAdapter(mAdapter);

        DatabaseReference myRef = database.getReference("room");
        myRef.addChildEventListener(new ChildEventListener()
        {
            //항목 목록을 검색하기 위한 이벤트
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                DataForm context = dataSnapshot.getValue(DataForm.class);
                dataContext.add(context);

                rcv_room.scrollToPosition(dataContext.size()-1);
                mAdapter.notifyItemInserted(dataContext.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // 리사이클러뷰
        return  v;
    }

}