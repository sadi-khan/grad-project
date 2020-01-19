package com.example.user.uchat;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChatRooms extends AppCompatActivity {

    private Button add_room_button;
    private EditText room_name;
    private ListView listview_of_ChatRoomNames;

    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();

    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_rooms);

        add_room_button = (Button) findViewById(R.id.add_room_btn);
        room_name = (EditText) findViewById(R.id.add_room);
        listview_of_ChatRoomNames = (ListView) findViewById(R.id.add_room_listView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);
        listview_of_ChatRoomNames.setAdapter(arrayAdapter);

        Request_Username();

        add_room_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRefrence = database.getReference(room_name.getText().toString());
                mRefrence.setValue("");

                room_name.setText("");
                room_name.requestFocus();

            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRefrence = database.getReference(room_name.getText().toString());
        mRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listview_of_ChatRoomNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent messages = new Intent(ChatRooms.this, MessagesActivity.class);
                messages.putExtra("user_name", name);
                messages.putExtra("room_name", ((TextView)view).getText().toString());
                startActivity(messages);

            }
        });



    }

    private void Request_Username() {
        AlertDialog.Builder builder = new AlertDialog.Builder (ChatRooms.this);
        builder.setTitle("Enter UserID");
        final EditText inputField = new EditText(ChatRooms.this);
        builder.setView(inputField);

        builder.setPositiveButton("Ok", new Dialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
               name = inputField.getText().toString();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();

                Request_Username();

            }
        });

      builder.show();

    }
}
