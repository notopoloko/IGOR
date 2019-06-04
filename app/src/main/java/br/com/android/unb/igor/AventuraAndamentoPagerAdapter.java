package br.com.android.unb.igor;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AventuraAndamentoPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] { "ANDAMENTO", "JOGADORES"};
    final int PAGE_COUNT = tabTitles.length;
    private Context context;

    public AventuraAndamentoPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return AventuraAndamentoFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
