package br.com.android.unb.igor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AventuraFragment extends Fragment {
    private RecyclerView mAventuraRecyclerView;
    private AventuraAdapter mAventuraAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_home, container, false);

        mAventuraRecyclerView = view.findViewById(R.id.aventura_recycler_view);
        mAventuraRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Atualizar view com dados vindos do Firebase
        updateUI();

        return view;
    }

    private void updateUI(){
        // Inicilizar aventuras
        List<Aventura> aventuras = new ArrayList<Aventura>();
        aventuras.add(new Aventura("Shunn Lee, o FastFoot...", new Date(), 0));
        aventuras.add(new Aventura("Campos de Nhame", new Date(), 0));
        aventuras.add(new Aventura("Meau, o cachorro-gato", new Date(), 0));
        if (mAventuraAdapter == null) {
            mAventuraAdapter = new AventuraAdapter(aventuras);
            mAventuraRecyclerView.setAdapter(mAventuraAdapter);
        } else {
            mAventuraAdapter.notifyDataSetChanged();
        }
    }

    private class AventuraHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Aventura mAventura;

        private ConstraintLayout mConstraintLayout;
        private TextView mAventuraTitulo;

        public AventuraHolder (View itemView){
            super(itemView);
            // Adicionair mais itens
            mAventuraTitulo = itemView.findViewById(R.id.titulo_aventura);
            mConstraintLayout = itemView.findViewById(R.id.list_item);
        }

        public void bindAventura(Aventura aventura) {
            // Adicionair mais itens
            mAventuraTitulo.setText(aventura.getNome());
            mConstraintLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.miniatura_imagem_automatica));
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "Não implementado", Toast.LENGTH_SHORT).show();
        }
    }

    private class AventuraAdapter extends RecyclerView.Adapter<AventuraHolder> {
        private List<Aventura> mAventuras;

        public AventuraAdapter(List<Aventura> aventuras) {
            mAventuras = aventuras;
        }

        @NonNull
        @Override
        public AventuraHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.aventura_list_item, viewGroup, false);
            return new AventuraHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AventuraHolder aventuraHolder, int i) {
            aventuraHolder.bindAventura(mAventuras.get(i));
        }

        @Override
        public int getItemCount() {
            return mAventuras.size();
        }
    }
}
