package com.example.ysh.hyena.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ysh.hyena.AddActivity;
import com.example.ysh.hyena.R;


public class TabActiviy extends AppCompatActivity {

    static final int FRAGMENT_PRODUCT = 1;
    static final int FRAGMENT_ROOM = 2;
    static final int FRAGMENT_BOOK = 3;
    static final int FRAGMENT_SEARCH = 4;

    static final String KEY_SEARCH_TEXT = "searchText";
    static final String KEY_FRAGMENT = "checkFragment";
    private FloatingActionButton fab;

    // 검색 프래그먼트 공개변수
    public Button btnSearch;
    public EditText etSearch;
    public int checkFragment = 1;
    public  String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnSearch = findViewById(R.id.btn_search);
        etSearch = findViewById(R.id.et_search);

        fab = findViewById(R.id.fab);
        callFragment(FRAGMENT_PRODUCT);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabActiviy.this, AddActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = etSearch.getText().toString();
                callFragment(FRAGMENT_SEARCH);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    callFragment(FRAGMENT_PRODUCT);
                    checkFragment = FRAGMENT_PRODUCT;
                    return true;
                case R.id.navigation_dashboard:
                    callFragment(FRAGMENT_ROOM);
                    checkFragment = FRAGMENT_ROOM;
                    return true;
                case R.id.navigation_notifications:
                    callFragment(FRAGMENT_BOOK);
                    checkFragment = FRAGMENT_BOOK;
                    return true;
            }
            return false;
        }
    };

    private void callFragment(int frament_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case FRAGMENT_PRODUCT:
                Fragment fragment_product = new ProductFragment();
                transaction.replace(R.id.fragment_container, fragment_product);
                transaction.commit();
                break;

            case FRAGMENT_ROOM:
                Fragment fragment_room = new RoomFragment();
                transaction.replace(R.id.fragment_container, fragment_room);
                transaction.commit();
                break;

            case FRAGMENT_BOOK:
                Fragment fragment_book = new BookFragment();
                transaction.replace(R.id.fragment_container, fragment_book);
                transaction.commit();
                break;

            case FRAGMENT_SEARCH:
                Fragment fragment_search = new SearchFragment();
                Bundle bundle = new Bundle();
                bundle.putString(KEY_SEARCH_TEXT, searchText);
                bundle.putInt(KEY_FRAGMENT, checkFragment);
               // Toast.makeText(getApplication(),searchText,Toast.LENGTH_SHORT).show();
                fragment_search.setArguments(bundle);
                transaction.replace(R.id.fragment_container, fragment_search);
                transaction.commit();
                break;
        }
    }
}
