package sample.info.minitong;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunghj on 15. 3. 5..
 */
public class MiniMsgFragment extends ListFragment implements AdapterView.OnItemClickListener {
    // The data to show
    private List<SmsInfo> msgList = new ArrayList<SmsInfo>();
    private TestSmsListAdapter mAdapter;
    private String bodystr;

    //    private static MiniMsgFragment inst;
//   ArrayList<String> smsMessagesList = new ArrayList<String>();
//    ListView lv;
//    ArrayAdapter arrayAdapter;
    Context context;

    public static MiniMsgFragment newInstance(CharSequence label) {
        MiniMsgFragment f;
        f = new MiniMsgFragment();
        Bundle b = new Bundle();
        b.putCharSequence("label", label);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //This layout contains your list view
        View view = inflater.inflate(R.layout.msglist_fragment, container, false);


        initList();

        // We get the ListView component from the layout
        final ListView lv = (ListView) view.findViewById(android.R.id.list);

        mAdapter = new TestSmsListAdapter(msgList, getActivity());
        setListAdapter(mAdapter);

        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {
//                Intent intent = new Intent(this, SmsDetailActivity.class);
//                SmsInfo smsInfo = mAdapter.getItem(position);
//                intent.putExtra("smsInfo", smsInfo);
//                startActivity(intent);

            }
        });

        // we register for the contextmneu
        registerForContextMenu(lv);

        return view;

    }

    private void initList() {

        ContentResolver contentResolver = context.getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex("body");
        int indexAddress = smsInboxCursor.getColumnIndex("address");
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        mAdapter.clear();
        do {
            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) +
                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            mAdapter.add(str);
        } while (smsInboxCursor.moveToNext());
    }

    public void updateList(final String smsMessage) {
        mAdapter.insert(smsMessage, 0);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            String[] smsMessages = smsMessagesList.get(position).split("\n");
            String address = smsMessages[0];
            String smsMessage = "";
            for (int i = 1; i < smsMessages.length; ++i) {
                smsMessage += smsMessages[i];
            }

            String smsMessageStr = address + "\n";
            smsMessageStr += smsMessage;
            Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
}