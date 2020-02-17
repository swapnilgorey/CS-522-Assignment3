package edu.stevens.cs522.chatserver.contracts;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import edu.stevens.cs522.base.InetAddressUtils;

/**
 * Created by dduggan.
 */

public class PeerContract implements BaseColumns {

    // TODO define column names, getters for cursors, setters for contentvalues

    public static final String NAME = "name";
    public static final String TIMESTAMP = "timestamp";
    public static final String ADDRESS = "address";
    public static final String PORT = "port";

    private static int idColumn = -1;

    public static long getId(Cursor cursor) {
        if (idColumn < 0) {
            idColumn = cursor.getColumnIndexOrThrow(_ID);
        }
        return cursor.getLong(idColumn);
    }

    public static void putId(ContentValues out, long id) {
        out.put(_ID, id);
    }

    private static int peerName = -1;

    public static String getName(Cursor cursor) {
        if (peerName < 0) {
            peerName = cursor.getColumnIndexOrThrow(NAME);
        }
        return cursor.getString(peerName);
    }

    public static void putName(ContentValues out, String name) {
        out.put(NAME, name);
    }

    private static int timeStampColumn = -1;

    public static Date getTimeStamp(Cursor cursor){
        if (timeStampColumn<0){
            timeStampColumn = cursor.getColumnIndexOrThrow(TIMESTAMP);
        }
        Date date = new Date(cursor.getLong(timeStampColumn));
        return (date);
    }

    public static void putTimeStamp(ContentValues out, Date date) {
        out.put(TIMESTAMP, date.getTime());
    }

    private static int addressColumn = -1;

    public static InetAddress getAddress(Cursor cursor){
        if (addressColumn<0){
            addressColumn = cursor.getColumnIndexOrThrow(ADDRESS);
        }
        return InetAddressUtils.getAddress(cursor, cursor.getColumnIndexOrThrow(ADDRESS));
    }
    public static void putAddress(ContentValues out, InetAddress address) {
        InetAddressUtils.putAddress(out,ADDRESS,address);
    }

    private static int portColumn = -1;

    public static int getPort(Cursor cursor){
        if (portColumn<0){
            portColumn = cursor.getColumnIndexOrThrow(PORT);
        }
        return cursor.getInt(portColumn);

    }
    public static void putPort(ContentValues out, int port) {
        out.put(PORT, port );
    }

}
