package com.example.alien.myapplication1.rankings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alien.myapplication1.R;


/**
 * Created by Adams on 2015-05-24.
 */
public class RankingsFragment extends Fragment {

    private View rootView;
    private TextView rankTitle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);
        rankTitle = (TextView) rootView.findViewById(R.id.rankTitle);
        System.out.println("argument " + getArguments().getString("title"));
        rankTitle.setText(getArguments().getString("title"));

        return rootView;
    }

    public void onResume()
    {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
}
