package com.example.alien.myapplication1.rankings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.alien.myapplication1.NetConnection.OnASyncTaskCompleted;
import com.example.alien.myapplication1.R;

import java.util.ArrayList;

/**
 * Created by Adams on 2015-05-24.
 */
public class ViewPagerFragment extends Fragment implements OnASyncTaskCompleted {

        static final int NUM_ITEMS = 3;

        private ViewPagerAdapter mAdapter;
        private ViewPager mPager;
        static ArrayList<Rank> rankList;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //container.removeAllViews();
            GetRanking ranking = new GetRanking(container.getContext(), this);
            ranking.execute();
            View rootView = inflater.inflate(R.layout.fragment_viewpager, container, false);
            mAdapter = new ViewPagerAdapter(getChildFragmentManager());
            mPager = (ViewPager) rootView.findViewById(R.id.pager);
            return rootView;
        }

    @Override
    public void onASyncTaskCompleted(Object... value) {
        rankList = (ArrayList<Rank>) value[0];
        mPager.setAdapter(mAdapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int num) {

            if (num == 0) {
                Fragment fr = new RankingsFragment();
                Bundle b = new Bundle();
                b.putInt("type", 1);
                b.putString("title", "Ranking dystans");
                fr.setArguments(b);
                return fr;
            } else if (num == 1) {
                Fragment fr = new RankingsFragment();
                Bundle b = new Bundle();
                b.putInt("type", 2);
                b.putString("title", "Ranking czas");
                fr.setArguments(b);
                return fr;
            } else {
                Fragment fr = new RankingsFragment();
                Bundle b = new Bundle();
                b.putInt("type", 3);
                b.putString("title", "Ranking srednia");
                fr.setArguments(b);
                return fr;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

    }
}

