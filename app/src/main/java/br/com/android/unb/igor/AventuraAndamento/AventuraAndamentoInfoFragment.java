package br.com.android.unb.igor.AventuraAndamento;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.UUID;

import br.com.android.unb.igor.Model.Aventura;
import br.com.android.unb.igor.R;
import br.com.android.unb.igor.Service;

public class AventuraAndamentoInfoFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public Aventura aventura;
    private int mPage;

    public static AventuraAndamentoInfoFragment newInstance(int page, Aventura adventure) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AventuraAndamentoInfoFragment fragment = new AventuraAndamentoInfoFragment();
        fragment.aventura = adventure;
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
            sinopseAventura.setText(aventura.getSinopse());
            return view;
        } else {
            View v = inflater.inflate(R.layout.fragment_aventura_andamento_jogadores, container, false);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.jogador_item_frame_layout);

            final TextView nomeMestre = v.findViewById(R.id.nomeJogador);
            if (fragment == null) {
                fragment  = new AventuraAndamentoJogadoresFragment();
                fm.beginTransaction().add(R.id.jogador_item_frame_layout, fragment).commit();
            }

            Service.getJogador(aventura.getMestre()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot ds = task.getResult();
                        nomeMestre.setText(ds.get("nome").toString());
                    } else {
                        Toast.makeText(getActivity(), "Erro ao resgatar o mestre", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return v;
        }
    }
}
