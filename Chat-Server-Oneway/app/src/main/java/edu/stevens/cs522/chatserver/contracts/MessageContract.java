package edu.stevens.cs522.chatserver.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by dduggan.
 */

public class MessageContract implements BaseColumns {

    public static final String MESSAGE_TEXT = "message_text";

    public static final String TIMESTAMP = "timestamp";

    public static final String SENDER = "sender";

    public static final String PEER_FOREIGN = "peer_fk";

    public static final String SENDER_ID = "senderId";

    // TODO remaining columns in Messages table
    private static int idColumn = -1;

    public static long getID(Cursor cursor) {
        if (idColumn < 0) {
            idColumn = cursor.getColumnIndexOrThrow(_ID);
        }
        return cursor.getLong(idColumn);
    }

    public static void putId(ContentValues out, long id) {
        out.put(_ID, id);
    }


    private static int messageTextColumn = -1;

    public static String getMessageText(Cursor cursor) {
        if (messageTextColumn < 0) {
            messageTextColumn = cursor.getColumnIndexOrThrow(MESSAGE_TEXT);
        }
        return cursor.getString(messageTextColumn);
    }

    public static void putMessageText(ContentValues out, String messageText) {
        out.put(MESSAGE_TEXT, messageText);
    }

    // TODO remaining getter and putter operations for other columns
    private static int timestampColumn = -1;

    public static Date getTimeStamp(Cursor cursor){
        if (timestampColumn<0){
            timestampColumn = cursor.getColumnIndexOrThrow(TIMESTAMP);
        }
        Date date = new Date(cursor.getLong(timestampColumn));
        return (date);
    }

    public static void putTimeStamp(ContentValues out, Date date) {
        out.put(TIMESTAMP, date.getTime());
    }

    private static int senderColumn = -1;

    public static String getMessageSender(Cursor cursor) {
        if (senderColumn < 0) {
            senderColumn = cursor.getColumnIndexOrThrow(SENDER);
        }
        return cursor.getString(senderColumn);
    }

    public static void putSender(ContentValues out, String senderText) {
        out.put(SENDER, senderText);
    }


    private static int senderIdColumn = -1;

    public static int getSenderId(Cursor cursor) {
        if (senderIdColumn < 0) {
            senderIdColumn = cursor.getColumnIndexOrThrow(SENDER_ID);
        }
        return cursor.getInt(senderIdColumn);
    }

    public static void putSenderId(ContentValues out, Long senderIdText) {
        out.put(SENDER_ID, senderIdText);
    }

}
