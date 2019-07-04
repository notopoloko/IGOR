package br.com.android.unb.igor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AventuraAndamentoFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static AventuraAndamentoFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AventuraAndamentoFragment fragment = new AventuraAndamentoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mPage == 0){
            View view = inflater.inflate(R.layout.fragment_aventura_andamento, container, false);

            TextView sinopseAventura = view.findViewById(R.id.sinopseAventura);
            sinopseAventura.setText("");
            return view;
        } else {
            View v = inflater.inflate(R.layout.fragment_aventura_andamento_jogadores, container, false);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.jogador_item_frame_layout);

            if (fragment == null) {
                fragment  = new AventuraAndamentoJogadoresFragment();
                fm.beginTransaction().add(R.id.jogador_item_frame_layout, fragment).commit();
            }

            return v;
        }
    }
}
