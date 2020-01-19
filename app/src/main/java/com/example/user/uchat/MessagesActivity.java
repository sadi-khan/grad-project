package com.example.user.uchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {

    private Button SendMessage;
    private TextView displayMessage;
    private EditText inputMessage;
    private String room_name, user_name;
    private DatabaseReference root;
    String temp_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        SendMessage = (Button) findViewById(R.id.send_message_btn);
        displayMessage = (TextView) findViewById(R.id.display_message);
        inputMessage = (EditText) findViewById(R.id.input_message);

        room_name = getIntent().getExtras().get("room_name").toString();
        user_name = getIntent().getExtras().get("user_name").toString();
        setTitle("Room - " + room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        SendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> uniqueKeyMap = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(uniqueKeyMap);

                DatabaseReference userRef = root.child(temp_key);
                Map<String, Object> userMessageMap = new HashMap<String, Object>();
                userMessageMap.put("name", user_name);
                userMessageMap.put("message", inputMessage.getText().toString());

                userRef.updateChildren(userMessageMap);

                inputMessage.setText("");
                inputMessage.requestFocus();

            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Append_chat_conversation(dataSnapshot);

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


    }
    String chatMessage, chatUserName;

    private void Append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext())
        {
            chatMessage = (String) ((DataSnapshot)i.next()).getValue();
            chatUserName = (String) ((DataSnapshot)i.next()).getValue();

            displayMessage.append(chatUserName + ": "+ chatMessage + "\n \n");

        }
    }
}
