package br.com.android.unb.igor.AventuraAndamento;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.android.unb.igor.Model.Jogador;
import br.com.android.unb.igor.R;
import br.com.android.unb.igor.Service;

public class AventuraAndamentoJogadoresFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private AventuraAndamentoJogadoresAdapter mAventuraAndamentoJogadoresAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aventura_andamento_jogadores_framelayout, container, false);

        mRecyclerView = view.findViewById(R.id.listaJogadoresRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        Pegar lista de jogadores desta aventura
        final List<Jogador> listaJogadores = new ArrayList<Jogador>();
        Service.getJogadores().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot dsList = task.getResult();
                if (dsList != null){
                    for (QueryDocumentSnapshot ds: task.getResult()) {
                        if (!ds.get("id").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            listaJogadores.add(new Jogador(
                                    ds.get("id").toString(),
                                    ds.get("email").toString(),
                                    ds.get("senha").toString(),
                                    ds.get("nome").toString(),
                                    ds.get("sexo").toString(),
                                    new Date()
                            ));
                        }
                    }
                    updateUI(listaJogadores);

                }
            }
        });
        return view;
    }

    private void updateUI(List<Jogador> l){
        if (mAventuraAndamentoJogadoresAdapter == null) {
            mAventuraAndamentoJogadoresAdapter = new AventuraAndamentoJogadoresAdapter(l);
            mRecyclerView.setAdapter(mAventuraAndamentoJogadoresAdapter);
        } else {
            mAventuraAndamentoJogadoresAdapter.notifyDataSetChanged();
        }
    }

    private class AventuraAndamentoJogadoresAdapter extends RecyclerView.Adapter<AventuraAndamentoJogadoresHolder> {
        List<Jogador> mJogadorList;
        public AventuraAndamentoJogadoresAdapter(List<Jogador> jogadoresLista) {
            mJogadorList = jogadoresLista;
        }

        @NonNull
        @Override
        public AventuraAndamentoJogadoresHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.jogador_list_item, viewGroup, false);
            return new AventuraAndamentoJogadoresHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AventuraAndamentoJogadoresHolder aventuraAndamentoJogadoresHolder, int i) {
            aventuraAndamentoJogadoresHolder.bindJogador(mJogadorList.get(i));
        }

        @Override
        public int getItemCount() {
            return mJogadorList.size();
        }
    }

    private class AventuraAndamentoJogadoresHolder extends RecyclerView.ViewHolder {
        private TextView mJogadorNome;
        private ImageView mImageView;

        public AventuraAndamentoJogadoresHolder(@NonNull View itemView) {
            super(itemView);
            mJogadorNome = itemView.findViewById(R.id.itemNomeJogador);
            mImageView = itemView.findViewById(R.id.itemIconeJogador);
        }

        public void bindJogador(Jogador jogador) {
            mJogadorNome.setText(R.string.no_description);
        }
    }
}
