package br.com.android.unb.igor;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AventuraAndamentoPagerAdapter extends FragmentPagerAdapter {
    private static final String tabTitles[] = { "ANDAMENTO", "JOGADORES"};
    private static final int PAGE_COUNT = tabTitles.length;
    private Aventura mAventura;

    public AventuraAndamentoPagerAdapter(FragmentManager fm, Aventura aventura) {
        super(fm);
        mAventura = aventura;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        AventuraAndamentoFragment fragment = AventuraAndamentoFragment.newInstance(position);

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
