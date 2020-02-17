package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.databases.ChatDbAdapter;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class ViewPeerActivity extends Activity {

    public static final String PEER_ID_KEY = "peer-id";
    private SimpleCursorAdapter messageAdapter;
    private ChatDbAdapter chatDbAdapter;
    private Cursor cursor;
    ListView messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_peer);

        long peerId = getIntent().getLongExtra(PEER_ID_KEY, -1);
        if (peerId < 0) {
            throw new IllegalArgumentException("Expected peer id as intent extra");
        }

        // TODO init the UI
        chatDbAdapter = new ChatDbAdapter(this);
        chatDbAdapter.open();
        Peer peer = chatDbAdapter.fetchPeer(peerId);


        cursor = chatDbAdapter.fetchMessagesFromPeer(peer);
        startManagingCursor(cursor);

        String[] from = new String[] {MessageContract.MESSAGE_TEXT};
        int [] to  = {android.R.id.text1,android.R.id.text2};
        messageAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,cursor,from, to);
        messageList = (ListView)findViewById(R.id.view_messages);
        messageList.setAdapter(messageAdapter);



        System.out.println("Peer is "+ peer.address);
        TextView username = (TextView)findViewById(R.id.view_user_name);
        TextView timestamp = (TextView)findViewById(R.id.view_timestamp);
        TextView address = (TextView)findViewById(R.id.view_address);

        username.setText(peer.name);
        timestamp.setText(String.valueOf(peer.timestamp));
        address.setText(String.valueOf(peer.address));
    }

}
