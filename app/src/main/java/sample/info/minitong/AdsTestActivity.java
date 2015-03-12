package sample.info.minitong;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.infobank.lab.ads.LinkVerifier;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import ib.ads.AdsHelper;
import ib.android.util.Log;
import test.chunghj.smsurlcheck.adapter.MessageListAdapter;
import test.chunghj.smsurlcheck.model.MsgItem;

public class AdsTestActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    protected AntiLeakHandler<AdsTestActivity> mHandler = new AntiLeakHandler<AdsTestActivity>(this);

    protected ListView mListView;
    protected MessageListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String str = "http://www.google.com 좋은 프로그래머란, 일방통행 도로에서도 양쪽을 모두 보고 건너는 사람이다. -더그 린더."
                + "http://www.infobank.net www.naver.com";

        AdsHelper.verifyLink(this, mHandler, -1, str);

        List<String> links = AdsHelper.extractUrl(str);
        if (links != null && links.size() > 0) {
            for (String url : links) {
                Log.e(url);
            }
        }

        initViews();
        new ReadSmsAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ads_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void initViews() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
    }



    public void onSuccess(long reqId, List<LinkVerifier.VerifyResult> list) {
        if (list == null || list.size() < 1) return;

        int i = 0;
        StringBuilder sb = new StringBuilder();

        sb.append("onSuccess(").append(reqId).append(")").append("\n");
        for (LinkVerifier.VerifyResult result : list) {
            String str = "[" + (i++) + "]: " + result.getLinkStr() + ", " + result.getLinkType() + ", " + result.getResultType().name();
            sb.append(str).append("\n");
            Log.e(str);
        }

        Toast.makeText(this, sb.toString().trim(), Toast.LENGTH_LONG).show();
    }

    public void onFail(long reqId) {
        StringBuilder sb = new StringBuilder();
        sb.append("onFail(").append(reqId).append(")");
        Toast.makeText(this, sb.toString().trim(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        try {
            String msg = (String) parent.getItemAtPosition(position);;
            String[] smsMessages = msg.split("\n");
            String address = smsMessages[0];
            String smsMessage = "";
            for (int i = 1; i < smsMessages.length; ++i) {
                smsMessage += smsMessages[i];
            }

            String smsMessageStr = address + "\n";
            smsMessageStr += smsMessage;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void updateList(final String smsMessage) {
//        mListAdapter.add(smsMessage);
//        mListAdapter.notifyDataSetChanged();
//    }



    /**
     * Instances of static inner classes do not hold an implicit reference to their outer class.
     * http://regularmotion.kr/android-how-to-leak-a-context-handlers-inner-classes/
     */
    private static class AntiLeakHandler<T> extends Handler {
        private final WeakReference<T> mComponent;

        public AntiLeakHandler(T component) {
            mComponent = new WeakReference<T>(component);
        }

        @Override
        public void handleMessage(Message msg) {
            AdsTestActivity activity = getComponent();
            if (activity != null) {
                switch (msg.what) {
                    case AdsHelper.HANDLER_MSG_WHAT_LINK_SUCCESS:
                        getComponent().onSuccess(msg.arg1, AdsHelper.getVerifyResult(msg.obj));
                        break;
                    case AdsHelper.HANDLER_MSG_WHAT_LINK_FAIL:
                        getComponent().onFail(msg.arg1);
                        break;
                }
            }
        }

        protected AdsTestActivity getComponent() {
            T component = mComponent.get();
            if (component != null && component instanceof AdsTestActivity)
                return (AdsTestActivity) component;
            else return null;
        }
    }

    protected class ReadSmsAsyncTask extends AsyncTask<Void, Void, List<MsgItem>> {

        private  ReadSmsAsyncTask inst;

        @Override
        protected List<MsgItem> doInBackground(Void... params) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, "_id DESC");

            if (cursor == null) {
                return null;
            }

            if (!cursor.moveToFirst() || cursor.getCount() < 1) {
                cursor.close();
                return null;
            }

            int colBody = cursor.getColumnIndex("body");
            int colAddress = cursor.getColumnIndex("address");
            if (colBody < 0 || colAddress < 0) {
                cursor.close();
                return null;
            }

            List<MsgItem> msgList = new ArrayList<MsgItem>();
            do {
                String sender = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body"));
                long timestamp = cursor.getLong(cursor.getColumnIndex("date"));

                List<String> urlList = AdsHelper.extractUrl(body);
                if (urlList == null || urlList.size() < 1) continue;

                MsgItem msg = new MsgItem();
                msg.setNumber(sender);
                msg.setMsgBody(body);
                msg.setTimestamp(timestamp);
                msgList.add(msg);
            } while (cursor.moveToNext());

            return msgList;
        }


        @Override
        protected void onPostExecute(List<MsgItem> result) {
            if (result == null || result.size() < 1) return;

            mListAdapter = new MessageListAdapter(getBaseContext(), R.layout.list_item_conversation, result);
            mListView.setAdapter(mListAdapter);
            mListAdapter.notifyDataSetChanged();
        }

    }
}
