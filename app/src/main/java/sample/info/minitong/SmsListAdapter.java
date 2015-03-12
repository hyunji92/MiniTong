package sample.info.minitong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chunghj on 15. 2. 6..
 */
public class SmsListAdapter extends ArrayAdapter<SmsInfo> {

    // private final LayoutInflater mInflater = null;

    private List<SmsInfo> msgList;
    private Context context;

    public SmsListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public SmsListAdapter(List<SmsInfo> msgList, Context ctx) {
        super(ctx, R.layout.custom_sms_list, msgList);
        this.msgList = msgList;
        this.context = ctx;
    }


    public int getCount() {
        return msgList.size();
    }

    public SmsInfo getItem(int position) {
        return msgList.get(position);
    }

    public long getItemId(int position) {
        return msgList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        SmsInfoHolder holder = new SmsInfoHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_sms_list, null);
            // Now we can fill the layout with the right values
            TextView mSmsAddress = (TextView) v.findViewById(R.id.sms_address);
            TextView mSmsBody = (TextView) v.findViewById(R.id.sms_body);
            TextView mSmsDate = (TextView) v.findViewById(R.id.sms_date);


            holder.SmsNnumberView = mSmsAddress;
            holder.SmsBodyView = mSmsBody;
            holder.SmsDateView = mSmsDate;

            v.setTag(holder);
        } else
            holder = (SmsInfoHolder) v.getTag();

        SmsInfo info = msgList.get(position);
        holder.SmsNnumberView.setText(info.getName());
        holder.SmsBodyView.setText("" + info.getBody());
        holder.SmsDateView.setText("" + info.getDate());


        return v;
    }

	/* *********************************
     * We use the holder pattern
	 * It makes the view faster and avoid finding the component
	 * **********************************/

    private static class SmsInfoHolder {
        public TextView SmsNnumberView;
        public TextView SmsBodyView;
        public TextView SmsDateView;
    }


}
