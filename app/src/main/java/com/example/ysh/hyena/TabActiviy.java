package com.example.ysh.hyena;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;


public class TabActiviy extends AppCompatActivity {

    private final int FRAGMENT1 = 1;
    private final int FRAGMENT2 = 2;
    private final int FRAGMENT3 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_activiy);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        callFragment(FRAGMENT1);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    callFragment(FRAGMENT1);
                    Toast.makeText(TabActiviy.this, "프래그먼트1", Toast.LENGTH_SHORT).show();
                    //ProductFragment fragment = new ProductFragment();
                    //switchFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:
                    callFragment(FRAGMENT2);
                    Toast.makeText(TabActiviy.this, "프래그먼트2", Toast.LENGTH_SHORT).show();
                  //  RoomFragment fragment2 = new RoomFragment();
                   // switchFragment(fragment2);
                    return true;
                case R.id.navigation_notifications:
                    callFragment(FRAGMENT3);
                    Toast.makeText(TabActiviy.this, "프래그먼트3", Toast.LENGTH_SHORT).show();
                   // BookFragment fragment3 = new BookFragment();
                   // switchFragment(fragment3);
                    return true;
            }
            return false;
        }
    };

    private void callFragment(int frament_no){

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no){
            case 1:
                // '프래그먼트1' 호출
                Fragment fragment1 = new ProductFragment();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

            case 2:
                // '프래그먼트2' 호출
                Fragment fragment2 = new RoomFragment();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;

            case 3:
                // '프래그먼트3' 호출
                Fragment fragment3 = new BookFragment();
                transaction.replace(R.id.fragment_container, fragment3);
                transaction.commit();
                break;
        }
    }

}
