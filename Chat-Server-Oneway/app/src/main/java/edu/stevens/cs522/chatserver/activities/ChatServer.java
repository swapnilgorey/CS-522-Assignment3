/*********************************************************************

    Chat server: accept chat messages from clients.
    
    Sender name and GPS coordinates are encoded
    in the messages, and stripped off upon receipt.

    Copyright (c) 2017 Stevens Institute of Technology

**********************************************************************/
package edu.stevens.cs522.chatserver.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.base.DatagramSendReceive;
import edu.stevens.cs522.chatserver.R;
import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.databases.ChatDbAdapter;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;

public class ChatServer extends Activity implements OnClickListener {

	final static public String TAG = ChatServer.class.getCanonicalName();
		
	/*
	 * Socket used both for sending and receiving
	 */
    private DatagramSendReceive serverSocket;
//  private DatagramSocket serverSocket;


    /*
	 * True as long as we don't get socket errors
	 */
	private boolean socketOK = true; 

    /*
     * UI for displayed received messages
     */
	private ListView messageList;

    private SimpleCursorAdapter messagesAdapter;

    private ChatDbAdapter chatDbAdapter;

    private Button next;
    private Cursor cursor;

    /*
     * Use to configure the app (user name and port)
     */
    private SharedPreferences settings;
	
	/*
	 * Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        /**
         * Let's be clear, this is a HACK to allow you to do network communication on the messages thread.
         * This WILL cause an ANR, and is only provided to simplify the pedagogy.  We will see how to do
         * this right in a future assignment (using a Service managing background threads).
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            /*
             * Get port information from the resources.
             */
            int port = getResources().getInteger(R.integer.app_port);

            // serverSocket = new DatagramSocket(port);

            serverSocket = new DatagramSendReceive(port);

        } catch (Exception e) {
            throw new IllegalStateException("Cannot open socket", e);
        }

        setContentView(R.layout.messages);

        // TODO open the database using the database adapter
        chatDbAdapter = new ChatDbAdapter(this);
        chatDbAdapter.open();

        // TODO query the database using the database adapter, and manage the cursor on the messages thread
        cursor = chatDbAdapter.fetchAllMessages();
        startManagingCursor(cursor);

        // TODO use SimpleCursorAdapter to display the messages received.
        String[] from = new String[]{MessageContract.SENDER, MessageContract.MESSAGE_TEXT};
        int[] to = new int[] {android.R.id.text1, android.R.id.text2};
        messagesAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2, cursor, from, to);
        messageList = (ListView) findViewById(R.id.message_list);
        messageList.setAdapter(messagesAdapter);
        // TODO bind the button for "next" to this activity as listener
        next = (Button)findViewById(R.id.next);
        next.setOnClickListener(this);
	}

    public void onClick(View v) {
		
		byte[] receiveData = new byte[1024];

		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

		try {
			
			serverSocket.receive(receivePacket);
			Log.d(TAG, "Received a packet");

			InetAddress sourceIPAddress = receivePacket.getAddress();
			Log.d(TAG, "Source IP Address: " + sourceIPAddress);
			
			String msgContents[] = new String(receivePacket.getData(), 0, receivePacket.getLength()).split(":");
            Message message = new Message();
            message.sender = msgContents[0];
            //message.timestamp = new Date(Long.parseLong(msgContents[1]));
            message.messageText = msgContents[1];
            message.timestamp = new Date();


			Log.d(TAG, "Received from " + message.sender + ": " + message.messageText);

			/*
			 * TODO upsert peer and insert message into the database
			 */
            Peer peer= new Peer();
            peer.name=message.sender;
            peer.timestamp = message.timestamp;
            peer.address = sourceIPAddress;
            peer.port = receivePacket.getPort();



            //updating MessageTable with SenderID

//            message.senderId = peer.id;

            message.senderId = chatDbAdapter.persist(peer);
            System.out.println("Sender id  is" +message.senderId);
            System.out.println("Sender id  is" +message.toString());

            chatDbAdapter.persist(message);



            /*
             * End TODO
             */
            Cursor cursor = chatDbAdapter.fetchAllMessages();
            messagesAdapter.swapCursor(cursor);
            messagesAdapter.notifyDataSetChanged();

		} catch (Exception e) {
			
			Log.e(TAG, "Problems receiving packet: " + e.getMessage(), e);
			socketOK = false;
		} 

	}

	/*
	 * Close the socket before exiting application
	 */
	public void closeSocket() {
	    if (serverSocket != null) {
            serverSocket.close();
            serverSocket = null;
        }
	}

	/*
	 * If the socket is OK, then it's running
	 */
	boolean socketIsOK() {
		return socketOK;
	}

    public void onDestroy() {
        super.onDestroy();
        chatDbAdapter.close();
        closeSocket();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // TODO inflate a menu with PEERS option
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatserver_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {

            case R.id.peers:
                Intent intentView = new Intent(this, ViewPeersActivity.class);
                startActivity(intentView);
                break;

            default:
        }
        return false;
    }

}