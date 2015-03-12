package sample.info.minitong;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chunghj on 15. 3. 5..
 */
public class MiniDealFrgment  extends ListFragment {
    // The data to show
    private List<SmsInfo> msgList = new ArrayList<SmsInfo>();
    private TestSmsListAdapter mAdapter;
    private String bodystr;

    public static MiniDealFrgment newInstance(CharSequence label) {
        MiniDealFrgment f;
        f = new MiniDealFrgment();
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

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, "_id DESC");
        //쿼리에 가져와서 쏘트 할때 address 로 해도 되고

        cursor.moveToFirst();

        int colBody = cursor.getColumnIndex("body");
        int colAddress = cursor.getColumnIndex("address");
//        int colDate = cursor.getColumnIndex("date");

        if (colBody < 0 || colAddress < 0) {
            cursor.close();
        }
        do {
            String addressstr = cursor.getString(colAddress);
            bodystr = cursor.getString(colBody);
//            Long datestr= cursor.getString(colDate);

            List<String> urlList = AdsHelper.extractUrl(addressstr + bodystr);
            if (urlList == null || urlList.size() < 1) continue;
            msgList.add(new SmsInfo(addressstr, bodystr, "02월 07일"));

        } while (cursor.moveToNext());

    }


}

