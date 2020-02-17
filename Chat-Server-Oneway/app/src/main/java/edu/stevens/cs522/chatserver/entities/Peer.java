package edu.stevens.cs522.chatserver.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.util.Date;

import edu.stevens.cs522.chatserver.contracts.PeerContract;

/**
 * Created by dduggan.
 */

public class Peer implements Parcelable, Persistable {

    // Will be database key
    public long id;

    public String name;

    // Last time we heard from this peer.
    public Date timestamp;

    // Where we heard from them
    public InetAddress address;

    public int port;

    public Peer() {
    }

    public Peer(Cursor cursor) {
        // TODO
        id= PeerContract.getId(cursor);
        name=PeerContract.getName(cursor);
        timestamp=PeerContract.getTimeStamp(cursor);
        address=PeerContract.getAddress(cursor);
        port=PeerContract.getPort(cursor);

    }

    public Peer(Parcel in) {
        // TODO
        id= in.readLong();
        name=in.readString();
        timestamp = new Date(in.readLong());
        address = (InetAddress) in.readSerializable();
        port= in.readInt();
    }

    @Override
    public void writeToProvider(ContentValues out) {
        // TODO
//        PeerContract.putId(out, id);
        PeerContract.putName(out,name);
        PeerContract.putTimeStamp(out, timestamp);
        PeerContract.putAddress(out,address);
        PeerContract.putPort(out,port);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // TODO
        out.writeLong(id);
        out.writeString(name);
        out.writeLong(timestamp.getTime());
        out.writeInt(port);
        out.writeSerializable(address);
    }

    public static final Creator<Peer> CREATOR = new Creator<Peer>() {

        @Override
        public Peer createFromParcel(Parcel source) {
            // TODO
            return new Peer(source);
//            return null;
        }

        @Override
        public Peer[] newArray(int size) {
            // TODO
            return new Peer[size];
            //return null;
        }

    };
}
