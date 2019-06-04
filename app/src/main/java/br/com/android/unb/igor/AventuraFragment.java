package br.com.android.unb.igor;

import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AventuraFragment extends Fragment {
    private RecyclerView mAventuraRecyclerView;
    private AventuraAdapter mAventuraAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.content_home, container, false);

        final ProgressBar pb = view.findViewById(R.id.progress_bar);
        mAventuraRecyclerView = view.findViewById(R.id.aventura_recycler_view);

        mAventuraRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Atualizar view com dados vindos do Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("aventuras").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Aventura> aventuras = new ArrayList<>();
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        // Pensar numa maneira deixar isso generalizado
                        Map<String, Object> data = document.getData();
                        aventuras.add(new Aventura(data.get("nome").toString(), new Date(), document.getLong("nImage").intValue(), document.getString("mestre"), document.getString("sinopse")));
                    }
                    updateUI(aventuras);
                } else {
                    Toast.makeText(getContext(), "Erro ao resgatar registros do back", Toast.LENGTH_SHORT).show();
                }
                pb.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.overlay)));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("aventuras").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<Aventura> aventuras;
                    if (mAventuraAdapter == null){
                        aventuras = new ArrayList<Aventura>();
                    } else {
                        aventuras = mAventuraAdapter.getAdapterAventuras();
                        aventuras.clear();
                    }

                    for (QueryDocumentSnapshot document: task.getResult()) {
                        // Pensar numa maneira deixar isso generalizado
                        Map<String, Object> data = document.getData();
                        aventuras.add(new Aventura(data.get("nome").toString(), new Date(), document.getLong("nImage").intValue(), document.getString("mestre"), document.getString("sinopse")));
                    }
                    updateUI(aventuras);
                } else {
                    Toast.makeText(getContext(), "Erro ao resgatar registros do back", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(List<Aventura> aventuraList){
        if (mAventuraAdapter == null) {
            mAventuraAdapter = new AventuraAdapter(aventuraList);
            mAventuraRecyclerView.setAdapter(mAventuraAdapter);
        } else {
            mAventuraAdapter.notifyDataSetChanged();
        }
    }

    private class AventuraHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ConstraintLayout mConstraintLayout;
        private TextView mAventuraTitulo;

        public AventuraHolder (View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            // Adicionair mais itens
            mAventuraTitulo = itemView.findViewById(R.id.titulo_aventura);
            mConstraintLayout = itemView.findViewById(R.id.list_item);
        }

        public void bindAventura(Aventura aventura) {
            // Adicionair mais itens
            mAventuraTitulo.setText(aventura.getNome());
            switch(aventura.getnImage()){
                case 0:
                    mConstraintLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.miniatura_coast));
                    break;
                case 1:
                    mConstraintLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.miniatura_corvali));
                    break;
                case 2:
                    mConstraintLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.miniatura_heartlands));
                    break;
                case 3:
                    mConstraintLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.miniatura_imagem_automatica));
                    break;
                case 4:
                    mConstraintLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.miniatura_krevast));
                    break;
            }
        }

        @Override
        public void onClick(View view) {
            Intent i = AventuraAndamentoActivity.newIntent(getContext(), mAventuraTitulo.getText().toString());
            startActivity(i);
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

        public List<Aventura> getAdapterAventuras(){
            return mAventuras;
        }
    }
}
