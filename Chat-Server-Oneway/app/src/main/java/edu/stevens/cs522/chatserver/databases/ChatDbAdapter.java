package edu.stevens.cs522.chatserver.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.stevens.cs522.chatserver.contracts.MessageContract;
import edu.stevens.cs522.chatserver.contracts.PeerContract;
import edu.stevens.cs522.chatserver.entities.Message;
import edu.stevens.cs522.chatserver.entities.Peer;

/**
 * Created by dduggan.
 */

public class ChatDbAdapter {

    private static final String DATABASE_NAME = "messages.db";

    private static final String MESSAGE_TABLE = "messages";

    private static final String PEER_TABLE = "peers";

    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper dbHelper;

    private SQLiteDatabase db;

    private Cursor cursor;


    public static class DatabaseHelper extends SQLiteOpenHelper {

        // TODO

        private static final String DATABASE_CREATE_MESSAGE_TABLE = "CREATE TABLE " + MESSAGE_TABLE + " ("
                +  MessageContract._ID + " INTEGER PRIMARY KEY, "
                +  MessageContract.MESSAGE_TEXT + " TEXT, "
                +  MessageContract.TIMESTAMP + " LONG, "
                +  MessageContract.SENDER + " TEXT, "
                +  MessageContract.SENDER_ID + " INTEGER NOT NULL, "
//                +  MessageContract.PEER_FOREIGN + " INTEGER NOT NULL, "
                +  "FOREIGN KEY (" + MessageContract.SENDER_ID +") REFERENCES PEER_TABLE(_ID) ON DELETE CASCADE"
                +")";

        private static final String DATABASE_PEER_TABLE = "CREATE TABLE " + PEER_TABLE + " ("
                +  PeerContract._ID + " INTEGER PRIMARY KEY, "
                +  PeerContract.NAME + " TEXT, "
                +  PeerContract.TIMESTAMP + " LONG, "
                +  PeerContract.ADDRESS + " TEXT NOT NULL, "
                +  PeerContract.PORT + " INTEGER "
                + ")";


        private static final String CREATE_MESSAGE_PEER_INDEX = "CREATE INDEX MessagesPeerIndex ON "
                + MESSAGE_TABLE + "(" + MessageContract.SENDER_ID + ")";

        private static final String CREATE_PEER_INDEX = "CREATE INDEX PeerNameIndex ON "
                + PEER_TABLE + "(" + PeerContract.NAME + ")";

        private static final String DELETE_MESSAGE_TABLE =
                "DROP TABLE IF EXISTS " + MESSAGE_TABLE;

        private static final String DELETE_PEER_TABLE =
                "DROP TABLE IF EXISTS " + PEER_TABLE;



        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO
            db.execSQL(DATABASE_CREATE_MESSAGE_TABLE);
            db.execSQL(DATABASE_PEER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
            Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to " + newVersion);
            db.execSQL(DELETE_MESSAGE_TABLE);
            db.execSQL(DELETE_PEER_TABLE);
            onCreate(db);
        }
    }


    public ChatDbAdapter(Context _context) {
        dbHelper = new DatabaseHelper(_context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() throws SQLException {
        // TODO
        db = dbHelper.getWritableDatabase();
    }

    public Cursor fetchAllMessages() {
        // TODO
        return db.query(MESSAGE_TABLE, new String[]{MessageContract._ID, MessageContract.MESSAGE_TEXT,MessageContract.TIMESTAMP,MessageContract.SENDER},null, null,null,null, null);
//        return null;
    }

    public Cursor fetchAllPeers() {
        // TODO
        return db.query(PEER_TABLE,new String[]{PeerContract._ID, PeerContract.NAME, PeerContract.TIMESTAMP,PeerContract.ADDRESS, PeerContract.PORT},null, null,null,null,null);

    }

    public Peer fetchPeer(long peerId) {
        // TODO
        String projection [] = {PeerContract._ID, PeerContract.NAME,PeerContract.TIMESTAMP,PeerContract.ADDRESS, PeerContract.PORT};
        String where = PeerContract._ID + " = " + peerId;
        Cursor cursor = db.query(PEER_TABLE,projection, where, null, null, null, null);
        if (cursor.moveToFirst()){
            Peer p = new Peer(cursor);
            cursor.close();
            return p;
        }
        cursor.close();
        return null;
    }

    public Cursor fetchMessagesFromPeer(Peer peer) {
        // TODO

        String query = "SELECT *"+ " FROM "+ MESSAGE_TABLE+","+PEER_TABLE+
                " WHERE messages.sender = "+"\""+peer.name+"\""+ " AND " + " peers.name = "+ "\""+peer.name+"\"";
        System.out.println(query);
//
       cursor = db.rawQuery(query,null, null);
        System.out.println(cursor.getCount());
        return cursor;

//        return null;
    }

    public long persist(Message message) throws SQLException {
        // TODO
        ContentValues contentValues = new ContentValues();
        message.writeToProvider(contentValues);
        System.out.println("contentValues for messages are " + contentValues.toString());

        long newMessageId = db.insert(MESSAGE_TABLE,null,contentValues);
        return newMessageId;
//        throw new IllegalStateException("Unimplemented: persist message");
    }

    /**
     * Add a peer record if it does not already exist; update information if it is already defined.
     */
//    public long persist(Peer peer) throws SQLException {
//        // TODO
//        ContentValues contentValues = new ContentValues();
//        peer.writeToProvider(contentValues);
//        System.out.println("contentValues for peers are " + contentValues.get("name"));
//        String projection[] = new String[]{PeerContract.NAME};
//        String selection = PeerContract.NAME+" = "+contentValues.get("name");
////        return db.insert(PEER_TABLE,null,contentValues);
//        Cursor cursor = db.query(PEER_TABLE, projection, selection, null, null, null, null);
//        if (cursor.moveToFirst()){
//            return
//
//        }
//        long newPeerId = db.insert(PEER_TABLE, null, contentValues);
//
//        if (newPeerId ==  -1)
//            throw new SQLException("Failed to add peer "+peer.name);
//
//        return newPeerId;
//
////        throw new IllegalStateException("Unimplemented: persist peer");
//    }

    public long persist(Peer peer) throws SQLException {
        // TODO

        ContentValues contentValues = new ContentValues();
        peer.writeToProvider(contentValues);
//        return db.insert(PEER_TABLE,null,contentValues);

        long newPeerId = db.insert(PEER_TABLE, null, contentValues);

        if (newPeerId ==  -1)
            throw new SQLException("Failed to add peer "+peer.name);

        return newPeerId;

//        throw new IllegalStateException("Unimplemented: persist peer");
    }

    public void close() {
        // TODO
        db.close();
    }
}