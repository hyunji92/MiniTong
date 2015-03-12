package sample.info.minitong;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chunghj on 15. 3. 5..
 */
public class MiniUnkownFragment extends Fragment {

    public static MiniUnkownFragment newInstance(CharSequence label) {
        MiniUnkownFragment f;
        f = new MiniUnkownFragment();
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
        View view = inflater.inflate(R.layout.msg_fragment, container, false);

        return view;
    }
}