package br.com.android.unb.igor.Home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
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

import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.android.unb.igor.AventuraAndamento.AventuraAndamentoActivity;
import br.com.android.unb.igor.Model.Aventura;
import br.com.android.unb.igor.R;
import br.com.android.unb.igor.Service;

public class AventuraFragment extends Fragment {
    private RecyclerView mAventuraRecyclerView;
    private AventuraAdapter mAventuraAdapter;

    ProgressBar pb;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Service.getAventuras().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Aventura> aventuras = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Pensar numa maneira deixar isso generalizado
                        Map<String, Object> data = document.getData();
                        aventuras.add(new Aventura(
                                data.get("nome").toString(),
                                new Date(),
                                document.getLong("nImage").intValue(),
                                document.getString("mestre"),
                                document.getString("sinopse"),
                                UUID.randomUUID()));
                    }
                    pb.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.overlay)));
                    updateUI(aventuras);
                } else {
                    Toast.makeText(getContext(), "Erro ao resgatar registros do back", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_home, container, false);
        mAventuraRecyclerView = view.findViewById(R.id.aventura_recycler_view);

        pb = view.findViewById(R.id.progress_bar);

        mAventuraRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton fb = view.findViewById(R.id.fab);
        fb.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_aventuraFragment_to_novoFragment, null));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pb.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.rediant_red)));

        Service.getAventuras().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Aventura> aventuras = new ArrayList<>();
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        // Pensar numa maneira deixar isso generalizado
                        Map<String, Object> data = document.getData();
                        aventuras.add(new Aventura(
                                data.get("nome").toString(),
                                new Date(),
                                document.getLong("nImage").intValue(),
                                document.getString("mestre"),
                                document.getString("sinopse"),
                                UUID.fromString(document.getId())));
                    }
                    pb.setIndeterminateTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.overlay)));
                    updateUI(aventuras);
                } else {
                    Toast.makeText(getContext(), "Erro ao resgatar registros do back", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(List<Aventura> adventures){
        if (mAventuraAdapter == null) {
            mAventuraAdapter = new AventuraAdapter(adventures);
            mAventuraRecyclerView.setAdapter(mAventuraAdapter);
        } else {
            mAventuraRecyclerView.setAdapter(mAventuraAdapter);
            mAventuraAdapter.getListAventuras().clear();
            mAventuraAdapter.getListAventuras().addAll(adventures);
            mAventuraAdapter.notifyDataSetChanged();
        }
    }

    private class AventuraHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ConstraintLayout mConstraintLayout;
        private TextView mAventuraTitulo;
        private Aventura mAventura;

        public AventuraHolder (View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            // Adicionair mais itens
            mAventuraTitulo = itemView.findViewById(R.id.titulo_aventura);
            mConstraintLayout = itemView.findViewById(R.id.list_item);
        }

        public void bindAventura(Aventura aventura) {
            // Adicionair mais itens
            mAventura = aventura;
            mAventuraTitulo.setText(mAventura.getNome());
            switch(mAventura.getnImage()){
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
            String id = mAventura.getId().toString();
            Intent i = AventuraAndamentoActivity.newIntent(getContext(), id);
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

        public List <Aventura> getListAventuras(){
            return mAventuras;
        }
    }
}
