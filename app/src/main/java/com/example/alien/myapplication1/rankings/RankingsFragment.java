package com.example.alien.myapplication1.rankings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alien.myapplication1.R;
import com.example.alien.myapplication1.tracks.Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by Adams on 2015-05-24.
 */
public class RankingsFragment extends Fragment {

    private TextView rankTitle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);
        rankTitle = (TextView) rootView.findViewById(R.id.rankTitle);
        System.out.println("argument " + getArguments().getString("title"));
        rankTitle.setText(getArguments().getString("title"));
//        Comparator comp;
//        switch(getArguments().getInt("type"))
//        {
//            case 1:
//                comp = new Comparator
//                break;
//            case 2:
//                break;
//            case 3:
//                break;
//
//        }
//        ArrayList<Rank> list = ViewPagerFragment.rankList;
//        Collections.sort(list, )
        ArrayList<Rank> list = new ArrayList<>();
        list.add(new Rank(1, "test", new Stats(111111, 11.1, "11:11:11")));

        RankAdapter adapter = new RankAdapter(container.getContext(), R.layout.rank_list_row, list);
        ListView lv = (ListView) rootView.findViewById(R.id.listView);
        lv.setAdapter(adapter);
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
