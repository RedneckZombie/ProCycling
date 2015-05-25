package com.example.alien.myapplication1.rankings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.myapplication1.R;

import java.util.ArrayList;

/**
 * Created by Adams on 2015-05-24.
 */
public class ViewPagerFragment extends Fragment {

        static final int NUM_ITEMS = 3;

        private ViewPagerAdapter mAdapter;
        private ViewPager mPager;
        static ArrayList<Rank> rankList;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //container.removeAllViews();
            View rootView = inflater.inflate(R.layout.fragment_viewpager, container, false);
            mAdapter = new ViewPagerAdapter(getChildFragmentManager());
            mPager = (ViewPager) rootView.findViewById(R.id.pager);
            mPager.setAdapter(mAdapter);
            rankList = new ArrayList<>(); //tu pobierz
            return rootView;
        }

        public class ViewPagerAdapter extends FragmentPagerAdapter {
            public ViewPagerAdapter(FragmentManager fm) {
                super(fm);
            }


            @Override
            public Fragment getItem(int num) {
                Fragment fr = new RankingsFragment();
                Bundle b = new Bundle();
                if (num == 0)
                {
                    b.putInt("type", 1);
                    b.putString("title","Ranking 1");
                    fr.setArguments(b);
                    return fr;
                }
                else if(num == 1)
                {
                    b.putInt("type", 2);
                    b.putString("title","Ranking 2");
                    fr.setArguments(b);
                    return fr;
                }
                else {
                    b.putInt("type", 3);
                    b.putString("title","Ranking 3");
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

