package com.example.settingsscreen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabMenuPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public TabMenuPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        //Returning the current tabs
        switch (position) {
            case 0:
                TabChildStatus tab1 = new TabChildStatus();
                return tab1;
            case 1:
                TabChildHistory tab2 = new TabChildHistory();
                return tab2;
            case 2:
                TabChildBusInfo tab3 = new TabChildBusInfo();
                return tab3;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
