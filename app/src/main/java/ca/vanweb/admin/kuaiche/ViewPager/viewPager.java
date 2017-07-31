package ca.vanweb.admin.kuaiche.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ca.vanweb.admin.kuaiche.Fragment.list_fragment;

/**
 * Created by momen on 8/14/2016.
 */
public class viewPager extends FragmentPagerAdapter {
    Context context;
    Activity activity;

    public viewPager(FragmentManager fm,  Context con,Activity activity) {
        super(fm);
        context=con;
        this.activity=activity;

    }

    @Override
    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
//                return new FragmentA(type,context,activity);
//            case 1:
//                return new FragmentB(type,context);
//        }
//        return null;

        return new list_fragment(position,context,activity);

    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "First";
            case 1:
                return "Sec";
            case 2:
                return "Thir";

        }
        return null;
    }
}
