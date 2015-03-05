package sample.info.minitong;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chunghj on 15. 3. 5..
 */
public class MainFragment extends Fragment {
    private static final String KEY_CONTENT = "TestFragment:Content";

    private String tapTitle;
    private int position;

    public static MainFragment create(String tapTitle, int position) {

        MainFragment mainFrg = new MainFragment();
        Bundle b = new Bundle();
        b.putString("tapTitle", tapTitle);
        b.putInt("position", position);
        mainFrg.setArguments(b);
        return mainFrg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.tapTitle = this.getArguments().getString("tapTitle");
        this.position = this.getArguments().getInt("posiotion");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.main_fragment, null);
//        TextView mTextTitle = (TextView) layout.findViewById(R.id.text_title);
//        mTextTitle.setText(tapTitle + " " + position);

        View view = inflater.inflate(R.layout.main_fragment, container, false);


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }



}
