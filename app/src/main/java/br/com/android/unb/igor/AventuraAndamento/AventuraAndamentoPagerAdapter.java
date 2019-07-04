package br.com.android.unb.igor.AventuraAndamento;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.android.unb.igor.Model.Aventura;

public class AventuraAndamentoPagerAdapter extends FragmentPagerAdapter {
    private static final String[] tabTitles = { "ANDAMENTO", "JOGADORES"};
    private static final int PAGE_COUNT = tabTitles.length;
    private Aventura adventure;

    public AventuraAndamentoPagerAdapter(FragmentManager fm, Aventura aventura) {
        super(fm);
        adventure = aventura;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return AventuraAndamentoInfoFragment.newInstance(position, adventure);
//        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
