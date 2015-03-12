package sample.info.minitong;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chunghj on 15. 2. 6..
 */
public class SmsInfo implements Parcelable {
    private long msgId;
    private String smsaddress;
    private String smsbody;
    private String smsdate;

    public SmsInfo(String smsaddress, String smsbody, String smsdate) {

        this.smsaddress = smsaddress;
        this.smsbody = smsbody;
        this.smsdate = smsdate;

    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public String getName() {
        return smsaddress;
    }

    public String getBody() {
        return smsbody;
    }

    public String getDate() {
        return smsdate;
    }

    public void setName(String name) {
        this.smsaddress = smsaddress;
    }

    public void setBody(String smsdate) {
        this.smsbody = smsbody;
    }

    public void setDate(String name) {
        this.smsdate = smsdate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.msgId);
        dest.writeString(this.smsaddress);
        dest.writeString(this.smsbody);
        dest.writeString(this.smsdate);
    }

    private SmsInfo(Parcel in) {
        this.msgId = in.readLong();
        this.smsaddress = in.readString();
        this.smsbody = in.readString();
        this.smsdate = in.readString();
    }

    public static final Creator<SmsInfo> CREATOR = new Creator<SmsInfo>() {
        public SmsInfo createFromParcel(Parcel source) {
            return new SmsInfo(source);
        }

        public SmsInfo[] newArray(int size) {
            return new SmsInfo[size];
        }
    };
}
