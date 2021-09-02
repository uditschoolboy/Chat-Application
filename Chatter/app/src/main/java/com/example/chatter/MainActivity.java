package com.example.chatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ListView mListView;
    private String currentUserId;
    private HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //resource init
        hashMap = new HashMap<String, String>();
        mListView = findViewById(R.id.main_ListView);
        final ArrayList<String> userList = new ArrayList<>();
        final ArrayAdapter<String> userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        mListView.setAdapter(userAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nameOfFriend = adapterView.getItemAtPosition(i).toString();
                //start chat between this user and friend;
                String uidOfFriend = hashMap.get(nameOfFriend);
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("Current", currentUserId);
                intent.putExtra("Friend", uidOfFriend);
                startActivity(intent);
            }
        });

        //FireBase Authentication
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null) {
            startStartActivity();
        }
        currentUserId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    User user = snapshot.getValue(User.class);
                    String nameOfUser = user.getUserName();
                    String uid = user.getUserID();
                    hashMap.put(nameOfUser, uid);
                    if(!uid.equals(currentUserId))
                        userAdapter.add(nameOfUser);
                }
                catch (Exception e) { }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void startStartActivity() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int itemSelected = item.getItemId();
        if(itemSelected == R.id.option_logout) {
            mAuth.signOut();
            startStartActivity();
        }
        return true;
    }
}