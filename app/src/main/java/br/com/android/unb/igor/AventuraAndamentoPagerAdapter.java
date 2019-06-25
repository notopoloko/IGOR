package br.com.android.unb.igor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AventuraAndamentoPagerAdapter extends FragmentPagerAdapter {
    private static final String[] tabTitles = { "ANDAMENTO", "JOGADORES"};
    private static final int PAGE_COUNT = tabTitles.length;

    public AventuraAndamentoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return AventuraAndamentoFragment.newInstance(position);

//        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
