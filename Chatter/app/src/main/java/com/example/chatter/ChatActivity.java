package com.example.chatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference mDatabase1, mDatabase2;
    private String user1id, user2id;
    private EditText mEditText;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Resources init
        mEditText = findViewById(R.id.chat_messageEditText);
        mListView = findViewById(R.id.chat_listView);
        ArrayList<String> messageList = new ArrayList<>();
        final ArrayAdapter<String> messageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageList);
        mListView.setAdapter(messageAdapter);

        //users
        try {
            user1id = getIntent().getExtras().getString("Current");
            user2id = getIntent().getExtras().getString("Friend");
        }
        catch (Exception e) {
            Toast.makeText(this, "NO way", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Database
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("messages").child(user1id).child(user2id);
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("messages").child(user2id).child(user1id);
        mDatabase1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //add Messages to array Adapter
                String message = snapshot.getValue(String.class);
                messageAdapter.add(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
    public void sendMessage(View view) {
        //addMessage to mDatabase1, mDatabase2;
        String message = mEditText.getText().toString();
        if(!(message == null || message.isEmpty())) {
            mDatabase1.push().setValue(message);
            mDatabase2.push().setValue(message);
            mEditText.setText("");
        }
    }

}