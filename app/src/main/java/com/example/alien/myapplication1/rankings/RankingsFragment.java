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
        Comparator comp = null;
        int listType = getArguments().getInt("type");
        switch(listType)
        {
            case 1:
                comp = new Comparator<Rank>(){
                    @Override
                    public int compare(Rank lhs, Rank rhs) {
                        Integer x = rhs.getStats().getDistance();
                        return x.compareTo(lhs.getStats().getDistance());
                    }
                };
                break;
            case 2:
                comp = new Comparator<Rank>(){
                    @Override
                    public int compare(Rank lhs, Rank rhs) {
                        String[] rhsTime = rhs.getStats().getTime().split(":");
                        String[] lhsTime = lhs.getStats().getTime().split(":");
                        int result = Integer.valueOf(rhsTime[0]).compareTo(Integer.valueOf(lhsTime[0]));
                        if(result == 0)
                            result = Integer.valueOf(rhsTime[1]).compareTo(Integer.valueOf(lhsTime[1]));
                        if(result == 0)
                            result = Integer.valueOf(rhsTime[2]).compareTo(Integer.valueOf(lhsTime[2]));
                        return result;
                    }
                };
                break;
            case 3:
                comp = new Comparator<Rank>(){
                    @Override
                    public int compare(Rank lhs, Rank rhs) {
                        Double x = rhs.getStats().getAverage();
                        return x.compareTo(lhs.getStats().getAverage());
                    }
                };
                break;

        }
        ArrayList<Rank> list = ViewPagerFragment.rankList;
        Collections.sort(list, comp);
        int i = 0;
        for(Rank r : list)
        {
            r.setPosition(++i);
        }
        RankAdapter adapter = new RankAdapter(container.getContext(), R.layout.rank_list_row, list, listType);
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
