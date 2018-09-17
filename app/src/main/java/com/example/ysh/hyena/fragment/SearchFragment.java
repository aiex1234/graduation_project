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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView rcvSearch;
    private RecyclerView.Adapter adapter;
    private ArrayList<DataForm> dataContext;
    private String email;
    private String searchText;
    private int checkFragment;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Query query;
    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_search, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();

        Bundle bundle = this.getArguments();

        if(bundle != null) {
            searchText = bundle.getString(TabActiviy.KEY_SEARCH_TEXT,"디폴트");
            checkFragment = bundle.getInt(TabActiviy.KEY_FRAGMENT,1);
        }

        Toast.makeText(getContext(),searchText,Toast.LENGTH_SHORT).show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        rcvSearch = v.findViewById(R.id.rcv_search);

        final LinearLayoutManager layoutManager_search = new LinearLayoutManager(getActivity());
        layoutManager_search.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager_search.setReverseLayout(true);
        layoutManager_search.setStackFromEnd(true);
        rcvSearch.setLayoutManager(layoutManager_search);

        dataContext = new ArrayList<>();
        adapter = new ContextAdapter(dataContext,email,getActivity());
        rcvSearch.setAdapter(adapter);

        if(checkFragment == TabActiviy.FRAGMENT_PRODUCT) {
            databaseReference = firebaseDatabase.getReference("product");
            query = databaseReference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        } else if (checkFragment == TabActiviy.FRAGMENT_ROOM) {
            databaseReference = firebaseDatabase.getReference("room");
            query = databaseReference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        } else {
            databaseReference = firebaseDatabase.getReference("book");
            query = databaseReference.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataContext.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataForm dataForm = snapshot.getValue(DataForm.class);
                    dataContext.add(dataForm);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"실패",Toast.LENGTH_SHORT).show();
            }
        });

        return  v;

    }

}
